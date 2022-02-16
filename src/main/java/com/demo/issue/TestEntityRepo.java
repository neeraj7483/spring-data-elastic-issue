package com.demo.issue;

import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

public interface TestEntityRepo extends ReactiveElasticsearchRepository<TestEntity, String> {

}
