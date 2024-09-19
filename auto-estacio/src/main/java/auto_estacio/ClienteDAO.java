package auto_estacio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ClienteDAO {
	private Connection conexao;
	
	
	 public ClienteDAO() {
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

		public boolean cadastrar(String nome, String email, String telefone, String senha) {
			if (conexao == null) {
				String status = conectar();
				if (!status.equals("Conectado com sucesso ao banco de dados PostgreSQL")) {
					System.err.println("Erro de conexão: " + status);
					return false;
				}
			}
		
			String sql = "INSERT INTO clientes (nome, email, telefone, senha) VALUES (?, ?, ?, ?)";
		
			try {
				PreparedStatement pstmt = conexao.prepareStatement(sql);
				pstmt.setString(1, nome);
				pstmt.setString(2, email);
				pstmt.setString(3, telefone);
				pstmt.setString(4, senha);
				pstmt.executeUpdate();
		
				System.out.println("Cliente cadastrado com sucesso!");
			} catch (SQLException e) {
				System.err.println(e.getMessage());
				return false;
			}
			return true;
		}
		
		public boolean login(String lnome, String lsenha) {
			if (conexao == null) {
				String status = conectar();
				if (!status.equals("Conectado com sucesso ao banco de dados PostgreSQL")) {
					System.err.println("Erro de conexão: " + status);
					return false;
				}
			}
		
			String sql = "SELECT * FROM clientes";
		
			List<Clientes> clientes = new ArrayList<>();
			boolean resp = false;
		
			try {
				PreparedStatement pstm = conexao.prepareStatement(sql);
				ResultSet rs = pstm.executeQuery();
		
				while (rs.next()) {
					int id = rs.getInt("id");
					String nome = rs.getString("nome");
					String email = rs.getString("email");
					String telefone = rs.getString("telefone");
					String senha = rs.getString("senha");
		
					Clientes cliente = new Clientes(id, nome, email, telefone, senha);
					clientes.add(cliente);
				}
		
				for (Clientes cliente : clientes) {
					if (lnome.equals(cliente.getNome()) && lsenha.equals(cliente.getSenha())) {
						resp = true;
						break;
					}
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		
			return resp;
		}
		


		
	}
