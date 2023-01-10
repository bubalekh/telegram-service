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

or in kubernetes (**telegram-service-deployment.yml**):

    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: nginx-deployment
      labels:
        app: telegram-service
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: telegram-service
      template:
        metadata:
          labels:
            app: telegram-service
        spec:
          containers:
          - name: telegram-service
            image: telegram-service:latest
          env:
          - name: RABBIT_HOST
            value: ""
          - name: RABBIT_QUEUE_RECEIVE
            value: ""
          - name: RABBIT_QUEUE_TRANSMIT
            value: ""
          - name: TELEGRAM_BOT_TOKEN
            value: ""

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

## TODO

- RabbitMQ's credentials (login and password);
- Automatic pagination of huge messages!
