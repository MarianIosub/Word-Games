# Word-Games
Word Games using Java.

Word games este un set de jocuri bazate pe cuvinte in limba romana, care cuprinde: Fazan, Spanzuratoarea, Type-Fast.

Intructiuni de folosire a aplicatiei :
- Se creaza o baza de date cu fisierul gasit in WORD_GAMES_AUXILIARY
- Se executa fisierul de crearea a tabelelor  din WORD_GAMES_AUXILIARY
- Se executa WORD_GAMES_AUXILIARY pentru popularea baza de date cu cuvintele din csv
- Odata finalizata popularea se executa server-ul, programul WORD-GAMES
- Pentru fiecare client in parte se va rula programul WORD-GAMES-CLIENT care se conecteaza la server

La baza jocurilor stau urmatoarele tehnologii:
- Server-client, intrucat un jucator ce vrea sa participe la orice joc reprezinta un client care se conecteaza la server.
- Login/register, un client isi poate crea cont pentru a-si salva scorurile personale si pentru a se identifica cu ceilalti jucatori.
- Creare de lobby: orice joc (fie de o persoana, fie de mai multe) trebuie jucat in un anumit lobby
- Conectare la lobby: un client ce doreste sa joace FAZAN cu alti prieteni se poate conecta la un lobby facut de unul dintre ei
- Logica jocurilor se desfasoara pe partea de server, clientul doar interactionand cu mutarile celorlalti jucatori(FAZAN) sau cu regulile jocului in sine(Spanzuratoare, Type-Fast)
- La finalul unui joc se acorda puncte jucatorilor care: FAZAN - celor care nu o pierdut, Spanzuratoare - celui care a reusit sa ghiceasca cuvantul, Type-Fast - daca a scris mai multe cuvinte corecte decat gresite

Descrierea jocurilor:
    Fazan: - Se poate juca in minim 2 playeri
           - Sever-ul se ocupa de ordinea introducerii cuvintelor de catre playeri
           - Cuvintele introduce sunt verificate in baza de date daca acestea exista
           - Regulile jocului sunt cele specifice FAZAN (5 vieti, inchiderea, etc.)
   
   Spanzuratoarea: - Player-ul isi selecteaza nivelul de joc dorit, iar in functie de aceta se stabileste lungimea cuvantului pe care trebuie sa il ghiceasca
                   - Player-ul introduce litere pe rand pana ramane fara vieti sau ghiceste cuvantul
                   - La o litera introdusa ce nu apartine cuvantului, i se scade o viata
                   - La una introdusa corect se afiseaza cuvantul ghicit pana la momentul respectiv
                   
   Type-Fast: - Player-ului i se afiseaza o lista cu 30 cuvinte independent logic pe care trebuie sa le scrie in ordinea data
              - Acesta are timp 60 de secunde pentru a introduce cat mai multe cuvinte posibil
              - La final acestuia i se afiseaza o statistica bazata pe numarul de cuvinte introduse, numarul celor corecte si gresite, cat si rapoarte.

Tehnologii Java folosite:
- Objects and Classes
- Interfaces
- Generics
- Collections
- Java Stream API
- Exceptions + Custom Exceptions
- Input/Output Streams. Working with Files
- Concurrency: Threads and Locks
- Java Persistence API (JPA)
- Network Programming
- GUI (nefinalizat si neintegrat)

Contribuitori:
- Marian: - Utilitare pentru extragerea si folosirea cuvintelor din baza de date (Verificarea corectudinii cuvintelor, extragerea lor, etc.)
          - Spanzuratorea (logica jocului + aplicara sa in retea)
          - Typefast Game (logica jocului + aplicarea sa in retea)
          - Custom Exceptions la nivel de server
          - Streams unde s-au putut utiliza

- Alin: - Crearea bazei de date ( cuvinte, useri )
        - Server ( conectarea userilor, comunicarea client - server)
        - Fazan ( logica jocului + aplicarea sa in retea )
        - Lobby-uri ( creare, conectare, stergere)

- Impreuna (via CodeWithMe): - Code refactoring
                             - Documentarea cu JavaDoc
                             - Gasirea unui CSV cu cuvinte in romana si popularea cu acestea a unei baze de date
                             - Login/Register user
                             - Incercarea de a introduce o interfata grafica pentru utilizator
