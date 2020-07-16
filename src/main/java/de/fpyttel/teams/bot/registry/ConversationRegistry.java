package de.fpyttel.teams.bot.registry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.bot.model.Action;
import lombok.NonNull;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ConversationRegistry {

	private volatile Map<String, Queue<Action>> conversationMap = new HashMap<>();
	
	public void put(@NonNull final Action action) {
		if (!conversationMap.containsKey(action.getConversation().getId())) {
			conversationMap.put(action.getConversation().getId(), new LinkedList<>());
		}
		conversationMap.get(action.getConversation().getId()).add(action);
	}
	
	public Action pull(@NonNull final String conversationId) {
		if (conversationMap.containsKey(conversationId)){
			return conversationMap.get(conversationId).poll();
		}
		return null;
	}
	
}
