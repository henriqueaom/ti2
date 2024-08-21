package exercicio2;

public class Produto {
    private int id;
    private String nome;
    private int preco;

    // Construtores
    public Produto() {}

    public Produto(String nome, int preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(int id ,String nome, int preco) {
        this.nome = nome;
        this.preco = preco;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }
}
