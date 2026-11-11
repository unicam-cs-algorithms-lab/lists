package it.unicam.cs.asdl.eslists;

/**
 * Nodo non vuoto della lista ricorsiva persistente: rappresenta
 * {@code Cons(first, rest)}. I due riferimenti sono {@code final}; non esistono
 * setter. Due liste possono condividere la stessa coda senza copiarla.
 * Gli oggetti {@code E} eventualmente mutabili non vengono copiati.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *            il tipo degli elementi della lista.
 */
public final class ConsList<E> implements ADTConsList<E> {
    
    private final E first;

    private final ADTConsList<E> rest;

    /**
     * Costruisce una lista a partire dalla lista vuota inserendo in testa un
     * elemento.
     * 
     * @param first
     *                 l'elemento da inserire nella lista.
     */
    public ConsList(E first) {
        this(first, ADTConsList.<E>empty());
    }

    /**
     * Costruisce una lista a partire da una {@code ADTConsList<E>} e inserendo in testa un
     * nuovo elemento.
     * 
     * @param first l'elemento in testa
     * @param rest la lista di partenza
     */
    public ConsList(E first, ADTConsList<E> rest) {
        // Invariante: un nodo non vuoto ha sempre una coda valida e
        // un elemento non null (contratto semplificato dell'esercitazione).
        this.first = java.util.Objects.requireNonNull(first, "first null");
        this.rest = java.util.Objects.requireNonNull(rest, "rest null");
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public E first() {
        return this.first;
    }

    @Override
    public ADTConsList<E> rest() {
        return this.rest;
    }

    @Override
    public ADTConsList<E> cons(E first) {
        // Il nodo corrente diventa la coda della nuova lista: persistenza.
        return new ConsList<E>(first, this);
    }

}
