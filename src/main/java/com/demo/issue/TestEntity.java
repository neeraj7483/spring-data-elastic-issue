package com.demo.issue;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Routing;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "test", createIndex = true)
@Setting(shards = 2, replicas = 1)
@Routing(value = "routVar")
public class TestEntity {

	@Id
	private String id;
	@Field(type = FieldType.Keyword)
	private String routVar;
	private int testVar;
	@Field(type = FieldType.Keyword)
	private String searchStr;

	public TestEntity() {
	}

	public TestEntity(String id, String routVar, int testVar, String searchStr) {
		this.id = id;
		this.routVar = routVar;
		this.testVar = testVar;
		this.searchStr = searchStr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoutVar() {
		return routVar;
	}

	public void setRoutVar(String routVar) {
		this.routVar = routVar;
	}

	public int getTestVar() {
		return testVar;
	}

	public void setTestVar(int testVar) {
		this.testVar = testVar;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	@Override
	public String toString() {
		return "TestEntity [id=" + id + ", routVar=" + routVar + ", testVar=" + testVar + ", searchStr=" + searchStr
				+ "]";
	}

}
