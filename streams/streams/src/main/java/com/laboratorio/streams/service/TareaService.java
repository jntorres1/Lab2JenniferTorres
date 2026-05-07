package com.laboratorio.streams.service;

import com.laboratorio.streams.dto.TareaRequestDTO;
import com.laboratorio.streams.dto.TareaResponseDTO;
import com.laboratorio.streams.model.EstadoTarea;
import com.laboratorio.streams.model.Tarea;
import com.laboratorio.streams.repository.TareaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.laboratorio.streams.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<TareaResponseDTO> listarTodas() {
        return tareaRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Page<TareaResponseDTO> listarPaginado(Pageable pageable) {
        return tareaRepository.findAll(pageable).map(this::toResponse);
    }

    public TareaResponseDTO obtenerPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        return toResponse(tarea);
    }

    public TareaResponseDTO crear(TareaRequestDTO dto) {
        Tarea tarea = toEntity(dto);
        return toResponse(tareaRepository.save(tarea));
    }

    public TareaResponseDTO actualizar(Long id, TareaRequestDTO dto) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException ("Tarea no encontrada con id: " + id));
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setEstado(dto.getEstado());
        tarea.setPrioridad(dto.getPrioridad());
        return toResponse(tareaRepository.save(tarea));
    }

    public void eliminar(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new ResourceNotFoundException ("Tarea no encontrada con id: " + id);
        }
        tareaRepository.deleteById(id);
    }

    public List<TareaResponseDTO> filtrarPorEstado(EstadoTarea estado) {
        return tareaRepository.findByEstado(estado)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Map<String, Long> resumen() {
        return tareaRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.getEstado().name(), Collectors.counting()
                ));
    }

    private TareaResponseDTO toResponse(Tarea tarea) {
        TareaResponseDTO dto = new TareaResponseDTO();
        dto.setId(tarea.getId());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        return dto;
    }

    private Tarea toEntity(TareaRequestDTO dto) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setEstado(dto.getEstado());
        tarea.setPrioridad(dto.getPrioridad());
        return tarea;
    }
}