package de.fpyttel.teams.webhook.model;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class WebhookRequest {

	private Type type;
	private String id;
	private LocalDateTime timestamp;
	private URL serviceUrl;
	private String channelId;
	private From from;
	private Conversation conversation;
	private Recipient recipient;
	private String text;
	private String textFormat;
	private Locale locale;
	
}
