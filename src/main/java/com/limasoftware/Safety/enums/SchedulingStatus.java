package com.limasoftware.Safety.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do agendamento")
public enum SchedulingStatus {
    @Schema(description = "Agendamento programado")
    SCHEDULED,
    @Schema(description = "Agendamento cancelado")
    CANCELED,
    @Schema(description = "Agendamento concluído")
    COMPLETED
}