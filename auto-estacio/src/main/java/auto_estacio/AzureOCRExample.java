package auto_estacio;

import okhttp3.*;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class AzureOCRExample {

    private static final String ENDPOINT = "https://parkingplaterecognizer.cognitiveservices.azure.com/";
    private static final String API_KEY = "d4c69d67a70f4138b1f32f7d5db8c6bf";

    public String processarImagem(String imagemBase64) throws IOException {
        // Decodifica a imagem Base64
        System.out.println("Iniciando o processamento da imagem...");
        byte[] imagemBytes = Base64.getDecoder().decode(imagemBase64);
        System.out.println("Imagem decodificada com sucesso. Tamanho (bytes): " + imagemBytes.length);

        // Salva localmente para depuração
        try (FileOutputStream fos = new FileOutputStream("imagem_decodificada.jpg")) {
            fos.write(imagemBytes);
            System.out.println("Imagem salva localmente como 'imagem_decodificada.jpg'.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar a imagem localmente: " + e.getMessage());
            throw e;
        }

        // Define o Content-Type com base no formato da imagem
        String contentType = imagemBase64.startsWith("/9j") ? "image/jpeg" : "image/png";
        System.out.println("Content-Type detectado: " + contentType);

        RequestBody requestBody = RequestBody.create(imagemBytes, MediaType.parse(contentType));
        OkHttpClient client = new OkHttpClient();

        // Constrói a requisição
        Request request = new Request.Builder()
                .url(ENDPOINT + "vision/v3.2/read/analyze")
                .addHeader("Ocp-Apim-Subscription-Key", API_KEY)
                .addHeader("Content-Type", contentType)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("Requisição ao Azure OCR enviada com sucesso.");
            String operationLocation = response.header("Operation-Location");
            System.out.println("Operation-Location: " + operationLocation);

            try {
                Thread.sleep(5000); // Aguarda 5 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaura o estado de interrupção
                System.err.println("Erro durante o delay: " + e.getMessage());
            }
            

            return obterResultado(operationLocation);
        } else {
            String erro = response.body().string();
            System.err.println("Erro na chamada do OCR: " + erro);
            return null;
        }
    }

    private String obterResultado(String operationLocation) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Requisição de status e resultado
        Request request = new Request.Builder()
                .url(operationLocation)
                .addHeader("Ocp-Apim-Subscription-Key", API_KEY)
                .get()
                .build();

        String status;
        do {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            System.out.println("Resposta do Azure OCR: " + responseBody);
            status = json.getString("status");
            System.out.println("Status atual do processamento: " + status);

            if ("succeeded".equals(status)) {
                System.out.println("texto retornado = ");
                return json.getJSONObject("analyzeResult")
                           .getJSONArray("readResults")
                           .getJSONObject(0)
                           .getJSONArray("lines")
                           .getJSONObject(0)
                           .getString("text");
                           
            }

            // Aguarda antes de repetir
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Erro durante o delay: " + e.getMessage());
            }

        } while (!"failed".equals(status));

        System.err.println("O processamento da imagem falhou.");
        return null;
    }
}
