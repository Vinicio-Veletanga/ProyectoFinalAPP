package ec.edu.ups.bancavirtual.on;

import ec.edu.ups.bancavirtual.modelo.*;
import ec.edu.ups.bancavirtual.dao.TransferenciaLocalDAO;
import ec.edu.ups.bancavirtual.dao.*;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import java.sql.SQLException;
import java.text.DateFormat;

import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.management.remote.NotificationResult;
import javax.persistence.NoResultException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;



/**
 * @author ADMINX
 *
 */
@Stateless
public class GestionUsuarios implements GestionUsuarioLocal {
	@Inject
	private ClienteDAO clienteDAO;
	@Inject
	private SesionClienteDAO sesionClienteDAO;
	@Inject
	private TransferenciaLocalDAO transferenciaLocalDAO;
	@Inject
	private EmpleadoDAO empleadoDAO;
	@Inject
	private CuentaDeAhorroDAO cuentaDeAhorroDAO;
	@Inject
	private TransaccionDAO transaccionDAO;


	/**
	 * Metodo que permite generar una numero de cuenta automatico
	 * 
	 * @return Numero de cuenta generado
	 */
	public String generarNumeroDeCuenta() {
		int numeroInicio = 4040;
		List<CuentaDeAhorro> listaCuentas = listaCuentaDeAhorros();
		int numero = listaCuentas.size() + 1;
		String resultado = String.format("%08d", numero);
		String resultadoFinal = String.valueOf(numeroInicio) + resultado;
		return resultadoFinal;
	}

	/**
	 * Metodo que permite generar un usuario aletorio
	 * 
	 * @param cedula   Cedula del usuario
	 * @param nombre   Nombre del usario
	 * @param apellido Apellido del usuario
	 * @return Usuario que se ha creado
	 */
	public String getUsuario(String cedula, String nombre, String apellido) {
		System.out.println(cedula);
		System.out.println(nombre);
		System.out.println(apellido);
		String ud = cedula.substring(cedula.length() - 1);
		String pln = nombre.substring(0, 1);
		int it = 0;
		for (int i = 0; i < apellido.length(); i++) {
			if (apellido.charAt(i) == 32) {
				it = i;
			}
		}
		String a = "";
		if (it == 0) {
			a = apellido.substring(0, apellido.length());
		} else {
			a = apellido.substring(0, it);
		}
		return pln.toLowerCase() + a.toLowerCase() + ud;
	}

	/**
	 * Metodo que permite la creacion de una contraseña aleatoria
	 * 
	 * @return Contraseña aleatoria
	 */
	public String getContraseña() {
		String simbolos = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefjhijklmnopqrstuvwxyz0123456789!#$%&()*+,-./:;<=>?@_";

		int tam = simbolos.length() - 1;
		System.out.println(tam);
		String clave = "";
		for (int i = 0; i < 10; i++) {
			int v = (int) Math.floor(Math.random() * tam + 1);
			clave += simbolos.charAt(v);
		}

		return clave;
	}

	/**
	 * Metodo que permite el envio de un correo
	 * 
	 * @param destinatario Destinario que se envia el correo
	 * @param asunto       Asunto del correo
	 * @param cuerpo       Cuerpo del correo
	 */
	public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
		Properties propiedad = new Properties();
		propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
		propiedad.setProperty("mail.smtp.starttls.enable", "true");
		propiedad.setProperty("mail.smtp.port", "587");

		Session sesion = Session.getDefaultInstance(propiedad);
		String correoEnvia = "gazir.2409@gmail.com";
		String contrasena = "Gavriellalx2";

