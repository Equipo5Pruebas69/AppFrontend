package ciclo3Demo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ciclo3Demo.TestJSON;
import ciclo3Demo.Usuarios;

/**
 * Servlet implementation class DemoServlet
 */
@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DemoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String listar = request.getParameter("Listar");
		String agregar = request.getParameter("Agregar");
		String eliminar = request.getParameter("Eliminar");
		if (agregar != null) {
			agregarUsuario(request, response);
		}
		if (eliminar != null) {
			eliminarUsuario(request, response);
		}
				
		if (listar != null) {
			listarUsuarios(request, response);
		}
	} 
        

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public void agregarUsuario(HttpServletRequest request, HttpServletResponse response) {
		Usuarios usuario = new Usuarios();
		usuario.setNombre_usuario(request.getParameter("nombre"));
		usuario.setCedula_usuario(request.getParameter("cedula"));
		usuario.setEmail_usuario(request.getParameter("email"));
		usuario.setUsuario(request.getParameter("usuario"));
		usuario.setPassword(request.getParameter("password"));
		int respuesta = 0;
		try {
			respuesta = TestJSON.postJSON(usuario);
			PrintWriter writer = response.getWriter();
			if (respuesta == 200) {
				writer.println("Registro Agregado!");
			}else {
				writer.println("Error: " + respuesta);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int eliminarUsuario(Long id) throws IOException {
		String sitio = null;
		URL url = new URL(sitio+"usuarios/eliminar");
		HttpURLConnection http;
		http= (HttpURLConnection) url.openConnection();
		try {
			http.setRequestMethod("POST");
		}catch(ProtocolException e) {
			e.printStackTrace();
		}
		http.setDoOutput(true);
		ServletRequest request = null;
		Usuarios usuario = null;
		http.setRequestProperty("Accept", "aplication/json");
		http.setRequestProperty("Content-type", "application/json");
		String data = "{"+"\"cedula_usuario\":\""+ id+ "\"}"; 
		byte[] out = data.getBytes(StandardCharsets.UTF_8);
		OutputStream stream = http.getOutputStream();
		stream.write(out);
		int respuesta = http.getResponseCode();
		http.disconnect();
		return respuesta;		
	}
	
	public void listarUsuarios(HttpServletRequest request, HttpServletResponse response) {
		try {
			ArrayList<Usuarios> lista = TestJSON.getJSON();
			String pagina = "/resultado.jsp";
			request.setAttribute("lista", lista);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pagina);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}







