package com.devsu.luis.pita.accounting.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.luis.pita.accounting.models.CuentaModel;
import com.devsu.luis.pita.accounting.models.MovimientoModel;

@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoModel, Long> {
    List<MovimientoModel> findByCuentaAndFechaBetween(CuentaModel cuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
