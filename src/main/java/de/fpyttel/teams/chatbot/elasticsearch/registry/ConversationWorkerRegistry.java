package de.fpyttel.teams.chatbot.elasticsearch.registry;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity.Action;
import de.fpyttel.teams.chatbot.elasticsearch.worker.boundary.ConversationWorker;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ConversationWorkerRegistry {

	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	private volatile Map<String, ConversationWorker> workerMap = new HashMap<>();

	public void register(@NonNull final Action action) {
		if (!workerMap.containsKey(action.getConversation().getId())) {
			final ConversationWorker worker = autowireCapableBeanFactory.createBean(ConversationWorker.class);
			worker.setConversationId(action.getConversation().getId());
			workerMap.put(action.getConversation().getId(), worker);
			worker.start();
			log.info("registered worker [ID={}]", worker.getId());
		}
	}

	public boolean hasWorker(@NonNull final String conversationId) {
		return workerMap.containsKey(conversationId);
	}

	public boolean hasWorker(@NonNull final Action action) {
		return hasWorker(action.getConversation().getId());
	}

	public void unregister(@NonNull final ConversationWorker worker) {
		workerMap.remove(worker.getConversationId());
		log.info("unregistered worker [ID={}]", worker.getId());
	}

}
