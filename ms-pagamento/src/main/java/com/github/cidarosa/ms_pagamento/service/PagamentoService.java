package com.github.cidarosa.ms_pagamento.service;

import com.github.cidarosa.ms_pagamento.dto.PagamentoDTO;
import com.github.cidarosa.ms_pagamento.entity.Pagamento;
import com.github.cidarosa.ms_pagamento.entity.Status;
import com.github.cidarosa.ms_pagamento.repository.PagamentoRepository;
import com.github.cidarosa.ms_pagamento.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Transactional(readOnly = true)
    public List<PagamentoDTO> getAll(){
        List<Pagamento> pagamentos = repository.findAll();
        return pagamentos.stream().map(PagamentoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagamentoDTO getById(Long id){
        Pagamento entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );
        return new PagamentoDTO(entity);
    }

    @Transactional
    public PagamentoDTO createPagamento(PagamentoDTO dto){
        Pagamento entity = new Pagamento();
        copyDtoToEntity(dto, entity);
        entity.setStatus(Status.CRIADO);
        entity = repository.save(entity);
        return new PagamentoDTO(entity);
    }

    private void copyDtoToEntity(PagamentoDTO dto, Pagamento entity) {
        entity.setValor(dto.getValor());
        entity.setNome(dto.getNome());
        entity.setNumeroDoCartao(dto.getNumeroDoCartao());
        entity.setValidade(dto.getValidade());
        entity.setCodigoDeSeguranca(dto.getCodigoDeSeguranca());
        entity.setPedidoId(dto.getPedidoId());
        entity.setFormaDePagamentoId(dto.getFormaDePagamentoId());

    }
}
