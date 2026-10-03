package caretaker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import memento.Memento;

/**
 * Caretaker del patrón Memento: historial de estados del documento.
 *
 * <p>Administra los Mementos en una pila LIFO (último guardado es el primero en salir),
 * pero nunca inspecciona ni modifica su contenido, y ni siquiera conoce al Editor:
 * recibe los Mementos ya creados y los entrega para su restauración. De esa forma el
 * historial no puede alterar el estado interno del Originator.</p>
 *
 * <p>La pila tiene una profundidad máxima ({@link #MAXIMO_ESTADOS}) para podar los
 * estados más antiguos y evitar que el historial crezca sin límite en memoria.</p>
 */
public final class Historial {

    public static final int MAXIMO_ESTADOS = 10;

    private final Deque<Memento> estados = new ArrayDeque<>();
    private int descartados;

    /** Almacena un Memento como el estado más reciente del historial. */
    public void guardarEstado(Memento memento) {
        Objects.requireNonNull(memento, "no se puede guardar un memento null");
        estados.push(memento);
        if (estados.size() > MAXIMO_ESTADOS) {
            estados.removeLast();
            descartados++;
        }
    }

    /** Consulta el último estado guardado sin consumirlo. */
    public Optional<Memento> obtenerUltimoEstado() {
        return Optional.ofNullable(estados.peek());
    }

    /** Extrae el último estado guardado (deshacer). Si no hay estados, devuelve vacío. */
    public Optional<Memento> extraerUltimoEstado() {
        return estados.isEmpty() ? Optional.empty() : Optional.of(estados.pop());
    }

    public boolean hayEstados() {
        return !estados.isEmpty();
    }

    public int cantidadDeEstados() {
        return estados.size();
    }

    /** Cuántos estados antiguos se podaron por superar la profundidad máxima. */
    public int estadosDescartados() {
        return descartados;
    }

    /** Lista de solo lectura, del estado más reciente al más antiguo. */
    public List<Memento> estadosGuardados() {
        return List.copyOf(estados);
    }

    public void limpiar() {
        estados.clear();
    }

    @Override
    public String toString() {
        return "Historial[" + estados.size() + " estado(s) guardados]";
    }
}
