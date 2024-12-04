package auto_estacio.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import auto_estacio.DAO.TicketDAO;
import auto_estacio.DAO.Veiculo2DAO;
import spark.Request;
import spark.Response;

public class Veiculo2Service {
    Veiculo2DAO veiculo2DAO = new Veiculo2DAO();
    TicketDAO ticketDAO = new TicketDAO();
    PlateRecognizerAPI plate = new PlateRecognizerAPI();

    public String cadastrarVeiculo(Request request, Response response) {
        String placa = request.queryParams("placa");
        String tipo = request.queryParams("tipo_veiculo");
        System.out.println("placa " + placa + " tipo " + tipo + " teste 3");

        String resultado = veiculo2DAO.cadastrarVeiculo(placa, tipo);
        return resultado;
    }

    public String processarImagem(Request request, Response response) {
        String tipo = "carro";
        try {
            // Recupera o corpo da requisição (JSON) com a imagem Base64
            String body = request.body(); // Obtém o corpo completo

            JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
            String imagemBase64 = jsonBody.get("imagem").getAsString(); // Pega o valor da imagem em Base64

            System.out.println("Imagem Base64 recebida: " + imagemBase64.substring(0, 30)); // Exemplo de log da imagem

            // Processa a imagem com a API do Azure
            // String placa = azureOCR.processarImagem(imagemBase64);
            String placa = plate.reconhecerPlaca(imagemBase64);
            System.out.println("Placa depois da API = " + placa);

            JsonObject jsonResponse = new JsonObject(); // Cria um objeto JSON de resposta

            if (placa != null) {
                String resultado = veiculo2DAO.cadastrarVeiculo(placa, tipo);
                String resultado2 = ticketDAO.cadastrarTicket(placa, tipo);
                System.out.println("vai retornar sucesso");

                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("placa", placa);
                jsonResponse.addProperty("ticket", resultado2);
                jsonResponse.addProperty("veiculo", resultado);
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Placa não encontrada");
            }

            return jsonResponse.toString(); // Retorna o objeto JSON convertido para string
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Erro ao processar a imagem");
            return jsonResponse.toString(); // Retorna o erro como JSON
        }
    }

}
