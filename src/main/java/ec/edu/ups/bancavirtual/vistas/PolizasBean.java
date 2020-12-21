package ec.edu.ups.bancavirtual.vistas;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.MoveEvent;

import ec.edu.ups.bancavirtual.modelo.*;
import ec.edu.ups.bancavirtual.on.GestionUsuarioLocal;
import ec.edu.ups.bancavirtual.dao.*;

/**
 * @author ADMINX
 *
 */
@ManagedBean
@ViewScoped
public class PolizasBean {

	@Inject
	private GestionUsuarioLocal polizaON;

	private Poliza poliza;

	private List<Poliza> listasPolizas;

	@PostConstruct
	public void init() {
		poliza = new Poliza();
		loadData();
	}

	public void setListaEmpleados(List<Poliza> listaEmpleados) {
		this.listasPolizas = listaEmpleados;
	}

	public GestionUsuarioLocal getPolizaON() {
		return polizaON;
	}

	public void setPolizaON(GestionUsuarioLocal polizaON) {
		this.polizaON = polizaON;
	}

	public Poliza getPoliza() {
		return poliza;
	}

	public void setPoliza(Poliza poliza) {
		this.poliza = poliza;
	}

	public List<Poliza> getListasPolizas() {
		return listasPolizas;
	}

	public void setListasPolizas(List<Poliza> listasPolizas) {
		this.listasPolizas = listasPolizas;
	}

	public void addMessage(String summary, String detail) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
	}

	public void handleClose(CloseEvent event) {
		addMessage(event.getComponent().getId() + " closed", "So you don't like nature?");
	}

	public void handleMove(MoveEvent event) {
		event.setTop(500);
		addMessage(event.getComponent().getId() + " moved", "Left: " + event.getLeft() + ", Top: " + event.getTop());
	}


	// METODO PARA GUARDAR POLIZAS
	public void guardarPoliza() {
		polizaON.guardarPoliza(poliza);
		System.out.println("Se muestra  la crear de la poliza desde Bean");
	}

	// METODO PARA LISTAR LAS POLIZAS
	public void loadData() {
		listasPolizas = polizaON.listasPolizas();
	}

}
