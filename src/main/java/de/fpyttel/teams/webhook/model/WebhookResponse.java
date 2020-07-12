package de.fpyttel.teams.webhook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@AllArgsConstructor
@Getter
public class WebhookResponse {

	private Type type;
	private From from;
	private Conversation conversation;
	private Recipient recipient;
	private String text;
	private String replyToId;

	public static WebhookResponseBuilder builder(@NonNull final WebhookRequest webhookRequest) {
		return new WebhookResponseBuilder().replyToId(webhookRequest.getId())
				.conversation(webhookRequest.getConversation())
				.recipient(Recipient.builder()
						.id(webhookRequest.getFrom().getId())
						.name(webhookRequest.getFrom().getName())
						.build());
	}

}
