package com.demo.issue;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
public class ReactiveElasticRestClientConfiguration extends AbstractReactiveElasticsearchConfiguration {

	@Override
	public ReactiveElasticsearchClient reactiveElasticsearchClient() {
		return ReactiveRestClients.create(ClientConfiguration.builder().connectedTo("localhost:9200")
				.withSocketTimeout(Duration.ofSeconds(200)).withWebClientConfigurer(webClient -> {
					ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
							.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(26214400)).build();
					return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
				}).build());
	}
	
//	@Override
//	public ReactiveElasticsearchOperations reactiveElasticsearchTemplate(ElasticsearchConverter elasticsearchConverter,
//			ReactiveElasticsearchClient reactiveElasticsearchClient) {
//
//		CustomReactiveElasticSearchTemplate template = new CustomReactiveElasticSearchTemplate(
//				reactiveElasticsearchClient, elasticsearchConverter);
//		template.setIndicesOptions(indicesOptions());
//		template.setRefreshPolicy(refreshPolicy());
//
//		return template;
//	}

}
