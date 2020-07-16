package de.fpyttel.teams.webhook.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.fpyttel.teams.bot.model.Environment;
import de.fpyttel.teams.bot.parser.Category;
import de.fpyttel.teams.bot.parser.ParserResult;
import de.fpyttel.teams.bot.parser.ParserResult.Status;
import de.fpyttel.teams.bot.parser.ResultJoiner;

public class ResultJoinerTest {

	@Test
	public void joinTest() {
		// mock data
		final ParserResult oldResult = ParserResult.builder()
				.category(Category.log_request_continue)
				.status(Status.incomplete)
				.build();
		final ParserResult newResult = ParserResult.builder()
				.category(Category.log_request)
				.environment(Environment.PROD)
				.status(Status.incomplete)
				.build();

		// execute test
		final ParserResult result = ResultJoiner.join(oldResult, newResult);

		// check result
		assertNotNull(result);
		assertEquals(Status.complete, result.getStatus());
		assertEquals(Environment.PROD, result.getEnvironment());
		assertEquals(Category.log_request, result.getCategory());
	}

	@Test
	public void joinTest_incomplete() {
		// mock data
		final ParserResult oldResult = ParserResult.builder()
				.category(Category.log_request_continue)
				.status(Status.incomplete)
				.build();
		final ParserResult newResult = ParserResult.builder()
				.category(Category.log_environment)
				.status(Status.incomplete)
				.build();

		// execute test
		final ParserResult result = ResultJoiner.join(oldResult, newResult);

		// check result
		assertNotNull(result);
		assertEquals(Status.incomplete, result.getStatus());
		assertNull(result.getEnvironment());
		assertEquals(Category.log_request_continue, result.getCategory());
	}

}
