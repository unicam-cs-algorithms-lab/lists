package it.unicam.cs.asdl.eslists;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SingleLinkedListTest {

    // Verifica ricerca lineare e divieto dei valori null.
    @Test
    void testContains() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        assertThrows(NullPointerException.class, () -> list.add(null));
        assertFalse(list.contains(1));
        list.add(1);
        assertTrue(list.contains(1));
        assertFalse(list.contains(2));
    }

    // Verifica accodamento e aggiornamento coerente della dimensione.
    @Test
    void testAddE() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        assertThrows(NullPointerException.class, () -> list.add(null));

        assertTrue(list.add(1));
        assertTrue(list.size() == 1);
        assertTrue(list.contains(1));

        assertTrue(list.add(2));
        assertTrue(list.size() == 2);
        assertTrue(list.contains(2));

        assertTrue(list.add(3));
        assertTrue(list.size() == 3);
        assertTrue(list.contains(3));

        assertTrue(list.add(1));
        assertTrue(list.size() == 4);
    }

    // La rimozione per oggetto elimina soltanto la prima occorrenza.
    @Test
    void testRemoveObject() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        assertThrows(NullPointerException.class, () -> list.remove(null));

        assertFalse(list.remove("Pippo"));
        assertTrue(list.size() == 0);

        list.add("Pippo");
        assertTrue(list.remove("Pippo"));
        assertTrue(list.size() == 0);

        list.add("Pluto");
        list.add("Pluto");
        assertTrue(list.remove("Pluto"));

        assertTrue(list.size() == 1);
        assertTrue(list.contains("Pluto"));
    }

    // Dopo clear la lista è vuota e riutilizzabile.
    @Test
    void testClear() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        list.add("");
        list.add(" ");
        list.add("  ");
        list.add("   ");
        list.add("    ");

        list.clear();

        assertFalse(list.contains(""));
        assertFalse(list.contains(" "));
        assertFalse(list.contains("  "));
        assertFalse(list.contains("   "));
        assertFalse(list.contains("    "));

        assertTrue(list.isEmpty());
    }

    // Accesso per indice e controllo dei limiti.
    @Test
    void testGet() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));

        assertTrue(list.get(0).equals("1"));
        assertTrue(list.get(1).equals("2"));
        assertTrue(list.get(2).equals("3"));
        assertTrue(list.get(3).equals("4"));
        assertTrue(list.get(4).equals("5"));
    }

    // Set modifica item senza alterare il numero di nodi.
    @Test
    void testSet() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        list.add("0");
        assertThrows(NullPointerException.class, () -> list.set(0, null));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.set(1, "Pippo"));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.set(-1, "Pippo"));

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        assertEquals("0", list.set(0, "0"));
        assertEquals("2", list.set(2, "2.5"));
        assertEquals("4", list.set(4, "6"));

        assertTrue(list.get(0).equals("0"));
        assertTrue(list.get(1).equals("1"));
        assertTrue(list.get(2).equals("2.5"));
        assertTrue(list.get(3).equals("3"));
        assertTrue(list.get(4).equals("6"));

        assertTrue(list.size() == 6);
    }

    // Inserimento agli indici 0, intermedi e size.
    @Test
    void testAddIntE() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        assertThrows(NullPointerException.class, () -> list.add(0, null));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "1"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "2"));

        list.add("0");
        assertTrue(list.get(0).equals("0"));

        list.add("1");
        list.add(1, "1.5");
        assertTrue(list.get(1).equals("1.5"));
        assertTrue(list.get(2).equals("1"));

        list.add("2");
        list.add("3");
        list.add("4");
        assertTrue(list.get(3).equals("2"));
        assertTrue(list.get(4).equals("3"));
        assertTrue(list.get(5).equals("4"));

        list.add(6, "6");
        assertTrue(list.get(6).equals("6"));

        assertTrue(list.size() == 7);
    }

    // Rimozione degli estremi e di nodi interni.
    @Test
    void testRemoveInt() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));

        list.remove(0);
        assertTrue(list.get(0).equals("2"));
        assertTrue(list.get(1).equals("3"));
        assertTrue(list.get(2).equals("4"));
        assertTrue(list.get(3).equals("5"));

        list.remove(1);
        assertTrue(list.get(0).equals("2"));
        assertTrue(list.get(1).equals("4"));
        assertTrue(list.get(2).equals("5"));

        list.remove(2);
        assertTrue(list.get(0).equals("2"));
        assertTrue(list.get(1).equals("4"));

        assertTrue(list.size() == 2);
    }

    // Prima occorrenza, con duplicati.
    @Test
    void testIndexOf() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        assertThrows(NullPointerException.class, () -> list.indexOf(null));
        assertTrue(list.indexOf("0") == -1);

        list.add("1");
        list.add("2");
        list.add("2");
        list.add("3");

        assertTrue(list.indexOf("1") == 0);
        assertTrue(list.indexOf("2") == 1);
        assertTrue(list.indexOf("3") == 3);
        assertTrue(list.indexOf("0") == -1);
    }

    // Ultima occorrenza, con duplicati.
    @Test
    void testLastIndexOf() {
        SingleLinkedList<String> list = new SingleLinkedList<String>();
        assertThrows(NullPointerException.class, () -> list.lastIndexOf(null));
        assertTrue(list.lastIndexOf("0") == -1);

        list.add("1");
        list.add("2");
        list.add("2");
        list.add("3");

        assertTrue(list.lastIndexOf("1") == 0);
        assertTrue(list.lastIndexOf("2") == 2);
        assertTrue(list.lastIndexOf("3") == 3);
        assertTrue(list.lastIndexOf("0") == -1);
    }
    
    // Un iteratore visita gli elementi in ordine e reagisce a modifiche strutturali.
    @Test
    final void testIterator() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        assertThrows(NoSuchElementException.class, () -> sLL.iterator().next());
        {
            Iterator<Integer> itr = sLL.iterator();
            sLL.add(0);
            assertThrows(ConcurrentModificationException.class,
                    () -> itr.next());
        }
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        Iterator<Integer> itr = sLL.iterator();
        assertTrue(itr.hasNext());
        assertTrue(Integer.valueOf(0).equals(itr.next()));
        assertTrue(itr.hasNext());
        assertTrue(Integer.valueOf(1).equals(itr.next()));
        for (int i = 0; i < 7; i++) {
            assertTrue(itr.hasNext());
            assertTrue(Integer.valueOf(2).equals(itr.next()));
        }
        assertFalse(itr.hasNext());
    }

    // Ricerca su lista con elementi ripetuti.
    @Test
    final void testContains1() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        assertThrows(NullPointerException.class, () -> sLL.contains(null));
        sLL.add(0);
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        assertTrue(sLL.contains(1));
        assertFalse(sLL.contains(3));
    }

    // Rimozione assente/presente con duplicati.
    @Test
    final void testRemove() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        sLL.add(0);
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        assertFalse(sLL.remove((Object) 3));
        assertTrue(sLL.remove((Object) 2));
        assertEquals(8, sLL.size());
    }

    // Una lista svuotata è equivalente a una nuova lista vuota.
    @Test
    final void testClear1() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        sLL.add(0);
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        sLL.clear();
        assertEquals(0, sLL.size());
        assertEquals(new SingleLinkedList<>(), sLL);
    }

    // Accesso a posizioni in lista non banale.
    @Test
    final void testGet1() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        sLL.add(0);
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        assertTrue(Integer.valueOf(0).equals(sLL.get(0)));
        assertTrue(Integer.valueOf(1).equals(sLL.get(1)));
        assertTrue(Integer.valueOf(2).equals(sLL.get(3)));
        assertTrue(Integer.valueOf(2).equals(sLL.get(6)));
    }

    // Aggiornamento di un elemento senza cambiare size.
    @Test
    final void testSet1() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        sLL.add(0);
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        assertThrows(NullPointerException.class, () -> sLL.set(0, null));
        assertTrue(Integer.valueOf(2).equals(sLL.set(4, 3)));
        assertTrue(Integer.valueOf(9).equals(sLL.size()));
        assertTrue(Integer.valueOf(3).equals(sLL.get(4)));
    }

    // Inserimento/rimozione nello stesso indice e integrità della catena.
    @Test
    final void testAddAndRemoveAtIndex() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        sLL.add(0);
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        assertThrows(NullPointerException.class, () -> sLL.add(0, null));
        sLL.add(5, 5);
        assertTrue(Integer.valueOf(2).equals(sLL.get(4)));
        assertTrue(Integer.valueOf(5).equals(sLL.get(5)));
        assertTrue(Integer.valueOf(2).equals(sLL.get(6)));
        assertTrue(sLL.remove(5) == 5);
        assertTrue(Integer.valueOf(2).equals(sLL.get(4)));
        assertTrue(Integer.valueOf(2).equals(sLL.get(5)));
        assertTrue(Integer.valueOf(2).equals(sLL.get(6)));
    }

    // Ricerca di prima e ultima posizione con duplicati.
    @Test
    final void testIndexes() {
        SingleLinkedList<Integer> sLL = new SingleLinkedList<>();
        sLL.add(0);
        sLL.add(1);
        for (int i = 0; i < 7; i++) {
            sLL.add(2);
        }
        sLL.add(1);
        assertEquals(1, sLL.indexOf(1));
        assertEquals(9, sLL.lastIndexOf(1));
        assertEquals(-1, sLL.indexOf(23));
        assertEquals(-1, sLL.lastIndexOf(23));
    }
}
