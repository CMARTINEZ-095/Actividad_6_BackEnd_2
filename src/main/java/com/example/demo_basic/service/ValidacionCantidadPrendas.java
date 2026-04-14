package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Orden;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ValidacionCantidadPrendas {

    private static final int MAX_PRENDAS = 20;

    @Transactional
    public void procesar(Orden orden) {
        if (orden.getPrendas() == null || orden.getPrendas().isEmpty()) {
            throw new IllegalArgumentException(
                "La orden debe tener al menos una prenda."
            );
        }
        if (orden.getPrendas().size() > MAX_PRENDAS) {
            throw new IllegalArgumentException(
                "La orden supera el máximo permitido de " + MAX_PRENDAS + " prendas. " +
                "Cantidad enviada: " + orden.getPrendas().size() + "."
            );
        }
    }
}
