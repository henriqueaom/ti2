package auto_estacio.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class Veiculo2DAO {
    private Connection conexao;

    public Veiculo2DAO() {
        conexao = null;
    }

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

    public String cadastrarVeiculo(String placa, String tipo) {
        System.out.println("Conectando ao banco de dados...");
        if (conexao == null) {
            String status = conectar();
            System.out.println(status);
            if (!status.equals("Conectado com sucesso ao banco de dados PostgreSQL")) {
                return "Erro de conexão: " + status;
            }
        }
    
        System.out.println("Placa recebida: " + placa + ", Tipo: " + tipo);
    
        String sql = "INSERT INTO veiculo (placa, tipo_veiculo) VALUES (?, ?)";
    
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            stmt.setString(2, tipo);
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Veículo cadastrado com sucesso!");
                System.out.println("placa " + placa + " tipo "+ tipo + " teste 1");
                return "success";
            } else {
                System.err.println("Erro ao cadastrar veículo.");
                System.out.println("teste ");
                return "Erro ao cadastrar veículo.";

            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro de SQL: " + e.getMessage();
        }
    }


      

}
