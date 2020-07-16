package de.fpyttel.teams.chatbot.elasticsearch.client.ms;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity.Action;
import de.fpyttel.teams.chatbot.elasticsearch.client.ms.entity.MsToken;
import lombok.NonNull;

@Component
public class TeamsClient {

	private static final String POST_CONVERSATION_ACTION = "v3/conversations/%s/activities/%s";
	private static final String MS_LOGIN_URL = "https://login.microsoftonline.com/botframework.com/oauth2/v2.0/token";

	@Value("${ms.app.id}")
	private String appId;

	@Value("${ms.app.password}")
	private String appPassword;

	@Autowired
	private ObjectMapper objectMapper;
	
	private MsToken msToken;

	@PostConstruct
	private void init() {
		// get token from MS
		msToken = getToken();
	}

	private MsToken getToken() {
		// create client
		final RestTemplate restTemplate = new RestTemplate();

		// prepare header
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// prepare form data
		final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("grant_type", "client_credentials");
		map.add("client_id", appId);
		map.add("client_secret", appPassword);
		map.add("scope", "https://api.botframework.com/.default");
		
		// prepare request
		final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);

		// execute call
		final ResponseEntity<MsToken> response = restTemplate.postForEntity(MS_LOGIN_URL, request, MsToken.class);

		return response.getBody();
	}

	public String postToConversation(@NonNull final Action action) {
		// create client
		final RestTemplate restTemplate = new RestTemplate();

		// prepare header
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(msToken.getAccess_token());

		// prepare URL
		final String url = action.getServiceUrl().toString()
				+ String.format(POST_CONVERSATION_ACTION, action.getConversation().getId(), action.getReplyToId());

		try {
			// prepare request
			final HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(action), headers);

			// execute call
			return restTemplate.postForObject(url, request, String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

}
