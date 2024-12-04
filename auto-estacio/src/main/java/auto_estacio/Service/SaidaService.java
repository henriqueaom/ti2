package auto_estacio.Service;

import spark.Request;
import spark.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import auto_estacio.DAO.SaidaDAO;


public class SaidaService {
    SaidaDAO saidaDAO = new SaidaDAO();
    PlateRecognizerAPI plate = new PlateRecognizerAPI();

    public String saida(Request request, Response response) {
        String placa = request.queryParams("placa");
    
        String resultado = saidaDAO.liberarVeiculo(placa);
        if (resultado.equals("Erro: Placa não encontrada ou veículo não está liberado.")) {
            return "{\"status\": \"error\", \"message\": \"Erro ao processar pagamento\"}";
        } else {
            return "{\"status\": \"success\", \"message\": \"" + resultado + "\"}";
        }
    }
    



public String processarImagem(Request request, Response response) {
 
    try {
        // Recupera o corpo da requisição (JSON) com a imagem Base64
        String body = request.body();  // Obtém o corpo completo

        
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        String imagemBase64 = jsonBody.get("imagem").getAsString();  // Pega o valor da imagem em Base64
        
        System.out.println("Imagem Base64 recebida: " + imagemBase64.substring(0, 30)); // Exemplo de log da imagem

        // Processa a imagem com a API do Azure
        //String placa = azureOCR.processarImagem(imagemBase64);
        String placa = plate.reconhecerPlaca(imagemBase64);
        System.out.println("Placa depois da API = " + placa);

        JsonObject jsonResponse = new JsonObject();  // Cria um objeto JSON de resposta

        if (placa != null) {
            String resultado = saidaDAO.liberarVeiculo(placa);
     
            System.out.println("vai retornar sucesso");

            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("placa", placa);
            jsonResponse.addProperty("saida", resultado);
        } else {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Placa não encontrada");
            jsonResponse.addProperty("saida", "erro");
        }
        
        return jsonResponse.toString();  // Retorna o objeto JSON convertido para string
    } catch (Exception e) {
        e.printStackTrace();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "error");
        jsonResponse.addProperty("message", "Erro ao processar a imagem");
        return jsonResponse.toString();  // Retorna o erro como JSON
    }
}



}