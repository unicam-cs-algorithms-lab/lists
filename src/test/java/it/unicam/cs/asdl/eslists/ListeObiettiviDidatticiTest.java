package it.unicam.cs.asdl.eslists;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

/**
 * Test mirati ai metodi assegnati agli studenti.
 * Sono volutamente brevi: i test completi delle altre classi verificano
 * ulteriori invarianti e sequenze di operazioni.
 */
class ListeObiettiviDidatticiTest {

    @Test
    void persistenzaECondivisioneDellaCoda() {
        ADTConsList<Integer> base = ADTConsList.<Integer>empty().cons(3).cons(2).cons(1);
        ADTConsList<Integer> cambiata = base.updateFirst(2, 8);
        assertEquals("1 2 3", base.print()); // la lista precedente non cambia
        assertEquals("1 8 3", cambiata.print());
        // Dopo il 2 sostituito la coda [3] si può riutilizzare.
        assertSame(base.rest().rest(), cambiata.rest().rest());
    }

    @Test
    void inserimentoInTestaSuListaVuotaMantieneTail() {
        SingleLinkedList<Integer> l = new SingleLinkedList<>();
        l.add(0, 10);
        l.add(20); // se tail non è aggiornato l'operazione può fallire
        assertEquals(Arrays.asList(10, 20), l);
        assertEquals(Integer.valueOf(20), l.remove(1));
        assertEquals(Integer.valueOf(10), l.remove(0));
        assertTrue(l.isEmpty());
    }

    @Test
    void iteratoreAvanzaEControllaModificheStrutturali() {
        SingleLinkedList<Integer> l = new SingleLinkedList<>();
        l.add(1); l.add(2);
        Iterator<Integer> it = l.iterator();
        assertEquals(Integer.valueOf(1), it.next());
        l.set(1, 3); // non modifica la struttura
        assertEquals(Integer.valueOf(3), it.next());
        assertFalse(it.hasNext());
        l.add(4); // modifica strutturale
        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }
}
