package br.com.fiap.ms_pedidos.dto;

import br.com.fiap.ms_pedidos.entities.ItemDoPedido;
import br.com.fiap.ms_pedidos.entities.Pedido;
import br.com.fiap.ms_pedidos.entities.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PedidoDTO {

    private Long id;

    // @CPF - valida o CPF
    @NotEmpty(message = "CPF requerido")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
    private String cpf;
    @NotEmpty(message = "Nome requerido")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private Status status;

    // valor calculado
    private BigDecimal valorTotal;

    @NotEmpty(message = "Deve ter pelo menos um item do pedido")
    private List<@Valid ItemDoPedidoDTO> itens = new ArrayList<>();

    public PedidoDTO(Pedido entity) {
        id = entity.getId();
        cpf = entity.getCpf();
        nome = entity.getNome();
        data = entity.getData();
        status = entity.getStatus();

        valorTotal = entity.getValorTotal();

        //para preencher os itens
        for (ItemDoPedido item : entity.getItens()) {
            ItemDoPedidoDTO itemDTO = new ItemDoPedidoDTO(item);
            itens.add(itemDTO);
        }
    }
}
