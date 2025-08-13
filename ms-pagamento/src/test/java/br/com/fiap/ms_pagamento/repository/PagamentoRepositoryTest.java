package br.com.fiap.ms_pagamento.repository;

import br.com.fiap.ms_pagamento.entity.Pagamento;
import br.com.fiap.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository repository;

    //declarando as variáveis
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalPagamento;

    //Vai ser executado ANTES de cada teste
    @BeforeEach
    void setup() throws Exception{
        // Arrange
        existingId = 1L;
        nonExistingId = 100L;
        //verificar quantos registros tem no seed do DB
        countTotalPagamento = 2L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        // Act
        repository.deleteById(existingId);
        // Assert
        // Após deletar, tenta encontrar o objeto com o ID no repositório.
        Optional<Pagamento> result = repository.findById(existingId);
        //testa se existe um obj dentro do Optional
        // verifica se o objeto não está presente no repositório.
        // Se result.isPresent() for false,
        // significa que o objeto foi deletado com sucesso.
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    //  Fornece uma descrição legível do teste, que será exibida nos relatórios de teste
    @DisplayName("Dado parâmetros válidos e Id nulo " +
            "quando chamar Criar Pagamento " +
            "então deve instanciar um Pagamento")
    public void givenValidParamsAndIdIsNull_whenCallCreatePagamento_thenInstantiateAPagamento(){

        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);
        pagamento = repository.save(pagamento);
        Assertions.assertNotNull(pagamento.getId());
        // verifica se o ID gerado é o próximo
        Assertions.assertEquals(countTotalPagamento + 1, pagamento.getId());
    }

    // Desafio
    @Test
//    @DisplayName("dado um ID existente quando chamar findById então deve retornar um Optional não vazio")
    @DisplayName("given a non-existing ID when calling findById then it should return an empty Optional")
    public void givenAnExistingId_whenCallFindById_thenReturnNonEmptyOptional(){

        Optional<Pagamento> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("dado um ID Não existente quando chamar findById então deve retornar um Optional vazio")
    public void givenANonExistingId_whenCallFindById_thenReturnAnEmptyOptional(){

        Optional<Pagamento> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
        // ou
        Assertions.assertTrue(result.isEmpty());
    }

}

// Não será implementado
//    @Test
//        public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
//
//            Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
//                repository.deleteById(nonExistingId);
//            });
//        }
//    Esse teste falhou por causa da versão do Spring Boot
//    Alterações do Spring Boot versão **3.X.X**,
//    o metodo deleteById do repositório não lança exceção se o ID não existir.
//    Em vez disso, ele simplesmente não faz nada.


