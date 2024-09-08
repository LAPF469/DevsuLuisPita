package com.devsu.luis.pita.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsu.luis.pita.management.models.PersonaModel;

public interface PersonaRepository extends JpaRepository<PersonaModel, Long> {
}
