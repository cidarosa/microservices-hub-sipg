package br.com.fiap.ms_pedidos.kafka;

import br.com.fiap.ms_pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PagamentoPendenteConsumer {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PagamentoConfirmadoProducer pagamentoConfirmadoProducer;

    @KafkaListener(topics = "pagamento-pendente", groupId = "ms-pedidos")
    public void consumirPagamentoPendente(String pagamentoId){

        Long id = Long.parseLong(pagamentoId);

        pedidoService.aprovarPagamentoDoPedido(id);

        pagamentoConfirmadoProducer.enviarConfirmacao(id.toString());

    }
}
