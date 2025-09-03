.PHONY: help dev build test clean install docker-up docker-down

# Цвета для вывода
GREEN := \033[32m
YELLOW := \033[33m
BLUE := \033[34m
RESET := \033[0m

help: ## Показать это сообщение помощи
	@echo "$(BLUE)FarmCRM - Команды для управления проектом$(RESET)"
	@echo ""
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "$(GREEN)%-15s$(RESET) %s\n", $$1, $$2}' $(MAKEFILE_LIST)

install: ## Установить все зависимости
	@echo "$(YELLOW)Устанавливаем зависимости...$(RESET)"
	npm install
	cd client && npm install

dev: ## Запустить разработку (client + server)
	@echo "$(YELLOW)Запускаем в режиме разработки...$(RESET)"
	npm run dev

dev-client: ## Запустить только клиент
	@echo "$(YELLOW)Запускаем клиент...$(RESET)"
	cd client && npm run dev

dev-server: ## Запустить только сервер
	@echo "$(YELLOW)Запускаем сервер...$(RESET)"
	cd server && ./mvnw spring-boot:run

build: ## Собрать проект
	@echo "$(YELLOW)Собираем проект...$(RESET)"
	npm run build

test: ## Запустить тесты
	@echo "$(YELLOW)Запускаем тесты...$(RESET)"
	npm run test

clean: ## Очистить кеши и сборочные файлы
	@echo "$(YELLOW)Очищаем проект...$(RESET)"
	npm run clean

docker-up: ## Запустить Docker контейнеры
	@echo "$(YELLOW)Запускаем Docker контейнеры...$(RESET)"
	docker-compose up --build

docker-down: ## Остановить Docker контейнеры
	@echo "$(YELLOW)Останавливаем Docker контейнеры...$(RESET)"
	docker-compose down

docker-logs: ## Посмотреть логи Docker контейнеров
	docker-compose logs -f

# Git команды
git-status: ## Показать статус git
	git status

git-push: ## Запушить изменения
	git push origin $$(git rev-parse --abbrev-ref HEAD)

git-pull: ## Получить изменения
	git pull origin $$(git rev-parse --abbrev-ref HEAD)
