package auto_estacio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // Importar a classe Timestamp correta
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class VeiculoDAO {
    private Connection conexao;

    public VeiculoDAO() {
        conexao = null;
    }

    public String conectar() {
        // Driver JDBC do MySQL
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
            return "Conectado com sucesso ao banco de dados MySQL";
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
            if (!status.equals("Conectado com sucesso ao banco de dados MySQL")) {
                return "Erro de conexão: " + status;
            }
        }
    
        System.out.println("Placa recebida: " + placa + ", Tipo: " + tipo);
    
        String sql = "INSERT INTO veiculo (placa, tipo_veiculo, horario_entrada) VALUES (?, ?, ?)";
    
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            stmt.setString(2, tipo);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Veículo cadastrado com sucesso!");
                return "success";
            } else {
                System.err.println("Erro ao cadastrar veículo.");
                return "Erro ao cadastrar veículo.";
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro de SQL: " + e.getMessage();
        }
    }

    

    public int calcularValor(String placa) {
        if (conexao == null) {
            String status = conectar();
            if (!status.equals("Conectado com sucesso ao banco de dados MySQL")) {
                System.err.println(status);
                return 0;
            }
        }

        String sql = "SELECT horario_entrada, tipo_veiculo FROM veiculo WHERE placa = ?";
        int valor = 0;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Timestamp horarioEntrada = rs.getTimestamp("horario_entrada");
                String tipo = rs.getString("tipo_veiculo");

                LocalDateTime agora = LocalDateTime.now();
                long minutos = ChronoUnit.MINUTES.between(horarioEntrada.toLocalDateTime(), agora);

                if (tipo.equals("carro")) {
                    valor += ((int) (minutos / 15) * 10) + 10; // R$10 por 15 minutos
                } else if (tipo.equals("moto")) {
                    valor += ((int) (minutos / 15) * 5) + 5; // R$5 por 15 minutos
                }
            } else {
                System.err.println("Placa não encontrada: " + placa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valor;
    }

    public String pagamento(String placa) {
        int preco = calcularValor(placa);
        if (conexao == null) {
            String status = conectar();
            if (!status.equals("Conectado com sucesso ao banco de dados MySQL")) {
                System.err.println(status);
                return "Erro ao conectar ao banco de dados.";
            }
        }

        String resultado = "";
        String sql = "UPDATE veiculo SET horario_saida_permitida = ?, liberar_veiculo = ? WHERE placa = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setBoolean(2, true);
            stmt.setString(3, placa);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                resultado = "Placa: " + placa + " - Valor: " + preco
                        + " - Horário de saída permitido atualizado com sucesso.";
            } else {
                System.err.println("Placa não encontrada: " + placa);
                resultado = "Erro: Placa não encontrada.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultado = "Erro ao processar pagamento: " + e.getMessage();
        }

        return resultado;
    }

    public String liberarVeiculo(String placa) {
        if (conexao == null) {
            String status = conectar();
            if (!status.equals("Conectado com sucesso ao banco de dados MySQL")) {
                System.err.println(status);
                return "Erro ao conectar ao banco de dados.";
            }
        }

        String sql = "SELECT * FROM veiculo WHERE placa = ? AND liberar_veiculo = ?";
        String resultado = "";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            stmt.setBoolean(2, true);
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
