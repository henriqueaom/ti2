package auto_estacio;

import spark.Request;
import spark.Response;

public class SaidaService {
    SaidaDAO saidaDAO = new SaidaDAO();

    public String saida(Request request, Response response) {
        String placa = request.queryParams("placa");
    
        String resultado = saidaDAO.liberarVeiculo(placa);
        if (resultado.equals("ERRO DE PAGAMENTO.")) {
            return "{\"status\": \"error\", \"message\": \"Erro ao processar pagamento\"}";
        } else {
            return "{\"status\": \"success\", \"message\": \"" + resultado + "\"}";
        }
    }
    

}



