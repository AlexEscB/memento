package originator;

import java.util.Objects;

import memento.Memento;
import memento.SelloOriginador;

/**
 * Originator del patrón Memento: representa al objeto cuyo estado cambia, en este
 * caso el documento de un editor de texto.
 *
 * <p>Es el único dueño de su estado interno ({@code contenido}) y, en consecuencia,
 * el único que puede modificarlo, crear sus propias instantáneas
 * ({@link #guardar()}) y restaurarlas ({@link #restaurar(Memento)}). El historial
 * nunca toca estos atributos: sólo le entrega Mementos ya cerrados.</p>
 */
public final class Editor implements SelloOriginador {

    private String contenido;
    private int numeroDeGuardados;

    public Editor() {
        this("");
    }

    public Editor(String contenidoInicial) {
        this.contenido = validar(contenidoInicial);
    }

    /** Permite establecer o modificar el contenido del documento. */
    public void setContenido(String contenido) {
        this.contenido = validar(contenido);
    }

    /** Permite consultar el contenido actual del documento. */
    public String getContenido() {
        return contenido;
    }

    /**
     * Crea y devuelve un Memento con el estado actual del documento.
     * Es la operación que el Caretaker necesita para guardar un punto de retorno.
     */
    public Memento guardar() {
        return guardar("Estado " + (numeroDeGuardados + 1));
    }

    /** Igual que {@link #guardar()} pero permite nombrar el punto de retorno. */
    public Memento guardar(String etiqueta) {
        numeroDeGuardados++;
        return Memento.crear(contenido, Objects.requireNonNull(etiqueta, "la etiqueta no puede ser null"), this);
    }

    /**
     * Restaura su estado a partir del Memento recibido.
     * Tras la restauración el contenido coincide con el que tenía cuando se creó el Memento.
     */
    public void restaurar(Memento memento) {
        Objects.requireNonNull(memento, "el memento no puede ser null");
        this.contenido = memento.leer(this);
    }

    public boolean estaVacio() {
        return contenido.isEmpty();
    }

    private static String validar(String contenido) {
        return Objects.requireNonNull(contenido, "el contenido no puede ser null");
    }

    @Override
    public String toString() {
        return contenido;
    }
}
