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
	private ChannelAccount from;
	private ConversationAccount conversation;
	private ChannelAccount recipient;
	private String text;
	private String replyToId;

	public static WebhookResponseBuilder builder(@NonNull final WebhookRequest webhookRequest) {
		return new WebhookResponseBuilder().replyToId(webhookRequest.getId())
				.conversation(webhookRequest.getConversation())
				.recipient(webhookRequest.getFrom());
	}

}
