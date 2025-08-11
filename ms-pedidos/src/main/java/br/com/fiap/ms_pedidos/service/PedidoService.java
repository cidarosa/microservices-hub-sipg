package br.com.fiap.ms_pedidos.service;

import br.com.fiap.ms_pedidos.dto.ItemDoPedidoDTO;
import br.com.fiap.ms_pedidos.dto.PedidoDTO;
import br.com.fiap.ms_pedidos.dto.StatusDTO;
import br.com.fiap.ms_pedidos.entities.ItemDoPedido;
import br.com.fiap.ms_pedidos.entities.Pedido;
import br.com.fiap.ms_pedidos.entities.Status;
import br.com.fiap.ms_pedidos.repositories.ItemDoPedidoRepository;
import br.com.fiap.ms_pedidos.repositories.PedidoRepository;
import br.com.fiap.ms_pedidos.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ItemDoPedidoRepository itemDoPedidoRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> getAllPedidos() {

        return repository.findAll()
                .stream().map(PedidoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO getById(Long id) {

        Pedido entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new PedidoDTO(entity);
    }

    @Transactional
    public PedidoDTO savePedido(PedidoDTO dto) {

        Pedido entity = new Pedido();
        entity.setData(LocalDate.now());
        entity.setStatus(Status.REALIZADO);
        copyDtoToEntity(dto, entity);
        entity.calcularTotalDoPedido();
        entity = repository.save(entity);
        itemDoPedidoRepository.saveAll(entity.getItens());
        return new PedidoDTO(entity);
    }

    @Transactional
    public PedidoDTO updatePedido(Long id, PedidoDTO dto) {
        try {
            Pedido entity = repository.getReferenceById(id);
            entity.setData(LocalDate.now());
            entity.setStatus(Status.REALIZADO);
            // Exclui os itens antigos
            itemDoPedidoRepository.deleteByPedidoId(id);
            copyDtoToEntity(dto, entity);
            entity.calcularTotalDoPedido();
            entity = repository.save(entity);
            // Atualiza os itens com os novos dados
            itemDoPedidoRepository.saveAll(entity.getItens());
            return new PedidoDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    public void deletePedido(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }

        repository.deleteById(id);
    }

    @Transactional
    public void aprovarPagamentoDoPedido(Long id){

        Pedido pedido = repository.getPedidoByIdWithItens(id);
        if(pedido == null){
            throw new ResourceNotFoundException("Pedido id: " + id + " não encontrado.");
        }

        pedido.setStatus(Status.PAGO);
        repository.updatePedido(Status.PAGO, pedido);
    }

    public PedidoDTO updatePedidoStatus(Long id, StatusDTO statusDTO){

        Pedido pedido = repository.getPedidoByIdWithItens(id);

        if(pedido == null){
            throw new ResourceNotFoundException("Pedido id: " + id + " não encontrado.");
        }
        pedido.setStatus(statusDTO.getStatus());
        repository.updatePedido(statusDTO.getStatus(), pedido);

        return new PedidoDTO(pedido);
    }

    private void copyDtoToEntity(PedidoDTO dto, Pedido entity) {

        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());

        List<ItemDoPedido> items = new ArrayList<>();

        for (ItemDoPedidoDTO itemDTO : dto.getItens()) {
            ItemDoPedido itemDoPedido = new ItemDoPedido();
            itemDoPedido.setQuantidade(itemDTO.getQuantidade());
            itemDoPedido.setDescricao(itemDTO.getDescricao());
            itemDoPedido.setValorUnitario(itemDTO.getValorUnitario());
            itemDoPedido.setPedido(entity);
            items.add(itemDoPedido);
        }
        entity.setItens(items);
    }


}
