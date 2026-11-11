package it.unicam.cs.asdl.eslists;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Lista concatenata singola <strong>mutabile</strong>, con riferimenti
 * {@code head} e {@code tail}, che implementa {@link java.util.List}.
 * Accetta duplicati ma non elementi {@code null}.
 *
 * <p>Obiettivi didattici: manipolare i collegamenti {@code next}, preservare
 * gli invarianti di testa/coda/dimensione e implementare un iteratore mediante
 * una <em>classe interna non statica</em> che accede all'istanza esterna.
 * La classe {@code Node} è invece statica: un nodo non ha bisogno di accedere
 * direttamente allo stato della lista contenitrice.</p>
 *
 * <p>L'iteratore è fail-fast <em>best effort</em>: le modifiche strutturali
 * dopo la sua creazione causano {@link java.util.ConcurrentModificationException}
 * quando vengono rilevate da {@code hasNext()} o {@code next()}.
 * La modifica dell'elemento con {@code set} non è strutturale.</p>
 *
 * <p>Questa è un'implementazione didattica PARZIALE di {@code List}:
 * {@code subList}, l'array tipizzato e alcune
 * operazioni collettive lanciano {@link UnsupportedOperationException}.
 * {@code listIterator} è di sola lettura e funziona, ma non è ottimizzato.
 * Anche {@code Iterator.remove()} non è supportato. I metodi di ricerca
 * e aggiornamento per indice costano O(n); l'inserimento in coda con
 * {@code tail} costa O(1).</p>
 *
 * @param <E> tipo degli elementi
 * @author Luca Tesei
 */
public class SingleLinkedList<E> implements List<E> {

    // Invarianti: size == 0 sse head == null e tail == null;
    // se size > 0, tail.next == null.
    private int size;

    private Node<E> head;

    private Node<E> tail;

    // Incrementare SOLO per cambiamenti strutturali (inserimenti/rimozioni).
    // L'iteratore confronta questo contatore con la propria copia iniziale.
    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. E' dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */
    private static class Node<E> {
        // Un nodo conosce il proprio elemento e il successivo, non la lista.
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     *
     * Il controllo delle modifiche è già implementato: il compito è
     * esclusivamente completare {@code hasNext()} e {@code next()}.
     *
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {
        // La classe è NON STATIC: accede a SingleLinkedList.this.head
        // e a SingleLinkedList.this.numeroModifiche.
        private Node<E> prossimo = SingleLinkedList.this.head;
        private final int modificheAttese = SingleLinkedList.this.numeroModifiche;

        private void verificaModifiche() {
            if (modificheAttese != SingleLinkedList.this.numeroModifiche)
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
        }

        @Override
        public boolean hasNext() {
            // TODO: verificare le modifiche strutturali e controllare
            // se esiste un nodo ancora da visitare. Costo atteso O(1).
            return false;
        }

        @Override
        public E next() {
            // TODO: verificare le modifiche strutturali; se esaurito,
            // lanciare NoSuchElementException; altrimenti restituire
            // l'elemento corrente e avanzare al nodo successivo. O(1).
            return null;
        }
        // remove() NON è richiesto: rimane UnsupportedOperationException.
    }

    /**
     * Uguaglianza per sequenza, compatibile con tutte le classi List.
     * Confronta gli elementi tramite equals, rispettandone l'ordine.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof List))
            return false;
        List<?> other = (List<?>) obj;
        // Dimensioni differenti implicano liste differenti.
        if (this.size != other.size())
            return false;
        // Stessa lunghezza: confronto in parallelo tramite iteratori.
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /**
     * Hash della sequenza secondo la convenzione java.util.List:
     * h = 31*h + element.hashCode() per ogni elemento da sinistra a destra.
     * Deve coincidere con l'hashCode di qualsiasi List uguale.
     */
    @Override
    public int hashCode() {
        int result = 1;
        for (E element : this)
            result = 31 * result + element.hashCode();
        return result;
    }

