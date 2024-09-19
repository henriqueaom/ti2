package exercicio2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.sql.Statement;
import java.util.List;

public class ProdutoDAO {
	private Connection conexao;
	
	
	 public ProdutoDAO() {
	        conexao = null;
	    }
	
	
	 public String conectar() {
		    // Driver JDBC do PostgreSQL
		    String driverName = "com.mysql.cj.jdbc.Driver";
		    String serverName = "localhost";
		    String databaseName = "auto_estacio";
            int porta = 3306;
		    String url = "jdbc:mysql://" + serverName + ":" + porta + "/" + databaseName;

		    // Adicionando credenciais para conexão
		    String user = "root";
		    String password = "ti2cc";

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


     public void incluir(Produto produto){

            String sql = "INSERT INTO produto_ex2 (nome, preco) VALUES (?, ?)";

            try (
            PreparedStatement pstmt = conexao.prepareStatement(sql)) {
           
           pstmt.setString(1, produto.getNome());
           pstmt.setInt(2, produto.getPreco());
           pstmt.executeUpdate();
           
           System.out.println("Produto inserido com sucesso!");
       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }
   }  

     public void deletar(String nome){

        String sql = "DELETE FROM produto_ex2 WHERE nome = ?";

        try(PreparedStatement pstmt = conexao.prepareStatement(sql)){
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

     public void atualizar(String nome, int valor){
        String sql = "UPDATE produto_ex2 SET preco = ? WHERE nome = ?";

        try(PreparedStatement pstmt = conexao.prepareStatement(sql)){
            
            
            pstmt.setInt(1,valor);
            pstmt.setString(2,nome);
            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Nenhum produto encontrado com esse nome.");
            }
            

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
      }

     public List<Produto> listar() {
        
        String sql = "SELECT * FROM produto_ex2";
        List<Produto> produtos = new ArrayList<>();

        try (PreparedStatement pstmt = conexao.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterando sobre o resultado da consulta
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int preco = rs.getInt("preco");

                // Criando um objeto Produto para cada registro
                Produto produto = new Produto(id, nome, preco);

                // Adicionando o produto à lista
                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }

        for (Produto produto : produtos) {
            System.out.println("Nome: " + produto.getNome() + ", Valor: " + produto.getPreco());
        }


        // Retornando a lista de produtos
        return produtos;
    }
}

    

        
    
