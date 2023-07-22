package com.filipegeniselli.desafiodev;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"})
class TransactionControllerTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.port = port;
    }

    @Test
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
    @WithMockUser
    void uploadCnabWithLineSmallerThanExpected_ShouldReturnBadRequest() throws IOException {
        File file = TestUtils.getFile("invalidCNAB.txt");
        given()
                .auth().oauth2(TestUtils.getAccessToken())
                .multiPart(file)
                .post("/transactions/upload")
                .then()
                .statusCode(400)
                .body("message", equalTo("Linha não está no formato correto"));
    }

    @Test
    @WithMockUser
    void uploadCnabWithLineGreaterThanExpected_ShouldReturnBadRequest() throws IOException {
        File file = TestUtils.getFile("invalidCNAB2.txt");
        given()
                .auth().oauth2(TestUtils.getAccessToken())
                .multiPart(file)
                .post("/transactions/upload")
                .then()
                .statusCode(400)
                .body("message", equalTo("Linha não está no formato correto"));
    }

    @Test
    @WithMockUser
    void uploadDuplicatedCnab_ShouldReturnConflict() throws IOException {
        File file = TestUtils.getFile("duplicatedCNAB.txt");
        String accessToken = TestUtils.getAccessToken();

        TestUtils.uploadCnabAndWaitFinalStatus(file, accessToken);
        given()
                .auth().oauth2(accessToken)
                .multiPart(file)
                .post("/transactions/upload")
                .then()
                .statusCode(409)
                .body("message", equalTo("Arquivo com esse nome já existe."));
    }

    @Test
    @WithMockUser
    void uploadEmtptyCnab_ShouldReturnBadRequest() throws IOException {
        File file = TestUtils.getFile("emptyCNAB.txt");
        given()
                .auth().oauth2(TestUtils.getAccessToken())
                .multiPart(file)
                .post("/transactions/upload")
                .then()
                .statusCode(400)
                .body("message", equalTo("Não é possível processar um arquivo vazio."));
    }

    @Test
    @WithMockUser
    void uploadCnabWithInvalidLines_ShouldReturnNoContent() throws IOException {
        String accessToken = TestUtils.getAccessToken();
        
        File singleLineCnab = TestUtils.getFile("singleLineValidCNAB.txt");
        TestUtils.uploadCnabAndWaitFinalStatus(singleLineCnab, accessToken);
        
        File file = TestUtils.getFile("CNABwithInvalidLines.txt");
        String uploadLocation = TestUtils.uploadCnabAndWaitFinalStatus(file, accessToken);

        given()
                .auth().oauth2(accessToken)
                .get(uploadLocation)
                .then()
                .statusCode(200)
                .body("status", equalTo("ERROR"))
                .body("fileName", equalTo("CNABwithInvalidLines.txt"))
                .body("insertTime", notNullValue())
                .body("startProcessTime", notNullValue())
                .body("endProcessTime", notNullValue())
                .body("errorReason", equalTo("Erro durante o processamento do arquivo, verifique a(s) linha(s) com problema(s)"));

        List<String> errorList = given()
                .auth().oauth2(accessToken)
                .get(uploadLocation + "/lines")
                .then()
                .statusCode(200)
                .body(".", hasSize(9))
                .extract()
                .jsonPath()
                .getList("errorDescription", String.class);
        assertThat(errorList.stream().distinct().toList(), hasItems("Tipo inválido",
                "Data da transação inválida",
                "Valor da transação inválido",
                "Já existe uma transação com os mesmos dados para essa loja",
                "Tipo inválido - Data da transação inválida - Valor da transação inválido"));
    }

    
}
