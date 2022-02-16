package com.demo.issue;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/test")
public class TestController {

	private TestProcessor testProcessor;

	public TestController(TestProcessor testProcessor) {
		this.testProcessor = testProcessor;
	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping(path = "/entity", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<TestEntity>> getVouchers(@RequestParam String routVar, @RequestParam String searchStr) {
		return testProcessor.getEntity(routVar, searchStr);
	}

	@GetMapping(path = "/entity/count", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	public Mono<Long> getVoucherCount(@RequestParam String routVar, @RequestParam String searchStr) {
		return testProcessor.getCount(routVar, searchStr);
	}
}
