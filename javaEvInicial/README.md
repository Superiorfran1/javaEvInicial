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


# Compilar todo el proyecto
javac com/inventario/main/Main.java

# Ejecutar la aplicación
java com.inventario.main.Main

## Extra elegido: Persistencia alternativa (exportación a CSV)

### Por qué lo he elegido
Es el extra que mejor encaja con el proyecto tal y como está: el historial de pedidos ya vive en una `List<Pedido>` en memoria y se pierde al cerrar el programa. Exportarlo a CSV permite conservar los datos y analizarlos en Excel sin añadir librerías ni cambiar la arquitectura por capas.

### Cómo se integra en el proyecto
- **Menú**: nueva opción `6. Exportar historial de pedidos a CSV (Excel)`. Pide un nombre de fichero (por defecto `historial_pedidos.csv`, en el directorio desde el que se ejecuta el programa).
- **`service.ExportService`**: nueva clase con la lógica de exportación. Recibe la lista de pedidos y la ruta y devuelve la ruta absoluta generada.
- **`StockService.exportarHistorialCSV(String)`**: delega en `ExportService` con el historial que ya gestiona.
- **`Controller.exportarHistorialCSV(String)`**: captura la excepción y muestra el error por consola, igual que el resto de métodos del controlador.
- **`exceptions.ExportException`**: excepción personalizada para los errores de exportación (historial vacío, ruta inválida, error de escritura en disco).
- **`Pedido`**: se añaden getters (`getIdPedido`, `getFecha`, `getUsuario`, `getLineas` como lista no modificable y `getTotal`).
- **`Product.getTipo()`**: método abstracto implementado por `PhysicalProduct` ("Físico") y `DigitalProduct` ("Digital"); el tipo se obtiene por polimorfismo, sin `instanceof`.

### Formato del fichero (pensado para Excel)
- Una fila por línea de pedido, con cabecera: `Pedido; Fecha; ID Usuario; Cliente; Email; ID Producto; Producto; Tipo; Cantidad; Precio unitario (EUR); Subtotal (EUR); Total pedido (EUR)`.
- Separador `;` y coma decimal (configuración regional española de Excel), codificación UTF-8 con BOM para que se vean bien tildes y `ñ`.
- Los campos que contengan `;`, comillas o saltos de línea se entrecomillan siguiendo RFC 4180.
- La columna *Total pedido* se repite en cada línea del mismo pedido; para sumar ingresos hay que usar *Subtotal*, no *Total pedido*.
- Si el fichero ya existe se sobrescribe.

# Imágenes
<img width="398" height="396" alt="image" src="https://github.com/user-attachments/assets/01684db2-c90b-4615-8bd5-ff478f25de59" />
<img width="384" height="379" alt="image" src="https://github.com/user-attachments/assets/81165520-4de6-47c3-9786-5e014091835f" /><br>
<img width="382" height="377" alt="image" src="https://github.com/user-attachments/assets/f17ed418-08b6-4720-beb9-f8a4b1ecc0b5" />
<img width="406" height="285" alt="image" src="https://github.com/user-attachments/assets/8b5320ac-a467-47ed-aaf0-56e578d8ab75" /><br>
<img width="383" height="291" alt="image" src="https://github.com/user-attachments/assets/32022722-ca78-4390-ad75-0baee6101ecc" />
<img width="383" height="286" alt="image" src="https://github.com/user-attachments/assets/1603afff-4a29-402b-bdf7-0cd34e804034" /><br>
