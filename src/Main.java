

import java.util.Arrays;

import caretaker.Historial;
import memento.Memento;
import originator.Editor;

/**
 * Programa principal del taller: editor de texto con historial mediante el patrón Memento.
 *
 * <p>Correspondencias exigidas por el taller:</p>
 * <pre>
 *   Originator -&gt; originator.Editor
 *   Memento     -&gt; memento.Memento
 *   Caretaker   -&gt; caretaker.Historial
 * </pre>
 *
 * <p>Uso por terminal:</p>
 * <pre>
 *   java main.Main            → menú interactivo
 *   java main.Main demo       → ejecuta el escenario de prueba del taller
 * </pre>
 */
public final class Main {

    private static final String[] MODOS_AYUDA = {"-h", "--help", "ayuda"};

    private Main() {
    }

    public static void main(String[] args) {
        Consola.mostrarEncabezado();

        if (Arrays.stream(args).anyMatch(Main::esSolicitudDeAyuda)) {
            Consola.mostrarAyuda();
            return;
        }
        if (Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("demo")
                || arg.equalsIgnoreCase("--demo") || arg.equalsIgnoreCase("-d"))) {
            escenarioDePrueba();
            return;
        }
        Consola.iniciar();
    }

    /**
     * Escenario de prueba del taller: un Editor, tres cambios distintos, dos estados
     * guardados como mínimo, una restauración y una nueva modificación después de restaurar.
     */
    public static void escenarioDePrueba() {
        System.out.println("\n=== ESCENARIO DE PRUEBA DEL TALLER ===");

        Editor editor = new Editor();
        Historial historial = new Historial();

        System.out.println("[1] Se crea el Editor y su Historial. Contenido inicial: \"" + editor.getContenido() + "\"");

        System.out.println("[2] El usuario escribe \"Hola\"");
        editor.setContenido("Hola");
        System.out.println("    Contenido actual: \"" + editor.getContenido() + "\"");

        System.out.println("[3] Se guarda el estado actual");
        historial.guardarEstado(editor.guardar());
        System.out.println("    " + historial.obtenerUltimoEstado().map(Memento::getResumen).orElse("sin estados"));
        System.out.println("    Estados guardados: " + historial.cantidadDeEstados());

        System.out.println("[4] El usuario sigue escribiendo: \"Hola, mundo\"");
        editor.setContenido("Hola, mundo");
        System.out.println("    Contenido actual: \"" + editor.getContenido() + "\"");

        System.out.println("[5] Se guarda el estado actual");
        historial.guardarEstado(editor.guardar());
        System.out.println("    " + historial.obtenerUltimoEstado().map(Memento::getResumen).orElse("sin estados"));
        System.out.println("    Estados guardados: " + historial.cantidadDeEstados());

        System.out.println("[6] Cambio que el usuario no desea conservar: \"Hola, mundo. Este texto fue modificado.\"");
        editor.setContenido("Hola, mundo. Este texto fue modificado.");
        System.out.println("    Contenido actual: \"" + editor.getContenido() + "\"");

        System.out.println("[7] Se restaura el último estado guardado (undo)");
        restaurarUltimo(editor, historial);
        String contenidoRestaurado = editor.getContenido();
        System.out.println("    Contenido actual: \"" + contenidoRestaurado + "\"");

        System.out.println("[8] Nueva modificación después de restaurar: \"Hola, mundo. Adiós\"");
        editor.setContenido("Hola, mundo. Adiós");
        System.out.println("    Contenido actual: \"" + editor.getContenido() + "\"");
        System.out.println("    Estados guardados: " + historial.cantidadDeEstados()
                + " (el cambio no se guardó, así que se puede deshacer)");

        System.out.println("[9] Segundo deshacer: la pila devuelve el estado guardado número 1");
        restaurarUltimo(editor, historial);
        System.out.println("    Contenido actual: \"" + editor.getContenido() + "\"");

        System.out.println("[10] Se limpia el historial y se intenta restaurar de nuevo");
        historial.limpiar();
        if (restaurarUltimo(editor, historial)) {
            System.out.println("    ERROR: se restauró un estado inexistente");
        }

        System.out.println("\nResultado esperado del taller tras la restauración del paso [7]: \"Hola, mundo\"");
        System.out.println("Contenido obtenido en esa restauración: \"" + contenidoRestaurado + "\"  "
                + ("Hola, mundo".equals(contenidoRestaurado) ? "CORRECTO" : "INCORRECTO"));
        System.out.println("=== FIN DEL ESCENARIO ===");
    }

    /**
     * Pide al Historial el último estado y se lo pasa al Editor para que sea él quien
     * restaure su propio estado. Devuelve {@code false} si no había estados guardados.
     */
    private static boolean restaurarUltimo(Editor editor, Historial historial) {
        return historial.extraerUltimoEstado().map(memento -> {
            editor.restaurar(memento);
            System.out.println("    Restaurado: " + memento.getResumen());
            return true;
        }).orElseGet(() -> {
            System.out.println("    No hay estados guardados: no se intenta restaurar.");
            return false;
        });
    }

    private static boolean esSolicitudDeAyuda(String arg) {
        return Arrays.stream(MODOS_AYUDA).anyMatch(ayuda -> ayuda.equalsIgnoreCase(arg));
    }
}
