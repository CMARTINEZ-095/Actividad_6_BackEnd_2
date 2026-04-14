package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Orden;
import com.example.demo_basic.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SumaPuntosLealtad {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public boolean procesar(Orden orden) {
        if (orden.getCliente() == null || orden.getCliente().getPuntosLealtad() == null) {
            return true;
        }

        if (orden.getTotal() > 0) {
            int puntosGanados = (int) (orden.getTotal() / 10000);
            int nuevosPuntos = orden.getCliente().getPuntosLealtad() + puntosGanados;
            orden.getCliente().setPuntosLealtad(nuevosPuntos);
            clienteRepository.save(orden.getCliente());
        }

        return true;
    }
}
