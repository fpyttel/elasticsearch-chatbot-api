package de.fpyttel.teams.webhook.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.fpyttel.teams.bot.model.Action;
import de.fpyttel.teams.bot.parser.ActionMessageParser;

public class ActionMessageParserTest {

	private ActionMessageParser parser = new ActionMessageParser();

	@Test
	@Disabled
	public void parseTest() {
		// mock data
		final Action action = Action.builder().text("Can you please search for NPEs in the PROD logs for me?").build();

		// execute test
		parser.parse(action);
	}

	@Test
//	@Disabled
	public void parseTest_env() {
		// mock data
		final Action action = Action.builder().text("please check the stuff on the PROD environment").build();

		// execute test
		parser.parse(action);
	}

	@Test
	@Disabled
	public void parseTest_greeting() {
		// mock data
		final Action action = Action.builder().text("Hi").build();

		// execute test
		parser.parse(action);
	}

}
