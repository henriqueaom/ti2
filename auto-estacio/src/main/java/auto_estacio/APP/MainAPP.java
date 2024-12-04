package auto_estacio.APP;

import static spark.Spark.*;

import auto_estacio.Service.ClienteService;
import auto_estacio.Service.SaidaService;
import auto_estacio.Service.TicketService;
import auto_estacio.Service.Veiculo2Service;

public class MainAPP {
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
            System.out.println("cadastrar veiculo");
            response.type("application/json");
        
            String resultadoVeiculo = veiculo2Service.cadastrarVeiculo(request, response);
            String resultadoTicket = ticketService.cadastrarTicket(request, response);

            System.out.println("resposta = " + resultadoTicket);
           
        
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
            System.out.println("Placa recebida: " + placa); //  linha para verificar a placa recebida
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

        post("/processarImagem", (request, response) -> {
            System.out.println("Recebendo requisição para processar imagem...");
            response.type("application/json");
        
            try {
                String imagemBase64 = request.body(); // Pega o corpo JSON inteiro
                System.out.println("Imagem Base64 recebida: " + (imagemBase64 != null ? imagemBase64.substring(0, 30) : "NULO"));
        
                if (imagemBase64 == null || imagemBase64.isEmpty()) {
                    return "{\"status\": \"error\", \"message\": \"Imagem não enviada.\"}";
                }
                System.out.println("vai retornar a service");
                return veiculo2Service.processarImagem(request, response); // Chama o serviço
            } catch (Exception e) {
                System.err.println("Erro ao processar imagem: " + e.getMessage());
                e.printStackTrace();
                return "{\"status\": \"error\", \"message\": \"Erro interno no servidor.\"}";
            }
        });

        post("/saidaimagem", (request, response) -> {
            System.out.println("Recebendo requisição para processar imagem...");
            response.type("application/json");
        
            try {
                String imagemBase64 = request.body(); // Pega o corpo JSON inteiro
                System.out.println("Imagem Base64 recebida: " + (imagemBase64 != null ? imagemBase64.substring(0, 30) : "NULO"));
        
                if (imagemBase64 == null || imagemBase64.isEmpty()) {
                    return "{\"status\": \"error\", \"message\": \"Imagem não enviada.\"}";
                }
                System.out.println("vai retornar a service");
                return saidaService.processarImagem(request, response); // Chama o serviço
            } catch (Exception e) {
                System.err.println("Erro ao processar imagem: " + e.getMessage());
                e.printStackTrace();
                return "{\"status\": \"error\", \"message\": \"Erro interno no servidor.\"}";
            }
        });
        
        


    }
}
