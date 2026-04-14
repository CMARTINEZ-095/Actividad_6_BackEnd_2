package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Orden;
import com.example.demo_basic.model.entity.Prenda;
import com.example.demo_basic.model.enums.TipoPrenda;
import com.example.demo_basic.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AplicacionDescuentoLealtad {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public boolean procesar(Orden orden) {
        if (orden.getCliente() == null || orden.getCliente().getPuntosLealtad() == null) {
            return true;
        }

        if (orden.getCliente().getPuntosLealtad() >= 50 && !orden.getPrendas().isEmpty()) {

            double tarifaMasBarata = Double.MAX_VALUE;
            for (Prenda prenda : orden.getPrendas()) {
                double tarifaActual = (prenda.getTipo() == TipoPrenda.CAMISA) ? 15000.0 : 20000.0;
                if (tarifaActual < tarifaMasBarata) {
                    tarifaMasBarata = tarifaActual;
                }
            }

            double nuevoTotal = orden.getTotal() - tarifaMasBarata;
            orden.setTotal(Math.max(nuevoTotal, 0));

            orden.getCliente().setPuntosLealtad(orden.getCliente().getPuntosLealtad() - 50);
            clienteRepository.save(orden.getCliente());
        }

        return true;
    }
}
