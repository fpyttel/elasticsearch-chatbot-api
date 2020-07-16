package de.fpyttel.teams.bot.parser;

import lombok.NonNull;

public class ResultJoiner {

	public static ParserResult join(final ParserResult oldResult, @NonNull final ParserResult newResult) {
		if (oldResult != null && ParserResult.Status.incomplete == newResult.getStatus()
				&& ParserResult.Status.incomplete == oldResult.getStatus()) {
			newResult.setEnvironment(
					oldResult.getEnvironment() != null ? oldResult.getEnvironment() : newResult.getEnvironment());
			newResult.setCategory(CategoryType.log == newResult.getCategoryType() || CategoryType.log == oldResult
					.getCategoryType()
							? Category.log_request
							: newResult.getCategory());
			newResult.setStatus(newResult.getEnvironment() != null
					&& CategoryType.log == newResult
							.getCategoryType()
									? ParserResult.Status.complete
									: ParserResult.Status.incomplete);
		}
		return newResult;
	}

}
