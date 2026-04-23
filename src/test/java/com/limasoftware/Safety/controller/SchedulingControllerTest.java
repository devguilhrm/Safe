package com.limasoftware.Safety.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limasoftware.Safety.dto.SchedulingCreateRequest;
import com.limasoftware.Safety.dto.SchedulingResponse;
import com.limasoftware.Safety.dto.SchedulingUpdateRequest;
import com.limasoftware.Safety.enums.SchedulingStatus;
import com.limasoftware.Safety.service.SchedulingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SchedulingController.class)
class SchedulingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchedulingService schedulingService;

    @Autowired
    private ObjectMapper objectMapper;

    private SchedulingResponse schedulingResponse;
    private SchedulingCreateRequest createRequest;
    private SchedulingUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 11, 0);

        schedulingResponse = new SchedulingResponse(
                1L,
                "Consulta de Rotina",
                "Check-up anual",
                start,
                end,
                "João Silva",
                SchedulingStatus.SCHEDULED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        createRequest = new SchedulingCreateRequest(
                "Consulta de Rotina",
                "Check-up anual",
                start,
                end,
                "João Silva"
        );

        updateRequest = new SchedulingUpdateRequest(
                "Consulta de Retorno",
                "Retorno médico",
                start,
                end,
                "João Silva"
        );
    }

    @Test
    @DisplayName("Deve criar um agendamento com sucesso")
    void shouldCreateSchedulingSuccessfully() throws Exception {
        when(schedulingService.create(any(SchedulingCreateRequest.class))).thenReturn(schedulingResponse);

        mockMvc.perform(post("/api/Agendamentos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Consulta de Rotina"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

        verify(schedulingService).create(any(SchedulingCreateRequest.class));
    }

    @Test
    @DisplayName("Deve retornar 400 quando criação tiver dados inválidos")
    void shouldReturn400WhenCreateWithInvalidData() throws Exception {
        SchedulingCreateRequest invalidRequest = new SchedulingCreateRequest(
                "", // nome vazio
                "Descrição",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "João Silva"
        );

        mockMvc.perform(post("/api/Agendamentos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(schedulingService, never()).create(any());
    }

    @Test
    @DisplayName("Deve atualizar um agendamento com sucesso")
    void shouldUpdateSchedulingSuccessfully() throws Exception {
        when(schedulingService.update(eq(1L), any(SchedulingUpdateRequest.class))).thenReturn(schedulingResponse);

        mockMvc.perform(post("/api/Agendamentos/{id}/editar", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Consulta de Rotina"));

        verify(schedulingService).update(eq(1L), any(SchedulingUpdateRequest.class));
    }

    @Test
    @DisplayName("Deve retornar 404 quando agendamento não for encontrado para update")
    void shouldReturn404WhenSchedulingNotFoundOnUpdate() throws Exception {
        when(schedulingService.update(eq(999L), any(SchedulingUpdateRequest.class)))
                .thenThrow(new EntityNotFoundException("Agendamento não encontrado."));

        mockMvc.perform(post("/api/Agendamentos/{id}/editar", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve cancelar um agendamento com sucesso")
    void shouldDeleteSchedulingSuccessfully() throws Exception {
        when(schedulingService.delete(1L)).thenReturn(schedulingResponse);

        mockMvc.perform(post("/api/Agendamentos/{id}/excluir", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

        verify(schedulingService).delete(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 quando agendamento não for encontrado para delete")
    void shouldReturn404WhenSchedulingNotFoundOnDelete() throws Exception {
        when(schedulingService.delete(999L))
                .thenThrow(new EntityNotFoundException("Agendamento não encontrado."));

        mockMvc.perform(post("/api/Agendamentos/{id}/excluir", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve concluir um agendamento com sucesso")
    void shouldFinishSchedulingSuccessfully() throws Exception {
        when(schedulingService.finish(1L)).thenReturn(schedulingResponse);

        mockMvc.perform(put("/api/Agendamentos/{id}/concluir", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(schedulingService).finish(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 quando agendamento não for encontrado para finish")
    void shouldReturn404WhenSchedulingNotFoundOnFinish() throws Exception {
        when(schedulingService.finish(999L))
                .thenThrow(new EntityNotFoundException("Agendamento não encontrado."));

        mockMvc.perform(put("/api/Agendamentos/{id}/concluir", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve buscar um agendamento por ID com sucesso")
    void shouldSearchSchedulingByIdSuccessfully() throws Exception {
        when(schedulingService.searchById(1L)).thenReturn(schedulingResponse);

        mockMvc.perform(get("/api/Agendamentos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Consulta de Rotina"))
                .andExpect(jsonPath("$.client").value("João Silva"));

        verify(schedulingService).searchById(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 quando agendamento não for encontrado na busca")
    void shouldReturn404WhenSchedulingNotFoundOnSearch() throws Exception {
        when(schedulingService.searchById(999L))
                .thenThrow(new EntityNotFoundException("Agendamento não encontrado."));

        mockMvc.perform(get("/api/Agendamentos/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar todos os agendamentos com sucesso")
    void shouldListAllSchedulingsSuccessfully() throws Exception {
        SchedulingResponse response2 = new SchedulingResponse(
                2L,
                "Exame de Sangue",
                "Coleta laboratorial",
                LocalDateTime.of(2026, 5, 2, 8, 0),
                LocalDateTime.of(2026, 5, 2, 9, 0),
                "Maria Santos",
                SchedulingStatus.SCHEDULED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<SchedulingResponse> responses = Arrays.asList(schedulingResponse, response2);
        when(schedulingService.findAll()).thenReturn(responses);

        mockMvc.perform(get("/api/Agendamentos/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Consulta de Rotina"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Exame de Sangue"));

        verify(schedulingService).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver agendamentos")
    void shouldReturnEmptyListWhenNoSchedulings() throws Exception {
        when(schedulingService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/Agendamentos/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
