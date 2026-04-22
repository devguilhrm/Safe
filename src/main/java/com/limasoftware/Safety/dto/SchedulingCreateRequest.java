package com.limasoftware.Safety.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record SchedulingCreateRequest(
        @NotBlank(message = "O nome é obrigatório")
        String name,
        String description,
        @NotNull(message = "A data de início é obrigatória")
        @Future(message = "A data de início deve ser no futuro")
        LocalDateTime startDate,

        @NotNull(message = "A data de término é obrigatória")
        @Future(message = "A data de término deve ser no futuro")
        LocalDateTime endDate,

        @NotNull(message = "O ID do cliente é obrigatório")
        UUID clientId // Recebemos o UUID do cliente que já existe no banco
) {
}