package serverest.tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import serverest.model.Produto;
import serverest.util.TokenHolder;
import serverest.util.ProdutoHelper;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ProdutoTest {

    static {
        RestAssured.baseURI = "https://serverest.dev";
    }

    String produtoId;
    Produto produtoCriado = ProdutoHelper.gerarProdutoAleatorio();

    @Test(description = "Deve cadastrar um produto aleatório na base de dados")
    public void cadastrarProduto() {

        produtoId = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + TokenHolder.token)
            .log().body()
            .body(produtoCriado)
        .when()
            .post("/produtos")
        .then()
            .log().ifValidationFails()
            .statusCode(201)
            .body("message", equalTo("Cadastro realizado com sucesso"))
            .body("_id", notNullValue())
            .body(matchesJsonSchemaInClasspath("schemas/cadastrar-produto-schema.json"))
            .extract()
            .path("_id");
    }

    @Test(
        dependsOnMethods = "cadastrarProduto",
        description = "NÃO deve cadastrar um produto já existente na base de dados"
    )
    public void cadastrarProdutoExistente() {

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + TokenHolder.token)
            .log().body()
            .body(produtoCriado)
        .when()
            .post("/produtos")
        .then()
            .log().ifValidationFails()
            .statusCode(400)
            .body("message", equalTo("Já existe produto com esse nome"))
            .body(matchesJsonSchemaInClasspath("schemas/cadastrar-produto-cadastrado-schema.json"));

    }

    @Test(
        dependsOnMethods = "cadastrarProduto",
        description = "Deve listar o produto cadastrado pelo id"
    )
    public void listarProduto() {
        given()
            .header("Authorization", "Bearer " + TokenHolder.token)
            .pathParam("id", produtoId)
            .log().uri()
        .when()
            .get("/produtos/{id}")
        .then()
            .log().ifValidationFails()
            .statusCode(200)
            .body("_id", equalTo(produtoId))
            .body("nome", equalTo(produtoCriado.getNome()))
            .body("preco", equalTo(produtoCriado.getPreco()))
            .body("descricao", equalTo(produtoCriado.getDescricao()))
            .body("quantidade", equalTo(produtoCriado.getQuantidade()))
            .body(matchesJsonSchemaInClasspath("schemas/listar-produto-schema.json"));
    }
}
