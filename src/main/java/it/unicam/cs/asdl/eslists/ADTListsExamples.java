package it.unicam.cs.asdl.eslists;

/**
 * Programma dimostrativo eseguibile anche prima di completare i TODO.
 * Gli esempi opzionali sono commentati perché dipendono dai metodi ricorsivi
 * che gli studenti devono ancora realizzare.
 */
public class ADTListsExamples {

    public static void main(String[] args) {
        ADTConsList<Integer> vuota = ADTConsList.empty();
        ADTConsList<Integer> originale = vuota.cons(3).cons(2).cons(1);
        // Costruisco una nuova testa: originale resta immutata.
        ADTConsList<Integer> estesa = originale.cons(0);
        System.out.println("Originale: " + originale.print()); // 1 2 3
        System.out.println("Estesa: " + estesa.print());       // 0 1 2 3
        System.out.println("Coda condivisa: " + (estesa.rest() == originale));
        System.out.println("Trova 2: " + originale.find(2));
        System.out.println("Senza il primo 2: " + originale.removeFirst(2).print());

        // Dopo avere completato i TODO in ADTConsList, provare anche:
        // System.out.println(originale.updateFirst(2, 7).print());
        // System.out.println(originale.updateAll(2, 7).print());
        // System.out.println(originale.removeAll(2).print());
        // System.out.println(originale.append(estesa).print());
        // System.out.println(originale.reverse().print());
    }
}
