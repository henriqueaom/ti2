package auto_estacio.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TicketDAO {
    private Connection conexao;

    public TicketDAO() {
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
    public String cadastrarTicket(String placa, String tipo) {
        System.out.println("Conectando ao banco de dados...");
        if (conexao == null) {
            String status = conectar();
            System.out.println(status);
            if (!status.equals("Conectado com sucesso ao banco de dados PostgreSQL")) {
                return "Erro de conexão: " + status;
            }
        }

        System.out.println("Placa recebida: " + placa + ", Tipo: " + tipo);

        String sql = "INSERT INTO ticket (placa, tipo_veiculo) VALUES (?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            stmt.setString(2, tipo);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("ticket cadastrado com sucesso!");
                return "success";
            } else {
                System.err.println("Erro ao cadastrar ticket.");
                return "Erro ao cadastrar ticket.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro de SQL: " + e.getMessage();
        }
    }

    public int calcularValor(String placa) {

        System.out.println("placa no dao = " + placa);
        if (conexao == null) {
            String status = conectar();
            if (!status.equals("Conectado com sucesso ao banco de dados PostgreSQL")) {
                System.err.println(status);
                return 0;
            }
        }
    
        String sql = "SELECT t.tipo_veiculo, t.data_entrada, tar.preco " +
                "FROM ticket t " +
                "JOIN tarifa tar ON t.tipo_veiculo = tar.tipo " +
                "WHERE t.placa = ?";
        int valor = 0;
    
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                //String tipo = rs.getString("tipo_veiculo");
                Timestamp horarioEntrada = rs.getTimestamp("data_entrada");
                float tarifa = rs.getFloat("preco");
    
                LocalDateTime agora = LocalDateTime.now();
                long minutos = ChronoUnit.MINUTES.between(horarioEntrada.toLocalDateTime(), agora);
    
                // Calcula o valor total baseado no tempo e tarifa
                int intervalos = (int) (minutos / 15) ; // quantidade de intervalos de 15 minutos
                valor += (intervalos * tarifa) + 10; // Aplica a tarifa para cada intervalo de 15 minutos
    
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
            if (!status.equals("Conectado com sucesso ao banco de dados PostgreSQL")) {
                System.err.println(status);
                return "Erro ao conectar ao banco de dados.";
            }
        }

        String resultado = "";
        String sql = "INSERT INTO saida (placa,data_saida_permitida, saida_autorizada) VALUES ( ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, placa);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setBoolean(3, true);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                resultado = "Placa: " + placa + " - Valor: " + preco
                        + " - PAGAMENTO CONCLUIDO.";
            } else {
                System.err.println(" PAGAMENTO ERRO: " + placa);
                resultado = " ERRO DE PAGAMENTO.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultado = "Erro ao processar pagamento: " + e.getMessage();
        }

        return resultado;
    }

}
