package com.devsu.luis.pita.accounting.controllers;

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
import com.devsu.luis.pita.accounting.models.CuentaModel;
import com.devsu.luis.pita.accounting.models.ErrorResponse;
import com.devsu.luis.pita.accounting.servicies.CuentaService;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public List<CuentaModel> getAllCuentas() {
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CuentaModel> getCuentaById(@PathVariable Long id) {
        return cuentaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CuentaModel> createCuenta(@RequestBody CuentaModel cuenta) {
        if (cuenta.getClienteId() != null) {
            //Aqui llamar al cliente mediante webhttp
            /*ClienteModel cliente = clienteService.findById(cuenta.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            cuenta.setCliente(cliente);*/
        }
        CuentaModel cuentaCreate = cuentaService.save(cuenta);
        return new ResponseEntity<>(cuentaCreate, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaModel> updateCuenta(@PathVariable Long id, @RequestBody CuentaModel cuenta) {
        CuentaModel existingCuenta = cuentaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con id " + id + " no fue encontrada."));
        existingCuenta.setClienteId(cuenta.getClienteId());
        existingCuenta.setEstado(cuenta.getEstado());
        existingCuenta.setNumeroCuenta(cuenta.getNumeroCuenta());
        existingCuenta.setSaldoInicial(cuenta.getSaldoInicial());
        existingCuenta.setTipoCuenta(cuenta.getTipoCuenta());

        CuentaModel cuentaUpdate = cuentaService.save(existingCuenta);
        return ResponseEntity.ok(cuentaUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteCuenta(@PathVariable Long id) {
        cuentaService.delete(id);
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
