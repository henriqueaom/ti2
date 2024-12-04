package auto_estacio.Service;

import okhttp3.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.Base64;

public class PlateRecognizerAPI {

    private static final String API_URL = "https://api.platerecognizer.com/v1/plate-reader/";
    private static final String API_KEY = "a05b9fc36a1d95ef68da10a65247d4ea66ef04a0"; // Substitua pela sua chave de API

    public String reconhecerPlaca(String imagemBase64) {
        try {
            // Remove o prefixo se necessário
            if (imagemBase64.startsWith("data:image/png;base64,")) {
                imagemBase64 = imagemBase64.replace("data:image/png;base64,", "");
            }

            // Decodifica o Base64 para bytes
            byte[] imagemBytes = Base64.getDecoder().decode(imagemBase64);

            // Envia a imagem para a API
            String respostaJson = enviarImagemParaAPI(imagemBytes);

            // Extrai apenas a placa da resposta
            return extrairPlaca(respostaJson);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\": \"error\", \"message\": \"Erro ao processar a imagem.\"}";
        }
    }

    private String enviarImagemParaAPI(byte[] imagemBytes) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Cria o corpo da requisição
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload", "imagem.png",
                        RequestBody.create(imagemBytes, MediaType.parse("image/png")))
                .build();

        // Configura a requisição
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Token " + API_KEY)
                .post(requestBody)
                .build();

        // Envia a requisição e captura a resposta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição: " + response);
            }
            return response.body().string();
        }
    }

    private String extrairPlaca(String respostaJson) {
        try {
            // Parseia o JSON
            JsonObject jsonObject = JsonParser.parseString(respostaJson).getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");

            if (results.size() > 0) {
                JsonObject primeiroResultado = results.get(0).getAsJsonObject();
                String placa = primeiroResultado.get("plate").getAsString();
                System.err.println("placa na apiii"+ placa);
                return placa;
            } else {
                return "Nenhuma placa encontrada.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao processar o JSON.";
        }
    }
}
