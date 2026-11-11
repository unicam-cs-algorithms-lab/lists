package it.unicam.cs.asdl.eslists;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la parte funzionale dell'esercitazione.
 * Ogni metodo deve restituire una NUOVA vista logica senza modificare
 * l'originale; condivisione delle code consentita e auspicabile.
 * Nei progetti con TODO questi test possono fallire fino al completamento.
 */
class ADTConsListTest {
    @SafeVarargs
    private static <E> ADTConsList<E> of(E... values) {
        // La costruzione procede da destra a sinistra, perché cons aggiunge in testa.
        ADTConsList<E> result = ADTConsList.empty();
        for (int i = values.length - 1; i >= 0; i--)
            result = result.cons(values[i]);
        return result;
    }

    @Test
    void vuotaEConsNonMutanoIlPassato() {
        ADTConsList<String> empty = ADTConsList.empty();
        ADTConsList<String> first = empty.cons("a");
        ADTConsList<String> second = first.cons("b");
        assertTrue(empty.isEmpty());
        assertEquals("a", first.first());
        assertEquals("b a", second.print());
        // I due oggetti condividono proprio la stessa coda (identità, non equals).
        assertSame(first, second.rest());
        assertThrows(IllegalStateException.class, empty::first);
        assertThrows(IllegalStateException.class, empty::rest);
    }

    @Test
    void ricorsioneBaseRicercaEStampa() {
        assertEquals("", ADTConsList.<Integer>empty().print());
        ADTConsList<Integer> l = of(1, 2, 3);
        assertEquals("1 2 3", l.print());
        assertTrue(l.find(2));
        assertFalse(l.find(8));
        assertFalse(ADTConsList.<Integer>empty().find(3));
    }

    @Test
    void removeFirstRicostruisceSoloIlPrefissoNecessario() {
        ADTConsList<Integer> l = of(1, 2, 3, 2);
        ADTConsList<Integer> r = l.removeFirst(2);
        assertEquals("1 3 2", r.print());
        assertEquals("1 2 3 2", l.print());
        assertSame(l.rest().rest(), r.rest()); // condivido la coda dopo il 2 eliminato
        assertEquals(l.print(), l.removeFirst(99).print());
    }

    @Test
    void removeAllTrattaVuotaTestaEConsecutivi() {
        ADTConsList<Integer> l = of(2, 2, 1, 2, 3, 2);
        assertEquals("1 3", l.removeAll(2).print());
        assertEquals("2 2 1 2 3 2", l.print());
        assertTrue(of(4, 4).removeAll(4).isEmpty());
        assertTrue(ADTConsList.<Integer>empty().removeAll(4).isEmpty());
    }

    @Test
    void updateFirstEAllRispettanoLaPersistenza() {
        ADTConsList<Integer> l = of(1, 2, 1, 3);
        assertEquals("9 2 1 3", l.updateFirst(1, 9).print());
        assertEquals("9 2 9 3", l.updateAll(1, 9).print());
        assertEquals("1 2 1 3", l.print());
        assertEquals("1 2 1 3", l.updateAll(99, 9).print());
        assertEquals("", ADTConsList.<Integer>empty().updateFirst(1, 2).print());
    }

    @Test
    void appendConservaEntrambeLeListe() {
        ADTConsList<Integer> a = of(1, 2);
        ADTConsList<Integer> b = of(3, 4);
        assertEquals("1 2 3 4", a.append(b).print());
        assertEquals("1 2", a.print());
        assertEquals("3 4", b.print());
        assertSame(b, ADTConsList.<Integer>empty().append(b));
        assertSame(b, a.append(b).rest().rest());
        assertEquals("1 2", a.append(ADTConsList.<Integer>empty()).print());
    }

    @Test
    void reversePreservaLaListaOriginale() {
        ADTConsList<Integer> l = of(1, 2, 3, 4);
        assertEquals("4 3 2 1", l.reverse().print());
        assertEquals("1 2 3 4", l.print());
        assertEquals("1", of(1).reverse().print());
        assertTrue(ADTConsList.<Integer>empty().reverse().isEmpty());
        assertEquals(l.print(), l.reverse().reverse().print());
    }

    @Test
    void elementiNullNonAmmessi() {
        assertThrows(NullPointerException.class, () -> ADTConsList.<String>empty().cons(null));
        assertThrows(NullPointerException.class, () -> new ConsList<>("x", null));
    }
}
