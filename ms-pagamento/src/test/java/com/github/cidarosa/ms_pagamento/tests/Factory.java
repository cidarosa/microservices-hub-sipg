package com.github.cidarosa.ms_pagamento.tests;

import com.github.cidarosa.ms_pagamento.dto.PagamentoDTO;
import com.github.cidarosa.ms_pagamento.entity.Pagamento;
import com.github.cidarosa.ms_pagamento.entity.Status;

import java.math.BigDecimal;

public class Factory {

    public static Pagamento createPagamento(){
        Pagamento pagamento = new Pagamento(1L, BigDecimal.valueOf(32.25),
                "Jon Snow", "2345789632145789",
                "07/32", "585", Status.CRIADO, 1L, 2L);

        return  pagamento;
    }

    public static PagamentoDTO createPagamentoDTO(){

        Pagamento pagamento = createPagamento();
        return new PagamentoDTO(pagamento);

    }

    public static PagamentoDTO createNewPagamentoDTO(){

        Pagamento pagamento = createPagamento();
        pagamento.setId(null);
        return new PagamentoDTO(pagamento);
    }

    public static PagamentoDTO createNewPagamentoDTOWithRequiredFields(){

        Pagamento pagamento = createPagamento();
        pagamento.setId(null);
        pagamento.setNome(null);
        pagamento.setNumeroDoCartao(null);
        pagamento.setCodigoDeSeguranca(null);
        pagamento.setValidade(null);
        return new PagamentoDTO(pagamento);
    }

    public  static PagamentoDTO createNewPagamentoDTOWithInvalidData(){

        Pagamento pagamento  = createPagamento();
        pagamento.setId(null);
        pagamento.setValor(BigDecimal.valueOf(-35.2));
        pagamento.setPedidoId(null);
        pagamento.setFormaDePagamentoId(null);

        return new PagamentoDTO(pagamento);
    }
}
