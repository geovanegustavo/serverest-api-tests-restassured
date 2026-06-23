package serverest.tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import serverest.model.Usuario;
import serverest.util.TokenHolder;
import serverest.util.UsuarioHelper;

import static serverest.util.Mensagens.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class UsuarioTest {

    static {
        RestAssured.baseURI = "https://serverest.dev";
    }

    String usuarioId;
    String email = UsuarioHelper.gerarEmail();
    String senha = "1234";
    Usuario usuarioCriado = new Usuario("Admin QA", email, senha, "true");

    @Test(description = "Deve cadastrar um usuário administrador com credenciais válidas")
    public void criarUsuarioAdmin() {
        usuarioId = given()
            .contentType("application/json")
            .log().all()
            .body(usuarioCriado)
        .when()
            .post("/usuarios")
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo(MSG_CADASTRO_SUCESSO))
            .body("_id", notNullValue())
            .body(matchesJsonSchemaInClasspath("schemas/usuario/cadastrar-usuario-schema.json"))
            .extract()
            .path("_id");

        TokenHolder.email = email;
        TokenHolder.password = senha;
    }

    @Test(
            dependsOnMethods = "criarUsuarioAdmin",
            description = "Deve listar o usuário cadastrado pelo id"
    )
    public void listarUsuarioPorId() {
        given()
            .pathParam("id", usuarioId)
            .log().all()
        .when()
            .get("/usuarios/{id}")
        .then()
            .log().all()
            .statusCode(200)
            .body("_id", equalTo(usuarioId))
            .body("nome", equalTo(usuarioCriado.getNome()))
            .body("email", equalTo(usuarioCriado.getEmail()))
            .body("password", equalTo(usuarioCriado.getPassword()))
            .body("administrador", equalTo(usuarioCriado.getAdministrador()))
            .body(matchesJsonSchemaInClasspath("schemas/usuario/listar-usuario-schema.json"));
    }
}
