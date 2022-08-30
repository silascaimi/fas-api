package com.silascaimi.fasapi.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.silascaimi.fasapi.dto.LancamentoEstatisticaPessoa;
import com.silascaimi.fasapi.mail.AppMailSender;
import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.model.Pessoa;
import com.silascaimi.fasapi.model.Usuario;
import com.silascaimi.fasapi.repository.LancamentoRepository;
import com.silascaimi.fasapi.repository.PessoaRepository;
import com.silascaimi.fasapi.repository.UsuarioRepository;
import com.silascaimi.fasapi.service.exception.PessoaInexistenteOuInativaException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {
	
	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";
	
	@Autowired
	private PessoaRepository pessoaRepositoy;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private AppMailSender mailer;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepositoy.findById(lancamento.getPessoa().getCodigo());
		
		if(!pessoa.isPresent() || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo)
				.orElseThrow(() -> new IllegalArgumentException());
		if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento.getPessoa());
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		
		return lancamentoRepository.save(lancamentoSalvo);
	}

	private void validarPessoa(Pessoa pessoa) {
		Optional<Pessoa> pessoaSalva = null;
		
		if(pessoa.getCodigo() != null) {
			pessoaSalva = pessoaRepositoy.findById(pessoa.getCodigo());
		}
		
		if (pessoaSalva.isEmpty() || pessoaSalva.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}
	
	
	// @Scheduled(fixedDelay = 1000 * 5) Executa no incicio da aplicacao e após 5 em 5 segundos
	@Scheduled(cron = "0 0 6 * * *") // segundo, minuto, hora, diaMes, mes, diaSemana
	public void avisarSobreLnacamentosVencidos() {
		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		
		List<Usuario> destinatarios = usuarioRepository
				.findByPermissoesDescricao(DESTINATARIOS);
		
		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
	}
	
	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws JRException {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream is = this.getClass().getResourceAsStream("/relatorios/lancamento-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(is, parametros, new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

}
