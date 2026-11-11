package it.unicam.cs.asdl.eslists;

/**
 * Tipo di dato astratto per liste persistenti strutturalmente immutabili a
 * contenuto generico costruite a partire dalla lista vuota con inserimento
 * in testa (operazione cons). L'implementazione di default è data da due
 * classi: EmptyList che implementa la lista vuota e ConsList che
 * implementa l'operazione cons con campi immutabili. In questa interface
 * sono definiti vari metodi ricorsivi generici sulle ADTConsList.
 * <p>
 * Il nome "cons" deriva dal linguaggio LISP in cui questa è l'operazione per
 * creare una lista a partire da due campi: car (corrisponde al nostro first) e
 * cdr (corrisponde al nostro rest). A differenza dell'approccio funzionale
 * classico, questa classe realizza il tipo di dato astratto nel paradigma ad
 * oggetti.
 *
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public interface ADTConsList<E> {

    /**
     * @return la lista vuota, con il tipo generico corretto.
     */
    static <T> ADTConsList<T> empty() {
        // Il diamond operator <> consente al compilatore di inferire il
        // parametro di tipo T dal tipo di ritorno del metodo.
        return new EmptyList<>();
    }

    /**
     * Determina se questa lista è vuota.
     *
     * @return true se questa lista è vuota.
     */
    public boolean isEmpty();

    /**
     * Restituisce l'elemento in testa alla lista.
     *
     * @return l'elemento in testa
     * @throws IllegalStateException se il metodo viene chiamato su una
     *                               lista vuota.
     */
    public E first();

    /**
     * Restituisce la "coda" di questa lista, cioè questa lista in cui è stato
     * tolto l'elemento di testa.
     *
     * @return la "coda" di questa lista, potrebbe essere la lista vuota
     * @throws IllegalStateException se il metodo viene chiamato su una
     *                               lista vuota
     */
    public ADTConsList<E> rest();

    /**
     * Aggiunge un elemento in testa a questa lista.
     *
     * @param first l'elemento da aggiungere in testa
     * @return una lista con l'elemento {@code head} in testa e questa lista
     * come "coda"
     */
    public ADTConsList<E> cons(E first);

    /**
     * Restituisce una stringa contenente il toString di tutti gli elementi di
     * questa lista separati da uno spazio.
     *
     * @return una stringa con tutti gli elementi di questa lista
     */
    // ESEMPIO GUIDATO: print() mostra il modello di ricorsione strutturale.
    // Non cambia né questa lista né le liste usate come coda.
    default String print() {
        // caso base
        if (this.isEmpty())
            return "";
        // caso ricorsivo
        // Separatore solo tra elementi; nessuno spazio finale.
        return this.first().toString()
                + (this.rest().isEmpty() ? "" : " " + this.rest().print());
    }

    /**
     * Cerca se un elemento è in questa lista.
     *
     * @param e l'elemento da cercare
     * @return true se questa lista contiene {@code element}
     */
    // ESEMPIO GUIDATO: corto circuito dopo il primo elemento trovato.
    default boolean find(E e) {
        // caso base
        if (this.isEmpty())
            return false;
        // caso ricorsivo
        if (this.first().equals(e))
            return true;
        else
            return this.rest().find(e);
    }

    /**
     * Cancella la prima occorrenza di un elemento in questa lista, se presente.
     *
     * @param element l'elemento da cancellare
     * @return una lista uguale a questa in cui la prima occorrenza di
     * {@code element}, se presente, è stata cancellata.
     */
    // ESEMPIO GUIDATO: se la testa non coincide, ricostruisco il prefisso
    // con cons(first). I nodi della lista originale non vengono cambiati.
    default ADTConsList<E> removeFirst(E element) {
        // caso base
        if (this.isEmpty())
            return this;
        // caso ricorsivo
        if (this.first().equals(element))
            return this.rest();
        else
            return this.rest().removeFirst(element).cons(this.first());
    }

    /**
     * Cancella tutti gli elementi uguali a un elemento dato in questa lista.
     *
     * @param element l'elemento da cancellare
     * @return una lista uguale a questa in cui tutte le occorrenze di
     * {@code element} sono state cancellate.
     */
    // Caso base: lista vuota. Se first coincide, elimino il nodo; altrimenti ricostruisco con cons.
    default ADTConsList<E> removeAll(E element) {
        // TODO implementare ricorsivamente
        return null;
    }

    /**
     * Aggiorna il primo elemento uguale a un elemento dato con un nuovo
     * elemento in questa lista.
     *
     * @param element    l'elemento da aggiornare
     * @param newElement il nuovo elemento che deve sostituire
     *                   {@code element}
     * @return una lista uguale a questa in cui la prima occorrenza
     * dell'elemento {@code e} è stata sostituita con
     * {@code newElement}
     */
    // Caso base: lista vuota. Alla prima corrispondenza sostituisco first e riuso rest.
    default ADTConsList<E> updateFirst(E element, E newElement) {
        // TODO implementare ricorsivamente
        return null;
    }

    /**
     * Aggiorna tutti gli elementi uguali a un elemento dato con un nuovo
     * elemento in questa lista.
     *
     * @param element    l'elemento da aggiornare
     * @param newElement il nuovo elemento che deve sostituire
     *                   {@code element}
     * @return una lista uguale a questa in cui tutte le occorrenze
     * dell'elemento {@code element} sono state sostituite con
     * {@code newElement}
     */
    // Ricorsione su rest e ricostruzione: conservo o sostituisco first a seconda del confronto.
    default ADTConsList<E> updateAll(E element, E newElement) {
        // TODO implementare ricorsivamente
        return null;
    }

    /**
     * Attacca una lista data in fondo a questa lista.
     *
     * @param list la lista da attaccare in fondo
     * @return una lista che contiene gli elementi di questa lista seguita dagli
     * elementi di {@code list}.
     */
    // Caso base: lista vuota restituisce list. Passo: ricostruisco first davanti al risultato ricorsivo.
    default ADTConsList<E> append(ADTConsList<E> list) {
        // TODO implementare ricorsivamente
        return null;
    }

    /**
     * Genera una lista che ha gli stessi elementi di questa lista, ma
     * nell'ordine inverso.
     *
     * @return una lista uguale a questa, ma con tutti gli elementi in ordine
     * inverso.
     */
    // Versione ricorsiva didattica mediante append: semplice, ma costo quadratico nel numero di nodi.
    default ADTConsList<E> reverse() {
        // TODO implementare ricorsivamente
        return null;
    }

}
