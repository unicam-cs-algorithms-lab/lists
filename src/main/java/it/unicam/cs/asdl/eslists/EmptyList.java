package it.unicam.cs.asdl.eslists;

/**
 * Caso base della definizione ricorsiva di lista.
 * Non contiene campi e non possiede né testa né coda. Il metodo
 * {@code cons} crea un nodo, senza modificare questa lista.
 * 
 * @author Luca Tesei
 *
 * @param <E> non significativo per la lista vuota
 */
public final class EmptyList<E> implements ADTConsList<E> {

    public boolean isEmpty() {
        return true;
    }

    public E first() {
        throw new IllegalStateException(
                "Tentativo di accedere alla testa di una lista vuota");
    }

    public ADTConsList<E> rest() {
        throw new IllegalStateException(
                "Tentativo di accedere alla coda di una lista vuota");
    }

    public ADTConsList<E> cons(E first) {
        // La vecchia lista vuota è la coda del nuovo nodo.
        return new ConsList<E>(first, this);
    }

}
