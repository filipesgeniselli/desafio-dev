package com.filipegeniselli.desafiodev;

import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"})
class UploadedTransactionControllerTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.port = port;
        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test
    @Order(1)
    void uploadCnab_ShouldReturnNoContent() throws IOException {
        File file = TestUtils.getFile("validCNAB.txt");

        String accessToken = TestUtils.getAccessToken();
        String uploadLocation = TestUtils.uploadCnabAndWaitFinalStatus(file, accessToken);

        given()
                .auth().oauth2(accessToken)
                .get(uploadLocation)
                .then()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("fileName", equalTo("validCNAB.txt"))
                .body("insertTime", notNullValue())
                .body("startProcessTime", notNullValue())
                .body("endProcessTime", notNullValue())
                .body("errorReason", nullValue());

        List<String> errorList = given()
                .auth().oauth2(accessToken)
                .get(uploadLocation + "/lines")
                .then()
                .statusCode(200)
                .body(".", hasSize(21))
                .extract()
                .jsonPath()
                .getList("errorDescription", String.class);
        assertThat(errorList.stream().filter(StringUtils::hasText).collect(Collectors.toList()), hasSize(0));
    }
    
    @Test
    void getStores_ShouldReturnOK() {
        String accessToken = TestUtils.getAccessToken();

        given()
                .auth().oauth2(accessToken)
                .get("/transactions/stores")
                .then()
                .statusCode(200)
                .body("id", hasItems(1,2,3,4,5))
                .body("name", hasItems("BAR DO JOÃO",
                        "LOJA DO Ó - FILIAL",
                        "LOJA DO Ó - MATRIZ",
                        "MERCADO DA AVENIDA",
                        "MERCEARIA 3 IRMÃOS"
                ))
                .body("owner", hasItems("JOÃO MACEDO",
                        "MARIA JOSEFINA",
                        "MARCOS PEREIRA",
                        "JOSÉ COSTA"
                ))
                .body("balance", hasItems(-102f, 152.32f, 230f, 489.2f, -7023f));
    }
    
    @Test
    void getTransactions_shouldReturnOk() {
        String accessToken = TestUtils.getAccessToken();
        Integer id = given()
                .auth().oauth2(accessToken)
                .get("/transactions/stores")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getList("findAll { it.name == \"BAR DO JOÃO\" }.id", Integer.class)
                .get(0);
        
        given()
                .auth().oauth2(accessToken)
                .get(String.format("/transactions/stores/%s/transactions", id))
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("type", hasItems("FINANCIAMENTO", "BOLETO", "DEBITO"))
                .body("amount", hasItems(-142f, -112f, 152f));
    }
    
}
