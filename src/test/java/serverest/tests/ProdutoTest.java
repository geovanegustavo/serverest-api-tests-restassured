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

    private static final String MSG_CADASTRO_SUCESSO = "Cadastro realizado com sucesso";
    private static final String MSG_REGISTRO_ALTERADO = "Registro alterado com sucesso";
    private static final String MSG_REGISTRO_EXCLUIDO = "Registro excluído com sucesso";
    private static final String MSG_PRODUTO_EXISTENTE = "Já existe produto com esse nome";
    private static final String MSG_TOKEN_INVALIDO = "Token de acesso ausente, inválido, expirado ou usuário do token não existe mais";
    private static final String MSG_PRODUTO_NAO_ENCONTRADO = "Produto não encontrado";

    String produtoId;
    Produto produtoCriado = ProdutoHelper.gerarProdutoAleatorio();

    @Test(description = "Deve cadastrar um produto aleatório na base de dados")
    public void cadastrarProduto() {
        produtoId = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + TokenHolder.token)
            .log().all()
            .body(produtoCriado)
        .when()
            .post("/produtos")
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo(MSG_CADASTRO_SUCESSO))
            .body("_id", notNullValue())
            .body(matchesJsonSchemaInClasspath("schemas/cadastrar-produto-schema.json"))
            .extract()
            .path("_id");
    }

    @Test(
        dependsOnMethods = "cadastrarProduto",
        description = "Deve listar o produto cadastrado pelo id"
    )
    public void listarProdutoPorId() {
        given()
            .header("Authorization", "Bearer " + TokenHolder.token)
            .pathParam("id", produtoId)
            .log().all()
        .when()
            .get("/produtos/{id}")
        .then()
            .log().all()
            .statusCode(200)
            .body("_id", equalTo(produtoId))
            .body("nome", equalTo(produtoCriado.getNome()))
            .body("preco", equalTo(produtoCriado.getPreco()))
            .body("descricao", equalTo(produtoCriado.getDescricao()))
            .body("quantidade", equalTo(produtoCriado.getQuantidade()))
            .body(matchesJsonSchemaInClasspath("schemas/listar-produto-schema.json"));
    }

    @Test(
            dependsOnMethods = "cadastrarProduto",
            description = "Deve pesquisar produto cadastrado pelo nome"
    )
    public void pesquisarProdutoPorNome() {
        given()
            .header("Authorization", "Bearer " + TokenHolder.token)
            .queryParam("nome", produtoCriado.getNome())
            .log().all()
        .when()
            .get("/produtos")
        .then()
            .log().all()
            .statusCode(200)
            .body("produtos[0]._id", equalTo(produtoId))
            .body("produtos[0].nome", equalTo(produtoCriado.getNome()))
            .body("produtos[0].preco", equalTo(produtoCriado.getPreco()))
            .body("produtos[0].descricao", equalTo(produtoCriado.getDescricao()))
            .body("produtos[0].quantidade", equalTo(produtoCriado.getQuantidade()))
            .body(matchesJsonSchemaInClasspath("schemas/pesquisar-produto-schema.json"));
    }

    @Test(
        dependsOnMethods = "listarProdutoPorId",
        description = "Deve editar o produto já cadastrado"
    )
    public void editarProduto() {
        // cria um novo objeto com dados atualizados
        Produto produtoEditado = new Produto(
        produtoCriado.getNome() + " - Edição",
        produtoCriado.getPreco() + 50,
        produtoCriado.getDescricao() + " (atualizado)",
        produtoCriado.getQuantidade() + 10
        );

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + TokenHolder.token)
            .pathParam("id", produtoId)
            .log().all()
            .body(produtoEditado)
        .when()
            .put("/produtos/{id}")
        .then()
            .log().all()
            .statusCode(200)
            .body("message", equalTo(MSG_REGISTRO_ALTERADO))
            .body(matchesJsonSchemaInClasspath("schemas/editar-produto-schema.json"));

    }

    @Test(
        dependsOnMethods = "editarProduto",
        description = "Deve excluir o produto cadastrado pelo id"
    )
    public void excluirProduto() {
        given()
            .header("Authorization", "Bearer " + TokenHolder.token)
            .pathParam("id", produtoId)
            .log().all()
        .when()
            .delete("/produtos/{id}")
        .then()
            .log().all()
            .statusCode(200)
            .body("message", equalTo(MSG_REGISTRO_EXCLUIDO))
            .body(matchesJsonSchemaInClasspath("schemas/excluir-produto-schema.json"));
    }

    @Test(
        dependsOnMethods = "excluirProduto",
        description = "Não deve encontrar produto já excluído"
    )
    public void listarProdutoExcluido() {
        given()
            .header("Authorization", "Bearer " + TokenHolder.token)
            .pathParam("id", produtoId)
            .log().all()
        .when()
            .get("/produtos/{id}")
        .then()
            .log().all()
            .statusCode(400)
            .body("message", equalTo(MSG_PRODUTO_NAO_ENCONTRADO))
            .body(matchesJsonSchemaInClasspath("schemas/listar-produto-excluido-schema.json"));
    }

    /**
     * CENÁRIOS DE EXCEÇÃO
     */

    @Test(
        dependsOnMethods = "cadastrarProduto",
        description = "NÃO deve cadastrar um produto já existente na base de dados"
    )
    public void cadastrarProdutoExistente() {

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + TokenHolder.token)
            .log().all()
            .body(produtoCriado)
        .when()
            .post("/produtos")
        .then()
            //.log().ifValidationFails()
            .log().all()
            .statusCode(400)
            .body("message", equalTo(MSG_PRODUTO_EXISTENTE))
            .body(matchesJsonSchemaInClasspath("schemas/cadastrar-produto-cadastrado-schema.json"));

    }

    @Test(
        dependsOnMethods = "cadastrarProduto",
        description = "NÃO deve cadastrar um produto sem token de autenticação"
    )
    public void cadastrarProdutoSemToken() {

        given()
            .contentType("application/json")
            //.header("Authorization", "Bearer " + TokenHolder.token)
            .log().all()
            .body(produtoCriado)
            .when()
            .post("/produtos")
            .then()
            //.log().ifValidationFails()
            .log().all()
            .statusCode(401)
            .body("message", equalTo(MSG_TOKEN_INVALIDO))
            .body(matchesJsonSchemaInClasspath("schemas/cadastrar-produto-sem-token-schema.json"));

    }

}
