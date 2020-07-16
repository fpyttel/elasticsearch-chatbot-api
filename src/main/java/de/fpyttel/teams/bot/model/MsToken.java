package de.fpyttel.teams.bot.model;

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
