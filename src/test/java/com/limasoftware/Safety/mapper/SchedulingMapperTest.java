package com.limasoftware.Safety.mapper;

import com.limasoftware.Safety.dto.SchedulingCreateRequest;
import com.limasoftware.Safety.dto.SchedulingResponse;
import com.limasoftware.Safety.dto.SchedulingUpdateRequest;
import com.limasoftware.Safety.enums.SchedulingStatus;
import com.limasoftware.Safety.model.Scheduling;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SchedulingMapperTest {

    @Test
    @DisplayName("Deve converter SchedulingCreateRequest para Entity corretamente")
    void shouldConvertCreateRequestToEntityCorrectly() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 11, 0);

        SchedulingCreateRequest request = new SchedulingCreateRequest(
                "Consulta de Rotina",
                "Check-up anual",
                start,
                end,
                "João Silva"
        );

        Scheduling result = SchedulingMapper.toEntity(request);

        assertNotNull(result);
        assertEquals("Consulta de Rotina", result.getName());
        assertEquals("Check-up anual", result.getDescription());
        assertEquals(start, result.getStartDate());
        assertEquals(end, result.getEndDate());
        assertEquals("João Silva", result.getClient());
        assertEquals(SchedulingStatus.SCHEDULED, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getAttAt());
    }

    @Test
    @DisplayName("Deve fazer merge de UpdateRequest na Entity corretamente")
    void shouldMergeUpdateRequestIntoEntityCorrectly() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 11, 0);

        Scheduling entity = Scheduling.builder()
                .id(1L)
                .name("Nome Antigo")
                .description("Descrição Antiga")
                .startDate(LocalDateTime.of(2026, 4, 1, 10, 0))
                .endDate(LocalDateTime.of(2026, 4, 1, 11, 0))
                .client("Cliente Antigo")
                .status(SchedulingStatus.SCHEDULED)
                .build();

        SchedulingUpdateRequest request = new SchedulingUpdateRequest(
                "Nome Atualizado",
                "Descrição Atualizada",
                start,
                end,
                "João Silva"
        );

        SchedulingMapper.merge(entity, request);

        assertEquals("Nome Atualizado", entity.getName());
        assertEquals("Descrição Atualizada", entity.getDescription());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
        assertEquals("Cliente Antigo", entity.getClient()); // client não é atualizado no merge
    }

    @Test
    @DisplayName("Deve fazer merge parcial corretamente")
    void shouldMergePartiallyCorrectly() {
        Scheduling entity = Scheduling.builder()
                .id(1L)
                .name("Nome Original")
                .description("Descrição Original")
                .startDate(LocalDateTime.of(2026, 4, 1, 10, 0))
                .endDate(LocalDateTime.of(2026, 4, 1, 11, 0))
                .client("Cliente")
                .status(SchedulingStatus.SCHEDULED)
                .build();

        SchedulingUpdateRequest request = new SchedulingUpdateRequest(
                null, // não atualiza nome
                "Nova Descrição",
                null, // não atualiza start
                LocalDateTime.of(2026, 5, 1, 12, 0), // atualiza end
                "João Silva"
        );

        SchedulingMapper.merge(entity, request);

        assertEquals("Nome Original", entity.getName()); // não mudou
        assertEquals("Nova Descrição", entity.getDescription());
        assertEquals(LocalDateTime.of(2026, 4, 1, 10, 0), entity.getStartDate()); // não mudou
        assertEquals(LocalDateTime.of(2026, 5, 1, 12, 0), entity.getEndDate()); // mudou
    }

    @Test
    @DisplayName("Deve converter Entity para SchedulingResponse corretamente")
    void shouldConvertEntityToResponseCorrectly() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 11, 0);
        LocalDateTime createdAt = LocalDateTime.of(2026, 4, 20, 8, 0);
        LocalDateTime attAt = LocalDateTime.of(2026, 4, 20, 9, 0);

        Scheduling entity = Scheduling.builder()
                .id(1L)
                .name("Consulta de Rotina")
                .description("Check-up anual")
                .startDate(start)
                .endDate(end)
                .client("João Silva")
                .status(SchedulingStatus.SCHEDULED)
                .createdAt(createdAt)
                .attAt(attAt)
                .build();

        SchedulingResponse result = SchedulingMapper.toResponse(entity);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Consulta de Rotina", result.name());
        assertEquals("Check-up anual", result.description());
        assertEquals(start, result.startDate());
        assertEquals(end, result.endDate());
        assertEquals("João Silva", result.client());
        assertEquals(SchedulingStatus.SCHEDULED, result.status());
        assertEquals(createdAt, result.createdAt());
        assertEquals(attAt, result.attAt());
    }

    @Test
    @DisplayName("Deve converter Entity COMPLETED para Response corretamente")
    void shouldConvertCompletedEntityToResponseCorrectly() {
        Scheduling entity = Scheduling.builder()
                .id(2L)
                .name("Exame Concluído")
                .description("Exame de sangue")
                .startDate(LocalDateTime.of(2026, 5, 1, 8, 0))
                .endDate(LocalDateTime.of(2026, 5, 1, 9, 0))
                .client("Maria Santos")
                .status(SchedulingStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .attAt(LocalDateTime.now())
                .build();

        SchedulingResponse result = SchedulingMapper.toResponse(entity);

        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals(SchedulingStatus.COMPLETED, result.status());
        assertEquals("Maria Santos", result.client());
    }

    @Test
    @DisplayName("Deve converter Entity CANCELED para Response corretamente")
    void shouldConvertCanceledEntityToResponseCorrectly() {
        Scheduling entity = Scheduling.builder()
                .id(3L)
                .name("Consulta Cancelada")
                .description("Não poderá comparecer")
                .startDate(LocalDateTime.of(2026, 5, 1, 14, 0))
                .endDate(LocalDateTime.of(2026, 5, 1, 15, 0))
                .client("Pedro Oliveira")
                .status(SchedulingStatus.CANCELED)
                .createdAt(LocalDateTime.now())
                .attAt(LocalDateTime.now())
                .build();

        SchedulingResponse result = SchedulingMapper.toResponse(entity);

        assertNotNull(result);
        assertEquals(3L, result.id());
        assertEquals(SchedulingStatus.CANCELED, result.status());
    }
}
