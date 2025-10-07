package com.example.demo.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todo Controller", description = "Operaciones CRUD para gestionar tareas (todos)")
public class TodoController {

    @Autowired
    private TodoRepository repo;

    @GetMapping
    @Operation(
        summary = "Obtener todas las tareas",
        description = "Retorna una lista con todas las tareas existentes en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tareas obtenida exitosamente",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = Todo.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<Todo> all() {
        return repo.findAll();
    }

    @PostMapping
    @Operation(
        summary = "Crear una nueva tarea",
        description = "Crea una nueva tarea en el sistema. El ID se genera automáticamente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea creada exitosamente",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = Todo.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Todo create(@Valid @RequestBody Todo t) {
        t.setId(null);
        return repo.save(t);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una tarea por ID",
        description = "Retorna una tarea específica basada en su ID único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea encontrada exitosamente",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = Todo.class))),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Todo> one(
        @Parameter(description = "ID único de la tarea", required = true, example = "1")
        @PathVariable Long id) {
        
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar una tarea existente",
        description = "Actualiza los datos de una tarea existente basada en su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = Todo.class))),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Todo> update(
        @Parameter(description = "ID único de la tarea a actualizar", required = true, example = "1")
        @PathVariable Long id,
        
        @Parameter(description = "Datos actualizados de la tarea", required = true)
        @Valid @RequestBody Todo updated) {
        
        return repo.findById(id).map(t -> {
            t.setTitle(updated.getTitle());
            t.setCompleted(updated.isCompleted());
            return ResponseEntity.ok(repo.save(t));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una tarea",
        description = "Elimina una tarea existente basada en su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID único de la tarea a eliminar", required = true, example = "1")
        @PathVariable Long id) {
        
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}