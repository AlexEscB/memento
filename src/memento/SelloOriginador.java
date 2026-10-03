package memento;

/**
 * Sello que el Originator presenta para poder usar la interfaz ancha del {@link Memento}
 * (leer el contenido guardado).
 *
 * <p>Por diseño sólo el {@code originator.Editor} —que es quien crea los Mementos—
 * implementa este sello. Aunque otra clase se布料 de él, el Memento compara la
 * identidad del solicitante con la del creador, de modo que un impostor no puede leer
 * el estado. Así, el Caretaker ({@code caretaker.Historial}) queda fuera de la
 * interfaz ancha tal como exige el patrón.</p>
 */
public interface SelloOriginador {
}
