package clientes.clientesApi.TesteApiRefatorado;

public class ClienteRefatorado {
    private String nome;
    private int idade;
    private int id;

    public ClienteRefatorado(String nome, int idade, int id) {
        setNome(nome);
        setIdade(idade);
        setId(id);
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
