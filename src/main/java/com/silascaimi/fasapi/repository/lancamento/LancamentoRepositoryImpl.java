package com.silascaimi.fasapi.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.silascaimi.fasapi.dto.LancamentoEstatisticaCategoria;
import com.silascaimi.fasapi.dto.LancamentoEstatisticaDia;
import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.model.Lancamento_;
import com.silascaimi.fasapi.model.Pessoa;
import com.silascaimi.fasapi.model.Pessoa_;
import com.silascaimi.fasapi.model.TipoLancamento;
import com.silascaimi.fasapi.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	EntityManager manager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);

		Root<Lancamento> root = criteria.from(Lancamento.class);
		root.fetch(Lancamento_.categoria, JoinType.LEFT);
		root.fetch(Lancamento_.pessoa, JoinType.LEFT);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates =criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}

		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoDe()));
		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoAte()));
		}

		if (lancamentoFilter.getCodigoPessoa() != null) {
			Join<Lancamento, Pessoa> joinPessoa = root.join(Lancamento_.pessoa);
			predicates.add(builder.equal(joinPessoa.get(Pessoa_.codigo), lancamentoFilter.getCodigoPessoa()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoria> query = builder.createQuery(LancamentoEstatisticaCategoria.class);
		
		Root<Lancamento> root = query.from(Lancamento.class);
		
		query.select(builder.construct(LancamentoEstatisticaCategoria.class, 
				root.get(Lancamento_.categoria), 
				builder.sum(root.get(Lancamento_.valor))
			));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		query.where(
				builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
				builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia),
				builder.equal(root.get(Lancamento_.tipo), TipoLancamento.DESPESA)
			);
		
		query.groupBy(root.get(Lancamento_.categoria));
		
		Order order = builder.desc(builder.sum(root.get(Lancamento_.valor)));
		query.orderBy(order);
		
		TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager.createQuery(query);
		
		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaDia> query = builder.createQuery(LancamentoEstatisticaDia.class);
		
		Root<Lancamento> root = query.from(Lancamento.class);
		
		query.select(builder.construct(LancamentoEstatisticaDia.class, 
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.dataVencimento),
				builder.sum(root.get(Lancamento_.valor))
			));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		query.where(
				builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
				builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia),
				builder.equal(root.get(Lancamento_.tipo), TipoLancamento.DESPESA)
			);
		
		query.groupBy(
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.dataVencimento)
			);
		
		Order order = builder.desc(builder.sum(root.get(Lancamento_.valor)));
		query.orderBy(order);
		
		TypedQuery<LancamentoEstatisticaDia> typedQuery = manager.createQuery(query);
		
		return typedQuery.getResultList();
	}

}
