package br.com.fiap.ms_pagamento.service;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.entity.Pagamento;
import br.com.fiap.ms_pagamento.repository.PagamentoRepository;
import br.com.fiap.ms_pagamento.service.exceptions.ResourceNotFoundException;
import br.com.fiap.ms_pagamento.tests.Factory;
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

    // Mock - Injetando
    @InjectMocks
    private PagamentoService service;

    // Mock do repositório
    @Mock
    private PagamentoRepository repository;

    //preparando os dados
    private Long existingId;
    private Long nonExistingId;

    // próximos testes
    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;
        //precisa simular o comportamento do objeto mockado
        //delete - quando id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        //delete - quando id não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        //Mockito.when(repository.existsById(Mockito.argThat(id -> !id.equals(existingId)))).thenReturn(false);
        //delete - primeiro caso - deleta
        // não faça nada (void) quando ...
        Mockito.doNothing().when(repository).deleteById(existingId);
        // próximos testes
        pagamento = Factory.createPagamento();
        pagamentoDTO = new PagamentoDTO(pagamento);
        // simulação dos comportamentos
        // getById (findById)
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pagamento));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        // createPagamento (insert)
        Mockito.when(repository.save(any())).thenReturn(pagamento);
        // updatePagamento (update) - primeito caso - id existe
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(pagamento);
        // updatePagamento (update) - segundo caso - id não existe
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("delete Deveria não fazer nada quando Id existe")
    public void deleteShouldDoNothingWhenIdExists() {
        // em PagamentoService, o metodo delete é do tipo void
        Assertions.assertDoesNotThrow(
                () -> {
                    service.deletePagamento(existingId);
                }
        );
    }

    @Test
    @DisplayName("delete Deveria lançar exceção ResourceNotFoundException quando Id não existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.deletePagamento(nonExistingId);
                }
        );
    }

    // respostas do desafio
    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdExists(){

        // nome da variável para ficar igual ao controller,
        // não é obrigatório
        PagamentoDTO dto = service.getById(existingId);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    public void getByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(nonExistingId);
        });
    }

    @Test
    public void createPagamentoShouldReturnPagamentoDTOWhenPagamentoIsCreated(){

        PagamentoDTO dto = service.createPagamento(pagamentoDTO);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), pagamento.getId());
    }

    @Test
    public void updatePagamentoShouldReturnPagamentoDTOWhenIdExists(){

        PagamentoDTO dto = service.updatePagamento(pagamento.getId(), pagamentoDTO);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), existingId);
        Assertions.assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    public void updatePagamentoShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.updatePagamento(nonExistingId, pagamentoDTO);
        });
    }

}









