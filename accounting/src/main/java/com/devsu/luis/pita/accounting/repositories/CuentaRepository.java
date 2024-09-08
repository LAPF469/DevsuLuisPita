package com.devsu.luis.pita.accounting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.luis.pita.accounting.models.CuentaModel;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaModel, Long> {
    List<CuentaModel> findByClienteId(long clienteId);
}
