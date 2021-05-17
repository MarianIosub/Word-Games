package pa.proj.word_games.controllers;

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
}
