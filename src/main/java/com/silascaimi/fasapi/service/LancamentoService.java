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
import org.springframework.stereotype.Service;

import com.silascaimi.fasapi.dto.LancamentoEstatisticaPessoa;
import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.model.Pessoa;
import com.silascaimi.fasapi.repository.LancamentoRepository;
import com.silascaimi.fasapi.repository.PessoaRepository;
import com.silascaimi.fasapi.service.exception.PessoaInexistenteOuInativaException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepositoy;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

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
