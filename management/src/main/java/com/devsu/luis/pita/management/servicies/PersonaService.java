package com.devsu.luis.pita.management.servicies;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.luis.pita.management.models.PersonaModel;
import com.devsu.luis.pita.management.repositories.PersonaRepository;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public PersonaModel save(PersonaModel persona) {
        return personaRepository.save(persona);
    }

    public Optional<PersonaModel> findById(Long id) {
        return personaRepository.findById(id);
    }

    public void deleteById(Long id) {
        personaRepository.deleteById(id);
    }
}
