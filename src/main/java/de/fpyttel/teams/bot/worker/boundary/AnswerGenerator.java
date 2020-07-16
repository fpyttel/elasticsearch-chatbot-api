package de.fpyttel.teams.bot.worker.boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import de.fpyttel.teams.bot.parser.entity.Category;
import de.fpyttel.teams.bot.parser.entity.Message;
import de.fpyttel.teams.bot.worker.entity.Answer;
import lombok.NonNull;

@Component
public class AnswerGenerator {

	private static final Map<Category, List<Answer>> answerMap = new HashMap<>();

	static {
		// add greeting answers
		final List<Answer> greetingAnswers = new ArrayList<>();
		greetingAnswers.add(new Answer("Cheers"));
		greetingAnswers.add(new Answer("Hi"));
		greetingAnswers.add(new Answer("Hey"));
		greetingAnswers.add(new Answer("Hello"));
		answerMap.put(Category.conversation_greeting, greetingAnswers);

		// add conversation complete answers
		final List<Answer> completeMessages = new ArrayList<>();
		completeMessages.add(new Answer("Bye"));
		completeMessages.add(new Answer("Bye bye"));
		completeMessages.add(new Answer("Cheers"));
		completeMessages.add(new Answer("It was nice to talk with you. Bye!"));
		answerMap.put(Category.conversation_complete, completeMessages);

		// add error answers
		final List<Answer> errorAnswers = new ArrayList<>();
		errorAnswers.add(new Answer("I'm not getting you, sorry."));
		errorAnswers.add(new Answer("I don't understand you."));
		errorAnswers.add(new Answer("What are you talking about buddy!?"));
		answerMap.put(Category.error, errorAnswers);

		// add log request messages
		final List<Answer> logRequestAnswers = new ArrayList<>();
		logRequestAnswers.add(new Answer("Ok, I'll check the logs of environment %s for you...", 1));
		logRequestAnswers.add(new Answer("Sure, let me check %s for you...", 1));
		answerMap.put(Category.log_request, logRequestAnswers);

		// add log request continue messages
		final List<Answer> logRequestContinueAnswers = new ArrayList<>();
		logRequestContinueAnswers.add(new Answer("No problem, but I still need the info about the specific %s!", 1));
		logRequestContinueAnswers.add(new Answer("Sure, but what about the %s?", 1));
		logRequestContinueAnswers.add(new Answer("Of course, but which %s do you mean?", 1));
		answerMap.put(Category.log_request_continue, logRequestContinueAnswers);
	}

	public String generate(@NonNull final Message parserResult) {
		// determine answer list
		final List<Answer> answers = answerMap.getOrDefault(parserResult.getCategory(), answerMap.get(Category.error));
		// select random answer
		final Answer answer = answers.get(ThreadLocalRandom.current().nextInt(answers.size()) % answers.size());
		// add params if needed and return text
		switch (parserResult.getCategory()) {
		case log_request:
			return answer.getText(parserResult.getEnvironment());
		case log_request_continue:
			// TODO calculate missing attributes using the parsing result
			return answer.getText("environment");
		default:
			return answer.getText();
		}
	}

}
