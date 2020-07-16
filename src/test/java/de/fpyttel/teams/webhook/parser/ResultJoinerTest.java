package de.fpyttel.teams.webhook.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.fpyttel.teams.bot.client.ms.entity.Environment;
import de.fpyttel.teams.bot.parser.boundary.ResultJoiner;
import de.fpyttel.teams.bot.parser.entity.Category;
import de.fpyttel.teams.bot.parser.entity.Message;
import de.fpyttel.teams.bot.parser.entity.Message.Status;

public class ResultJoinerTest {

	@Test
	public void joinTest() {
		// mock data
		final Message oldResult = Message.builder()
				.category(Category.log_request_continue)
				.status(Status.incomplete)
				.build();
		final Message newResult = Message.builder()
				.category(Category.log_request)
				.environment(Environment.PROD)
				.status(Status.incomplete)
				.build();

		// execute test
		final Message result = ResultJoiner.join(oldResult, newResult);

		// check result
		assertNotNull(result);
		assertEquals(Status.complete, result.getStatus());
		assertEquals(Environment.PROD, result.getEnvironment());
		assertEquals(Category.log_request, result.getCategory());
	}

	@Test
	public void joinTest_incomplete() {
		// mock data
		final Message oldResult = Message.builder()
				.category(Category.log_request_continue)
				.status(Status.incomplete)
				.build();
		final Message newResult = Message.builder()
				.category(Category.log_environment)
				.status(Status.incomplete)
				.build();

		// execute test
		final Message result = ResultJoiner.join(oldResult, newResult);

		// check result
		assertNotNull(result);
		assertEquals(Status.incomplete, result.getStatus());
		assertNull(result.getEnvironment());
		assertEquals(Category.log_request_continue, result.getCategory());
	}

}
