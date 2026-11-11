package it.unicam.cs.asdl.eslists;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test integrativi degli invarianti di collegamento e del contratto java.util.List.
 * Alcuni test richiedono più metodi: una singola implementazione mancante può
 * far fallire più di un test nella versione studenti.
 */
class SingleLinkedListContrattiTest {
    @Test
    void addRemoveHeadTailAndSingleton() {
        SingleLinkedList<Integer> a = new SingleLinkedList<>();
        a.add(0, 10); // lista vuota: head e tail coincidono
        assertEquals(1, a.size());
        // Regressione: se add(0, 10) non aggiorna tail sulla lista vuota,
        // add(E) non riesce a concatenare il nodo successivo.
        a.add(20);
        assertEquals(Arrays.asList(10, 20), a);
        assertEquals(Integer.valueOf(20), a.remove(1));
        assertEquals(Integer.valueOf(10), a.remove(0));
        assertTrue(a.isEmpty());
        a.add(20); // dopo lo svuotamento la tail deve essere valida
        a.add(30);
        assertEquals(Arrays.asList(20, 30), a);
        a.add(0, 5);
        a.add(a.size(), 40);
        assertEquals(Arrays.asList(5, 20, 30, 40), a);
        assertEquals(Integer.valueOf(40), a.remove(a.size() - 1));
        assertEquals(Integer.valueOf(5), a.remove(0));
        assertEquals(Arrays.asList(20, 30), a);
    }

    @Test
    void equalsHashCodeDevonoEssereCompatibiliConOgniList() {
        List<String> standard = new ArrayList<>(Arrays.asList("a", "b", "b"));
        SingleLinkedList<String> ours = new SingleLinkedList<>();
        ours.add("a"); ours.add("b"); ours.add("b");
        // L'interfaccia List impone simmetria di equals anche con ArrayList.
        assertEquals(standard, ours);
        assertEquals(ours, standard);
        assertEquals(standard.hashCode(), ours.hashCode());
        assertNotEquals(Arrays.asList("a", "b"), ours);
    }

    @Test
    void iteratorFailFastSeCambiaLaStruttura() {
        SingleLinkedList<Integer> a = new SingleLinkedList<>();
        a.add(1); a.add(2);
        Iterator<Integer> it = a.iterator();
        assertEquals(Integer.valueOf(1), it.next());
        a.add(3);
        assertThrows(ConcurrentModificationException.class, it::next);
        // Un nuovo iteratore vede la lista aggiornata.
        assertEquals(Arrays.asList(1, 2, 3), a);
    }

    @Test
    void iteratorSetNonModificaLaStruttura() {
        SingleLinkedList<String> a = new SingleLinkedList<>();
        a.add("A"); a.add("B");
        Iterator<String> it = a.iterator();
        assertEquals("A", it.next());
        a.set(1, "C"); // il cursore resta valido: cambia item, non next
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void iteratorVuotoEConModificaPrimaDiNext() {
        SingleLinkedList<Integer> a = new SingleLinkedList<>();
        Iterator<Integer> it = a.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
        a.add(5);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void foreachArrayEVisitaDelleDuplicazioni() {
        SingleLinkedList<Integer> a = new SingleLinkedList<>();
        a.add(3); a.add(1); a.add(3);
        int sum = 0;
        for (int x : a) sum += x;
        assertEquals(7, sum);
        assertArrayEquals(new Object[]{3, 1, 3}, a.toArray());
        assertEquals(0, a.indexOf(3));
        assertEquals(2, a.lastIndexOf(3));
    }

    @Test
    void operazioniNonSupportateLanciandoEccezione() {
        SingleLinkedList<Integer> a = new SingleLinkedList<>();
        // ListIterator esiste per interoperabilità con altre implementazioni List.
        assertFalse(a.listIterator().hasNext());
        a.add(7);
        assertEquals(Integer.valueOf(7), a.listIterator().next());
        assertEquals(Integer.valueOf(7), a.listIterator(1).previous());
        assertThrows(UnsupportedOperationException.class, () -> a.listIterator().add(3));
        assertThrows(UnsupportedOperationException.class, () -> a.subList(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> a.toArray(new Integer[0]));
    }
    @Test
    void operazioniMisteConfrontateConArrayList() {
        // Confronto differenziale riproducibile: le due liste devono
        // contenere sempre esattamente gli stessi elementi nello stesso ordine.
        java.util.Random random = new java.util.Random(20261111L);
        SingleLinkedList<Integer> actual = new SingleLinkedList<>();
        ArrayList<Integer> expected = new ArrayList<>();
        for (int step = 0; step < 250; step++) {
            int action = random.nextInt(4);
            int value = random.nextInt(15);
            if (action == 0 || expected.isEmpty()) {
                actual.add(value);
                expected.add(value);
            } else if (action == 1) {
                int index = random.nextInt(expected.size() + 1);
                actual.add(index, value);
                expected.add(index, value);
            } else if (action == 2) {
                int index = random.nextInt(expected.size());
                assertEquals(expected.remove(index), actual.remove(index));
            } else {
                int index = random.nextInt(expected.size());
                assertEquals(expected.set(index, value), actual.set(index, value));
            }
            assertEquals(expected.size(), actual.size());
            assertEquals(expected, actual);
            assertArrayEquals(expected.toArray(), actual.toArray());
        }
    }

}
