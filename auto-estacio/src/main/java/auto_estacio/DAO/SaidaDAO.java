package auto_estacio.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // Importar a classe Timestamp correta

public class SaidaDAO {
    private Connection conexao;

    public SaidaDAO() {
        conexao = null;
    }

    public String conectar() {
        // Driver JDBC do PostgreSQL
        String driverName = "org.postgresql.Driver";
        String serverName = "localhost";
        String databaseName = "auto_estacio";
        int porta = 5432; // Porta padrão do PostgreSQL
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + databaseName;

        //  credenciais para conexão
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

    public String cadastrarSaida(String placa, String tipoVeiculo, Timestamp horarioSaidaPermitida, boolean liberarVeiculo) {
        System.out.println("Conectando ao banco de dados...");
        if (conexao == null) {
            String status = conectar();
            System.out.println(status);
            if (!status.equals("Conectado com sucesso ao banco de dados PostgreSQL")) {
                return "Erro de conexão: " + status;
            }
        }
        
        String sql = "INSERT INTO saida (placa, tipo_veiculo, data_saida_permitida, saida_autorizada) VALUES (?, ?, ?, ?)";
    
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            stmt.setString(2, tipoVeiculo);
            stmt.setTimestamp(3, horarioSaidaPermitida);
            stmt.setBoolean(4, liberarVeiculo);
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Saída registrada com sucesso!");
                return "success";
            } else {
                System.err.println("Erro ao registrar saída.");
                return "Erro ao registrar saída.";
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro de SQL: " + e.getMessage();
        }
    }

    

    public String liberarVeiculo(String placa) {
        if (conexao == null) {
            String status = conectar();
            if (!status.equals("Conectado com sucesso ao banco de dados MySQL")) {
                System.err.println(status);
                return "Erro ao conectar ao banco de dados.";
            }
        }

        String sql = "SELECT * FROM saida WHERE placa = ? AND saida_autorizada = true";
        String resultado = "";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
           //stmt.setBoolean(2, true);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                resultado = "Veículo liberado";
            } else {
                System.err.println("Placa não encontrada ou veículo não está liberado: " + placa);
                resultado = "Erro: Placa não encontrada ou veículo não está liberado.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultado = "Erro: " + e.getMessage();
        }

        return resultado;
    }
}
