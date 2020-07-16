package de.fpyttel.teams.bot.registry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.bot.client.ms.entity.Action;
import de.fpyttel.teams.bot.parser.entity.Message;
import lombok.NonNull;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ConversationRegistry {

	private volatile Map<String, Queue<Action>> conversationMap = new HashMap<>();
	private volatile Map<String, Message> lastParserResultMap = new HashMap<>();

	public void put(@NonNull final Action action) {
		if (!conversationMap.containsKey(action.getConversation().getId())) {
			conversationMap.put(action.getConversation().getId(), new LinkedList<>());
		}
		conversationMap.get(action.getConversation().getId()).add(action);
	}

	public Action pull(@NonNull final String conversationId) {
		if (conversationMap.containsKey(conversationId)) {
			return conversationMap.get(conversationId).poll();
		}
		return null;
	}

	public void setLastParserResult(@NonNull final String conversationId, final Message parserResult) {
		lastParserResultMap.put(conversationId, parserResult);
	}

	public Message getLastParserResult(@NonNull final String conversationId) {
		return lastParserResultMap.getOrDefault(conversationId, null);
	}

}
