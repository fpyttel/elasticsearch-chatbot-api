package de.fpyttel.teams.webhook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class ChannelAccount {

	private String id;
	private String name;
	private String aadObjectId;
	
}
