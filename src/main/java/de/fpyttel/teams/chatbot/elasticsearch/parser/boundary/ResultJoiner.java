package de.fpyttel.teams.chatbot.elasticsearch.parser.boundary;

import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Category;
import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.CategoryType;
import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Message;
import lombok.NonNull;

public class ResultJoiner {

	public static Message join(final Message oldResult, @NonNull final Message newResult) {
		if (oldResult != null && (Message.Status.incomplete == newResult.getStatus()
				|| Message.Status.incomplete == oldResult.getStatus())) {
			// join environment
			newResult.setEnvironment(
					oldResult.getEnvironment() != null ? oldResult.getEnvironment() : newResult.getEnvironment());
			// join status
			newResult.setStatus(newResult.getEnvironment() != null && CategoryType.log == newResult.getCategoryType()
					? Message.Status.complete
					: Message.Status.incomplete);
			// join category
			if (CategoryType.log == newResult.getCategoryType()) {
				if (Message.Status.complete == newResult.getStatus()) {
					newResult.setCategory(Category.log_request);
				} else {
					newResult.setCategory(Category.log_request_continue);
				}
			}
		}
		return newResult;
	}

}
