package de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Action {

	private Type type;
	private String id;
	private LocalDateTime timestamp;
	private URL serviceUrl;
	private String channelId;
	private ChannelAccount from;
	private ConversationAccount conversation;
	private ChannelAccount recipient;
	private String text;
	private String textFormat;
	private Locale locale;
	private String replyToId;

	public ActionBuilder replyBuilder() {
		return Action.builder()
				.replyToId(id)
				.serviceUrl(serviceUrl)
				.conversation(conversation)
				.type(Type.message)
				.from(recipient)
				.recipient(from);
	}

}
