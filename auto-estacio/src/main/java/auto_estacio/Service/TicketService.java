package auto_estacio.Service;

import auto_estacio.DAO.TicketDAO;
import spark.Request;
import spark.Response;

public class TicketService {
    TicketDAO ticketDAO = new TicketDAO();

    public String cadastrarTicket(Request request, Response response) {
        String placa = request.queryParams("placa");
        String tipo = request.queryParams("tipo_veiculo");

        System.out.println("PLACA NA SERVICE TI9CKEIT " + placa);
        System.out.println("PLACA NA SERVICE TI9CKEIT " + tipo);
       
        String resultado = ticketDAO.cadastrarTicket(placa, tipo);
        System.out.println("PLACA NA SERVICE TI9CKEIT " + resultado);
        return resultado;
    }

    public String calcularValor(Request request, Response response) {
        String placa = request.queryParams("placa");
        
        // Chama o DAO para calcular o valor
        int valor = ticketDAO.calcularValor(placa);
        System.out.println("placa na service  = " + placa + " valor = " +valor);
    
        if (valor == 0) {
            response.status(400); // Bad Request
            return "{\"status\": \"error\", \"message\": \"Veículo não encontrado ou erro ao calcular valor."+valor+"\"}";
        }
    
        // Retorna o valor em formato JSON
        return "{\"status\": \"success\", \"valor\": " + valor + "}";
    }

    public String pagamento(Request request, Response response) {
        String placa = request.queryParams("placa");
    
        String resultado = ticketDAO.pagamento(placa);
        if (resultado.equals("ERRO DE PAGAMENTO.")) {
            return "{\"status\": \"error\", \"message\": \"Erro ao processar pagamento\"}";
        } else {
            return "{\"status\": \"success\", \"message\": \"" + resultado + "\"}";
        }
    }
    

}