    /** Numero di nodi effettivamente raggiungibili da head, mantenuto nel campo size. O(1). */
    @Override
    public int size() {
        return this.size;
    }

    /** Lista vuota se e solo se size == 0 e head/tail sono null. O(1). */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /** Ricerca lineare; null non ammesso. O(n). */
    @Override
    public boolean contains(Object o) {
        if (o == null)
            throw new NullPointerException(
                    "Richiesto contains di elemento null");
        Node<E> n = this.head;
        // cerco un elemento uguale a o
        while (n != null)
            if (o.equals(n.item)) {
                return true;
            } else
                n = n.next;
        return false;
    }

    /**
     * Accoda un elemento in O(1), grazie al riferimento {@code tail}.
     *
     * <p><strong>TODO studenti.</strong> Gestire separatamente la lista vuota
     * (il nuovo nodo diventa sia {@code head} sia {@code tail}) e la lista
     * non vuota (collegare il nuovo nodo a {@code tail.next}). Dopo l'inserimento
     * aggiornare {@code tail}, {@code size} e {@code numeroModifiche}.</p>
     *
     * @param e elemento non nullo
     * @return {@code true} se l'elemento viene inserito
     * @throws NullPointerException se {@code e} è null
     */
    @Override
    public boolean add(E e) {
        // TODO implementare
        return false;
    }

    /** Rimuove la prima occorrenza, se presente. O(n). */
    @Override
    public boolean remove(Object o) {
        if (o == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un oggetto null");
        int index = this.indexOf(o);
        if (index == -1)
            // oggetto non presente
            return false;
        // rimuovo la prima occorrenza dell'oggetto in posizione index
        this.remove(index);
        return true;
    }

    /** Scollega tutta la catena rendendo head e tail null. O(1). */
    @Override
    public void clear() {
        // scollego tutta la catena
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.numeroModifiche++;
    }

    /**
     * Restituisce l'elemento in posizione {@code index}, visitando i nodi
     * dalla testa. Costo O(n) nel caso peggiore.
     * <p><strong>TODO studenti.</strong> Verificare {@code 0 <= index < size},
     * quindi avanzare di {@code index} collegamenti {@code next}.</p>
     * @throws IndexOutOfBoundsException per un indice non valido
     */
    @Override
    public E get(int index) {
        // TODO implementare
        return null;
    }

    /**
     * Sostituisce l'elemento di indice {@code index} e restituisce il precedente.
     * Costo O(n). <strong>TODO studenti.</strong>
     * NOTA: set(int, E) sostituisce soltanto il valore memorizzato nel
     * nodo individuato dall'indice.
     * <p>Non cambiare i collegamenti {@code next} e non incrementare
     * {@code numeroModifiche}</p>
     * Non cambiando il numero di nodi né i collegamenti next questo metodo
     * NON costituisce una modifica strutturale e NON incrementa
     * numeroModifiche. Di conseguenza, un iteratore già creato rimane
     * valido e potrà osservare il nuovo valore quando raggiungerà questo
     * nodo.
     *
     * @throws IndexOutOfBoundsException per un indice non valido
     * @throws NullPointerException se {@code element} è null
     */
    @Override
    public E set(int index, E element) {
        // TODO implementare
        return null;
    }

    /**
     * Inserisce {@code element} nella posizione specificata, spostando a destra
     * gli indici logici successivi. Costo O(n) nel caso peggiore.
     * <p><strong>TODO studenti.</strong> Sono ammessi gli indici da 0 a
     * {@code size} inclusi. Trattare il caso testa, quello intermedio e
     * quello coda; in particolare, inserire in una lista vuota deve aggiornare
     * sia {@code head} sia {@code tail}. Incrementare i contatori strutturali.</p>
     * @throws IndexOutOfBoundsException per un indice non valido
     * @throws NullPointerException se {@code element} è null
     */
    @Override
    public void add(int index, E element) {
        // TODO implementare

    }

    /**
     * Rimuove e restituisce l'elemento di indice {@code index}.
     * Costo O(n) nel caso peggiore. <strong>TODO studenti.</strong>
     * <p>Gestire in modo esplicito la rimozione dell'unico nodo, della testa,
     * della coda e di un nodo interno. Mantenere {@code tail.next == null},
     * aggiornare {@code size} e {@code numeroModifiche}.</p>
     * @throws IndexOutOfBoundsException per un indice non valido
     */
    @Override
    public E remove(int index) {
        // TODO implementare
        return null;
    }

    /** Prima posizione in cui l'elemento compare, oppure -1. O(n). */
    @Override
    public int indexOf(Object o) {
        if (o == null)
            throw new NullPointerException("Richiesto index di elemento null");
        Node<E> n = this.head;
        int index = -1;
        // cerco un elemento uguale a o
        while (n != null) {
            index++;
            // l'indice del nodo n è index
            if (o.equals(n.item))
                // prima occorrenza dell'elemento trovata
                return index;
            else
                n = n.next;
        }
        // elemento non trovato
        return -1;
    }

    /** Ultima posizione di un elemento, oppure -1. O(n). */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null)
            throw new NullPointerException("Richiesto index di elemento null");
        // parto dal nodo testa
        Node<E> n = this.head;
        int index = -1;
        int lastIndex = -1;
        // cerco un elemento uguale a o fino a quando non ho guardato tutti gli
        // elementi
        while (n != null) {
            index++;
            // l'indice del nodo n è index
            if (o.equals(n.item))
                // trovata una occorrenza dell'elemento
                lastIndex = index;
            // vado avanti
            n = n.next;
        }
        return lastIndex;
    }

