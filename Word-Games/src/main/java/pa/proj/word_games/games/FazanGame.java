package pa.proj.word_games.games;

import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.managers.EntityFactoryManager;

import java.io.IOException;
import java.util.*;

public class FazanGame
{
    /**
     * Retine player-ul a carui tura este in acest moment.
     */
    private int playerTurnIndex;

    /**
     * Retine lista jucatorilor.
     */
    private List<Player> listOfPlayers;

    /**
     * Retine scorul unui jucator.
     */
    private Map<Player, Integer> playerScores;

    /**
     * Retine numarul turei.
     */
    private int turnNumber;

    /**
     * Verifica daca un cuvant este valid (daca apare in baza de date).
     * @param word Cuvantul verificat.
     * @return true, daca este valid; false, altfel
     */
    private boolean isValidWord(String word) throws IOException
    {
        // Verific lungimea
        if(word.length() < 3)
            return false;

        // Verific daca incepe cu doua consoane
        if(!"aeiouăîâ".contains(word.subSequence(0, 1)) && !"aeiouăîâ".contains(word.subSequence(1, 2)))
            return false;

        return WordController.verifyWordExistence(word);
    }

    /**
     * Verifica regula fazanului: noul cuvant trebuie sa inceapa cu ultimele 2 litere ale cuvantului anterior.
     * @param previousWord Cuvantul anterior
     * @param actualWord Cuvantul actual.
     * @return true, daca regula este respectata; false, altfel
     */
    private boolean verifyFazanRule(String previousWord, String actualWord)
    {
        if(previousWord.length() == 1)
            return previousWord.charAt(0) == actualWord.charAt(0);

        previousWord = WordController.stringWithoutDiacritics(previousWord);
        actualWord = WordController.stringWithoutDiacritics(actualWord);
        return previousWord.substring(previousWord.length() - 2).contentEquals(actualWord.subSequence(0, 2));
    }

    /**
     * Verifica daca s-a terminat jocul: nu exista niciun cuvant care sa inceapa cu ultimele doua litere ale cuvantului actual.
     * @param actualWord Cuvantul actual
     * @return true, daca jocul s-a terminat; false, altfel
     */
    private boolean verifyEndOfFazanGame(String actualWord)
    {
        // Verific daca se termina cu doua consoane
        if(!"aeiouăîâ".contains(actualWord.subSequence(actualWord.length()-2, actualWord.length()-1)) &&
                !"aeiouăîâ".contains(actualWord.subSequence(actualWord.length()-1, actualWord.length())))
            return true;

        return !WordController.existsWordsWithStartPattern(actualWord.substring(actualWord.length() - 2));
    }

    /**
     * Trec la jucatorul urmator din lista de playeri.
     */
    private void nextPlayerTurn()
    {
        playerTurnIndex = (playerTurnIndex + 1) % listOfPlayers.size();
    }

    /**
     * Alege un caracter random din alfabetul latin.
     * @return Caracterul ales.
     */
    private char generateRandomAlphabetCharacter() {
        String alphabet = "abcdefghijklmnoprstuv";
        Random random = new Random();
        return alphabet.charAt(random.nextInt(alphabet.length()));
    }

    /**
     * Se joaca o runda din jocul Fazan.
     * @param roundNumber Numarul rundei actuale.
     * @throws IOException
     * @return Index-ul jucatorului care a pierdut.
     */
    private int gameSession(int roundNumber, int startingPlayerIndex) throws IOException
    {
        // Initializarea variabilelor
        System.out.println("Fazan Game - Runda " + roundNumber);

        playerTurnIndex = startingPlayerIndex;
        turnNumber = 0;

        List<String> usedWords = new ArrayList<>();
        String previousWord = String.valueOf(generateRandomAlphabetCharacter());
        String actualWord;

        Scanner scanner = new Scanner(System.in);

        // Jocul
        do {
            turnNumber++;

            System.out.println("\nTura jucatorului " + listOfPlayers.get(playerTurnIndex).getName());
            if(previousWord.length() == 1)
                System.out.println("Caracterul cu care trebuie sa inceapa cuvantul: " + previousWord);
            else
                System.out.println("Caracterele cu care trebuie sa inceapa cuvantul: " + previousWord.substring(previousWord.length() - 2));
            System.out.print(" >> ");
            actualWord = scanner.next();

            while(true)
            {
                // Verific daca cuvantul este valid
                if(actualWord.length() < 3 || !isValidWord(actualWord))
                {
                    System.out.println("Cuvantul nu este valid!");
                    System.out.print(" >> ");
                    actualWord = scanner.next();
                }
                // Verific daca cuvantul respecta regula fazanului
                else if(!verifyFazanRule(previousWord, actualWord))
                {
                    System.out.println("Cuvantul nu respecta regula fazanului!");
                    System.out.print(" >> ");
                    actualWord = scanner.next();
                }
                // Verific daca a mai fost folosit cuvantul in aceasta runda
                else if(usedWords.contains(actualWord.toLowerCase(Locale.ROOT)))
                {
                    System.out.println("Cuvantul a mai fost folosit!");
                    System.out.print(" >> ");
                    actualWord = scanner.next();
                }
                // Verific daca este cuvant care inchide si daca are voie sa inchida
                else if((verifyEndOfFazanGame(actualWord)) && (turnNumber <= listOfPlayers.size()))
                {
                    System.out.println("Nu ai voie sa inchizi jocul atat de devreme!");
                    System.out.print(" >> ");
                    actualWord = scanner.next();
                }
                // Cuvant bun
                else
                    break;
            }

            usedWords.add(actualWord.toLowerCase(Locale.ROOT));
            previousWord = actualWord;
            nextPlayerTurn();

        } while(!verifyEndOfFazanGame(actualWord));

        // Afisarea pierzatorului si updatarea scorului acestuia
        System.out.println("In runda " + roundNumber + " a pierdut jucatorul " + listOfPlayers.get(playerTurnIndex).getName() + ".");
        playerScores.replace(listOfPlayers.get(playerTurnIndex),
                playerScores.get(listOfPlayers.get(playerTurnIndex)) - 1);
        return playerTurnIndex;
    }

