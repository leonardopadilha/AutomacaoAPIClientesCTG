import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TesteClienteSemRefatorar {

    String enderecoApiCliente = "http://localhost:8080/";
    String endpointCliente = "cliente";
    String apagarTodos = "/apagaTodos";
    String listaVazia = "{}";

    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar clientes, Então a lista deve estar vazia")
    public void quandoPegarListaSemCadastrar_EntaoListaDeveSerVazia() {
        apagaTodosClientes();

        given()
                .contentType(ContentType.JSON)
        .when()
                .get(enderecoApiCliente)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(containsString(listaVazia));
    }

    @Test
    @DisplayName("Quando cadastrar um cliente, Então ele deve estar disponível no resultado")
    public void cadastraCliente() {
        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 35,\n" +
                "  \"nome\": \"Homem de Ferro\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"1\":{\"nome\":\"Homem de Ferro\",\"idade\":35,\"id\":1,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(enderecoApiCliente + endpointCliente)
        .then()
                .statusCode(201)
                .assertThat().body(containsString(respostaEsperada));
    }

    @Test
    @DisplayName("Quando alterar um cliente, Então o cliente deverá ser alterado")
    public void alteraCliente() {

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 35,\n" +
                "  \"nome\": \"Homem de Ferro\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String clienteParaAlterar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 35,\n" +
                "  \"nome\": \"Homem de Ferro 3\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"1\":{\"nome\":\"Homem de Ferro 3\",\"idade\":35,\"id\":1,\"risco\":0}}";

        //Incluindo cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(enderecoApiCliente + endpointCliente)
        .then()
                .statusCode(201);

        //Alterando cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaAlterar)
        .when()
                .put(enderecoApiCliente + endpointCliente)
        .then()
                .statusCode(200)
                .assertThat().body(containsString(respostaEsperada));
    }

    @Test
    @DisplayName("Quando deletar um cliente, Então o cliente deverá ser apagado")
    public void deletarCliente() {
        String clienteParaCadastrar = "{\n" +
                "  \"id\": 10,\n" +
                "  \"idade\": 90,\n" +
                "  \"nome\": \"Hulk\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{ NOME: Hulk, IDADE: 90, ID: 10 }";

        int idCliente = 10;

        //Incluindo cliente
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(enderecoApiCliente + endpointCliente)
        .then()
                .statusCode(201);

        //Deletando cliente
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(enderecoApiCliente + endpointCliente + "/" + idCliente)
        .then()
                .statusCode(200)
                .assertThat().body(containsString(respostaEsperada));
    }


    //Método de apoio
    public void apagaTodosClientes() {

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(enderecoApiCliente + endpointCliente + apagarTodos)
        .then()
                .statusCode(200)
                .assertThat().body(containsString(listaVazia));
    }
}
