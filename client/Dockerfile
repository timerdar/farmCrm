# Используем node образ для запуска lite-server
FROM node:18-alpine

# Рабочая директория в контейнере
WORKDIR /app

# Копируем package.json и package-lock.json (если есть) для установки зависимостей
COPY package*.json ./

# Устанавливаем lite-server (devDependencies)
RUN npm install

# Копируем весь фронтенд-код в контейнер
COPY . .

# Открываем порт lite-server (по умолчанию 3000)
EXPOSE 3000

# Запускаем lite-server
CMD ["npm", "start"]