# Elasticsearch Teams Chatbot API

A simple project to run a MS Teams Chatbot which fetches log infos from an elasticsearch instance.

## Start the bot

Set proper values for `elastic.indices.pattern`, `ms.app.id` and `ms.app.password` in the application.properties and run following command.

```
gradlew clean build run
```

Don't forget to register your bot in Azure (below endpoint should be used e.g. for tunneling)! 

## Endpoints

POST http://127.0.0.1:8070/api/message

## Sample

![Example Chat](https://github.com/fpyttel/elasticsearch-chatbot-api/blob/master/samples/sample-conversation.png?raw=true "Example Chat")