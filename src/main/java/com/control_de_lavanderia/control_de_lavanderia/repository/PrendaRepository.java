package com.control_de_lavanderia.control_de_lavanderia.repository;

import com.control_de_lavanderia.control_de_lavanderia.model.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrendaRepository extends JpaRepository<Prenda, Long> {
}
