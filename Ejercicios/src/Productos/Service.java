package Productos;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


@WebServlet("/Service")

public class Service extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
   
    public Service() {
        super();
        
        if (Producto.getLista().isEmpty()) { 
        	Producto.getLista().add(new Videojuego("Super Mario", 6, "SWITCH",true));
        	Producto.getLista().add(new Videojuego("StarField", 4, "PC",false));
        	Producto.getLista().add(new Videojuego("GTA V", 3, "PS4",true));
        	Producto.getLista().add(new Videojuego("7 Days to Die", 5, "PC",false));
        	Producto.getLista().add(new Producto("Pan",1,false));
        	Producto.getLista().add(new Producto("Cereales",2,false));
            
            
        }
        
 
    }

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String valor = request.getParameter("valor");
        String nombre = request.getParameter("nombre");
        String id = request.getParameter("id");
        String plataforma = request.getParameter("plataforma");
        String orderby = request.getParameter("orderby");
        String ordertype = request.getParameter("ordertype");

        switch (valor) {
            case "list":
                list(response,plataforma,orderby, ordertype);
                break;
            case "getId":
                getId(response, id);
                break;
            case "save":
                save(response, nombre, id, plataforma);
                break;
            case "delete":
            	delete(response,id); 
            	break;
            
            default:
                response.getWriter().append("Parámetro no válido.");
        }
    }



	private void save(HttpServletResponse response, String nombre, String id, String plataforma) throws IOException {
      
        
		if (nombre == null || id == null) {
            response.getWriter().append("Los valores nombre y id no pueden ser nulos.");
            return;
        }

        boolean encontrado = false;
        for (Producto listado : Producto.getLista()) {
            if (listado.getId() == Integer.parseInt(id)) {
                listado.setNombre(nombre);
                response.getWriter().append("El id " + listado.getId() + " ha sido actualizado.");
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
          
            if (plataforma != null && !plataforma.isEmpty() && 
                (plataforma.equalsIgnoreCase("PS4") || 
                 plataforma.equalsIgnoreCase("SWITCH") || 
                 plataforma.equalsIgnoreCase("XBOX") || 
                 plataforma.equalsIgnoreCase("PC"))) {
                
           
            	            	
                String resultado = Videojuego.anadirVideojuego(nombre, Integer.parseInt(id),plataforma, false );
                response.getWriter().append(resultado);
            } else {
                
                Producto.getLista().add(new Producto(nombre, Integer.parseInt(id),false));
                response.getWriter().append("Producto añadido: " + nombre + " sin plataforma.");
            }
        }
    }


    private void list(HttpServletResponse response, String plataforma, String orderby, String ordertype) throws IOException {
        StringBuilder resp = new StringBuilder();
       
        Producto.getLista().sort(new ComparatorProducto(orderby,ordertype));
        
   
           	
        	Gson gson = new Gson();
        	
        	PrintWriter out = response.getWriter();
        	response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
        	out.print(gson.toJson(Producto.getLista()));
        	out.flush();

    }

    private void getId(HttpServletResponse response, String idd) throws IOException {
        try {
            int id = Integer.parseInt(idd);
            boolean encontrado = false;
            StringBuilder respuesta = new StringBuilder();

            for (Producto listado : Producto.getLista()) {
                if (listado.getId() == id) {
                    respuesta.append("El producto ").append(listado.getNombre()).append(" es ").append(listado instanceof Videojuego ? ((Videojuego) listado).getPlataforma() : " Producto ").append(listado.getRelevancia());
                    encontrado = true;
                    break;
                }
            }

            response.getWriter().append(encontrado ? respuesta.toString() : "No hay un producto con ese ID.");
        } catch (NumberFormatException e) {
            response.getWriter().append("El ID debe ser un número.");
        }
    }
    private void delete(HttpServletResponse response, String idd) throws IOException {
    	try {
    		int id  = Integer.parseInt(idd);
    		boolean encontrado = false;
    		
    		for (int x=0; x < Producto.getLista().size(); x++)	{
    			
    		if 	(Producto.getLista().get(x).getId() == id)	{
    			Producto.getLista().remove(x);
    			response.getWriter().append("El producto con ID " + id + " ha sido eliminado.");
    			encontrado = true;
    			break;
    		}
    			
    		}
    		if (!encontrado) {
                response.getWriter().append("No hay un producto con ese ID.");
            }
    
    	} catch (NumberFormatException e) {
    		response.getWriter().append("El id debe ser un numero");
    	}
    	
    	
    	
    }
}
