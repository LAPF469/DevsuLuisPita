package com.devsu.luis.pita.accounting.servicies;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.luis.pita.accounting.exceptions.ResourceNotFoundException;
import com.devsu.luis.pita.accounting.exceptions.SaldoInsuficienteException;
import com.devsu.luis.pita.accounting.models.CuentaModel;
import com.devsu.luis.pita.accounting.models.MovimientoModel;
import com.devsu.luis.pita.accounting.repositories.MovimientoRepository;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaService cuentaService;

    public List<MovimientoModel> findAll() {
        return movimientoRepository.findAll();
    }

    public Optional<MovimientoModel> findById(Long id) {
        return movimientoRepository.findById(id);
    }

    public MovimientoModel save(MovimientoModel movimiento) {
        CuentaModel cuenta = cuentaService.findById(movimiento.getCuenta().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                "Cuenta con id " + movimiento.getCuenta().getId() + " no fue encontrada."));

        if (cuenta.getSaldoInicial() + movimiento.getValor() < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible.");
        }
        cuenta.setSaldoInicial(cuenta.getSaldoInicial() + movimiento.getValor());
        cuentaService.save(cuenta);

        movimiento.setSaldo(cuenta.getSaldoInicial());

        return movimientoRepository.save(movimiento);
    }

    public void delete(Long id) {
        movimientoRepository.deleteById(id);
    }

    public List<MovimientoModel> findByCuentaAndFechaBetween(CuentaModel cuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByCuentaAndFechaBetween(cuenta, fechaInicio, fechaFin);
    }

}
