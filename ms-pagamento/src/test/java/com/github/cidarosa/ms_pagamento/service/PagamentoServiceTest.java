package com.github.cidarosa.ms_pagamento.service;

import com.github.cidarosa.ms_pagamento.repository.PagamentoRepository;
import com.github.cidarosa.ms_pagamento.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;

        // simulando o comportamento do objeto mokado
        // delete - quando ID existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        // delete - quando ID não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        // não faça nada quando .... (void)
        Mockito.doNothing().when(repository).deleteById(existingId);

    }

    @Test
    @DisplayName("delete Deveria não fazer nada quando ID existe")
    public void deleteShouldDoNothingWhenIsExists() {

        Assertions.assertDoesNotThrow(
                () -> {
                    service.deletePagamento(existingId);
                }
        );
    }

    @Test
    @DisplayName("delete Deveria lança exceção ResourceNotFoundException quando ID não existe")
    public void deleteShouldThrowResourceNotFondExceptionIsDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.deletePagamento(nonExistingId);
                }
        );

    }
}



