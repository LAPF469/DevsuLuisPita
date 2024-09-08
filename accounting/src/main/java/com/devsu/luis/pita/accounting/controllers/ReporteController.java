package com.devsu.luis.pita.accounting.controllers;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.luis.pita.accounting.DTOs.EstadoCuentaDTO;
import com.devsu.luis.pita.accounting.servicies.ReporteService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/estadoCuenta")
    public Mono<List<EstadoCuentaDTO>> getEstadoCuenta(
            @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam("fechaFin") String fechaFinStr,
            @RequestParam("clienteId") Long clienteId) throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
        LocalDate fechaFin = LocalDate.parse(fechaFinStr, formatter);

        return reporteService.generarReporte(fechaInicio, fechaFin, clienteId);
    }
}
