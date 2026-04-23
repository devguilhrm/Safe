package com.limasoftware.Safety.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para atualização de agendamentos existentes.
 * Campos são opcionais na lógica de negócio, mas validados se enviados.
 */
@Schema(description = "Requisição para atualização de um agendamento existente")
public record SchedulingUpdateRequest(

        @Schema(description = "Nome do agendamento", example = "Consulta de Retorno", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 120, message = "O nome não pode exceder 120 caracteres")
        String name,

        @Schema(description = "Descrição detalhada do agendamento", example = "Retorno médico para avaliação de exames", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 4000)
        String description,

        @Schema(description = "Nova data e hora de início do agendamento", example = "2026-05-15T14:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDateTime startDate,

        @Schema(description = "Nova data e hora de término do agendamento", example = "2026-05-15T15:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDateTime endDate,

        @Schema(description = "Nome ou identificador do cliente", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String client
) {
}