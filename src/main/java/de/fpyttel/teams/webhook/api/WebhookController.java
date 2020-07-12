package de.fpyttel.teams.webhook.api;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fpyttel.teams.webhook.model.Action;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

	@PostMapping
	Action hook(@RequestBody @NonNull Action action) {
		return action.replyBuilder().text("Hi I'm here!").build();
	}

}
