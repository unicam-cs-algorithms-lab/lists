# Due modelli di liste: ricorsione e collegamenti — ASDL

## Perché questa esercitazione?

La parola *lista* può indicare strutture molto diverse. L'esercitazione confronta:

1. **`ADTConsList<E>`**, una lista ricorsiva, **persistente e strutturalmente immutabile**, usata per imparare la ricorsione pura mediante metodi `default` di un'interfaccia.
2. **`SingleLinkedList<E>`**, una lista concatenata **mutabile** che implementa parzialmente `java.util.List<E>`, usata per imparare a modificare riferimenti, mantenere invarianti e implementare un `Iterator<E>` in una classe interna non statica.

**Le due parti sono obbligatorie, ma separate.** Si consiglia di completare prima la prima parte, eseguire i relativi test e poi passare alla seconda. I metodi di supporto già implementati vanno studiati, ma **non riscritti**.

## 1. Liste ricorsive `ADTConsList<E>`

### Rappresentazione e terminologia

Una lista è `EmptyList<E>` oppure `ConsList<E>(first, rest)` dove `rest` è un'altra `ADTConsList<E>`. Gli oggetti concreti implementano l'interfaccia; i metodi comuni vengono definiti come **`default`** nell'interfaccia stessa, così funzionano per entrambe le varianti.

```text
L = Cons(1, Cons(2, Cons(3, Empty)))

L       -> [1 | rest] -> [2 | rest] -> [3 | Empty]
L.cons(0) -> [0 | rest] --┘  (stessa coda, nessuna modifica di L)
```

I campi `first` e `rest` dei nodi sono `final`: un nodo esistente **non cambia struttura**. Una trasformazione restituisce **una nuova lista**, talvolta condividendo nodi con la precedente. Una variabile Java può comunque essere riassegnata a una nuova lista, e gli **oggetti contenuti** possono essere mutabili: non si parla quindi di immutabilità profonda.

Esempio già funzionante:

```java
ADTConsList<Integer> a = ADTConsList.<Integer>empty().cons(2).cons(1);
ADTConsList<Integer> b = a.cons(0);
System.out.println(a.print()); // 1 2
System.out.println(b.print()); // 0 1 2
System.out.println(b.rest() == a); // true: coda condivisa
```

### Esempi svolti e TODO

Nell'interfaccia sono **già implementati** `print`, `find` e `removeFirst`: sono esempi da leggere attentamente, perché mostrano **caso base**, **chiamata su `rest()`** e **ricostruzione tramite `cons()`**.

I **cinque TODO obbligatori** sono tutti nell'interfaccia `ADTConsList<E>`:

| Metodo | Comportamento | Idea da applicare |
|---|---|---|
| `removeAll(e)` | Elimina tutte le occorrenze | Saltare i nodi coincidenti; ricostruire gli altri |
| `updateFirst(e, nuovo)` | Sostituisce solo la prima occorrenza | Al primo match condividere il resto invariato |
| `updateAll(e, nuovo)` | Sostituisce tutte le occorrenze | Ricostruire ricorsivamente ogni nodo |
| `append(altra)` | Concatena due liste | Caso base: la lista corrente è vuota |
| `reverse()` | Inverte l'ordine | Usare ricorsione su `rest` e `append` |

**Vincoli:** implementare con ricorsione strutturale e funzioni pure, **senza `for`, `while`, array, `ArrayList` o modifica dei campi**; non modificare mai la lista ricevuta. Non perdere duplicati e preservare l'ordine degli elementi dove previsto. L'implementazione semplice di `reverse()` mediante `append()` può essere **O(n²)** ed è ammessa; una versione lineare con accumulatore è **facoltativa**.

**Percorso consigliato:** `removeAll` → `updateFirst` → `updateAll` → `append` → `reverse`. Controllare ogni metodo su lista vuota, lista di un solo elemento, elemento assente e duplicati. Verificare anche che la lista originale non cambi.

`ADTListsExamples` è un esempio guidato eseguibile **anche prima dei TODO**: scommentare i casi più avanzati solo dopo l'implementazione.

## 2. Lista semplicemente concatenata `SingleLinkedList<E>`

### Struttura e invarianti

```text
head -> [item | next] -> [item | next] -> [item | null] <- tail
```

Ogni `Node<E>` conserva un valore `item` e un riferimento modificabile `next`. La lista conserva `head`, `tail`, `size` e `numeroModifiche`.

Gli invarianti fondamentali sono:

- `size == 0` se e solo se `head == null` e `tail == null`;
- se la lista è non vuota, `tail.next == null`;
- da `head` sono raggiungibili tramite `next` **esattamente `size` nodi**;
- dopo **inserimenti e rimozioni** si incrementa `numeroModifiche`;
- `set` cambia solo `item` e **non** incrementa `numeroModifiche`.

