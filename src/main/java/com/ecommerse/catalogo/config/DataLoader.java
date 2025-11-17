package com.ecommerse.catalogo.config;

import com.ecommerse.catalogo.model.Comentario;
import com.ecommerse.catalogo.model.Producto;
import com.ecommerse.catalogo.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public DataLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {

        if (productoRepository.count() > 0) {
            System.out.println("✔ Productos ya insertados. No se cargan nuevamente.");
            return;
        }

        System.out.println("⏳ Insertando productos por defecto...");

        List<Producto> productos = List.of(
                new Producto(
                        "P001",
                        "Laptop Gamer Titan X15",
                        "Laptop de alto rendimiento con tarjeta gráfica dedicada",
                        new BigDecimal("4599.99"),
                        "Tecnología",
                        List.of("16GB RAM", "RTX 4060", "1TB SSD"),
                        4.8,
                        List.of(
                                new Comentario("Juan", "Excelente laptop", 5.0, LocalDateTime.now()),
                                new Comentario("María", "Muy rápida", 4.5, LocalDateTime.now())
                        ),
                        true,
                        12,
                        "Aorus",
                        "2 años",
                        List.of("img/laptop1.jpg")
                ),

                new Producto(
                        "P002",
                        "Audífonos BassPro",
                        "Audífonos Bluetooth con cancelación de ruido",
                        new BigDecimal("299.99"),
                        "Audio",
                        List.of("Bluetooth 5.3", "Cancelación activa"),
                        4.5,
                        List.of(
                                new Comentario("Carlos", "Gran sonido", 5.0, LocalDateTime.now())
                        ),
                        true,
                        30,
                        "Sony",
                        "1 año",
                        List.of("img/audifonos1.jpg")
                ),
                crearProductoSimple("P003", "Smartwatch ActiveFit 2", "Tecnología", "Samsung"),
                crearProductoSimple("P004", "Televisor 55'' 4K Ultra HD", "Hogar", "LG"),
                crearProductoSimple("P005", "Silla Ergonómica ProOffice", "Oficina", "ErgoPlus"),
                crearProductoSimple("P006", "Teclado Mecánico RGB", "Tecnología", "HyperX"),
                crearProductoSimple("P007", "Mouse Gamer UltraLight", "Tecnología", "Logitech"),
                crearProductoSimple("P008", "Cámara Reflex ProShot 2500", "Fotografía", "Canon"),
                crearProductoSimple("P009", "Parlante Bluetooth MaxSound", "Audio", "JBL"),
                crearProductoSimple("P010", "Horno Microondas 30L", "Hogar", "Samsung"),
                crearProductoSimple("P011", "Monitor Curvo 32''", "Tecnología", "MSI"),
                crearProductoSimple("P012", "Impresora Multifuncional JetPrint", "Oficina", "HP"),
                crearProductoSimple("P013", "Tablet ProTab 11", "Tecnología", "Xiaomi"),
                crearProductoSimple("P014", "Aspiradora Robot CleanMate 3", "Hogar", "iRobot"),
                crearProductoSimple("P015", "Cafetera Espresso MasterBrew", "Hogar", "DeLonghi")
        );

        productoRepository.saveAll(productos);

        System.out.println("✔ Productos insertados correctamente.");
    }

    private Producto crearProductoSimple(String id, String nombre, String categoria, String marca) {
        return new Producto(
                id,
                nombre,
                "Producto de categoría " + categoria,
                new BigDecimal("199.99"),
                categoria,
                List.of("Característica estándar"),
                4.0,
                List.of(
                        new Comentario("Usuario", "Buen producto", 4.0, LocalDateTime.now())
                ),
                true,
                20,
                marca,
                "1 año",
                List.of("img/default.jpg")
        );
    }
}