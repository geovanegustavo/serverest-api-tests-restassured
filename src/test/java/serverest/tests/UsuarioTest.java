package serverest.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import serverest.model.Usuario;
import serverest.util.TokenHolder;
import serverest.util.UsuarioHelper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class UsuarioTest {

    static {
        RestAssured.baseURI = "https://serverest.dev";
    }

    @Test
    public void criarUsuarioAdmin() {
        String email = UsuarioHelper.gerarEmail();
        String senha = "1234";

        Usuario usuario = new Usuario("Admin QA", email, senha, "true");

        given()
            .contentType("application/json")
            .log().all()
            .body(usuario)
        .when()
            .post("/usuarios")
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("Cadastro realizado com sucesso"))
            .body("_id", notNullValue())
            .body(matchesJsonSchemaInClasspath("schemas/usuario-schema.json"));

        TokenHolder.email = email;
        TokenHolder.password = senha;
    }
}
