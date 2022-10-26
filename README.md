# Telegram APIGateway MicroService

## Usage

run in **docker**:

    docker run -d -e HOST=dummy telegram-service:v0.1

or in **docker-compose.yml**:

    telegram-service:
        image: telegram-service:v0.1
        environment:
            HOST:
            RECEIVE_QUEUE:
            TRANSMIT_QUEUE:
            TOKEN:

## Environment variables

**HOST** - hostname for RabbitMQ connection (*default*: localhost).  
**RECEIVE_QUEUE** - name of the queue from which messages will be sent using the Telegram Service.  
**TRANSMIT_QUEUE** - the name of the queue to which messages from Telegram users will be sent to your service.  
**TOKEN** - Telegram Bot API token.  

## Message structure

JSON syntax:  

    {
        "chatId": 100,
        "payload": ["test message 1", "test message 2"]
    }

where:  
**chatId** - Telegram chat id for specific dialog (`long`);  
**payload** - JSON array of strings (`List<String>`).
