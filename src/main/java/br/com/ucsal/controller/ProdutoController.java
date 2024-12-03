package br.com.ucsal.controller;

import java.io.IOException;

import br.com.ucsal.util.DependencyInjectorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/view/*")  // Mapeia todas as requisições com "/view/*"
public class ProdutoController extends HttpServlet {


    @Override
    public void init() {

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        Command command = null;

        DependencyInjectorUtil injector = DependencyInjectorUtil.getInstance();
        try {
            command = injector.getRota(path);
            injector.initialize(command);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (command != null) {
            command.execute(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página não encontrada");
        }
    }

	


}
