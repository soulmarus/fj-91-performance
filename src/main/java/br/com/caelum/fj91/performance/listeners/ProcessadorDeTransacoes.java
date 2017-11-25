package br.com.caelum.fj91.performance.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.caelum.fj91.performance.daos.TransacaoDao;
import br.com.caelum.fj91.performance.models.Transacao;

@Component
public class ProcessadorDeTransacoes {


	@Autowired
	private TransacaoDao transacaoDao;
	
	@CacheEvict(value = "transacoes", allEntries = true)
	@JmsListener(destination = "processador-de-transacoes")
	public void processa(Transacao transacao) throws InterruptedException {
		Thread.sleep(5 * 1000);
		transacaoDao.save(transacao);
		System.out.println("salvar");
	}
		
	
}
