package br.com.fiap.ms_pedidos.controller;

import br.com.fiap.ms_pedidos.dto.PedidoDTO;
import br.com.fiap.ms_pedidos.dto.StatusDTO;
import br.com.fiap.ms_pedidos.entities.Pedido;
import br.com.fiap.ms_pedidos.kafka.PedidoProducer;
import br.com.fiap.ms_pedidos.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @Autowired
    private PedidoProducer pedidoProducer;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> findAllPedidos() {

        List<PedidoDTO> list = service.getAllPedidos();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(@PathVariable Long id) {

        PedidoDTO dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    // testando load balancing - balanceamento de carga
    // devolve a porta da instância que está rodando
    @GetMapping("/port")
    public String getPort(@Value("${local.server.port}") String porta) {

        return String.format("Request da Instância recebida na porta %s", porta);

    }

    @PostMapping
    public ResponseEntity<PedidoDTO> createPedido(@Valid @RequestBody PedidoDTO dto) {

        dto = service.savePedido(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarMensagem(@RequestParam String mensagem){

        pedidoProducer.enviarMensagem(mensagem);
        return ResponseEntity.ok("Mensagem enviada para o Kafka: " + mensagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> updatePedido(@PathVariable Long id,
                                                  @Valid @RequestBody PedidoDTO dto) {

        dto = service.updatePedido(id, dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/pago")
    public ResponseEntity<String> aprovarPagamentoDoPedido(@PathVariable @NotNull Long id){

        service.aprovarPagamentoDoPedido(id);
        String msg = "Pedido pago, aguardar confirmação de pagamento";
        return ResponseEntity.ok().body(msg);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> updatePedidoStatu(@PathVariable Long id,
                                                       @RequestBody StatusDTO statusDTO){
        PedidoDTO dto = service.updatePedidoStatus(id, statusDTO);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedidoById(@PathVariable Long id) {

        service.deletePedido(id);

        return ResponseEntity.noContent().build();
    }
}















