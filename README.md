# FarmCRM

Система для ведения заказов частного фермерского хозяйства

## Архитектура проекта

Монорепозиторий с разделением на:
- **Frontend** - Vanilla JavaScript клиент с адаптивным дизайном
- **Backend** - Java Spring Boot API с JWT авторизацией
- **Database** - PostgreSQL

## Структура проекта

```
farmCrm/
├── client/                 # Frontend приложение (Vanilla JS)
│   ├── src/
│   │   ├── core/          # Основная логика (router, api, etc.)
│   │   ├── components/    # UI компоненты
│   │   ├── assets/        # Статические ресурсы
│   │   ├── services/      # API сервисы
│   │   └── types/         # TypeScript типы (если нужно)
│   ├── index.html
│   └── package.json
├── server/                 # Backend API (Java Spring Boot)
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── src/test/
│   └── pom.xml
├── shared/                 # Общие ресурсы
│   ├── api-contracts/     # OpenAPI спецификации
│   ├── database/          # SQL миграции
│   ├── docs/              # Документация
│   └── types/             # Общие типы
├── docker-compose.yml     # Docker конфигурация
├── Makefile              # Команды управления
└── package.json          # Root package.json
```

## Быстрый старт

### Установка зависимостей
```bash
make install
# или
npm install && cd client && npm install
```

### Запуск в режиме разработки
```bash
make dev
# или
npm run dev
```

### Запуск отдельных частей
```bash
# Только клиент
make dev-client

# Только сервер  
make dev-server
```

### Docker (полный стек с БД)
```bash
make docker-up
```

## Технологии

### Frontend
- Vanilla JavaScript (ES6+)
- Адаптивный дизайн для мобильных устройств
- Drag & Drop функционал
- lite-server для разработки

### Backend
- Java 17
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI документация
- Lombok

## API

Swagger UI доступен по адресу: http://localhost:8080/swagger-ui.html

## Разработка

Для удобства используйте команды из Makefile:
```bash
make help  # Показать все доступные команды
```

