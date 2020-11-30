package nl.leonw.springmicrodemo.adservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.vault.token=123"})
class AdserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
