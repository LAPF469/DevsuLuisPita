package com.devsu.luis.pita.accounting.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.luis.pita.accounting.models.CuentaModel;
import com.devsu.luis.pita.accounting.repositories.CuentaRepository;

@Service
public class CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;

    public List<CuentaModel> findAll() {
        return cuentaRepository.findAll();
    }

    public Optional<CuentaModel> findById(Long id) {
        return cuentaRepository.findById(id);
    }

    public List<CuentaModel> findByClienteId(long clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    public CuentaModel save(CuentaModel cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public void delete(Long id) {
        cuentaRepository.deleteById(id);
    }
}
