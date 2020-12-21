package ec.edu.ups.bancavirtual.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.ups.bancavirtual.modelo.*;

/**
 * @author ADMINX
 *
 */
@Stateless
public class PolizaDAO {

	// Atributo de la clase
	@PersistenceContext(name = "bancavirtualPersistenceUnit")
	private EntityManager em;

	public void insert(Poliza p) {
		em.persist(p);
	}

	public void update(Poliza p) {
		em.merge(p);
	}

	public Poliza read(int id) {
		return em.find(Poliza.class, id);
	}

	public void delete(int id) {
		Poliza p = read(id);
		em.remove(p);
	}

	public List<Poliza> getPolizas() {
		String jpql = "SELECT c FROM Poliza c ";

		Query q = em.createQuery(jpql, Poliza.class);
		return q.getResultList();
	}

}
