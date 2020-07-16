package de.fpyttel.teams.chatbot.elasticsearch.worker.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Category;
import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Message;
import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Message.Status;
import de.fpyttel.teams.chatbot.elasticsearch.worker.ConversationWorker;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ConversationTaskFactory {

	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	public ConversationTask createTask(@NonNull final ConversationWorker worker, @NonNull final Message message) {
		if (Category.log_request == message.getCategory() && Status.complete == message.getStatus()) {
			final ConversationTask task = autowireCapableBeanFactory.createBean(ExceptionLookupTask.class);
			task.setConversationWorker(worker);
			task.setMessage(message);
			log.info("new ExceptionLookupTask created");
			return task;
		}
		return null;
	}

}
