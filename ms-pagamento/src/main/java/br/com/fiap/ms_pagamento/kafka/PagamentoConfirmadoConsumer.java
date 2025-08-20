package br.com.fiap.ms_pagamento.kafka;

import br.com.fiap.ms_pagamento.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PagamentoConfirmadoConsumer {

    @Autowired
    private PagamentoService pagamentoService;

    @KafkaListener(topics = "pagamento-confirmado", groupId = "ms-pagamento")
    private void consumirConfirmacao(String pagamentoId){

        Long id = Long.parseLong(pagamentoId);

        pagamentoService.confirmarPagamentoKafka(id);
    }
}
