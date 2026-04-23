package com.limasoftware.Safety.service;

import com.limasoftware.Safety.dto.SchedulingCreateRequest;
import com.limasoftware.Safety.dto.SchedulingResponse;
import com.limasoftware.Safety.dto.SchedulingUpdateRequest;
import com.limasoftware.Safety.enums.SchedulingStatus;
import com.limasoftware.Safety.mapper.SchedulingMapper;
import com.limasoftware.Safety.model.Scheduling;
import com.limasoftware.Safety.repository.SchedulingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private SchedulingRepository schedulingRepository;

    @InjectMocks
    private SchedulingService schedulingService;

    private Scheduling scheduling;
    private SchedulingCreateRequest createRequest;
    private SchedulingUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 11, 0);

        scheduling = Scheduling.builder()
                .id(1L)
                .name("Consulta de Rotina")
                .description("Check-up anual")
                .startDate(start)
                .endDate(end)
                .client("João Silva")
                .status(SchedulingStatus.SCHEDULED)
                .createdAt(LocalDateTime.now())
                .attAt(LocalDateTime.now())
                .build();

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
    void shouldCreateSchedulingSuccessfully() {
        when(schedulingRepository.existsConflict(anyString(), any(), any(), any())).thenReturn(false);
        when(schedulingRepository.save(any(Scheduling.class))).thenReturn(scheduling);

        SchedulingResponse response = schedulingService.create(createRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Consulta de Rotina", response.name());
        assertEquals(SchedulingStatus.SCHEDULED, response.status());
        verify(schedulingRepository).save(any(Scheduling.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver conflito de horário")
    void shouldThrowExceptionWhenConflictExists() {
        when(schedulingRepository.existsConflict(anyString(), any(), any(), any())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> schedulingService.create(createRequest)
        );

        assertEquals("Conflito na agenda: Horário desejado em conflito com outro agendamento", exception.getMessage());
        verify(schedulingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando intervalo de datas for inválido")
    void shouldThrowExceptionWhenInvalidInterval() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 14, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 10, 0); // end before start

        SchedulingCreateRequest invalidRequest = new SchedulingCreateRequest(
                "Consulta",
                "Descrição",
                start,
                end,
                "João Silva"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> schedulingService.create(invalidRequest)
        );

        assertEquals("Intervalo Inválido! Data de início deve ser anterior a data final!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar um agendamento com sucesso")
    void shouldUpdateSchedulingSuccessfully() {
        when(schedulingRepository.findById(1L)).thenReturn(Optional.of(scheduling));
        when(schedulingRepository.existsConflict(anyString(), any(), any(), any())).thenReturn(false);
        when(schedulingRepository.save(any(Scheduling.class))).thenReturn(scheduling);

        SchedulingResponse response = schedulingService.update(1L, updateRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(schedulingRepository).findById(1L);
        verify(schedulingRepository).save(scheduling);
    }

    @Test
    @DisplayName("Deve lançar exceção quando agendamento não for encontrado para update")
    void shouldThrowExceptionWhenSchedulingNotFoundOnUpdate() {
        when(schedulingRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> schedulingService.update(999L, updateRequest)
        );

        assertEquals("Agendamento não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve cancelar um agendamento com sucesso")
    void shouldDeleteSchedulingSuccessfully() {
        when(schedulingRepository.findById(1L)).thenReturn(Optional.of(scheduling));
        when(schedulingRepository.save(any(Scheduling.class))).thenReturn(scheduling);

        SchedulingResponse response = schedulingService.delete(1L);

        assertNotNull(response);
        assertEquals(SchedulingStatus.CANCELED, scheduling.getStatus());
        verify(schedulingRepository).save(scheduling);
    }

    @Test
    @DisplayName("Deve lançar exceção quando agendamento não for encontrado para delete")
    void shouldThrowExceptionWhenSchedulingNotFoundOnDelete() {
        when(schedulingRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> schedulingService.delete(999L)
        );

        assertEquals("Agendamento não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve concluir um agendamento com sucesso")
    void shouldFinishSchedulingSuccessfully() {
        when(schedulingRepository.findById(1L)).thenReturn(Optional.of(scheduling));
        when(schedulingRepository.save(any(Scheduling.class))).thenReturn(scheduling);

        SchedulingResponse response = schedulingService.finish(1L);

        assertNotNull(response);
        assertEquals(SchedulingStatus.COMPLETED, scheduling.getStatus());
        verify(schedulingRepository).save(scheduling);
    }

    @Test
    @DisplayName("Deve lançar exceção quando agendamento não for encontrado para finish")
    void shouldThrowExceptionWhenSchedulingNotFoundOnFinish() {
        when(schedulingRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> schedulingService.finish(999L)
        );

        assertEquals("Agendamento não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar um agendamento por ID com sucesso")
    void shouldSearchSchedulingByIdSuccessfully() {
        when(schedulingRepository.findById(1L)).thenReturn(Optional.of(scheduling));

        SchedulingResponse response = schedulingService.searchById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Consulta de Rotina", response.name());
        verify(schedulingRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando agendamento não for encontrado na busca")
    void shouldThrowExceptionWhenSchedulingNotFoundOnSearch() {
        when(schedulingRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> schedulingService.searchById(999L)
        );

        assertEquals("Agendamento não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar lista de todos os agendamentos")
    void shouldReturnAllSchedulings() {
        Scheduling scheduling2 = Scheduling.builder()
                .id(2L)
                .name("Exame de Sangue")
                .description("Coleta laboratorial")
                .startDate(LocalDateTime.of(2026, 5, 2, 8, 0))
                .endDate(LocalDateTime.of(2026, 5, 2, 9, 0))
                .client("Maria Santos")
                .status(SchedulingStatus.SCHEDULED)
                .build();

        when(schedulingRepository.findAll()).thenReturn(Arrays.asList(scheduling, scheduling2));

        List<SchedulingResponse> response = schedulingService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(schedulingRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver agendamentos")
    void shouldReturnEmptyListWhenNoSchedulings() {
        when(schedulingRepository.findAll()).thenReturn(Arrays.asList());

        List<SchedulingResponse> response = schedulingService.findAll();

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
}
