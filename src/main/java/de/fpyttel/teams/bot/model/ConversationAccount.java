package de.fpyttel.teams.bot.model;

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
public class ConversationAccount {

	public enum Type {
		channel, personal, group
	}

	private String id;
	private String name;
	private Type conversationType;
	private String tenantId;
	private boolean isGroup;
	private Role role;
	private String aadObjectId;

}
