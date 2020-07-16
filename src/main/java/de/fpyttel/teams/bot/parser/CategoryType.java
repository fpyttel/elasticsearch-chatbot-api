package de.fpyttel.teams.bot.parser;

import lombok.NonNull;

public enum CategoryType {
	error,
	conversation,
	log;

	public static CategoryType valueOf(@NonNull final Category category) {
		switch (category) {
		case conversation_continue:
			return conversation;
		case conversation_complete:
			return conversation;
		case conversation_greeting:
			return conversation;
		case log_request:
			return log;
		case log_environment:
			return log;
		default:
			return error;
		}
	}
}
