package com.github.cidarosa.ms_pedido.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_item_do_pedido")
public class ItemDoPedido {

    private Long id;
    private Integer quantidade;
    private String descricao;
    private BigDecimal valorUnitario;
}
