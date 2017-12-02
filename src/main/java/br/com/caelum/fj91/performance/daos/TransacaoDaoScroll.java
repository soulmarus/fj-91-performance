package br.com.caelum.fj91.performance.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.springframework.stereotype.Repository;

import br.com.caelum.fj91.performance.models.ScrollableIterable;
import br.com.caelum.fj91.performance.models.Transacao;

@Repository
public class TransacaoDaoScroll {

	@PersistenceContext
	private EntityManager manager;
	
	public Iterable<Transacao> findAll() {
		Session session = manager.unwrap(Session.class);
		
		StatelessSession statelessSession = session.getSessionFactory().openStatelessSession();
		
		ScrollableResults scrollableResults = statelessSession.createQuery("select t from Transacao t").scroll();
		
		return new ScrollableIterable<>(scrollableResults);
	}
}
