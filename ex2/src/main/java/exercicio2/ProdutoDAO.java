package exercicio2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO() {
        conexao = null;
    }

    // Método para conectar ao PostgreSQL
    public String conectar() {
        // Driver JDBC do PostgreSQL
        String driverName = "org.postgresql.Driver";
        String serverName = "localhost";
        String databaseName = "auto_estacio";
        int porta = 5432; // Porta padrão do PostgreSQL
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + databaseName;

        // Adicionando credenciais para conexão
        String user = "postgres"; // usuário do PostgreSQL
        String password = "ti2cc"; // senha do PostgreSQL

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, user, password); // Conexão está agora aberta
            return "Conectado com sucesso ao banco de dados PostgreSQL";
        } catch (ClassNotFoundException e) {
            return "Driver JDBC não encontrado: " + e.getMessage();
        } catch (SQLException e) {
            return "Erro ao conectar ao banco de dados: " + e.getMessage();
        }
    }

    // Método para incluir um produto na tabela
    public void incluir(Produto produto) {
        String sql = "INSERT INTO produto_ex2 (nome, preco) VALUES (?, ?)";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setInt(2, produto.getPreco());
            pstmt.executeUpdate();
            System.out.println("Produto inserido com sucesso!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para deletar um produto
    public void deletar(String nome) {
        String sql = "DELETE FROM produto_ex2 WHERE nome = ?";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto deletado com sucesso!");
            } else {
                System.out.println("Nenhum produto encontrado com esse nome.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para atualizar o preço de um produto
    public void atualizar(String nome, int valor) {
        String sql = "UPDATE produto_ex2 SET preco = ? WHERE nome = ?";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, valor);
            pstmt.setString(2, nome);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Nenhum produto encontrado com esse nome.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para listar os produtos
    public List<Produto> listar() {
        String sql = "SELECT * FROM produto_ex2";
        List<Produto> produtos = new ArrayList<>();

        try (PreparedStatement pstmt = conexao.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int preco = rs.getInt("preco");

                Produto produto = new Produto(id, nome, preco);
                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }

        // Imprime os produtos na tela
        for (Produto produto : produtos) {
            System.out.println("Nome: " + produto.getNome() + ", Valor: " + produto.getPreco());
        }

        return produtos;
    }
}
