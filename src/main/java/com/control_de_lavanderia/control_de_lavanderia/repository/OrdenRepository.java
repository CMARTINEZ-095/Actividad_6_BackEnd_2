package com.control_de_lavanderia.control_de_lavanderia.repository;

import com.control_de_lavanderia.control_de_lavanderia.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
