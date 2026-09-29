# Sistema de Gestión de Inventario

Proyecto de consola desarrollado en Java para la asignatura de Desarrollo de Aplicaciones Multiplataforma (2º DAM). El objetivo es consolidar conceptos fundamentales de Programación Orientada a Objetos, gestión de colecciones, excepciones personalizadas e interfaces basadas en consola.

## Estructura del Proyecto y Decisiones de Diseño

La aplicación sigue una arquitectura por capas sencilla con el fin de mantener una clara separación de responsabilidades:

1. **com.inventario.modelo**
    - **Producto**: Clase abstracta base que encapsula los atributos comunes (`id`, `nombre`, `precio`, `stock`). Define el método abstracto `calcularPrecioFinal()`.
    - **ProductoFisico / ProductoDigital**: Subclases que aplican polimorfismo. Los productos físicos incorporan gastos de envío al cálculo total, mientras que los digitales no suman costes adicionales.
    - **Usuario**: Modela los datos de los clientes y soporta el estado activo/inactivo para gestionar bajas lógicas.
    - **Carrito**: Utiliza internamente una estructura `Map` para asociar instancias de producto únicas con sus respectivas cantidades.
    - **LineaPedido y Pedido**: Encapsulan la compra una vez cerrada. `Pedido` genera el formato de factura por consola.

2. **com.inventario.excepcion**
    - Jerarquía de excepciones personalizadas derivadas de `InventarioException` (`ProductoNoEncontradoException`, `StockInsuficienteException`, `UsuarioNoEncontradoException`). Permiten capturar y notificar errores específicos del dominio de negocio sin interrumpir la ejecución del programa.

3. **com.inventario.servicio**
    - **InventarioServicio**: Actúa como controlador de negocio. Para garantizar la unicidad de IDs en productos y usuarios, se implementan mapas (`Map` y `Map`). Para el historial de compras se hace uso de un `List`. Carga una serie de datos iniciales en memoria para facilitar las pruebas.

4. **com.inventario.main**
    - **Main**: Maneja el flujo de interacción por consola mediante `Scanner` y menús interactivos organizados en bucle.

## Estructuras de Datos Seleccionadas

- **Map / Map**: Seleccionados para garantizar que los identificadores no se dupliquen y para permitir búsquedas directas por clave en tiempo constante O(1).
- **Map (Carrito)**: Se eligió esta estructura para asociar dinámicamente un objeto Producto con su cantidad correspondiente sin necesidad de repetir referencias en listas. Se sobreescribieron los métodos `equals` y `hashCode` en la clase base `Producto` utilizando el `id` como criterio de unicidad.
- **List**: Utilizado para mantener un registro cronológico del historial de compras.

## Requisitos para Ejecutar

- JDK 17 o superior (desarrollado y probado sobre entorno Java 26 estándar).
- No requiere librerías externas ni herramientas de construcción especiales.

## Compilación y Ejecución desde Consola

Desde la raíz del directorio `src`:

```bash
# Compilar todo el proyecto
javac com/inventario/main/Main.java

# Ejecutar la aplicación
java com.inventario.main.Main

```

# Imagenes

<img width="343" height="392" alt="image" src="https://github.com/user-attachments/assets/47d13087-49a9-4c62-b081-2ef4fc46ffb4" /><br>

<img width="335" height="349" alt="image" src="https://github.com/user-attachments/assets/b8e8f6af-82b3-4344-b4bb-80958fed3dbf" /><br>

<img width="335" height="355" alt="image" src="https://github.com/user-attachments/assets/a65dfcca-f132-42ac-a7b8-c30dd3312b85" /><br>

<img width="405" height="270" alt="image" src="https://github.com/user-attachments/assets/ae0993fc-b839-4f59-bbe1-2c002e11ee3a" /><br>

<img width="357" height="266" alt="image" src="https://github.com/user-attachments/assets/42f1052a-b909-4ab3-8023-5ba44bb8ae75" /><br>
