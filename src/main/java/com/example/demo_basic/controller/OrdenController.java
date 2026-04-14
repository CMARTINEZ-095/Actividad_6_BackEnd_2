package com.example.demo_basic.controller;

import com.example.demo_basic.model.entity.Cliente;
import com.example.demo_basic.model.entity.Orden;
import com.example.demo_basic.model.entity.Prenda;
import com.example.demo_basic.repository.ClienteRepository;
import com.example.demo_basic.repository.OrdenRepository;
import com.example.demo_basic.service.AplicacionDescuentoLealtad;
import com.example.demo_basic.service.AsignacionFechaEntrega;
import com.example.demo_basic.service.CalculoTarifaTotal;
import com.example.demo_basic.service.SumaPuntosLealtad;
import com.example.demo_basic.service.ValidacionCantidadPrendas;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/ordenes")
@Tag(name = "Ordenes", description = "Gestión de órdenes de lavandería")
public class OrdenController {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ValidacionCantidadPrendas validacionCantidadPrendas;

    @Autowired
    private CalculoTarifaTotal calculoTarifaTotal;

    @Autowired
    private AplicacionDescuentoLealtad aplicacionDescuentoLealtad;

    @Autowired
    private SumaPuntosLealtad sumaPuntosLealtad;

    @Autowired
    private AsignacionFechaEntrega asignacionFechaEntrega;

    @Operation(summary = "Obtener todas las órdenes")
    @GetMapping
    public List<Orden> getAll() {
        return ordenRepository.findAll();
    }

    @Operation(summary = "Obtener una orden por ID")
    @GetMapping("/{id}")
    public Orden getById(@PathVariable Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Orden no encontrada con id: " + id));
    }

    @Operation(summary = "Crear una orden")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Orden create(@RequestBody Orden request) {
        Cliente cliente = clienteRepository.findById(request.getCliente().getId())
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id: " + request.getCliente().getId()));
        request.setCliente(cliente);

        for (Prenda p : request.getPrendas()) {
            p.setOrden(request);
        }

        validacionCantidadPrendas.procesar(request); // lanza excepción si hay 0 prendas o más de 20
        calculoTarifaTotal.procesar(request);
        aplicacionDescuentoLealtad.procesar(request);
        sumaPuntosLealtad.procesar(request);
        asignacionFechaEntrega.procesar(request);

        return ordenRepository.save(request);
    }

    @Operation(summary = "Actualizar una orden")
    @PutMapping("/{id}")
    public Orden update(@PathVariable Long id, @RequestBody Orden request) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Orden no encontrada con id: " + id));
        if (request.getFechaEntregaEstimada() != null) {
            orden.setFechaEntregaEstimada(request.getFechaEntregaEstimada());
        }
        if (request.getTotal() != null) {
            orden.setTotal(request.getTotal());
        }
        return ordenRepository.save(orden);
    }

    @Operation(summary = "Eliminar una orden")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ordenRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Orden no encontrada con id: " + id));
        ordenRepository.deleteById(id);
    }
}
