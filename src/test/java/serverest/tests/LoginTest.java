package serverest.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import serverest.util.TokenHolder;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {

    static {
        RestAssured.baseURI = "https://serverest.dev";
    }

    @Test(
            dependsOnMethods = "cadastrarUsuarioAdmin",
            description = "Deve logar usuário administrador com credenciais válidas"
    )
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
            .body(matchesJsonSchemaInClasspath("schemas/login/realizar-login-schema.json"))
            .extract().response();

        TokenHolder.token = response.jsonPath().getString("authorization").replace("Bearer ", "");
    }
}
