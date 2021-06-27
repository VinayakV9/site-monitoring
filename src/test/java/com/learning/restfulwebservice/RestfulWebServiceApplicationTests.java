package com.learning.restfulwebservice;

import com.learning.restfulwebservice.entity.IntervalType;
import com.learning.restfulwebservice.entity.SiteStatus;
import com.learning.restfulwebservice.model.WebSiteStatusResponse;
import com.learning.restfulwebservice.model.WebSiteTrackingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = { RestfulWebServiceApplication.class
			, ScheduledConfiguration.class, Controller.class
		},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestfulWebServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void test() throws InterruptedException {
		WebSiteTrackingRequest request = new WebSiteTrackingRequest();
		request.setInterval(5);
		request.setIntervalType(IntervalType.SECONDS);
		request.setName("Google");
		request.setUrl("https://www.google.com");

		ResponseEntity<Void> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/site/" + request.getName(), request, Void.class);

		assertEquals(HttpStatus.OK, postResponse.getStatusCode());

		Thread.sleep(10000);

		ResponseEntity<WebSiteStatusResponse> statusResponseEntity = restTemplate.getForEntity("http://localhost:" + port + "/site/" + request.getName() + "/status", WebSiteStatusResponse.class);

		assertEquals(HttpStatus.OK, statusResponseEntity.getStatusCode());
		assertEquals(SiteStatus.UP, statusResponseEntity.getBody().getSiteStatus());

	}

}
