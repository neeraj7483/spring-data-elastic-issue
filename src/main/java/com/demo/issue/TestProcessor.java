package com.demo.issue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.routing.RoutingResolver;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class TestProcessor {

	private ReactiveElasticsearchOperations reactiveElasticsearchOperations;

	public TestProcessor(ReactiveElasticsearchOperations reactiveElasticsearchOperations) {
		this.reactiveElasticsearchOperations = reactiveElasticsearchOperations;
	}

	public Mono<List<TestEntity>> getEntity(String routVar, String searchStr) {
		BoolQueryBuilder boolQuerybuilders = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("searchStr", searchStr));

		Query query = new NativeSearchQueryBuilder().withFilter(boolQuerybuilders).withPageable(PageRequest.of(0, 10))
				.withSort(SortBuilders.fieldSort("testVar").order(SortOrder.DESC)).build();

		return reactiveElasticsearchOperations.withRouting(RoutingResolver.just(routVar))
				.searchForPage(query, TestEntity.class).map(page -> {
					List<TestEntity> list = new ArrayList<>();
					for (SearchHit<TestEntity> searchHit : page.get().collect(Collectors.toList())) {
						System.out.println("Routing: " + searchHit.getRouting());
						list.add(searchHit.getContent());
					}
					return list;
				});
	}

	public Mono<Long> getCount(String routVar, String searchStr) {
		BoolQueryBuilder boolQuerybuilders = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("searchStr", searchStr));

		Query query = new NativeSearchQueryBuilder().withFilter(boolQuerybuilders).withPageable(PageRequest.of(0, 10))
				.withSort(SortBuilders.fieldSort("testVar").order(SortOrder.DESC)).build();

		return reactiveElasticsearchOperations.withRouting(RoutingResolver.just(routVar)).count(query,
				TestEntity.class);
	}
}
