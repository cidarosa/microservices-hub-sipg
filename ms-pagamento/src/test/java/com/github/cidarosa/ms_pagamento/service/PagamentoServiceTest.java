package com.github.cidarosa.ms_pagamento.service;

import com.github.cidarosa.ms_pagamento.dto.PagamentoDTO;
import com.github.cidarosa.ms_pagamento.entity.Pagamento;
import com.github.cidarosa.ms_pagamento.repository.PagamentoRepository;
import com.github.cidarosa.ms_pagamento.service.exceptions.ResourceNotFoundException;
import com.github.cidarosa.ms_pagamento.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    private Long existingId;
    private Long nonExistingId;

    private Pagamento pagamento;
    private PagamentoDTO dto;

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

        pagamento = Factory.createPagamento();
        dto = new PagamentoDTO(pagamento);
        // simulando os comportamentos
        // getById
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pagamento));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        // createPagamento
        Mockito.when(repository.save(any())).thenReturn(pagamento);
        // updatePagamento - quando ID existe
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(pagamento);
        // updatePagamento - quando ID não existe
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);


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

    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdEixsts() {

        dto = service.getById(existingId);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    public void getByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {


        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.getById(nonExistingId);
                }
        );
    }

    @Test
    public void createPagamentoShouldReturnPagamentoDTOWhenPagamentoIsCreated() {

        dto = service.createPagamento(dto);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), pagamento.getId());
    }

    @Test
    public void updatePagamentoShouldReturnPagamentoDTOWhenIdExists() {

        dto = service.updatePagamento(pagamento.getId(), dto);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
        Assertions.assertEquals(dto.getStatus(), pagamento.getStatus());
    }

    @Test
    public void updatePagamentoShouldReturnResourceNotFoundExcenptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.updatePagamento(nonExistingId, dto);
                }
        );
    }
}