    /** Copia gli elementi, nell'ordine, in un array Object[]. O(n). */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[this.size];
        int i = 0;
        // uso il foreach
        for (E el : this) {
            result[i] = el;
            i++;
        }
        return result;
    }

    /** Restituisce un nuovo iteratore fail-fast che parte da head. O(1). */
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iteratore bidirezionale di sola lettura. È fornito già implementato per
     * rispettare il contratto di List e l'uguaglianza con altre liste Java.
     * Per semplicità usa get(index): next/previous costano O(n), a differenza
     * del nostro Iterator didattico che avanza in O(1) usando i collegamenti.
     */
    private class ReadOnlyListItr implements ListIterator<E> {
        private int cursor;
        private final int expectedModCount = SingleLinkedList.this.numeroModifiche;

        ReadOnlyListItr(int index) {
            cursor = index;
        }

        private void check() {
            if (expectedModCount != SingleLinkedList.this.numeroModifiche)
                throw new ConcurrentModificationException("Lista modificata");
        }

        @Override
        public boolean hasNext() {
            check();
            return cursor < SingleLinkedList.this.size;
        }
        @Override
        public E next() {
            check();
            if (cursor >= SingleLinkedList.this.size)
                throw new NoSuchElementException();
            return SingleLinkedList.this.get(cursor++);
        }
        @Override
        public boolean hasPrevious() {
            check();
            return cursor > 0;
        }
        @Override
        public E previous() {
            check();
            if (cursor == 0)
                throw new NoSuchElementException();
            return SingleLinkedList.this.get(--cursor);
        }
        @Override public int nextIndex() { check(); return cursor; }
        @Override public int previousIndex() { check(); return cursor - 1; }
        @Override public void remove() { throw new UnsupportedOperationException(); }
        @Override public void set(E e) { throw new UnsupportedOperationException(); }
        @Override public void add(E e) { throw new UnsupportedOperationException(); }
    }

    /** Restituisce il ListIterator di sola lettura, posizionato all'inizio. */
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /** Restituisce il ListIterator di sola lettura all'indice specificato. */
    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        return new ReadOnlyListItr(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
