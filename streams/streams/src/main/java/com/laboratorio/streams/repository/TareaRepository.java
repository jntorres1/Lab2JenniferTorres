package com.laboratorio.streams.repository;

import com.laboratorio.streams.model.EstadoTarea;
import com.laboratorio.streams.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    // Consulta por convención
    List<Tarea> findByEstado(EstadoTarea estado);

    // Consulta personalizada
    @Query("SELECT t FROM Tarea t WHERE t.prioridad = :prioridad")
    List<Tarea> findByPrioridad(int prioridad);
}