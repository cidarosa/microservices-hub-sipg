package br.com.fiap.ms_pedidos.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // @Column - define características da coluna no DB
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    //@Column(unique = true, nullable = false, length = 11)
    @Column(nullable = false)
    private String cpf;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private Status status;

    // Valor calculado
    private BigDecimal valorTotal;

    // Relacionamento
    @OneToMany(mappedBy = "pedido",
            cascade = CascadeType.ALL)
    private List<ItemDoPedido> itens = new ArrayList<>();

    public void calcularTotalDoPedido() {
        this.valorTotal = this.itens.stream()
                .map(i -> i.getValorUnitario()
                        .multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //equivalente

//    public void calcularTotalDoPedido_() {
//        BigDecimal total = BigDecimal.ZERO;
//
//        for (ItemDoPedido i : this.items) {
//            BigDecimal valorItem = i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade()));
//            total = total.add(valorItem);
//        }
//
//        this.valorTotal = total;
//    }


}
