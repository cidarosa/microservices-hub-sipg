package br.com.fiap.ms_pedidos.repositories;

import br.com.fiap.ms_pedidos.entities.ItemDoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ItemDoPedidoRepository extends JpaRepository<ItemDoPedido, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ItemDoPedido i WHERE i.pedido.id = :pedidoId")
    void deleteByPedidoId(Long pedidoId);
}
