package de.fpyttel.teams.webhook.api;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fpyttel.teams.webhook.model.From;
import de.fpyttel.teams.webhook.model.Recipient;
import de.fpyttel.teams.webhook.model.WebhookRequest;
import de.fpyttel.teams.webhook.model.WebhookResponse;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

	@PostMapping
	WebhookResponse hook(@RequestBody @NonNull WebhookRequest webhookRequest) {
		return WebhookResponse.builder(webhookRequest).text("Hi I'm here!").build();
	}

}
