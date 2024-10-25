package auto_estacio;

import static spark.Spark.*;

public class ClienteAPP {
    public static void main(String[] args) {
        port(4568);

        ClienteService clienteService = new ClienteService();
        Veiculo2Service veiculo2Service = new Veiculo2Service();
        TicketService ticketService = new TicketService();
        SaidaService saidaService = new SaidaService();
        

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
        
            String resultadoVeiculo = veiculo2Service.cadastrarVeiculo(request, response);
            String resultadoTicket = ticketService.cadastrarTicket(request, response);
        
            if (resultadoVeiculo.equals("success") && resultadoTicket.equals("success")) {
                return "{\"status\": \"success\"}";
            } else if (!resultadoVeiculo.equals("success")) {
                return "{\"status\": \"error\", \"message\": \"Erro ao cadastrar veículo: " + resultadoVeiculo + "\"}";
            } else {
                return "{\"status\": \"error\", \"message\": \"Erro ao cadastrar ticket: " + resultadoTicket + "\"}";
            }
        });
         
        post("/calcularvalor", (request, response) -> {
            System.out.println("Recebida requisição de cálculo");
            String placa = request.queryParams("placa");
            System.out.println("Placa recebida: " + placa); // Adicione esta linha para verificar a placa recebida
            response.type("application/json");
            
            return ticketService.calcularValor(request, response);
        });
        post("/pagamento", (request, response) -> {
            System.out.println("Recebida requisição de pagamento");
            response.type("application/json");
            
            return ticketService.pagamento(request, response);
        });

        post("/saida", (request, response) -> {
            System.out.println("Recebida requisição de saida");
            response.type("application/json");
            
            return saidaService.saida(request, response);
        });
        


    }
}
