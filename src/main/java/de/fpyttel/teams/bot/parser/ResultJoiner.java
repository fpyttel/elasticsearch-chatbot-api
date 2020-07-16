package de.fpyttel.teams.bot.parser;

import lombok.NonNull;

public class ResultJoiner {

	public static ParserResult join(final ParserResult oldResult, @NonNull final ParserResult newResult) {
		if (oldResult != null && (ParserResult.Status.incomplete == newResult.getStatus()
				|| ParserResult.Status.incomplete == oldResult.getStatus())) {
			// join environment
			newResult.setEnvironment(
					oldResult.getEnvironment() != null ? oldResult.getEnvironment() : newResult.getEnvironment());
			// join status
			newResult.setStatus(newResult.getEnvironment() != null && CategoryType.log == newResult.getCategoryType()
					? ParserResult.Status.complete
					: ParserResult.Status.incomplete);
			// join category
			if (CategoryType.log == newResult.getCategoryType()) {
				if (ParserResult.Status.complete == newResult.getStatus()) {
					newResult.setCategory(Category.log_request);
				} else {
					newResult.setCategory(Category.log_request_continue);
				}
			}
		}
		return newResult;
	}

}
