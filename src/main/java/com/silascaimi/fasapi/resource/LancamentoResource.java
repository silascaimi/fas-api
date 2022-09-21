package com.silascaimi.fasapi.resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.silascaimi.fasapi.dto.LancamentoEstatisticaCategoria;
import com.silascaimi.fasapi.dto.LancamentoEstatisticaDia;
import com.silascaimi.fasapi.event.RecursoCriadoEvent;
import com.silascaimi.fasapi.exceptionhandler.FASExceptionHandler.Erro;
import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.repository.LancamentoRepository;
import com.silascaimi.fasapi.repository.filter.LancamentoFilter;
import com.silascaimi.fasapi.service.LancamentoService;
import com.silascaimi.fasapi.service.exception.PessoaInexistenteOuInativaException;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscaPeloCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
		return ResponseEntity.ok(lancamento);
	}
	
	@GetMapping("/estatisticas/por-categoria")
	public ResponseEntity<List<LancamentoEstatisticaCategoria>> porCategoria() {
		return ResponseEntity.ok(lancamentoRepository.porCategoria(LocalDate.now()));
	}
	
	@GetMapping("/estatisticas/por-dia")
	public List<LancamentoEstatisticaDia> porDia() {
		return this.lancamentoRepository.porDia(LocalDate.now());
	}
	
	@GetMapping("relatorio/por-pessoa")
	public ResponseEntity<byte[]> relatorioPorPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws JRException {
		byte[] relatorio = lancamentoService.relatorioPorPessoa(inicio, fim);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
		headers.add("Content-Disposition", "attachment; filename=lancamento_por_pessoa.pdf");
		
		return ResponseEntity.ok()
				.headers(headers)
				.body(relatorio);
	}

	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@PostMapping("/anexo")
	public String uploadArquivo(@RequestParam MultipartFile anexo) throws IOException {
		OutputStream out = new FileOutputStream("/home/cemig/Desktop/anexo" + anexo.getOriginalFilename());
		out.write(anexo.getBytes());
		out.close();
		return "ok";
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		try {
			Lancamento lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
			return ResponseEntity.ok(lancamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}
	
	@ExceptionHandler({PessoaInexistenteOuInativaException.class})
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
}
