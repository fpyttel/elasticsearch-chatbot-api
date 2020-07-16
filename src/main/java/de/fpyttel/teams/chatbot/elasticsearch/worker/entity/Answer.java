package de.fpyttel.teams.chatbot.elasticsearch.worker.entity;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class Answer {

	@NonNull
	private String text;
	private int numberOfParams;

	public String getText(final Object... params) {
		if (numberOfParams > 0) {
			if (numberOfParams == params.length) {
				return String.format(text, params);
			} else {
				return null;
			}
		}
		return text;
	}

}
