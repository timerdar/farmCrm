const worker = self as unknown as {
	skipWaiting(): void;
	clients: {
		claim(): Promise<void>;
		matchAll(options: { type: string }): Promise<AppClient[]>;
	};
	addEventListener(
		type: "activate",
		listener: (event: ExtendableEventLike) => void
	): void;
	addEventListener(
		type: "message",
		listener: (event: WorkerMessageEvent) => void
	): void;
};

interface AppClient {
	url: string;
	navigate?: (url: string) => Promise<unknown>;
}

interface ExtendableEventLike {
	waitUntil(promise: Promise<unknown>): void;
}

interface WorkerMessageEvent {
	data: unknown;
	source: { postMessage(message: unknown): void } | null;
}

interface ConnectionLostRequest {
	method?: string;
	id?: unknown;
}

worker.skipWaiting();

// Старый worker Vaadin при обрыве навигации отдавал закэшированную оболочку.
// Она поднимала мёртвую сессию, и приложение не оживала без очистки кэша.
// Этот worker сеть не перехватывает и при активации стирает прежние Cache Storage.
worker.addEventListener("activate", (event) => {
	event.waitUntil(dropCachedShell());
});

async function dropCachedShell(): Promise<void> {
	const keys = await caches.keys();
	await Promise.all(keys.map((key) => caches.delete(key)));
	await worker.clients.claim();
	const windows = await worker.clients.matchAll({ type: "window" });
	await Promise.all(windows.map(async (client) => {
		if (!client.navigate) {
			return;
		}
		try {
			await client.navigate(client.url);
		} catch {
			// На старом WebKit Client.navigate нет. Кэш уже стёрт, следующая загрузка идёт в сеть.
		}
	}));
}

worker.addEventListener("message", (event) => {
	if (typeof event.data !== "object" || event.data === null) {
		return;
	}
	const request = event.data as ConnectionLostRequest;
	if (request.method === "Vaadin.ServiceWorker.isConnectionLost" && request.id !== undefined && event.source) {
		event.source.postMessage({ id: request.id, result: false });
	}
});
