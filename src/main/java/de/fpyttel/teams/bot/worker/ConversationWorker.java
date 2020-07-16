package de.fpyttel.teams.bot.worker;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.bot.client.ElasticClient;
import de.fpyttel.teams.bot.client.TeamsClient;
import de.fpyttel.teams.bot.model.Action;
import de.fpyttel.teams.bot.parser.ActionMessageParser;
import de.fpyttel.teams.bot.parser.CategoryType;
import de.fpyttel.teams.bot.parser.ParserResult;
import de.fpyttel.teams.bot.parser.ResultJoiner;
import de.fpyttel.teams.bot.registry.ConversationRegistry;
import lombok.Setter;

@Component
public class ConversationWorker extends Thread {

	@Autowired
	private ActionMessageParser messageParser;

	@Autowired
	private ConversationRegistry conversationRegistry;

	@Autowired
	private TeamsClient teamsClient;

	@Autowired
	private ElasticClient elasticClient;

	@Setter
	private String conversationId;
	private ParserResult lastMessage;

	@Override
	public void run() {
		while (true) {
			// get latest action OR wait
			final Action currentAction = conversationRegistry.pull(conversationId);
			if (currentAction == null) {
				relax(5000);
				continue;
			}

			// parse message & merge with previous message if possible
			final ParserResult parserResult = ResultJoiner.join(lastMessage, messageParser.parse(currentAction));

			// prepare response
			String responseText = "";
			switch (parserResult.getCategory()) {
			case log_request:
				responseText = buildLoggerMessage(parserResult);
				break;
			case error:
				responseText = "I'm not getting your point.";
				break;
			default:
				responseText = "Do you have something to do for me?";
				break;
			}

			// process action
			teamsClient.postToConversation(currentAction.replyBuilder().text(responseText).build());
			if (ParserResult.Status.complete == parserResult.getStatus()
					&& CategoryType.log == parserResult.getCategoryType()) {
				// fetch data from ElasticSearch
				final SearchResponse response = elasticClient.search(parserResult.getEnvironment(), "Exception");
				if (response != null) {
					teamsClient.postToConversation(currentAction.replyBuilder()
							.text("Found " + response.getHits().getTotalHits().value + " Exceptions on "
									+ parserResult.getEnvironment() + " during the last "
									+ ElasticClient.DEFAULT_TIMEFRAME_HOURS + " hour.")
							.build());
				} else {
					teamsClient.postToConversation(currentAction.replyBuilder()
							.text("Coudn't find anything relevant in the last hour.")
							.build());
				}
			}

			// update last message
			this.lastMessage = parserResult;
		}
	}

	private void relax(final long millis) {
		try {
			sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String buildLoggerMessage(final ParserResult parserResult) {
		switch (parserResult.getStatus()) {
		case complete:
			return "Ok, I'll check the logs of environment " + parserResult.getEnvironment() + " for you...";
		case incomplete:
			return "No Problem, but for which environment?";
		default:
			return "I'm not getting your point.";
		}
	}

}
