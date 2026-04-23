package com.limasoftware.Safety.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Papel do usuário no sistema")
public enum UserRole {
    @Schema(description = "Administrador com acesso total")
    ADMIN,
    @Schema(description = "Cliente/Paciente")
    CLIENT
}
