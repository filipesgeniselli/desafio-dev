package com.filipegeniselli.desafiodev;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {

    public static File getFile(String resourceName) throws IOException {
        return new ClassPathResource(resourceName).getFile();
    }

    public static String uploadCnabAndWaitFinalStatus(File file, String accessToken) {
        String uploadLocation = given()
                .multiPart(file)
                .auth().oauth2(accessToken)
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
                    .auth().oauth2(accessToken)
                    .get(uploadLocation)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .jsonPath()
                    .getString("status");

            assertTrue(System.currentTimeMillis() - currentTime < 30 * 1000,
                    "Processamento do arquivo atingiu o tempo limite");
        } while (!List.of("SUCCESS", "ERROR").contains(status));

        return uploadLocation;
    }

    public static String getAccessToken() {
        return given()
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", "desafio-api")
                .formParam("client_secret", "QPURoAD2xvoIhHgqQO0x0aFXD0TsSUaF")
                .post("http://localhost:8080/realms/desafio/protocol/openid-connect/token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("access_token");
    }
    
}
