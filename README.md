# Telegram APIGateway MicroService

## Usage

run in **docker**:

    docker run -d -e RABBIT_HOST=dummy telegram-service:v0.1

or in **docker-compose.yml**:

    telegram-service:
        image: telegram-service:v0.1
        environment:
            RABBIT_HOST:
            RABBIT_QUEUE_RECEIVE:
            RABBIT_QUEUE_TRANSMIT:
            TELEGRAM_BOT_TOKEN:

## Environment variables

**RABBIT_HOST** - hostname for RabbitMQ connection (*default*: localhost).  
**RABBIT_QUEUE_RECEIVE** - name of the queue from which messages will be sent using the Telegram Service.  
**RABBIT_QUEUE_TRANSMIT** - the name of the queue to which messages from Telegram users will be sent to your service.  
**TELEGRAM_BOT_TOKEN** - Telegram Bot API token.  

## Message structure

JSON syntax:  

    {
        "chatId": 100,
        "payload": ["test message 1", "test message 2"]
    }

where:  
**chatId** - Telegram chat id for specific dialog (`long`);  
**payload** - JSON array of strings (`List<String>`).