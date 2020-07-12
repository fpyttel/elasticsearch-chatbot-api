package de.fpyttel.teams.webhook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class ConversationAccount {

	public enum Type {
		channel
	}

	private String id;
	private String name;
	private Type conversationType;
	private String tenantId;
	private boolean isGroup;
	private Role role;
	private String aadObjectId;

}
