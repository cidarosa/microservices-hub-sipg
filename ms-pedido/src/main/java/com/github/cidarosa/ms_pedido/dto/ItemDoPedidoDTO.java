package com.github.cidarosa.ms_pedido.dto;

import com.github.cidarosa.ms_pedido.entities.ItemDoPedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemDoPedidoDTO {

    private Long id;

    @NotNull(message = "Quantidade requerida")
    @Positive(message = "A quantidade deve ser um valor positivo")
    private Integer quantidade;

    @NotBlank(message = "Descricação é requerida")
    private String descricao;

    @NotNull(message = "Valor unitário é requerido")
    @Positive(message = "O valor unitário deve ser um número positivo")
    private BigDecimal valorUnitario;

    public ItemDoPedidoDTO(ItemDoPedido entity) {
        id = entity.getId();
        quantidade = entity.getQuantidade();
        descricao = entity.getDescricao();
        valorUnitario = entity.getValorUnitario();
    }
}
