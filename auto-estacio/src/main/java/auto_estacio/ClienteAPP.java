package auto_estacio;

import static spark.Spark.*;

public class ClienteAPP {
    public static void main(String[] args) {
        port(4568);

        ClienteService clienteService = new ClienteService();
        VeiculoService veiculoService = new VeiculoService();

        // Configurar CORS
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        // Permitir que OPTIONS seja tratado
        options("/*", (request, response) -> {
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            return "";
        });

        post("/cadastrar", (request, response) -> {
            System.out.println("Recebida requisição de login");
            response.type("application/json");
            boolean sucesso = clienteService.cadastrar(request, response);
            return sucesso ? "{\"status\": \"success\"}" : "{\"status\": \"error\"}";
        });

        post("/login", (request, response) -> {
            System.out.println("Recebida requisição de login");
            response.type("application/json");
            boolean sucesso = clienteService.login(request, response);
            return sucesso ? "{\"status\": \"success\"}" : "{\"status\": \"error\"}";
        });

        post("/cadastrarveiculo", (request, response) -> {
            System.out.println("Recebida requisição de cadastro de veículo");
            response.type("application/json");
            
            String resultado = veiculoService.cadastrarVeiculo(request, response);
            
            if (resultado.equals("success")) {
                return "{\"status\": \"success\"}";
            } else {
                return "{\"status\": \"error\", \"message\": \"" + resultado + "\"}";
            }
        });

        
        post("/calcularValor", (request, response) -> {
            System.out.println("Recebida requisição de cálculo");
            response.type("application/json");
            
            return veiculoService.calcularValor(request, response);
        });

        post("/pagamento", (request, response) -> {
            System.out.println("Recebida requisição de pagamento");
            response.type("application/json");
            
            return veiculoService.pagamento(request, response);
        });
        


    }
}
