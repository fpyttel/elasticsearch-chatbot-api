package de.fpyttel.teams.bot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fpyttel.teams.bot.client.ms.entity.Action;
import de.fpyttel.teams.bot.registry.ConversationRegistry;
import de.fpyttel.teams.bot.registry.ConversationWorkerRegistry;

@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private ConversationRegistry conversationRegistry;
	
	@Autowired
	private ConversationWorkerRegistry conversationWorkerRegistry;
	
	@PostMapping
	Action message(@RequestBody @NonNull Action action) {
		// add to conversation queue
		conversationRegistry.put(action);
		
		// register worker if needed
		if (!conversationWorkerRegistry.hasWorker(action)) {
			conversationWorkerRegistry.register(action);
		}
		
		// return simple answer
		return action.replyBuilder().text("Hi I'm here!").build();
	}
	
}
