package de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MsToken {

	private String token_type;
	private long expires_in;
	private long ext_expires_in;
	private String access_token;
	
}
