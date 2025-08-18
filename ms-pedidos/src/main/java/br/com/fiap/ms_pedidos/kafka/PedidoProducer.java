package br.com.fiap.ms_pedidos.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void enviarMensagem(String mensagem){
        kafkaTemplate.send("topico-pedidos", mensagem);
    }
}
