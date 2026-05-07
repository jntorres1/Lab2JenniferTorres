package com.laboratorio.streams.controller;

import com.laboratorio.streams.dto.TareaRequestDTO;
import com.laboratorio.streams.dto.TareaResponseDTO;
import com.laboratorio.streams.model.EstadoTarea;
import com.laboratorio.streams.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) EstadoTarea estado,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (estado != null) {
            List<TareaResponseDTO> filtradas = tareaService.filtrarPorEstado(estado);
            return ResponseEntity.ok(filtradas);
        }

        if (page != null && size != null) {
            Page<TareaResponseDTO> paginado = tareaService.listarPaginado(PageRequest.of(page, size));
            return ResponseEntity.ok(paginado);
        }

        return ResponseEntity.ok(tareaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody TareaRequestDTO dto) {
        return ResponseEntity.ok(tareaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Long>> resumen() {
        return ResponseEntity.ok(tareaService.resumen());
    }
}