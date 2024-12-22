package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CheckListenIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CheckListenRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testGetCheckListenByTitle() throws Exception {
        CheckList check1 = new CheckList();
        check1.setUeberschrift("Test Title 1");
        repository.save(check1);

        CheckList check2 = new CheckList();
        check2.setUeberschrift("Another Title");
        repository.save(check2);

        String url = "http://localhost:" + port + "/checklisten/search?title="+check1.getUeberschrift();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<CheckList> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUeberschrift()).isEqualTo("Test Title 1");
    }
}