package de.fpyttel.teams.webhook.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.fpyttel.teams.bot.client.ms.entity.Action;
import de.fpyttel.teams.bot.client.ms.entity.Environment;
import de.fpyttel.teams.bot.parser.boundary.ActionMessageParser;
import de.fpyttel.teams.bot.parser.entity.Category;
import de.fpyttel.teams.bot.parser.entity.CategoryType;
import de.fpyttel.teams.bot.parser.entity.Message;
import de.fpyttel.teams.bot.parser.entity.Message.Status;

public class ActionMessageParserTest {

	private ActionMessageParser parser = new ActionMessageParser();

	@Test
	public void parseTest() {
		// mock data
		final Action action = Action.builder().text("Can you please search for NPEs in the PROD logs for me?").build();

		// execute test
		final Message result = parser.parse(action);

		// check result
		assertEquals(CategoryType.log, result.getCategoryType());
		assertEquals(Category.log_request, result.getCategory());
		assertEquals(Environment.PROD, result.getEnvironment());
		assertEquals(Status.complete, result.getStatus());
	}

	@Test
	public void parseTest_env() {
		// mock data
		final Action action = Action.builder().text("please check the stuff on the PROD environment").build();

		// execute test
		final Message result = parser.parse(action);

		// check result
		assertEquals(CategoryType.log, result.getCategoryType());
		assertEquals(Category.log_request, result.getCategory());
		assertEquals(Environment.PROD, result.getEnvironment());
		assertEquals(Status.complete, result.getStatus());
	}

	@Test
	public void parseTest_incomplete() {
		// mock data
		final Action action = Action.builder().text("can you help me with the logs?").build();

		// execute test
		final Message result = parser.parse(action);

		// check result
		assertEquals(CategoryType.log, result.getCategoryType());
		assertEquals(Category.log_request_continue, result.getCategory());
		assertNull(result.getEnvironment());
		assertEquals(Status.incomplete, result.getStatus());
	}

	@Test
	public void parseTest_greeting() {
		// mock data
		final Action action = Action.builder().text("Hi").build();

		// execute test
		final Message result = parser.parse(action);

		// check result
		assertEquals(CategoryType.conversation, result.getCategoryType());
		assertEquals(Category.conversation_greeting, result.getCategory());
	}

	@Test
	public void parseTest_env_prod() {
		// mock data
		final Action action = Action.builder().text("on PROD").build();

		// execute test
		final Message result = parser.parse(action);

		// check result
		assertEquals(CategoryType.log, result.getCategoryType());
		assertEquals(Category.log_request, result.getCategory());
		assertEquals(Environment.PROD, result.getEnvironment());
		assertEquals(Status.complete, result.getStatus());
	}

}
