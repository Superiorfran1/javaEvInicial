package service;

import exceptions.ExportException;
import models.LineaPedido;
import models.Pedido;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Exporta el historial de pedidos a un CSV pensado para abrirse directamente en Excel:
 *  - Separador ';' (el que espera Excel con configuración regional española).
 *  - Codificación UTF-8 con BOM, para que se vean bien tildes y la 'ñ'.
 *  - Números con coma decimal y sin separador de miles, para que Excel los trate como números.
 *  - Una fila por línea de pedido (formato "plano", fácil de filtrar y de usar en tablas dinámicas).
 */
public class ExportService {
    private static final char SEPARADOR = ';';
    private static final String SALTO_LINEA = "\r\n";
    private static final String BOM_UTF8 = "\uFEFF";
    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-ES");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final String[] CABECERA = {
            "Pedido", "Fecha", "ID Usuario", "Cliente", "Email",
            "ID Producto", "Producto", "Tipo", "Cantidad",
            "Precio unitario (EUR)", "Subtotal (EUR)", "Total pedido (EUR)"
    };

    /**
     * Escribe el historial en el fichero indicado (lo sobrescribe si ya existe).
     *
     * @return la ruta absoluta del fichero generado
     * @throws ExportException si no hay pedidos, la ruta no es válida o falla la escritura
     */
    public Path exportarHistorialCSV(List<Pedido> historial, Path destino) throws ExportException {
        if (historial == null || historial.isEmpty()) {
            throw new ExportException("No hay pedidos en el historial que exportar.");
        }
        if (destino == null) {
            throw new ExportException("La ruta del fichero de destino no es válida.");
        }

        Path absoluto = destino.toAbsolutePath();
        try {
            Path carpeta = absoluto.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            try (BufferedWriter out = Files.newBufferedWriter(absoluto, StandardCharsets.UTF_8)) {
                out.write(BOM_UTF8);
                escribirFila(out, CABECERA);
                for (Pedido pedido : historial) {
                    for (LineaPedido linea : pedido.getLineas()) {
                        escribirFila(out, new String[]{
                                pedido.getIdPedido(),
                                pedido.getFecha().format(FORMATO_FECHA),
                                pedido.getUsuario().getId(),
                                pedido.getUsuario().getNombre(),
                                pedido.getUsuario().getEmail(),
                                linea.getProducto().getId(),
                                linea.getProducto().getNombre(),
                                linea.getProducto().getTipo(),
                                String.valueOf(linea.getCantidad()),
                                numero(linea.getPrecioUnitario()),
                                numero(linea.getSubtotal()),
                                numero(pedido.getTotal())
                        });
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            throw new ExportException("No se pudo escribir el fichero CSV en '" + absoluto + "': " + e.getMessage(), e);
        }
        return absoluto;
    }

    private void escribirFila(BufferedWriter out, String[] campos) throws IOException {
        StringBuilder fila = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                fila.append(SEPARADOR);
            }
            fila.append(escapar(campos[i]));
        }
        out.write(fila.toString());
        out.write(SALTO_LINEA);
    }

    // Entrecomilla el campo si contiene separador, comillas o saltos de línea (RFC 4180)
    private String escapar(String campo) {
        if (campo == null) {
            return "";
        }
        boolean necesitaComillas = campo.indexOf(SEPARADOR) >= 0 || campo.indexOf('"') >= 0
                || campo.indexOf('\n') >= 0 || campo.indexOf('\r') >= 0;
        if (necesitaComillas) {
            return '"' + campo.replace("\"", "\"\"") + '"';
        }
        return campo;
    }

    private String numero(double valor) {
        return String.format(LOCALE_ES, "%.2f", valor);
    }
}