La classe `Node` è **interna statica** perché un nodo non deve conoscere la lista esterna. `Itr` è una **classe interna non statica**: può accedere direttamente ai campi della specifica lista che l'ha creato.

### Metodi già implementati e commentati

Non sono TODO: costruttore, `size`, `isEmpty`, `contains`, `remove(Object)`, `clear`, `indexOf`, `lastIndexOf`, `toArray`, `equals`, `hashCode`, `iterator`, `listIterator` di sola lettura e le eccezioni delle operazioni opzionali non supportate.

**Leggere il codice fornito:** in particolare `remove(Object)` combina `indexOf` con `remove(int)`; `equals` confronta le liste per elementi, anche con `ArrayList`; `hashCode` segue il contratto di `List`.

### Cinque TODO sui nodi (obbligatori)

| Metodo | Obiettivo | Casi che devono funzionare |
|---|---|---|
| `add(E)` | Accodare in O(1) usando `tail` | Lista vuota / non vuota |
| `get(int)` | Raggiungere il nodo per indice | Indici validi / non validi |
| `set(int,E)` | Sostituire solo `item` | Restituire il vecchio elemento; iteratore valido |
| `add(int,E)` | Ricollegare i riferimenti dopo l'inserimento | Testa, mezzo, coda, lista vuota |
| `remove(int)` | Scollegare un nodo e restituire il valore | Nodo unico, testa, mezzo, coda |

**Eccezioni:** la lista non ammette elementi `null` (dove applicabile, `NullPointerException`); gli indici fuori intervallo producono `IndexOutOfBoundsException`. Per `add(index,e)` è valido anche `index == size`, mentre per `get`, `set` e `remove(index)` vale `0 <= index < size`. I duplicati sono ammessi.

**Progressione consigliata:** `add(E)` → `get` → `set` → `add(index,E)` → `remove(index)`. Prima di provare le operazioni complesse, verificare sempre il caso **lista vuota → un nodo → due nodi**. Non dimenticare di aggiornare `tail` quando si inserisce in una lista vuota o si elimina l'ultimo nodo.

### Due TODO dell'iteratore (obbligatori)

In `SingleLinkedList.Itr` completare **solo**:

- `hasNext()`: ritorna se il nodo `prossimo` esiste; deve verificare le modifiche strutturali;
- `next()`: verifica le modifiche, lancia `NoSuchElementException` a iterazione finita, altrimenti restituisce `prossimo.item` e avanza a `prossimo.next`.

I campi `prossimo` e `modificheAttese`, e il metodo `verificaModifiche()`, sono **già implementati**. Il controllo *fail-fast* usa `numeroModifiche` e non è un meccanismo di sincronizzazione tra thread. `Iterator.remove()` e un `ListIterator` modificabile **non sono richiesti**.

L'iteratore deve avanzare in **O(1) per chiamata**, senza richiamare `get(index)`. `for-each` e diversi metodi forniti dipendono da questa implementazione: è normale che prima di completarla alcuni test falliscano a cascata.

## 3. Quali test eseguire

Dalla directory principale del progetto Maven:

```bash
mvn test
```

Per concentrarsi sulla prima parte, in un terminale Maven:

```bash
mvn -Dtest=ADTConsListTest test
```

Per la lista concatenata, dopo aver completato i cinque metodi e l'iteratore:

```bash
mvn -Dtest=SingleLinkedListTest,SingleLinkedListContrattiTest,ListeObiettiviDidatticiTest test
```

I test JUnit sono documentati e rappresentano esempi di comportamento atteso. **Non occorre modificare i test**. Alcuni fallimenti dipendono da metodi ancora non implementati e non implicano necessariamente molti errori distinti. I metodi `equals`, `hashCode`, `toArray` e `remove(Object)` possono esercitare indirettamente l'iteratore e i metodi assegnati.

## 4. Approfondimenti facoltativi (non richiesti)

- Ricavare una versione **O(n)** di `reverse` con accumulatore ricorsivo.
- Confrontare complessità e costo della ricerca per indice fra `ArrayList` e `SingleLinkedList`.
- Disegnare gli stati di `head`, `tail` e `next` prima e dopo gli inserimenti/rimozioni.
- Ragionare sul perché il comportamento *fail-fast* non garantisca sicurezza in presenza di thread concorrenti.

## 5. Consegna

La consegna avviene su **Moodle**. Le classi che contengono i metodi da completare e quindi da consegnare sono **`ADTConsList.java`** e **`SingleLinkedList.java`**; non modificare package, interfacce pubbliche o firme dei metodi. La valutazione considera anche contratti ed eccezioni, non soltanto gli esempi di prova.

**Riepilogo:** cinque TODO ricorsivi + cinque TODO di gestione nodi + due TODO dell'iteratore = **12 metodi assegnati**, suddivisi in due blocchi didattici distinti. Tutto il resto è fornito e commentato.
