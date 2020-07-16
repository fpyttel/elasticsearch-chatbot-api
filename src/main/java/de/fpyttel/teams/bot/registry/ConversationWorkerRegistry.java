package de.fpyttel.teams.bot.registry;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.bot.model.Action;
import de.fpyttel.teams.bot.worker.ConversationWorker;
import lombok.NonNull;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ConversationWorkerRegistry {

	@Autowired
	private ApplicationContext context;

	private volatile Map<String, ConversationWorker> workerMap = new HashMap<>();

	public void register(@NonNull final Action action) {
		if (!workerMap.containsKey(action.getConversation().getId())) {
			final ConversationWorker worker = context.getBean(ConversationWorker.class);
			worker.setConversationId(action.getConversation().getId());
			workerMap.put(action.getConversation().getId(), worker);
			worker.start();
		}
	}

	public boolean hasWorker(@NonNull final String conversationId) {
		return workerMap.containsKey(conversationId);
	}

	public boolean hasWorker(@NonNull final Action action) {
		return hasWorker(action.getConversation().getId());
	}

}
