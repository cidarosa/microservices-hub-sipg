package br.com.fiap.ms_pagamento.service;

import br.com.fiap.ms_pagamento.repository.PagamentoRepository;
import br.com.fiap.ms_pagamento.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // carrega o contexto da aplicação
@Transactional // Rollback no DB
public class PagamentoServiceIT {

    @Autowired
    private PagamentoService service;
    @Autowired
    private PagamentoRepository repository;
    //preparando os dados
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalPagamento;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        nonExistingId = 100L;
        countTotalPagamento = 2L;
    }

    @Test
    public void deletePagamentoShouldDeleteResourceWhenIdExists(){

        service.deletePagamento(existingId);
        Assertions.assertEquals(countTotalPagamento - 1, repository.count());

    }

    @Test
    public void deletePagamentoShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.deletePagamento(nonExistingId);
        });
    }

    @Test
    public void getAllShouldReturnListPagamentoDTO(){

        var result = service.getAll();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countTotalPagamento, result.size());
        Assertions.assertEquals(Double.valueOf(790.0), result.get(0).getValor().doubleValue());
        Assertions.assertEquals("Amadeus Mozart", result.get(0).getNome());
        Assertions.assertEquals("Chiquinha Gonzaga", result.get(1).getNome());

        //ou Assertions.assertEquals(null, result.get(5).getNome());
    }


}




