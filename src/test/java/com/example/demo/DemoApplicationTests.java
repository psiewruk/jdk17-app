package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}
}
