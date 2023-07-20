package com.filipegeniselli.desafiodev;

import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"})
class TransactionControllerTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.port = port;
        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test
    void uploadCnab_ShouldReturnNoContent() throws IOException {
        File file = getFile("CNAB.txt");

        String uploadLocation = given()
                .multiPart(file)
                .post("/transactions/upload")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract()
                .header("Location");

        long currentTime = System.currentTimeMillis();
        String status = "";
        do {
            status = given()
                    .get(uploadLocation)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .jsonPath()
                    .getString("status");

            assertTrue(System.currentTimeMillis() - currentTime < 5000,
                    "Processamento do arquivo atingiu o tempo limite");
        } while (!List.of("SUCCESS", "ERROR").contains(status));

        given()
                .get(uploadLocation)
                .then()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("fileName", equalTo("CNAB.txt"))
                .body("insertTime", notNullValue())
                .body("startProcessTime", notNullValue())
                .body("endProcessTime", notNullValue())
                .body("errorReason", nullValue());

        List<String> errorList = given()
                .get(uploadLocation + "/lines")
                .then()
                .statusCode(200)
                .body(".", hasSize(21))
                .extract()
                .jsonPath()
                .getList("errorDescription", String.class);
        assertThat(errorList.stream().filter(x -> StringUtils.hasText(x)).collect(Collectors.toList()), hasSize(0));
    }

    private File getFile(String resourceName) throws IOException {
        return new ClassPathResource(resourceName).getFile();
    }

}
