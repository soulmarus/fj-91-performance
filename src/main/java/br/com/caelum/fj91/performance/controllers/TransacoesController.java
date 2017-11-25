package br.com.caelum.fj91.performance.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.caelum.fj91.performance.daos.TransacaoDao;
import br.com.caelum.fj91.performance.models.Transacao;

@Controller
public class TransacoesController {
	
	@Autowired
	private JmsTemplate jms;
	
	@Autowired
	private TransacaoDao transacaoDao;

	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE,value="/transacoes")
	@ResponseBody
	@Cacheable("transacoes")
	@Async
	public ResponseEntity<List<Transacao>> lista(@RequestParam(value = "page") Optional<Integer> page){
		
		System.out.println("Lista Transacoes");
		
		PageRequest pageRequest = new PageRequest(page.orElse(0), 100);
		
		Page<Transacao> result = transacaoDao.findAll(pageRequest);
				
		return ResponseEntity
				.ok()
				.cacheControl(CacheControl.maxAge(90, TimeUnit.MINUTES))
				.body(result.getContent());
	}
	
	@RequestMapping(method= RequestMethod.POST, value = "/transacoes")
	@ResponseBody
	public ResponseEntity<?> salvar(@RequestBody Transacao transacao) throws InterruptedException {
		
		jms.send("processador-de-transacoes", 
				(session) -> session.createObjectMessage(transacao));
		
		return ResponseEntity.accepted().build();
	}
	
	@RequestMapping(method= RequestMethod.GET, value = "/transacoes/{id}")
	public ResponseEntity<?>  buscaPorId(@PathVariable Long id) {
		System.out.println("buscaPorId");
		Optional<Transacao> optionalTransacao = transacaoDao.findOne(id);
		
		if (optionalTransacao.isPresent()) {
			return ResponseEntity.ok(optionalTransacao.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	 
}
