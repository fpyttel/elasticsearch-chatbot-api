package de.fpyttel.teams.chatbot.elasticsearch.worker.task;

import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Message;
import de.fpyttel.teams.chatbot.elasticsearch.worker.ConversationWorker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ConversationTask {

	private ConversationWorker conversationWorker;
	private Message message;

	public abstract boolean execute();

}
