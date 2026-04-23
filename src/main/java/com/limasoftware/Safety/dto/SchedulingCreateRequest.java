package com.limasoftware.Safety.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "Requisição para criação de um novo agendamento")
public record SchedulingCreateRequest(
        @Schema(description = "Nome do agendamento", example = "Consulta de Rotina", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @Schema(description = "Descrição detalhada do agendamento", example = "Consulta médica de rotina para check-up anual", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        String description,

        @Schema(description = "Data e hora de início do agendamento", example = "2026-05-01T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A data de início é obrigatória")
        @Future(message = "A data de início deve ser no futuro")
        LocalDateTime startDate,

        @Schema(description = "Data e hora de término do agendamento", example = "2026-05-01T11:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A data de término é obrigatória")
        @Future(message = "A data de término deve ser no futuro")
        LocalDateTime endDate,

        @Schema(description = "Nome ou identificador do cliente", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID do cliente é obrigatório")
        @NotBlank @Size(max = 80) String client
) {
}