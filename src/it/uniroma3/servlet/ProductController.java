package it.uniroma3.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/productController")
public class ProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		boolean ciSonoErrori = false;
		String prossimaPagina = response.encodeURL("/product.jsp");
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String price = request.getParameter("price");//deve essere un intero

		if(name.isEmpty()) {
			ciSonoErrori = true;
			//faccio così perchè è un campo che interessa solo alla richiesta, ossia alle due classi ProductController 
			// e product.jsp
			request.setAttribute("nameError", "Campo obbligatorio");
		}
		if(description.isEmpty()) {
			ciSonoErrori = true;
			request.setAttribute("descriptionError", "Campo obbligatorio");
		}
		if(price.isEmpty()) {
			ciSonoErrori = true;
			request.setAttribute("priceError", "Campo obbligatorio");
		}else {
			try{
				if(price.charAt(price.length()-1)>='A')
					throw new NumberFormatException();
				Float.parseFloat(price);
			}
			catch (NumberFormatException e) {
				ciSonoErrori = true;
				request.setAttribute("priceError","Deve essere un numero!");
			}
		}
		//qui ci vanno tutti gli altri

		//Se non ci sono errori inserisco il prodotto
		if (!ciSonoErrori) {
			Product product = new Product();
			product.setName(name);
			product.setPrice(Float.valueOf(price));
			product.setDescription(description);
			HttpSession session = request.getSession();
			session.setAttribute("product", product);
			prossimaPagina = response.encodeURL("/product.jsp");
		}else
			prossimaPagina = response.encodeURL("/newProduct.jsp");

		ServletContext application = this.getServletContext();
		RequestDispatcher rd = application.getRequestDispatcher(prossimaPagina);
		rd.forward(request, response);

	}

}