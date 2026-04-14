package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Orden;
import com.example.demo_basic.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AsignacionFechaEntrega {

    @Autowired
    private OrdenRepository ordenRepository;

    @Transactional
    public boolean procesar(Orden orden) {
        if (orden.getFechaRecibido() == null) {
            orden.setFechaRecibido(LocalDateTime.now());
        }

        int cantidadPrendas = orden.getPrendas().size();

        if (cantidadPrendas < 5) {
            orden.setFechaEntregaEstimada(orden.getFechaRecibido().plusHours(24));
        } else {
            orden.setFechaEntregaEstimada(orden.getFechaRecibido().plusHours(48));
        }

        return true;
    }
}