		MimeMessage mail = new MimeMessage(sesion);
		try {
			mail.setFrom();
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			mail.setSubject(asunto);
			mail.setText(cuerpo);

			Transport transportar = sesion.getTransport("smtp");
			transportar.connect(correoEnvia, contrasena);
			transportar.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
		} catch (AddressException ex) {
			System.out.println(ex.getMessage());
		} catch (MessagingException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Metodo que permite cambiar el formato de la fecha
	 * 
	 * @return Fecha con nuevo formato
	 */
	public String fecha() {
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return hourdateFormat.format(date);
	}

	/**
	 * Metodo que permite cambiar el formato de la fecha
	 * 
	 * @param fecha Fecha que se cambiara el formato
	 * @return La fecha en un formato requerido de tipo texto.
	 */
	public String obtenerFecha(Date fecha) {
		DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return hourdateFormat.format(fecha);
	}

	/**
	 * Metodo que me permite guardar el cliente en la base de datos
	 * 
	 * @param c Cliente que se guarda en la base de datos
	 */
	public void guardarCliente(Cliente c) {
		clienteDAO.insert(c);

	}

	/**
	 * Metodo que permite la busqueda de un cliente
	 * 
	 * @param cedulaCliente Cedula del cliente que se busca
	 * @return Cliente obtenido de la busqueda
	 */
	public Cliente buscarCliente(String cedulaCliente) {
		Cliente cliente = clienteDAO.read(cedulaCliente);
		return cliente;
	}

	/**
	 * Metodo que permite la busqueda del cliente en base a su usuario y contraseña
	 * 
	 * @param usuario    Usuario del cliente
	 * @param contraseña Contraseña del cliente
	 * @return Cliente obtenido de la busqueda
	 */
	public Cliente buscarClienteUsuarioContraseña(String usuario, String contraseña) {
		try {
			return clienteDAO.obtenerClienteUsuarioContraseña(usuario, contraseña);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo que permite eliminar un cliente
	 * 
	 * @param cedulaCliente Cedula del cliente que se elimina
	 */
	public void eliminarCliente(String cedulaCliente) {
		clienteDAO.delete(cedulaCliente);
	}

	/**
	 * Metodo que permite actualizar un cliente
	 * 
	 * @param cliente Cliente que se actualiza
	 */
	public void actualizarCliente(Cliente cliente) {
		clienteDAO.update(cliente);
	}

	/**
	 * Metodo que permite listar los clientes
	 * 
	 * @return Lista de todos los clientes
	 */
	public List<Cliente> listaClientes() {
		List<Cliente> clientes = clienteDAO.getClientes();
		return clientes;
	}

	/**
	 * Metodo que permite guardar una cuenta de ahorro
	 * 
	 * @param c Cuenta de ahorro que se guarda
	 */
	public void guardarCuentaDeAhorros(CuentaDeAhorro c) {
		Cliente cliente = clienteDAO.read(c.getCliente().getCedula());
		if (cliente == null) {
			Cliente cli = c.getCliente();
			String usuario = getUsuario(cli.getCedula(), cli.getNombre(), cli.getApellido());
			String contraseña = getContraseña();
			cli.setUsuario(usuario);
			cli.setClave(contraseña);
			c.setCliente(cli);
			String destinatario = cli.getCorreo(); // A quien le quieres escribir.

			String asunto = "CREACION DE USUARIO";
			String cuerpo = "BANCA VIRTUAL                                             SISTEMA TRANSACCIONAL\n"
						  + "-------------------------------------------------------------------------------\n"
					      + "   Estimado(a): " + cli.getNombre().toUpperCase() + " "
					      +                      cli.getApellido().toUpperCase() + "\n"
					      + "-------------------------------------------------------------------------------\n"
					      + "BANCA VIRTUAL le informa que el usuario ha sido habilitado exitosamente.       \n"
					      + "                                                                               \n"
					      + "                     Su nombre de usuario es : " + usuario + "                 \n"
					      + "                   	Su clave de acceso es:   " + contraseña + "               \n"
					      + "                     Fecha: " + fecha() + "                                    \n"
					      + "                                                                               \n"
					      + "-------------------------------------------------------------------------------\n";
			CompletableFuture.runAsync(() -> {
				try {
					enviarCorreo(destinatario, asunto, cuerpo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			cuentaDeAhorroDAO.insert(c);
		}

	}

	/**
	 * Metodo que permite buscar una cuenta de ahorros
	 * 
	 * @param numeroCuentaDeAhorro Numero de la cuenta de ahorros que se desea
	 *                             buscar
	 * @return Cuenta de ahorros que se obtiende de la busqueda
	 */
	public CuentaDeAhorro buscarCuentaDeAhorro(String numeroCuentaDeAhorro) {
		CuentaDeAhorro cuentaDeAhorro = cuentaDeAhorroDAO.read(numeroCuentaDeAhorro);
		return cuentaDeAhorro;
	}

	/**
	 * Metodo que me permite buscar una cuenta de ahorros
	 * 
	 * @param cedulaCliente Cedula del cliente de la cuenta de ahorros
	 * @return Cuenta de ahorro obtenida de la busqueda
	 */
	public CuentaDeAhorro buscarCuentaDeAhorroCliente(String cedulaCliente) {
		CuentaDeAhorro cuentaDeAhorro = cuentaDeAhorroDAO.getCuentaCedulaCliente(cedulaCliente);
		return cuentaDeAhorro;

	}

	/**
	 * Metodo que me permite eliminar una cuenta de ahorros
	 * 
	 * @param numeroCuentaDeAhorro Numero de la cuenta de ahorros que se desea
	 *                             eliminar
	 */
	public void eliminarCuentaDeAhorro(String numeroCuentaDeAhorro) {
		cuentaDeAhorroDAO.delete(numeroCuentaDeAhorro);
	}

	/**
	 * Metodo que permite actualizar una cuenta de ahorros
	 * 
	 * @param cuentaDeAhorro Cuenta de Ahorros que se desea actualizar
	 */
	public void actualizarCuentaDeAhorro(CuentaDeAhorro cuentaDeAhorro) {
		cuentaDeAhorroDAO.update(cuentaDeAhorro);
	}

	/**
	 * Metodo que me permite listar las cuentas de ahorros
	 * 
	 * @return Lista de todas las cuentas de ahorros
	 */
	public List<CuentaDeAhorro> listaCuentaDeAhorros() {
		List<CuentaDeAhorro> clientes = cuentaDeAhorroDAO.getCuentaDeAhorros();
		return clientes;
	}

	/**
	 * Metodo que permite guardar la sesion y enviar un correo al usuario que se le
	 * ha asignado esa sesion
	 * 
	 * @param sesionCliente Sesion que se guarda
	 */
	public void guardarSesion(SesionCliente sesionCliente) {
		Cliente cli = sesionCliente.getCliente();
		String destinatario = cli.getCorreo();
		if (sesionCliente.getEstado().equalsIgnoreCase("Incorrecto")) {
			// A quien le quieres escribir.

			String asunto = "INICIO DE SESION FALLIDA";
			String cuerpo = "BANCA VIRTUAL                                             SISTEMA TRANSACCIONAL\n"
						  + "-------------------------------------------------------------------------------\n"
					      + "        Estimado(a): " + cli.getNombre().toUpperCase() + " "
					      +                                              cli.getApellido().toUpperCase() + "\n"
					      + "-------------------------------------------------------------------------------\n"
					      + "BANCA VIRTUAL le informa que el acceso a su cuenta ha sido fallida en la fecha.\n"
					      + "  		Fecha: " + obtenerFecha(sesionCliente.getFechaSesion())+               "\n"
					      + "                                                                               \n"
					      + "-------------------------------------------------------------------------------\n";
			CompletableFuture.runAsync(() -> {
				try {
					enviarCorreo(destinatario, asunto, cuerpo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} else {
			// A quien le quieres escribir.

			String asunto = "INICIO DE SESION CORRECTA";
			String cuerpo = "BANCA VIRTUAL                                             SISTEMA TRANSACCIONAL\n"
					  + "-------------------------------------------------------------------------------\n"
				      + "        Estimado(a): " + cli.getNombre().toUpperCase() + " "
				      +                                              cli.getApellido().toUpperCase() + "\n"
				      + "-------------------------------------------------------------------------------\n"
				      + "BANCA VIRTUAL le informa que el acceso a su cuenta ha sido correcta en la fecha.\n"
				      + "  		Fecha: " + obtenerFecha(sesionCliente.getFechaSesion())+               "\n"
				      + "                                                                               \n"
				      + "-------------------------------------------------------------------------------\n";

			CompletableFuture.runAsync(() -> {
				try {
					enviarCorreo(destinatario, asunto, cuerpo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		sesionClienteDAO.insert(sesionCliente);

	}

	/**
	 * Metodo que permite buscar una Sesion
	 * 
	 * @param codigoSesionCliente Codigo de la sesion que se desea buscar
	 * @return Sesion obtenida de la busqueda
	 */

	public SesionCliente buscarSesionCliente(int codigoSesionCliente) {
		return sesionClienteDAO.read(codigoSesionCliente);
	}

	/**
	 * Metodo que permite obtener las sesiones de un cliente
	 * 
	 * @param cedulaCliente Cedula del cliente que tiene la sesion que se desea
	 *                      buscar
	 * @return Lista de sesiones de un cliente
	 */
	public List<SesionCliente> obtenerSesionesCliente(String cedulaCliente) {
		try {
			return sesionClienteDAO.obtenerSesionCliente(cedulaCliente);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo para validacion
	 * 
	 * @param cedula El parmetro cedula sirve para la validacion de la una cedula
	 *               Ecuatoriana
	 * @return Si la cedula esta correcta o incorrecta en una variable booleana TRUE
	 *         o FALSE
	 * @throws Exception
	 */
	public boolean validadorDeCedula(String cedula) throws Exception {
		System.out.println(cedula + "    En Metodo ");
		boolean cedulaCorrecta = false;
		try {
			if (cedula.length() == 10) // ConstantesApp.LongitudCedula
			{
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9, 10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
					}
					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
					} else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
					} else {
						cedulaCorrecta = false;
					}
				} else {
					cedulaCorrecta = false;
				}
			} else {
				cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
			cedulaCorrecta = false;
		} catch (Exception err) {
			cedulaCorrecta = false;
			throw new Exception("Error cedula");
		}
		if (!cedulaCorrecta) {
			return cedulaCorrecta;
			// throw new Exception("Cedula Incorrecta");

		}
		return cedulaCorrecta;
	}

	/**
	 * Metodo para guardar Empleado
	 * 
	 * @param empleado El parametro empleado me permite registrarlo en la Base de
	 *                 Datos un Empleado
	 * @throws SQLException Excepcion para un fallo de ingreso en la base de datos
	 * @throws Exception    Excepcion de registro en la base de datos
	 */
	public void guardarEmpleado(Empleado empleado) throws SQLException, Exception {

		if (!validadorDeCedula(empleado.getCedula())) {
			throw new Exception("Cedula Incorrecta");
		} else {

			try {
				empleadoDAO.insertarEmpleado(empleado);
			} catch (Exception e) {
				throw new Exception(e.toString());
			}
		}
	}

	/**
	 * Metodo para obtener un Empleado
	 * 
	 * @param cedula El parametro cedula me permite obtener un Empleado que contenga
	 *               la cedual igual al parametro
	 * @return Un Empleado registrado en la Base de Datos
	 */
	public Empleado usuarioRegistrado(String cedula) {
		return empleadoDAO.obtenerEmpleado(cedula);
	}

	/**
	 * Metodo para obtener una Lista de Empleados
	 * 
	 * @return La lita con todos los empleado registrados en la Institucion
	 */
	public List<Empleado> listadoEmpleados() {
		return empleadoDAO.obtener();
	}

	/**
	 * Metodo para obtener un Empleado
	 * 
	 * @param usuario El parametro usuario me permite obtener un Empleado que
	 *                contenga el usuario pasado como parametro
	 * @param contra  El parametro contra permite obtener un Empleado que contenga
	 *                el usuario pasado como parametro
	 * @return Un Empleado con los usuario y contraseña de acuerdo a los parametros
	 * @throws Exception Excepcion cuando no se obtiene ningun usuario
	 */
	public Empleado usuario(String usuario, String contra) throws Exception {
		try {
			Empleado em = empleadoDAO.obtenerUsuario(usuario, contra);
			if (em != null) {
				return em;
			}
		} catch (NoResultException e) {
			throw new Exception("Credenciales Incorrectas");
		}
		return null;

	}

	/**
	 * Metodo para obtener una Lista de Transacciones
	 * 
	 * @param cedula El parametro cedula me permite obtener la lista de
	 *               transacciones de acuedo al parametro
	 * @return Lista de Transacciones que realizo un Cliente de acuerdo al parametro
	 */
	public List<Transaccion> listadeTransacciones(String cedula) {
		try {
			return transaccionDAO.getListaTransacciones(cedula);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Metodo para guardad una Transaccion
	 * 
	 * @param t El parametro t me permite registrar una Transaccion de acuerdo al
	 *          parametro
	 **/
	public void guardarTransaccion(Transaccion t) throws Exception {

		try {
			transaccionDAO.insert(t);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
	}

	/**
	 * Metodo que permite buscar las transacciones de un usuario entre fechas
	 * 
	 * @param cedula Numero de cedula de la persona que busca
	 * @param fechaI La fecha de inicio desde donde se quieren ver las
	 *               transacciones.
	 * @param fechaF La fecha de fin hasta donde se quieren ver las transacciones.
	 * @return Una lista de las transacciones/movimientos del usuario entre las
	 *         fechas indicadas.
	 * @throws Exception Excepción por si el cliente no tiene transacciones.
	 */
	public List<Transaccion> obtenerTransaccionesFechaHora(String cedula, String fechaI, String fechaF) {
		String fechaInicio = fechaI + " 00:00:00.000000";
		String fechaFinal = fechaF + " 23:59:59.000000";
		try {
			return transaccionDAO.getListaTransaccionesFechas(cedula, fechaInicio, fechaFinal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo que permite realizar una transacción por parte del cajero
	 * 
	 * @param cuenta          Numero de cuenta de la persona a la que se hace la
	 *                        transacción.
	 * @param monto           El valor de transacción.
	 * @param tipoTransaccion El tipo de transacción que se realiza depósito o
	 *                        retiro;
	 * @return Un mensaje indicado si se completo correctamente el proceso o algo
	 *         error que pueda ocurrir.
	 * @throws Exception Excepción por si sucede algún error.
	 */

	public String realizarTransaccion(String cuenta, double monto, String tipoTransaccion) {
		CuentaDeAhorro clp = cuentaDeAhorroDAO.read(cuenta);
		if (clp != null) {
			if (tipoTransaccion.equalsIgnoreCase("deposito")) {
				Double nvmonto = clp.getSaldoCuentaDeAhorro() + monto;
				clp.setSaldoCuentaDeAhorro(nvmonto);
//				actualizarCuentaDeAhorro(clp);
				Transaccion t = new Transaccion();
				t.setCliente(clp.getCliente());
				t.setMonto(monto);
				t.setFecha(new Date());
				t.setTipo("deposito");
				t.setSaldoCuenta(nvmonto);
				try {
					// editable = false;
					guardarTransaccion(t);
					return "Hecho";
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.getMessage();
				}
			} else if (tipoTransaccion.equalsIgnoreCase("retiro") && monto <= clp.getSaldoCuentaDeAhorro()) {
				Double nvmonto2 = clp.getSaldoCuentaDeAhorro() - monto;
				clp.setSaldoCuentaDeAhorro(nvmonto2);
//				actualizarCuentaDeAhorro(clp);
				Transaccion t2 = new Transaccion();
				t2.setCliente(clp.getCliente());
				t2.setMonto(monto);
				t2.setFecha(new Date());
				t2.setTipo("retiro");
				t2.setSaldoCuenta(nvmonto2);
				try {
					guardarTransaccion(t2);
					return "Hecho";
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.getMessage();
				}
			} else {
				return "Monto exedido";
			}
		} else {
			return "Cuenta Inexistente";
		}
		return "Fallido";
	}
//
//	/**
//	 * Metodo que permite realizar una transferencia
//	 * 
//	 * @param cedula Numero de cedula de la persona que hace la transferencia.
//	 * @param cuentaAhorro2 El numero de cuenta de la persona a la que se hace la transferencia.
//	 * @param monto El valor de la transferencia.
//	 * @return Un clase Respuesta indicando los datos del desarrollo del proceso, con un codigo, una descripción.
//	 * @throws Exception Excepción por si sucede algún error en el proceso.
//	 */
//	public Respuesta realizarTransferencia(String cedula, String cuentaAhorro2, double monto) {
//		Respuesta respuesta = new Respuesta(); 
//		CuentaDeAhorro cuentaAhorro = cuentaDeAhorroDAO.getCuentaCedulaCliente(cedula);
//		CuentaDeAhorro cuentaAhorroTransferir = cuentaDeAhorroDAO.read(cuentaAhorro2);
//		try {
//			if (cuentaAhorro.getSaldoCuentaDeAhorro() >= monto) {
//				cuentaAhorro.setSaldoCuentaDeAhorro(cuentaAhorro.getSaldoCuentaDeAhorro() - monto);
//				actualizarCuentaDeAhorro(cuentaAhorro);
//				cuentaAhorroTransferir.setSaldoCuentaDeAhorro(cuentaAhorroTransferir.getSaldoCuentaDeAhorro() + monto);
//				actualizarCuentaDeAhorro(cuentaAhorroTransferir);
//				TransfereciaLocal transfereciaLocal = new TransfereciaLocal();
//				transfereciaLocal.setCliente(cuentaAhorro.getCliente());
//				transfereciaLocal.setCuentaDeAhorroDestino(cuentaAhorroTransferir);
//				transfereciaLocal.setMonto(monto);
//				guardarTransferenciaLocal(transfereciaLocal); 
//				respuesta.setCodigo(1); 
//				respuesta.setDescripcion("Transferencia Satisfactoria");
//			} else { 
//				respuesta.setCodigo(2); 
//				respuesta.setDescripcion("Monto Excedido");
//			}
//		} catch (Exception e) {
//			respuesta.setCodigo(3); 
//			respuesta.setDescripcion(e.getMessage());
//		} 
//		return respuesta;
//	}
//
//	/**
//	 * Método que permite guardar una transferencia local.
//	 * 
//	 * @param transfereciaLocal Una clase TransferenciaLocal para realizar el proceso de guardado.
//	 */
//	
	public void guardarTransferenciaLocal(TransferenciaLocal transferenciaLocal) {
		transferenciaLocalDAO.insert(transferenciaLocal);
	}




	/**
	 * Metodo que permite obtener un cliente para el proceso de transacciones o
	 * transferencias.
	 * 
	 * @param numeroCuenta El numero de cuenta de la persona a la que se hace la
	 *                     transaccion o transferencia.
	 * @return Un clase Respuesta indicando los datos del desarrollo del proceso,
	 *         con un codigo, una descripción.
	 * @throws Exception Excepción por si sucede algún error en el proceso.
	 */
//	public Respuesta obtenerClienteCuentaAhorro(String numeroCuenta) {
//		Respuesta respuesta = new Respuesta();
//		CuentaDeAhorro cuentaDeAhorro = cuentaDeAhorroDAO.read(numeroCuenta); 
//		try {
//			if(cuentaDeAhorro!=null) {
//				 respuesta.setCodigo(1); 
//				 respuesta.setDescripcion("Se ha obtenido la cuenta exitosamente"); 
//				 respuesta.setCuentaDeAhorro(cuentaDeAhorro);
//			}else{ 
//				respuesta.setCodigo(2); 
//				respuesta.setDescripcion("La Cuenta de Ahorro no existe");
//			}
//		} catch (Exception e) {
//			respuesta.setCodigo(3); 
//			respuesta.setDescripcion("Error "+e.getMessage());
//		}
//		return respuesta;
//	}

	/**
	 * Metodo que permite convertir una clase InputStream en un byte [] arreglo de
	 * bytes para su posterior guardado en la base de datos.
	 * 
	 * @param in Una clase InputStream que continue la información de un archivo que
	 *           se selecciona en el proceso de la solicitud de credito.
	 * @return Un clase byte [] un arreglo de bytes del InputStream pasado como
	 *         parametro.
	 * @throws IOException Excepción para el manejo de clases que tengan que ver con
	 *                     archivos.
	 */

	public byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		// read bytes from the input stream and store them in buffer
		while ((len = in.read(buffer)) != -1) {
			// write bytes from the buffer into output stream
			os.write(buffer, 0, len);
		}
		return os.toByteArray();
	}

	/**
	 * Metodo que permite cambiar el formato de la fecha
	 * 
	 * @param fecha Fecha que se cambiara el formato
	 * @return La fecha en un formato requerido de tipo texto.
	 */

	public String obtenerFecha2(Date fecha) {
		DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return hourdateFormat.format(fecha);
	}


	/**
	 * Metodo que permite indicar los datos para enviar mediante el correo el
	 * mensaje de cambio de contraseña.
	 * 
	 * @param cliente Una clase Cliente con los datos del cliente.
	 * @throws Exception Excepción por si sucede algún error en el proceso de envio.
	 */
	public void cambioContrasena(Cliente cliente) {
		String destinatario = cliente.getCorreo();
		String asunto = "CAMBIO DE CONTRASEÑA";
		String cuerpo = "JAMVirtual                                               SISTEMA TRANSACCIONAL\n"
				+ "------------------------------------------------------------------------------\n"
				+ "              Estimado(a): " + cliente.getNombre().toUpperCase() + "          "
				+ cliente.getApellido().toUpperCase() + "\n"
				+ "------------------------------------------------------------------------------\n"
				+ "COOPERATIVA JAM le informa que su contraseña ha sido cambiada exitosamente.   \n"
				+ "                                                                              \n"
				+ "                   Su nueva contraseña es:   " + cliente.getClave() + "       \n"
				+ "                       Fecha: " + fecha() + "                                 \n"
				+ "                                                                              \n"
				+ "------------------------------------------------------------------------------\n";
		CompletableFuture.runAsync(() -> {
			try {
				enviarCorreo(destinatario, asunto, cuerpo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	/**
	 * Método que permite calcular la edad del cliente.
	 * 
	 * @param fechaNacimiento La fecha de nacimiento del cliente.
	 * @return La edad del cliente en base a su fecha de nacimiento.
	 */
	public int obtenerEdad(Date fechaNacimiento) {
		Calendar a = Calendar.getInstance();
		Calendar b = Calendar.getInstance();
		a.setTime(fechaNacimiento);
		b.setTime(new Date());
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
				|| (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
			diff--;
		}
		return diff;
	}


	/**
	 * Método que permite obtener los datos de los tipos de cliente para hacer una
	 * gráfica, del servicio web de Django.
	 * 
	 * @return Un mensaje indicando los resultados separados por ";" para su
	 *         posterior gráfica.
	 */
	public String getDatos() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://35.238.98.31:8000/apiAnalisis/verDiagrama/");
		String res = target.request().get(String.class);
		client.close();
		return res;
	}

	/**
	 * Método que permite cambiar el formato de los nuemros que se generen.
	 * 
	 * @param valor El valor del double para transformar.
	 */
	public double valorDecimalCr(double valor) {
		String num = String.format(Locale.ROOT, "%.2f", valor);
		return Double.parseDouble(num);
	}

	/*
	 * @Override public void eliminarCuentaDeAhorro(String numeroCuentaDeAhorro) {
	 * // TODO Auto-generated method stub
	 * 
	 * }
	 */
}
