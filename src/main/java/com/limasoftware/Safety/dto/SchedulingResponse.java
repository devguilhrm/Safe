package com.limasoftware.Safety.dto;

import com.limasoftware.Safety.enums.SchedulingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta com os dados de um agendamento")
public record SchedulingResponse(
        @Schema(description = "ID único do agendamento", example = "1")
        Long id,

        @Schema(description = "Nome do agendamento", example = "Consulta de Rotina")
        String name,

        @Schema(description = "Descrição detalhada do agendamento", example = "Consulta médica de rotina para check-up anual")
        String description,

        @Schema(description = "Data e hora de início do agendamento", example = "2026-05-01T10:00:00")
        LocalDateTime startDate,

        @Schema(description = "Data e hora de término do agendamento", example = "2026-05-01T11:00:00")
        LocalDateTime endDate,

        @Schema(description = "Nome ou identificador do cliente", example = "João Silva")
        String client,

        @Schema(description = "Status atual do agendamento", example = "SCHEDULED", allowableValues = {"SCHEDULED", "CANCELED", "COMPLETED"})
        SchedulingStatus status,

        @Schema(description = "Data e hora de criação do agendamento", example = "2026-04-20T08:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora da última atualização do agendamento", example = "2026-04-20T08:00:00")
        LocalDateTime attAt
) {
}