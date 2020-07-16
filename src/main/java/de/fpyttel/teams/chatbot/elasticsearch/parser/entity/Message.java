package de.fpyttel.teams.chatbot.elasticsearch.parser.entity;

import de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Message {

	public enum Status {
		complete, incomplete, error
	}
	
	private Category category;
	private Status status;
	private Environment environment;
	
	public CategoryType getCategoryType() {
		return category != null ? CategoryType.valueOf(category) : null;
	}

}
