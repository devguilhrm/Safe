package com.limasoftware.Safety.controller;

import com.limasoftware.Safety.dto.SchedulingCreateRequest;
import com.limasoftware.Safety.dto.SchedulingResponse;
import com.limasoftware.Safety.dto.SchedulingUpdateRequest;
import com.limasoftware.Safety.service.SchedulingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Agendamentos")
@Tag(name = "Agendamentos", description = "API para gerenciamento de agendamentos médicos")
public class SchedulingController {


    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    private final SchedulingService schedulingService;


    @PostMapping("/criar")
    @Operation(summary = "Criar novo agendamento", description = "Cria um novo agendamento médico. Valida conflitos de horário e intervalo de datas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = SchedulingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida - intervalo de datas inválido ou conflito de horário",
                    content = @Content)
    })
    public SchedulingResponse create(@Valid @RequestBody SchedulingCreateRequest request) {
        return schedulingService.create(request);
    }


    @PostMapping("/{id}/editar")
    @Operation(summary = "Editar agendamento existente", description = "Atualiza os dados de um agendamento existente. Valida conflitos de horário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = SchedulingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida - intervalo de datas inválido ou conflito de horário",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content)
    })
    public SchedulingResponse update(
            @Parameter(description = "ID do agendamento a ser editado", example = "1") @PathVariable Long id,
            @Valid @RequestBody SchedulingUpdateRequest request) {
        return schedulingService.update(id, request);
    }


    @PostMapping("/{id}/excluir")
    @Operation(summary = "Excluir/Cancelar agendamento", description = "Cancela um agendamento existente, alterando seu status para CANCELED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso",
                    content = @Content(schema = @Schema(implementation = SchedulingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content)
    })
    public SchedulingResponse delete(
            @Parameter(description = "ID do agendamento a ser excluído", example = "1") @PathVariable Long id) {
        return schedulingService.delete(id);
    }


    @PutMapping("/{id}/concluir")
    @Operation(summary = "Concluir agendamento", description = "Marca um agendamento como concluído, alterando seu status para COMPLETED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento concluído com sucesso",
                    content = @Content(schema = @Schema(implementation = SchedulingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content)
    })
    public SchedulingResponse completed(
            @Parameter(description = "ID do agendamento a ser concluído", example = "1") @PathVariable Long id) {
        return schedulingService.finish(id);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os dados de um agendamento específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado",
                    content = @Content(schema = @Schema(implementation = SchedulingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content)
    })
    public SchedulingResponse searchById(
            @Parameter(description = "ID do agendamento a ser buscado", example = "1") @PathVariable Long id) {
        return schedulingService.searchById(id);
    }


    @GetMapping("/ativos")
    @Operation(summary = "Listar todos os agendamentos", description = "Retorna uma lista com todos os agendamentos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso",
            content = @Content(schema = @Schema(implementation = SchedulingResponse.class)))
    public ResponseEntity<List<SchedulingResponse>> findAll() {
        List<SchedulingResponse> response = schedulingService.findAll();
        return ResponseEntity.ok(response);
    }
}

