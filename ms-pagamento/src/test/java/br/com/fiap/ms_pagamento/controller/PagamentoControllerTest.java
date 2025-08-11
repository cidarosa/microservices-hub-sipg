package br.com.fiap.ms_pagamento.controller;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.service.PagamentoService;
import br.com.fiap.ms_pagamento.service.exceptions.ResourceNotFoundException;
import br.com.fiap.ms_pagamento.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc; // para chamar o endpoint
    // controller tem dependência do service
    // dependência mockada
    @MockitoBean
    private PagamentoService service;
    private PagamentoDTO dto;
    private Long existingId;
    private Long nonExistisId;

    //converter para JSON o objeto Java e enviar na requisição
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {

        existingId = 1L;
        nonExistisId = 100L;
        //criando um PagamentoDTO
        dto = Factory.createPagamentoDTO();

        // Listando PagamentoDTO
        List<PagamentoDTO> list = List.of(dto);

        //simulando o comportamento do service - getAll
        Mockito.when(service.getAll()).thenReturn(list);

        //simulando o comportamento do service - getById
        // Id existe
        Mockito.when(service.getById(existingId)).thenReturn(dto);
        // Id não existe - lança exception
        Mockito.when(service.getById(nonExistisId)).thenThrow(ResourceNotFoundException.class);

        // simulando o comportamento do service - createPagamento
        // any() simula o comportamento de qualquer objeto
        Mockito.when(service.createPagamento(any())).thenReturn(dto);

        // simulando o comportamento do service - updatePagamento
        // any() simula o comportamento de qualquer objeto
        // quando utilizamos any(), não podemo usar obejtos simples,
        // então precisamos do eq()
        Mockito.when(service.updatePagamento(eq(existingId), any())).thenReturn(dto);
        Mockito.when(service.updatePagamento(eq(nonExistisId), any())).thenThrow(ResourceNotFoundException.class);

        // para quando o metodo é void
        // simulando o comportamento do service - deletePagamento
        Mockito.doNothing().when(service).deletePagamento(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).deletePagamento(nonExistisId);
    }

    @Test
    public void getAllShouldReturnListPagamentoDTO() throws Exception {

        //chmando requisição com o metodo get em /pagamentos
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void getByIdShouldReturnPagamentoDTOWhenIdExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        // Assertions
        result.andExpect(status().isOk());
        // verifica se tem os campos em result
        // $ - acessar o objeto result
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void getByIdShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", nonExistisId)
                .accept(MediaType.APPLICATION_JSON));
        // Assertions
        result.andExpect(status().isNotFound());
    }

    @Test
    public void createPagamentoShouldReturnPagamentoDTOCreated() throws Exception {
        PagamentoDTO newPagamentoDTO = Factory.createNewPagamentoDTO();
        // Bean objectMapper usado para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(newPagamentoDTO);
        // POST - tem corpo na requisição - JSON
        mockMvc.perform(post("/pagamentos")
                        .content(jsonRequestBody) //RequestBody
                        .contentType(MediaType.APPLICATION_JSON) // request
                        .accept(MediaType.APPLICATION_JSON))    // response
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.pedidoId").exists())
                .andExpect(jsonPath("$.formaDePagamentoId").exists());
    }

    @Test
    public void updatePagamentoShouldReturnPagamentoDTOWhenIdExist() throws Exception {
        // Bean objectMapper usado para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(dto);
        // PUT - tem corpo na requisição - JSON
        // é preciso passar o corpo da requisição
        mockMvc.perform(put("/pagamentos/{id}", existingId)
                        .content(jsonRequestBody) //RequestBody
                        .contentType(MediaType.APPLICATION_JSON) // request
                        .accept(MediaType.APPLICATION_JSON))    // response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.pedidoId").exists())
                .andExpect(jsonPath("$.formaDePagamentoId").exists());
    }

    @Test
    public void updatePagamentoShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        // Bean objectMapper usado para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(dto);
        // PUT - tem corpo na requisição - JSON
        // é preciso passar o corpo da requisição
        mockMvc.perform(put("/pagamentos/{id}", nonExistisId)
                        .content(jsonRequestBody) //RequestBody
                        .contentType(MediaType.APPLICATION_JSON) // request
                        .accept(MediaType.APPLICATION_JSON))    // response
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePagamentoShouldReturnPagamentoDTOWhenIdExist() throws Exception {

        mockMvc.perform(delete("/pagamentos/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePagamentoShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        mockMvc.perform(delete("/pagamentos/{id}", nonExistisId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
