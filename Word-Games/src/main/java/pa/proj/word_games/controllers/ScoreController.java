package pa.proj.word_games.controllers;

import pa.proj.word_games.models.FazanScore;
import pa.proj.word_games.models.HangmanScore;
import pa.proj.word_games.models.TypeFastScore;
import pa.proj.word_games.repositories.FazanScoreRepository;
import pa.proj.word_games.repositories.HangmanScoreRepository;
import pa.proj.word_games.repositories.TypeFastScoreRepository;

import java.util.HashMap;
import java.util.Map;

public class ScoreController {
    /**
     * Preia din baza de date scorurile unui utilizator.
     * @param userId Id-ul utilizatorului.
     * @return Un map, cheia fiind numele jocului, iar valoarea fiind scorul utilizatorului la acel joc.
     */
    public static Map<String, Integer> getScoresByUserId(int userId) {
        Map<String, Integer> playerScores = new HashMap<>();

        playerScores.put("fazan",
                FazanScoreRepository.getInstance().findById(userId) != null ?
                        FazanScoreRepository.getInstance().findById(userId).getScore() :
                        0
        );
        playerScores.put("hangman",
                HangmanScoreRepository.getInstance().findById(userId) != null ?
                        HangmanScoreRepository.getInstance().findById(userId).getScore() :
                        0
        );
        playerScores.put("typefast",
                TypeFastScoreRepository.getInstance().findById(userId) != null ?
                        TypeFastScoreRepository.getInstance().findById(userId).getScore() :
                        0
        );

        return playerScores;
    }

    /**
     * Preia din baza de date scorul din jocul TypeFast al unui utilizator.
     * @param userId Id-ul utilizatorului.
     * @return Scorul, de tipul TypeFastScore; null, daca utilizatorul nu are un scor inregistrat in baza de date pentru acest joc.
     */
    public static TypeFastScore getTypeFastScoreByUserId(int userId) {
        return TypeFastScoreRepository.getInstance().findById(userId);
    }

    /**
     * @return Urmatorul id disponibil pentru un obiect de tipul TypeFastScore.
     */
    public static int getNextTypeFastScoreAvailableId() {
        return TypeFastScoreRepository.getInstance().getNextAvailableId();
    }

    /**
     * Salveaza in baza de date o instanta de tipul TypeFastScore.
     * @param typeFastScore Instanta care va fi salvata.
     * @return Instanta salvata, in caz de succes; NULL, in caz de esuare
     */
    public static TypeFastScore saveTypeFastScore(TypeFastScore typeFastScore) {
        return TypeFastScoreRepository.getInstance().save(typeFastScore);
    }

    /**
     * Actualizeaza un obiect de tipul TypeFastScore din baza de date (obiectul este deja salvat).
     * @param typeFastScore Obiectul salvat, cu noile date, care vor fi actualizate in baza de date.
     * @return Obiectul actualizat, in caz de succes; NULL, in caz de esuare
     */
    public static TypeFastScore updateTypeFastScore(TypeFastScore typeFastScore) {
        return TypeFastScoreRepository.getInstance().update(typeFastScore);
    }

    /**
     * Preia din baza de date scorul din jocul HangMan al unui utilizator.
     * @param userId Id-ul utilizatorului.
     * @return Scorul, de tipul HangmanScore; null, daca utilizatorul nu are un scor inregistrat in baza de date pentru acest joc.
     */
    public static HangmanScore getHangmanScoreByUserId(int userId) {
        return HangmanScoreRepository.getInstance().findById(userId);
    }

    /**
     * @return Urmatorul id disponibil pentru un obiect de tipul HangManScore.
     */
    public static int getNextHangmanScoreAvailableId() {
        return HangmanScoreRepository.getInstance().getNextAvailableId();
    }

    /**
     * Salveaza in baza de date o instanta de tipul HangmanScore.
     * @param hangmanScore Instanta care va fi salvata.
     * @return Instanta salvata, in caz de succes; NULL, in caz de esuare
     */
    public static HangmanScore saveHangmanScore(HangmanScore hangmanScore) {
        return HangmanScoreRepository.getInstance().save(hangmanScore);
    }

    /**
     * Actualizeaza un obiect de tipul HangmanScore din baza de date (obiectul este deja salvat).
     * @param hangmanScore Obiectul salvat, cu noile date, care vor fi actualizate in baza de date.
     * @return Obiectul actualizat, in caz de succes; NULL, in caz de esuare
     */
    public static HangmanScore updateHangmanScore(HangmanScore hangmanScore) {
        return HangmanScoreRepository.getInstance().update(hangmanScore);
    }

    /**
     * Preia din baza de date scorul din jocul Fazan al unui utilizator.
     * @param userId Id-ul utilizatorului.
     * @return Scorul, de tipul FazanScore; null, daca utilizatorul nu are un scor inregistrat in baza de date pentru acest joc.
     */
    public static FazanScore getFazanScoreByUserId(int userId) {
        return FazanScoreRepository.getInstance().findById(userId);
    }

    /**
     * @return Urmatorul id disponibil pentru un obiect de tipul FazanScore.
     */
    public static int getNextFazanScoreAvailableId() {
        return FazanScoreRepository.getInstance().getNextAvailableId();
    }

    /**
     * Salveaza in baza de date o instanta de tipul FazanScore.
     * @param fazanScore Instanta care va fi salvata.
     * @return Instanta salvata, in caz de succes; NULL, in caz de esuare
     */
    public static FazanScore saveFazanScore(FazanScore fazanScore) {
        return FazanScoreRepository.getInstance().save(fazanScore);
    }

    /**
     * Actualizeaza un obiect de tipul FazanScore din baza de date (obiectul este deja salvat).
     * @param fazanScore Obiectul salvat, cu noile date, care vor fi actualizate in baza de date.
     * @return Obiectul actualizat, in caz de succes; NULL, in caz de esuare
     */
    public static FazanScore updateFazanScore(FazanScore fazanScore) {
        return FazanScoreRepository.getInstance().update(fazanScore);
    }
}
