package de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity;

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
public class ChannelAccount {

	private String id;
	private String name;
	private String aadObjectId;
	
}
