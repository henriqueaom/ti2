package exercicio3;

public class ClienteAPP {
    
}
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

       

    }
}
