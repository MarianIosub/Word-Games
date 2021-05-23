package pa.proj.word_games.controllers;

import pa.proj.word_games.models.FazanScore;
import pa.proj.word_games.models.HangmanScore;
import pa.proj.word_games.models.TypeFastScore;
import pa.proj.word_games.models.User;
import pa.proj.word_games.repositories.FazanScoreRepository;
import pa.proj.word_games.repositories.HangmanScoreRepository;
import pa.proj.word_games.repositories.TypeFastScoreRepository;
import pa.proj.word_games.repositories.UserRepository;

public class UserController {
    /**
     * Adauga un nou utilizator in baza de date.
     *
     * @param username Username-ul acestuia.
     * @param password Parola acestuia.
     *                 <p>@return 1, daca utilizatorul a fost adaugat; 0, daca username-ul este deja folosit; -1 daca username-ul sau parola
     *                 nu au cel putin 5 caractere</p>
     */
    public static int registerUser(String username, String password) {
        if (username.length() < 5 || password.length() < 5) {
            return -1;
        }

        if (UserRepository.getInstance().existsUserWithCertainUsername(username)) {
            return 0;
        }

        User newUser = new User(
                UserRepository.getInstance().getNextAvailableId(), username, password
        );
        UserRepository.getInstance().save(newUser);
        FazanScoreRepository.getInstance().save(new FazanScore(
                FazanScoreRepository.getInstance().getNextAvailableId(), newUser.getId(), 0)
        );
        HangmanScoreRepository.getInstance().save(new HangmanScore(
                HangmanScoreRepository.getInstance().getNextAvailableId(), newUser.getId(), 0)
        );
        TypeFastScoreRepository.getInstance().save(new TypeFastScore(
                TypeFastScoreRepository.getInstance().getNextAvailableId(), newUser.getId(), 0
        ));
        return 1;
    }

    /**
     * Face o cautare in baza de date dupa un anumit username si o naumita parola.
     *
     * @param username Username-ul dupa care se face cautarea.
     * @param password Parola dupa care se face cautarea.
     * @return User-ul gasit, daca exista unul cu acest username si aceasta parola; NULL, altfel
     */
    public static User getUserByCredentials(String username, String password) {
        if (username.length() < 5 || password.length() < 5) {
            return null;
        }

        return UserRepository.getInstance().findByCredentials(username, password);
    }
}
