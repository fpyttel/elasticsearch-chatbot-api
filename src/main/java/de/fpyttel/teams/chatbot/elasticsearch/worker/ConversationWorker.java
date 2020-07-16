package de.fpyttel.teams.chatbot.elasticsearch.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.chatbot.elasticsearch.client.ms.TeamsClient;
import de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity.Action;
import de.fpyttel.teams.chatbot.elasticsearch.parser.ActionMessageParser;
import de.fpyttel.teams.chatbot.elasticsearch.parser.AnswerGenerator;
import de.fpyttel.teams.chatbot.elasticsearch.parser.MessageJoiner;
import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Message;
import de.fpyttel.teams.chatbot.elasticsearch.registry.ConversationRegistry;
import de.fpyttel.teams.chatbot.elasticsearch.registry.ConversationWorkerRegistry;
import de.fpyttel.teams.chatbot.elasticsearch.worker.task.ConversationTask;
import de.fpyttel.teams.chatbot.elasticsearch.worker.task.ConversationTaskFactory;
import lombok.Getter;
import lombok.Setter;

@Component
public class ConversationWorker extends Thread {

	@Autowired
	private ActionMessageParser messageParser;

	@Autowired
	private ConversationRegistry conversationRegistry;

	@Autowired
	private ConversationWorkerRegistry conversationWorkerRegistry;

	@Autowired
	private TeamsClient teamsClient;

	@Autowired
	private AnswerGenerator answerGenerator;

	@Autowired
	private ConversationTaskFactory taskFactory;

	@Getter
	@Setter
	private String conversationId;

	@Override
	public void run() {
		Action currentAction = null;
		// process all actions in the queue
		while ((currentAction = conversationRegistry.pull(conversationId)) != null) {
			// parse message & merge with previous message if possible
			final Message message = MessageJoiner.join(conversationRegistry.getLastMessage(conversationId),
					messageParser.parse(currentAction));

			// prepare response
			final String responseText = answerGenerator.generate(message);

			// post instant answer
			teamsClient.postToConversation(currentAction.replyBuilder().text(responseText).build());

			// create task & execute if needed
			final ConversationTask task = taskFactory.createTask(this, message);
			if (task != null && task.execute()) {
				// task executed fine -> reset last message and start "new conversation"
				conversationRegistry.setLastMessage(conversationId, null);
			} else {
				// update last message
				conversationRegistry.setLastMessage(conversationId, message);
			}
		}

		// worker is done -> unregister
		conversationWorkerRegistry.unregister(this);
	}

}
