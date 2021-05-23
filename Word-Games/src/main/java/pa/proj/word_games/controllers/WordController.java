package pa.proj.word_games.controllers;

import pa.proj.word_games.custom_exceptions.WordNotExtractedException;
import pa.proj.word_games.models.Word;
import pa.proj.word_games.repositories.WordRepository;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;

public class WordController {
    /**
     * Extrage un cuvant random din baza de date.
     *
     * @return Cuvantul extras.
     * @throws IOException
     */
    private static Word extractWord() throws IOException {
        Random random = new Random();
        int numberOfRepeats = random.nextInt(100);
        int generatedId = 0;

        while (numberOfRepeats != 0) {
            generatedId = random.nextInt(WordRepository.getInstance().getNumberOfEntries().intValue());
            numberOfRepeats--;
        }
        try {
            if (WordRepository.getInstance().findById(generatedId) == null) {

                throw new WordNotExtractedException("Word cannot be extracted from database!");
            }
            return WordRepository.getInstance().findById(generatedId);
        } catch (WordNotExtractedException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    /**
     * Elimina diacriticile din string-ul dat ca si parametru.
     *
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
     *
     * @param word Cuvantul care va fi verificat.
     * @return true, daca am gasit cuvantul in baza de date; false, atlfel
     * @throws IOException
     */
    public static boolean verifyWordExistence(String word) throws IOException {
        return WordRepository.getInstance().findByText(word) != null;
    }

    /**
     * Extrage un cuvant random, cu lungimea in intervalul [3,6], din baza de date.
     *
     * @return Cuvantul extras.
     * @throws IOException
     */
    public static Word extractEasyWord() throws IOException {
        Word extractedWord = extractWord();
        if (extractedWord == null || extractedWord.getText() == null) {
            return WordController.extractEasyWord();
        }
        while (extractedWord.getText().length() < 3 || extractedWord.getText().length() > 6) {
            extractedWord = extractWord();
            if (extractedWord == null || extractedWord.getText() == null) {
                return WordController.extractEasyWord();
            }
        }
        try {
            if (extractedWord == null) {

                throw new WordNotExtractedException("Niciun cuvant usor nu a fost extras din baza de date!");
            }
            return extractedWord;
        } catch (WordNotExtractedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Extrage un cuvant random, cu lungimea in intervalul [7,9], din baza de date.
     *
     * @return Cuvantul extras.
     * @throws IOException
     */
    public static Word extractMediumWord() throws IOException {
        Word extractedWord = extractWord();
        if (extractedWord == null || extractedWord.getText() == null) {
            return WordController.extractEasyWord();
        }
        while (extractedWord.getText().length() < 6 || extractedWord.getText().length() > 9) {
            extractedWord = extractWord();
            if (extractedWord == null || extractedWord.getText() == null) {
                return WordController.extractEasyWord();
            }
        }

        try {
            if (extractedWord == null) {

                throw new WordNotExtractedException("Niciun cuvant mediu nu a fost extras din baza de date!");
            }
            return extractedWord;
        } catch (WordNotExtractedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Extrage un cuvant random, cu lungimea mai mare decat 9, din baza de date.
     *
     * @return Cuvantul extras.
     * @throws IOException
     */
    public static Word extractHardWord() throws IOException {
        Word extractedWord = extractWord();
        if (extractedWord == null || extractedWord.getText() == null) {
            return WordController.extractEasyWord();
        }
        while (extractedWord.getText().length() < 9) {
            extractedWord = extractWord();
            if (extractedWord == null || extractedWord.getText() == null) {
                return WordController.extractEasyWord();
            }
        }

        try {
            if (extractedWord == null) {

                throw new WordNotExtractedException("Niciun cuvant greu nu a fost extras din baza de date!");
            }
            return extractedWord;
        } catch (WordNotExtractedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Verifica daca doua cuvinte sunt asemanatoare, chiar daca unul dintre ele sau ambele contin diacritici.
     *
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
     *
     * @param pattern Pattern-ul.
     * @return true, daca exista cel putin un cuvant; false, altfel
     */
    public static boolean existsWordsWithStartPattern(String pattern) {
        Integer result = WordRepository.getInstance().findByStartPattern(pattern);
        if (result == 1)
            return true;

        if (pattern.equals(stringWithoutDiacritics(pattern)))
            return false;

        // Caut fara sa tin cont de diacritici
        result = WordRepository.getInstance().findByStartPattern(stringWithoutDiacritics(pattern));
        return result == 1;
    }
}
