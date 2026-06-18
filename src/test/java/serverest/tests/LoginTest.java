package serverest.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import serverest.util.TokenHolder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {

    static {
        RestAssured.baseURI = "https://serverest.dev";
    }

    @Test(dependsOnMethods = "serverest.tests.UsuarioTest.criarUsuarioAdmin")
    public void loginAdmin() {
        String body = "{ \"email\": \"" + TokenHolder.email + "\", \"password\": \"" + TokenHolder.password + "\" }";

        Response response = given()
            .contentType("application/json")
            .log().all()
            .body(body)
        .when()
            .post("/login")
        .then()
            .log().all()
            .statusCode(200)
            .body("message", equalTo("Login realizado com sucesso"))
            .extract().response();

        TokenHolder.token = response.jsonPath().getString("authorization").replace("Bearer ", "");
    }
}
