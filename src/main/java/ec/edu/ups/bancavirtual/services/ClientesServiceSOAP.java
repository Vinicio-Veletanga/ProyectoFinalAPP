package ec.edu.ups.bancavirtual.services;

import ec.edu.ups.bancavirtual.modelo.Cliente;
import ec.edu.ups.bancavirtual.on.GestionUsuarioLocal;
import javax.jws.WebMethod;
import javax.jws.WebService;
@WebService
public class ClientesServiceSOAP {
	
	private GestionUsuarioLocal on;
	
	@WebMethod
	public String saludar(String nombre) {
		return nombre ;
	}
	
	@WebMethod
	public String Guardar(Cliente cliente) {
		
		try {
			on.guardarCliente(cliente);
			return "OK";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
			// TODO: handle exception
		}
		
	}


}
