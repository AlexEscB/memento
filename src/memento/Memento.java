package memento;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Memento: copia inmutable del estado del Originator ({@code originator.Editor}).
 *
 * <p>Sus estados son {@code final} y sólo existe un constructor privado, así que
 * una vez creado el Memento nadie puede alterar el contenido capturado. Además se
 * aplican las dos interfaces del patrón:</p>
 * <ul>
 *   <li><b>Interfaz estrecha</b> (la que ve el Caretaker): los descriptores
 *       {@link #getEtiqueta()}, {@link #getCreadoEn()} y {@link #getResumen()}. No
 *       permiten inspeccionar ni modificar el contenido.</li>
 *   <li><b>Interfaz ancha</b> (la que ve el Originator): {@link #leer(SelloOriginador)}
 *       y la fábrica {@link #crear(String, String, SelloOriginador)}, que exigen el sello
 *       del Editor que creó la instantánea.</li>
 * </ul>
 *
 * <p>Para este taller el estado a recuperar es únicamente el {@code contenido} del
 * documento; la etiqueta y la fecha son metadatos de lectura para el historial.</p>
 */
public final class Memento {

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    private final String contenido;
    private final String etiqueta;
    private final Instant creadoEn;
    private final SelloOriginador creador;

    private Memento(String contenido, String etiqueta, SelloOriginador creador) {
        this.contenido = Objects.requireNonNull(contenido, "el contenido no puede ser null");
        this.etiqueta = Objects.requireNonNull(etiqueta, "la etiqueta no puede ser null");
        this.creador = Objects.requireNonNull(creador, "el creador no puede ser null");
        this.creadoEn = Instant.now();
    }

    /**
     * Interfaz ancha: crea una instantánea del contenido indicado.
     * Uso exclusivo del Originator, que se identifica con su sello.
     */
    public static Memento crear(String contenido, String etiqueta, SelloOriginador creador) {
        return new Memento(contenido, etiqueta, creador);
    }

    /**
     * Interfaz ancha: devuelve el contenido capturado.
     *
     * @throws IllegalStateException si el solicitante no es el Originator que creó el Memento.
     */
    public String leer(SelloOriginador solicitante) {
        if (solicitante == null || solicitante != this.creador) {
            throw new IllegalStateException(
                    "Acceso denegado: sólo el Editor que creó este Memento puede leer su contenido");
        }
        return contenido;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    /** Descripción legible para el historial, sin revelar el contenido guardado. */
    public String getResumen() {
        return String.format("%s  [creado %s, %d caracteres]",
                etiqueta, FORMATO_HORA.format(creadoEn), contenido.length());
    }

    @Override
    public String toString() {
        return "Memento{" + getResumen() + "}";
    }
}
