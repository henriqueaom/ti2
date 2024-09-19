package auto_estacio;

import spark.Request;
import spark.Response;

public class VeiculoService {
    VeiculoDAO veiculoDAO = new VeiculoDAO();

    public String cadastrarVeiculo(Request request, Response response) {
        String placa = request.queryParams("placa");
        String tipo = request.queryParams("tipo_veiculo");

        String resultado = veiculoDAO.cadastrarVeiculo(placa, tipo);
        return resultado;
    }

    public String calcularValor(Request request, Response response) {
        String placa = request.queryParams("placa");

        int valor = veiculoDAO.calcularValor(placa);
        if (valor >= 0) {
            return "{\"status\": \"success\", \"valor\": " + valor + "}";
        } else {
            return "{\"status\": \"error\", \"message\": \"Placa não encontrada ou erro ao calcular o valor.\"}";
        }
    }

    public String pagamento(Request request, Response response) {
        String placa = request.queryParams("placa");

        String resultado = veiculoDAO.pagamento(placa);
        return "{\"status\": \"success\", \"message\": \"" + resultado + "\"}";
    }
}



