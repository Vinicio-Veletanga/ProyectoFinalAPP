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

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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


public class Respuesta {

	private int codigo;
	private String descripcion;
	private @JsonProperty("Cliente") Cliente cliente;
	private @JsonProperty("Cuenta") CuentaDeAhorro cuentaDeAhorro;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CuentaDeAhorro getCuentaDeAhorro() {
		return cuentaDeAhorro;
	}

	public void setCuentaDeAhorro(CuentaDeAhorro cuentaDeAhorro) {
		this.cuentaDeAhorro = cuentaDeAhorro;
	}


}
