package de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity;

public enum Type {
	message,
	contactRelationUpdate,
	conversationUpdate,
	typing,
	endOfConversation,
	event,
	invoke,
	deleteUserData,
	messageUpdate,
	messageDelete,
	installationUpdate,
	messageReaction,
	suggestion,
	trace,
	handoff
}