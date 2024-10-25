package auto_estacio;

import spark.Request;
import spark.Response;

public class Veiculo2Service {
    Veiculo2DAO veiculo2DAO = new Veiculo2DAO();

    public String cadastrarVeiculo(Request request, Response response) {
        String placa = request.queryParams("placa");
        String tipo = request.queryParams("tipo_veiculo");
        System.out.println("placa " + placa + " tipo "+ tipo + " teste 3");

        String resultado = veiculo2DAO.cadastrarVeiculo(placa, tipo);
        return resultado;
    }



}



