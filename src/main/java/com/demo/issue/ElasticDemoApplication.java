package com.demo.issue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticDemoApplication implements CommandLineRunner {

	@Autowired
	private TestEntityRepo testEntityRepo;

	public static void main(String[] args) {
		SpringApplication.run(ElasticDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		TestEntity test1 = new TestEntity("id1", "rout1", 1, "search1");
		TestEntity test2 = new TestEntity("id2", "rout1", 2, "search1");
		TestEntity test3 = new TestEntity("id3", "rout2", 3, "search2");
		TestEntity test4 = new TestEntity("id4", "rout2", 4, "search2");
		List<TestEntity> list = new ArrayList<>();
		list.add(test1);
		list.add(test2);
		list.add(test3);
		list.add(test4);
		testEntityRepo.saveAll(list).subscribe(abc -> System.out.println("data saved"));

	}

}
