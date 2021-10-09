package clientes.clientesApi.TesteApiRefatorado;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestClienteRefatorado {

    private static final String SERVICO_CLIENTE = "http://localhost:8080";
    private static final String RECURSO_CLIENTE = "/cliente";
    private static final String APAGA_TODOS_CLIENTES = "/apagaTodos";
    private static final String RISCO = "/risco/";
    private static final String LISTA_CLIENTES_VAZIA = "{}";
    ClienteRefatorado clienteParaCadastrar = new ClienteRefatorado("Homem de ferro", 45, 10);

    @BeforeEach
    public void testExecutarPrimeiro() {
        testApagaTodosClientesDoServidor();
    }

    @Test
    @DisplayName("Exibir lista de clientes vazia")
    public void testExibirListaClienteVazia() {
        pegaTodosClientes()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo(LISTA_CLIENTES_VAZIA));
    }

    @Test
    @DisplayName("Cadastrar cliente")
    public void testCadastrarCliente() {
        postaClientes(clienteParaCadastrar)
                .statusCode(HttpStatus.SC_CREATED)
                .body("10.nome", equalTo("Homem de ferro"))
                .body("10.idade", equalTo(45))
                .body("10.id", equalTo(10));
    }

    @Test
    @DisplayName("Atualizar dados do cliente")
    public void testAtualizarDadosCliente() {
        postaClientes(clienteParaCadastrar);

        clienteParaCadastrar.setNome("Homem de Ferro");
        clienteParaCadastrar.setId(40);
        clienteParaCadastrar.setId(11);

        atualizaCliente(clienteParaCadastrar)
                .statusCode(HttpStatus.SC_OK)
                .body("11.id", equalTo(11))
                .body("11.idade", equalTo(40))
                .body("11.nome", equalTo("Homem de Ferro"));
    }

    @Test
    @DisplayName("Deletar cliente")
    public void testDeletarCliente() {
        postaClientes(clienteParaCadastrar);

        apagarCliente(clienteParaCadastrar)
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                    .body(not(contains("Homem de ferro")));
    }

    @Test
    @DisplayName("Exibir risco com autorização")
    public void testExibirRiscoComAutorizacao() {
        int riscoEsperado = -50;

        postaClientes(clienteParaCadastrar);

        given()
                .contentType(ContentType.JSON)
                .auth()
                    .basic("aluno", "senha")
        .when()
                .get(SERVICO_CLIENTE + RISCO + clienteParaCadastrar.getId())
        .then()
                .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("risco", equalTo(riscoEsperado));
    }

    private ValidatableResponse apagarCliente(ClienteRefatorado clienteParaApagar) {
        return given()
                    .contentType(ContentType.JSON)
                .when()
                    .delete(SERVICO_CLIENTE + RECURSO_CLIENTE + "/" + clienteParaApagar.getId())
                .then();
    }

    private ValidatableResponse atualizaCliente(ClienteRefatorado clienteParaAtualizar) {
        return given()
                    .contentType(ContentType.JSON)
                    .body(clienteParaAtualizar)
                .when()
                    .put(SERVICO_CLIENTE + RECURSO_CLIENTE)
                .then();
    }

    private ValidatableResponse postaClientes(ClienteRefatorado clienteParaPostar) {
        return given()
                    .contentType(ContentType.JSON)
                    .body(clienteParaPostar)
            .when()
                .post(SERVICO_CLIENTE + RECURSO_CLIENTE)
            .then();
    }

    private ValidatableResponse pegaTodosClientes() {
        return given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(SERVICO_CLIENTE)
                .then();
    }

    //Método de apoio
    public void testApagaTodosClientesDoServidor() {
        when()
                .delete(SERVICO_CLIENTE + APAGA_TODOS_CLIENTES)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                    .body(new IsEqual(LISTA_CLIENTES_VAZIA));
    }
}
