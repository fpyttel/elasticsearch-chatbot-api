package de.fpyttel.teams.chatbot.elasticsearch.client.elasticsearch;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.fpyttel.teams.chatbot.elasticsearch.parser.entity.Environment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ElasticClient {

	public static final long DEFAULT_TIMEFRAME_HOURS = 1L;

	@Value("${elastic.host}")
	private String elasticHost;

	@Value("${elastic.port}")
	private int elasticPort;

	@Value("${elastic.indices.pattern}")
	private String indicesPattern;

	private RestHighLevelClient client;

	@PostConstruct
	private void init() {
		client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticHost, elasticPort, "http")));
	}

	public String[] getIndices() {
		log.info("get all indices by pattern=[{}]", indicesPattern);
		GetIndexRequest request = new GetIndexRequest(indicesPattern);
		try {
			GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
			log.info("found indices=[{}]", Arrays.stream(response.getIndices()).collect(Collectors.joining(",")));
			return response.getIndices();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SearchResponse search(@NonNull final Environment env, @NonNull final String messageQuery) {
		final String[] indices = getIndices();
		if (indices != null && indices.length >= 1) {
			String index = Arrays.stream(indices)
					.filter(i -> i.endsWith(env.toString().toLowerCase()))
					.findFirst()
					.orElse(null);
			if (index == null) {
				log.info("found no matching index for environment=[{}]", env);
				return null;
			}
			return search(index, messageQuery);
		}
		log.info("found no indices for pattern=[{}]", indicesPattern);
		return null;
	}

	public SearchResponse search(@NonNull final String index, @NonNull final String messageQuery) {
		log.info("search is using index=[{}]", index);

		long to = System.currentTimeMillis();
		long from = to - (DEFAULT_TIMEFRAME_HOURS * 60L * 60L * 1000L);

		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.filter(QueryBuilders.wildcardQuery("message", "*" + messageQuery + "*"));
		boolQueryBuilder.filter(QueryBuilders.rangeQuery("@timestamp").gte(from).lte(to).format("epoch_millis"));

		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(boolQueryBuilder);
		searchRequest.source(searchSourceBuilder);

		try {
			return client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
