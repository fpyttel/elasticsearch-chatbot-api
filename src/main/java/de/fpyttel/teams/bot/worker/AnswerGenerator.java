package de.fpyttel.teams.bot.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import de.fpyttel.teams.bot.parser.Category;
import de.fpyttel.teams.bot.parser.ParserResult;
import lombok.NonNull;

@Component
public class AnswerGenerator {

	private static final Map<Category, List<String>> answerMap = new HashMap<>();

	static {
		// add greeting answers
		final List<String> greetingAnswers = new ArrayList<>();
		greetingAnswers.add("Cheers");
		greetingAnswers.add("Hi");
		greetingAnswers.add("Hey");
		greetingAnswers.add("Hello");
		answerMap.put(Category.conversation_greeting, greetingAnswers);

		// add error answers
		final List<String> errorAnswers = new ArrayList<>();
		errorAnswers.add("I'm not getting you, sorry.");
		errorAnswers.add("I don't understand you.");
		errorAnswers.add("What are you talking about buddy!?");
		answerMap.put(Category.error, errorAnswers);
	}

	public String generate(@NonNull final ParserResult parserResult) {
		// determine answer list
		final List<String> answers = answerMap.getOrDefault(parserResult.getCategory(), answerMap.get(Category.error));
		// select random answer
		return answers.get(ThreadLocalRandom.current().nextInt(answers.size()) % answers.size());
	}

}
