package de.fpyttel.teams.chatbot.elasticsearch.worker.task;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.chatbot.elasticsearch.client.elasticsearch.ElasticClient;
import de.fpyttel.teams.chatbot.elasticsearch.client.ms.TeamsClient;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ExceptionLookupTask extends ConversationTask {

	@Autowired
	private TeamsClient teamsClient;

	@Autowired
	private ElasticClient elasticClient;

	@Override
	public boolean execute() {
		// fetch data from ElasticSearch
		final SearchResponse response = elasticClient.search(getMessage().getEnvironment(), "Exception");
		if (response != null) {
			teamsClient.postToConversation(getMessage()
					.getOrigin()
					.replyBuilder()
					.text("Found " + response.getHits().getTotalHits().value + " Exceptions on "
							+ getMessage().getEnvironment() + " during the last "
							+ ElasticClient.DEFAULT_TIMEFRAME_HOURS + " hour.")
					.build());
		} else {
			teamsClient.postToConversation(getMessage()
					.getOrigin()
					.replyBuilder()
					.text("Couldn't find anything relevant in the last hour.")
					.build());
		}
		return true;
	}

}