    /**
     * Verifica daca exista un jucator care a pierdut (are scorul 0).
     * @return true, daca exista; false, altfel
     */
    private boolean existsPlayerWhoLost()
    {
        for(Player player : listOfPlayers)
        {
            if(playerScores.get(player) == 0)
                return true;
        }
        return false;
    }

    /**
     * Afiseaza pe ercan scorul fiecarui jucator.
     */
    private void printPlayerScores()
    {
        System.out.println("Scorul fiecarui jucator: ");
        for(Player player : listOfPlayers)
        {
            System.out.print("\t" + player.getName() + " - ");
            switch(playerScores.get(player))
            {
                case 5:
                {
                    System.out.print("");
                    break;
                }
                case 4:
                {
                    System.out.print("F");
                    break;
                }
                case 3:
                {
                    System.out.print("FA");
                    break;
                }
                case 2:
                {
                    System.out.print("FAZ");
                    break;
                }
                case 1:
                {
                    System.out.print("FAZA");
                    break;
                }
                case 0:
                {
                    System.out.print("FAZAN");
                    break;
                }
            }
            System.out.print("\n");
        }
    }

    public FazanGame()
    {
        EntityFactoryManager.getInstance();
        listOfPlayers = new ArrayList<>();
        playerScores = new HashMap<>();
    }

    /**
     * Adauga un player in lista de playeri.
     * @param player Player-ul care va fi adaugat.
     * @return true, daca acesta a fost adaugat; false, atlfel
     */
    public boolean addPlayer(Player player)
    {
        if(listOfPlayers.contains(player))
            return false;

        listOfPlayers.add(player);
        playerScores.put(player, 0);
        return true;
    }

    /**
     * Sterge un player din lista de playeri.
     * @param player Player-ul care va fi sters.
     * @return true, daca acesta a fost sters; false, atlfel
     */
    public boolean removePlayer(Player player)
    {
        if(listOfPlayers.contains(player))
        {
            listOfPlayers.remove(player);
            playerScores.remove(player);
            return true;
        }

        return false;
    }

    /**
     * Incepe jocul.
     */
    public void startGame() throws IOException
    {
        if(listOfPlayers.size() < 2)
        {
            System.out.println("Jocul de Fazan nu poate incepe deoarece nu sunt destui jucatori.");
            return;
        }

        // Initializez scorurile
        for(Player player : listOfPlayers)
        {
            playerScores.replace(player, 5);
        }

        // Jucarea rundelor
        int roundNumber = 1;
        int startingPlayerIndex = 0;
        while(!existsPlayerWhoLost())
        {
            startingPlayerIndex = gameSession(roundNumber, startingPlayerIndex);

            // Afisarea scorurilor fiecarui player dupa fiecare runda
            printPlayerScores();

            roundNumber++;
        }

        // Afisarea player-ului pierzator
        System.out.println(listOfPlayers.get(startingPlayerIndex).getName() + " a pierdut jocul de Fazan!");
    }

    /* TODO: play cu alti jucatori (in retea) - setSoTimeout pentru timeout la introducerea cuvantului */

    /* TODO: play vs un computer (cand este un singur player)
    *       basically player-ul incepe, computerul interogheaza baza de date pentru cuvinte bune si ia unul random*/
}
