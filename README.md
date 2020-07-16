# Elasticsearch Teams Chatbot API

This is an example project to run a MS Teams chatbot with Spring Boot which fetches log information by using the Elasticsearch REST API. During startup it trains a simple categorizer using OpenNLP for proper chat message parsing.

## Start the bot

Set proper values for `elastic.indices.pattern`, `ms.app.id` and `ms.app.password` in the application.properties and run following command.

```
gradlew clean build run
```

Don't forget to register your bot in Azure (below endpoint should be used e.g. for tunneling)! After that you should be able to use the Web Chat or integrate the bot via MS Teams link provided to you in the Azure portal.

## Endpoint

```
POST http://127.0.0.1:8070/api/message
```

## Example

![Example Chat](https://github.com/fpyttel/elasticsearch-chatbot-api/blob/master/samples/sample-conversation.png?raw=true "Example Chat")