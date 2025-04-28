package com.github.cidarosa.ms_pedido.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_pedido")
public class Pedido {

    private Long id;
    private String nome;
    private String cpf;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private Status status;

}
