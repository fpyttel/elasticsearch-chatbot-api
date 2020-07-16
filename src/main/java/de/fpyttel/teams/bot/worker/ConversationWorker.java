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
import de.fpyttel.teams.bot.registry.ConversationWorkerRegistry;
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
	private ElasticClient elasticClient;

	@Autowired
	private AnswerGenerator answerGenerator;

	@Getter
	@Setter
	private String conversationId;

	@Override
	public void run() {
		while (true) {
			// get latest action OR wait
			final Action currentAction = conversationRegistry.pull(conversationId);
			if (currentAction == null) {
				break;
			}

			// parse message & merge with previous message if possible
			final ParserResult parserResult = ResultJoiner
					.join(conversationRegistry.getLastParserResult(conversationId), messageParser.parse(currentAction));

			// prepare response
			final String responseText = answerGenerator.generate(parserResult);

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
				// reset last message
				conversationRegistry.setLastParserResult(conversationId, null);
			} else {
				// update last message
				conversationRegistry.setLastParserResult(conversationId, parserResult);
			}
		}

		// worker is done -> unregister
		conversationWorkerRegistry.unregister(this);
	}

}
