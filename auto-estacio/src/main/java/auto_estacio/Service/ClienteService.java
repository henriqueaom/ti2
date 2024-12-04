package auto_estacio.Service;

import auto_estacio.DAO.ClienteDAO;
import spark.Request;
import spark.Response;

public class ClienteService {
    ClienteDAO clienteDAO = new ClienteDAO();

    public boolean cadastrar(Request request, Response response) {
        String nome = request.queryParams("nome");
        String email = request.queryParams("email");
        String telefone = request.queryParams("telefone");
        String senha = request.queryParams("senha");

        if (!clienteDAO.cadastrar(nome, email, telefone, senha)) {
            return false;

        }
        return true;

    }

    public boolean login(Request request, Response response) {
        String nome = request.queryParams("nome");
        String senha = request.queryParams("senha");

        boolean resp = clienteDAO.login(nome, senha);

        return resp;

    }
}