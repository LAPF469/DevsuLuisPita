package com.devsu.luis.pita.accounting.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.luis.pita.accounting.exceptions.ResourceNotFoundException;
import com.devsu.luis.pita.accounting.models.ErrorResponse;
import com.devsu.luis.pita.accounting.models.MovimientoModel;
import com.devsu.luis.pita.accounting.servicies.MovimientoService;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public List<MovimientoModel> getAllMovimientos() {
        return movimientoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<MovimientoModel> getMovimientoById(@PathVariable Long id) {
        return movimientoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<MovimientoModel> createMovimiento(@RequestBody MovimientoModel movimiento) {
        movimiento.setFecha(LocalDateTime.now());

        MovimientoModel savedMovimiento = movimientoService.save(movimiento);
        return ResponseEntity.ok(savedMovimiento);
    }

    @PutMapping("/{id}")
    public MovimientoModel updateMovimiento(@PathVariable Long id, @RequestBody MovimientoModel movimiento) {
        movimiento.setId(id);
        return movimientoService.save(movimiento);
    }

    @DeleteMapping("/{id}")
    public void deleteMovimiento(@PathVariable Long id) {
        movimientoService.delete(id);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
