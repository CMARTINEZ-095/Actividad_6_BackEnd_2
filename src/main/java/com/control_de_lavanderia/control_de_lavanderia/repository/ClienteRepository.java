package com.control_de_lavanderia.control_de_lavanderia.repository;

import com.control_de_lavanderia.control_de_lavanderia.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
