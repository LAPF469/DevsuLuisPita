package com.devsu.luis.pita.accounting.servicies;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.devsu.luis.pita.accounting.DTOs.ClienteDTO;
import com.devsu.luis.pita.accounting.DTOs.EstadoCuentaDTO;
import com.devsu.luis.pita.accounting.models.CuentaModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReporteService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteService.class);

    private final WebClient webClient;
    private final CuentaService cuentaService;
    private final MovimientoService movimientoService;

    @Autowired
    public ReporteService(WebClient webClient, CuentaService cuentaService, MovimientoService movimientoService) {
        this.webClient = webClient;
        this.cuentaService = cuentaService;
        this.movimientoService = movimientoService;
    }

    public Mono<List<EstadoCuentaDTO>> generarReporte(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId) {
        logger.info("/// Generando reporte para clienteId: {}", clienteId);
        Mono<ClienteDTO> clienteMono = webClient
                .get()
                .uri("clientes/" + clienteId)
                .retrieve()
                .bodyToMono(ClienteDTO.class)
                .doOnNext(cliente -> logger.info("/// Cliente obtenido: {}", cliente));

        Mono<List<CuentaModel>> cuentasMono = Mono.fromCallable(() -> {
            List<CuentaModel> cuentas = cuentaService.findByClienteId(clienteId);
            logger.info("/// Cuentas obtenidas para clienteId {}: {}", clienteId, cuentas.size());
            return cuentas;
        });

        return Mono.zip(clienteMono, cuentasMono)
                .flatMap(tuple -> {
                    ClienteDTO cliente = tuple.getT1();
                    List<CuentaModel> cuentas = tuple.getT2();

                    return Flux.fromIterable(cuentas)
                            .flatMap(cuenta -> {
                                logger.info("/// Procesando cuenta: {}", cuenta.getNumeroCuenta());

                                LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
                                LocalDateTime fechaFinDateTime = fechaFin.atTime(23, 59, 59);

                                return Mono.fromCallable(() -> movimientoService.findByCuentaAndFechaBetween(cuenta, fechaInicioDateTime, fechaFinDateTime))
                                        .flatMapMany(movimientos -> Flux.fromIterable(movimientos)
                                        .map(movimiento -> {
                                            EstadoCuentaDTO dto = new EstadoCuentaDTO();
                                            dto.setFecha(movimiento.getFecha());
                                            dto.setCliente(cliente.getPersona().getNombre());
                                            dto.setNumeroCuenta(cuenta.getNumeroCuenta());
                                            dto.setTipo(cuenta.getTipoCuenta());
                                            dto.setSaldoInicial(cuenta.getSaldoInicial());
                                            dto.setEstado(cuenta.getEstado());
                                            dto.setMovimiento(movimiento.getValor());
                                            dto.setSaldoDisponible(movimiento.getSaldo());
                                            logger.info("/// Movimiento procesado: {}", movimiento);

                                            return dto;
                                        })
                                        );
                            })
                            .collectList();
                });
    }
}
