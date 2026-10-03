
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import caretaker.Historial;
import memento.Memento;
import originator.Editor;

/**
 * Interfaz de línea de comandos del editor con historial.
 *
 * <p>El menú sólo usa la API pública de cada participante del patrón: quien escribe
 * y restaura es el Editor, quien apila y desapila es el Historial, y los Mementos
 * viajan entre ambos sin que nadie pueda alterarlos.</p>
 */
final class Consola {

    private static final String SEPARADOR = "--------------------------------------------------";

    private final Editor editor = new Editor();
    private final Historial historial = new Historial();
    private final BufferedReader lector =
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    static void mostrarEncabezado() {
        System.out.println(SEPARADOR);
        System.out.println("  EDITOR DE TEXTO CON HISTORIAL - Patrón de diseño Memento");
        System.out.println("  Originator = originator.Editor | Memento = memento.Memento"
                + " | Caretaker = caretaker.Historial");
        System.out.println(SEPARADOR);
    }

    static void mostrarAyuda() {
        System.out.println("""
                Uso:
                  java main.Main            Inicia el menú interactivo
                  java main.Main demo       Ejecuta el escenario de prueba del taller
                  java main.Main --help     Muestra esta ayuda

                En el menú:
                  1 Escribir o modificar el contenido   4 Consultar el contenido actual
                  2 Guardar el estado actual            5 Ver el historial de estados
                  3 Restaurar el último estado          6 Ejecutar el escenario de prueba
                                                      0 Salir""");
    }

    /** Bucle principal del menú. */
    static void iniciar() {
        Consola consola = new Consola();
        System.out.println("\nMenú interactivo (opción 0 para salir).");
        while (true) {
            consola.mostrarMenu();
            String opcion = consola.leer("\nOpción: ");
            if (opcion == null) {
                System.out.println("\nFin de la entrada. Hasta pronto.");
                return;
            }
            switch (opcion.trim()) {
                case "1" -> consola.escribirContenido();
                case "2" -> consola.guardarEstado();
                case "3" -> consola.restaurarUltimoEstado();
                case "4" -> consola.consultarContenido();
                case "5" -> consola.verHistorial();
                case "6" -> Main.escenarioDePrueba();
                case "0", "salir", "exit", "q" -> {
                    System.out.println("¡Hasta pronto!");
                    return;
                }
                default -> System.out.println("Opción no válida. Escribe un número del 0 al 6.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n" + SEPARADOR);
        System.out.println("Contenido actual: " + formatear(editor.getContenido()));
        System.out.println("Estados guardados: " + historial.cantidadDeEstados()
                + (historial.estadosDescartados() > 0
                        ? " (" + historial.estadosDescartados() + " descartado(s) por límite de "
                          + Historial.MAXIMO_ESTADOS + ")" : ""));
        System.out.println(SEPARADOR);
        System.out.println("  1) Escribir o modificar el contenido");
        System.out.println("  2) Guardar el estado actual");
        System.out.println("  3) Restaurar el último estado guardado");
        System.out.println("  4) Consultar el contenido actual");
        System.out.println("  5) Ver el historial de estados guardados");
        System.out.println("  6) Ejecutar el escenario de prueba del taller");
        System.out.println("  0) Salir");
    }

    private void escribirContenido() {
        String contenido = leer("\nEscribe el nuevo contenido (no puede quedar vacío): ");
        if (contenido == null) {
            return;
        }
        if (contenido.isBlank()) {
            System.out.println("No se admiten contenidos vacíos: el documento no cambia.");
            return;
        }
        editor.setContenido(contenido);
        System.out.println("Contenido actualizado: " + formatear(editor.getContenido()));
    }

    private void guardarEstado() {
        String etiqueta = leer("\nNombre del estado (vacío para numerarlo automáticamente): ");
        if (etiqueta == null) {
            return;
        }
        Memento memento = etiqueta.isBlank() ? editor.guardar() : editor.guardar(etiqueta.trim());
        historial.guardarEstado(memento);
        System.out.println("Estado guardado -> " + memento.getResumen());
        System.out.println("Estados guardados: " + historial.cantidadDeEstados());
    }

    private void restaurarUltimoEstado() {
        Optional<Memento> memento = historial.extraerUltimoEstado();
        if (memento.isEmpty()) {
            System.out.println("No hay estados guardados: no se intenta restaurar.");
            return;
        }
        editor.restaurar(memento.get());
        System.out.println("Estado restaurado -> " + memento.get().getResumen());
        System.out.println("Contenido actual: " + formatear(editor.getContenido()));
    }

    private void consultarContenido() {
        System.out.println("Contenido actual: " + formatear(editor.getContenido())
                + (editor.estaVacio() ? " (vacío)" : "")
                + "  [" + editor.getContenido().length() + " caracteres]");
    }

    private void verHistorial() {
        List<Memento> estados = historial.estadosGuardados();
        if (estados.isEmpty()) {
            System.out.println("El historial está vacío.");
            return;
        }
        System.out.println("Historial (del más reciente al más antiguo):");
        int posicion = 1;
        for (Memento memento : estados) {
            System.out.println("  " + posicion++ + ". " + memento.getResumen());
        }
        System.out.println("El historial sólo ve metadatos: el contenido sigue encapsulado en el Editor.");
    }

    /** Lee una línea; devuelve {@code null} si el usuario cerró la entrada (Ctrl+D / EOF). */
    private String leer(String mensaje) {
        System.out.print(mensaje);
        System.out.flush();
        try {
            return lector.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private static String formatear(String contenido) {
        return contenido.isEmpty() ? "\"\"" : "\"" + contenido + "\"";
    }
}
