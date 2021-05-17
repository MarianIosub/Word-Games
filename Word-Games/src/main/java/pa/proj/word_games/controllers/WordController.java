package pa.proj.word_games.controllers;

import pa.proj.word_games.managers.RepositoryManager;
import pa.proj.word_games.models.Word;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Primeste request-uri si, in functie de acestea, apeleaza functii din repository-uri.
 */
public class WordController {
    /**
     * Extrage un cuvant random din baza de date.
     * @return Cuvantul extras.
     * @throws IOException
     */
    private static Word extractWord() throws IOException {
        RepositoryManager repositoryManager = RepositoryManager.getInstance();
        Random random = new Random();
        int numberOfRepeats = random.nextInt(100);
        int generatedId = 0;

        while (numberOfRepeats != 0) {
            generatedId = random.nextInt(repositoryManager.getWordRepository().getNumberOfEntries().intValue());
            numberOfRepeats--;
        }
        return repositoryManager.getWordRepository().findById(generatedId);
    }

    /**
     * Elimina diacriticile din string-ul dat ca si parametru.
     * @param text Un string.
     * @return Parametrul string dat, dar fara diacritice.
     */
    public static String stringWithoutDiacritics(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");
        text = text.toLowerCase(Locale.ROOT);

        return text;
    }

    /**
     * Verifica daca un cuvant se afla in baza de date.
     * @param word Cuvantul care va fi verificat.
     * @return true, daca am gasit cuvantul in baza de date; false, atlfel
     * @throws IOException
     */
    public static boolean verifyWordExistence(String word) throws IOException {
        RepositoryManager repositoryManager = RepositoryManager.getInstance();
        return repositoryManager.getWordRepository().findByText(word) != null;
    }

    /**
     * Extrage un cuvant random, cu lungimea in intervalul [3,6], din baza de date.
     * @return Cuvantul extras.
     * @throws IOException
     */
    public static Word extractEasyWord() throws IOException {
        Word extractedWord = extractWord();
        if (extractedWord==null||extractedWord.getText()==null){
            return WordController.extractEasyWord();
        }
        while (extractedWord.getText().length() < 3 || extractedWord.getText().length() > 6) {
            extractedWord = extractWord();
            if (extractedWord==null||extractedWord.getText()==null){
                return WordController.extractEasyWord();
            }
        }

        return extractedWord;
    }

    /**
     * Extrage un cuvant random, cu lungimea in intervalul [7,9], din baza de date.
     * @return Cuvantul extras.
     * @throws IOException
     */
    public static Word extractMediumWord() throws IOException {
        Word extractedWord = extractWord();
        if (extractedWord==null||extractedWord.getText()==null){
            return WordController.extractEasyWord();
        }
        while (extractedWord.getText().length() < 6 || extractedWord.getText().length() > 9) {
            extractedWord = extractWord();
            if (extractedWord==null||extractedWord.getText()==null){
                return WordController.extractEasyWord();
            }
        }

        return extractedWord;
    }

    /**
     * Extrage un cuvant random, cu lungimea mai mare decat 9, din baza de date.
     * @return Cuvantul extras.
     * @throws IOException
     */
    public static Word extractHardWord() throws IOException {
        Word extractedWord = extractWord();
        if (extractedWord==null||extractedWord.getText()==null){
            return WordController.extractEasyWord();
        }
        while (extractedWord.getText().length() < 9) {
            extractedWord = extractWord();
            if (extractedWord==null||extractedWord.getText()==null){
                return WordController.extractEasyWord();
            }
        }

        return extractedWord;
    }

    /**
     * Verifica daca doua cuvinte sunt asemanatoare, chiar daca unul dintre ele sau ambele contin diacritici.
     * @param word1 Primul cuvant.
     * @param word2 Al doilea cuvant.
     * @return true, daca cuvintele date sunt asemanatoare; false, altfel
     */
    public static boolean compareWords(String word1, String word2) {
        word1 = stringWithoutDiacritics(word1);
        word2 = stringWithoutDiacritics(word2);
        return word1.equals(word2);
    }

    /**
     * Verifica daca exista in baza de date cuvinte care incep cu un anumit pattern.
     * @param pattern Pattern-ul.
     * @return true, daca exista cel putin un cuvant; false, altfel
     */
    public static boolean existsWordsWithStartPattern(String pattern) {
        RepositoryManager repositoryManager = RepositoryManager.getInstance();
        Integer result = repositoryManager.getWordRepository().findByStartPattern(pattern);
        if (result == 1)
            return true;

        if (pattern.equals(stringWithoutDiacritics(pattern)))
            return false;

        // Caut fara sa tin cont de diacritici
        result = repositoryManager.getWordRepository().findByStartPattern(stringWithoutDiacritics(pattern));
        return result == 1;
    }
}
