package com.github.cidarosa.ms_pagamento.repository;

import com.github.cidarosa.ms_pagamento.entity.Pagamento;
import com.github.cidarosa.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository repository;

    //declarando variáveis
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalPagamento;

    @BeforeEach
    void setup() throws Exception{
        // Arrange
        existingId = 1L;
        nonExistingId = 100L;
        countTotalPagamento = 3L;

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
         // Act
        repository.deleteById(existingId);
        // Assert
        Optional<Pagamento> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());

    }

    @Test
    @DisplayName("Dado parâmetros válidos e Id nulo, quando chamar Criar Pagamento, então deve instanciar um Pagamento")
    public void givenValidParamsAndIdIsNull_whenCallCreatePagamento_thenInstantiateAPagamento(){

        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);
        pagamento = repository.save(pagamento);
        Assertions.assertNotNull(pagamento.getId());
        Assertions.assertEquals(countTotalPagamento + 1 , pagamento.getId());

    }

    @Test
    @DisplayName("given an existing Id when call findById then return non empty optional")
    public void givenAnExistingId_whenCallFindById_thenReturnNonEmptyOptional(){

        Optional<Pagamento> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("given an existing Id when call findById then return empty optional")
    public void givenAnExistingId_whenCallFindById_thenReturnEmptyOptional(){

        Optional<Pagamento> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
        Assertions.assertTrue(result.isEmpty());
    }


}
