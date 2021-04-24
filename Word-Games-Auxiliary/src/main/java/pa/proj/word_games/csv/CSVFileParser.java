package pa.proj.word_games.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import pa.proj.word_games.database.models.Word;

import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

public class CSVFileParser
{
    /**
     * Citeste datele dintr-un fisier CSV.
     * @param fileName Numele fisierului CSV.
     * @return Lista cu randurile din fisier.
     */
    private static List<String[]> readCSV(String fileName) throws IOException, CsvException
    {
        CSVReader reader = new CSVReader(new FileReader(fileName));
        reader.readNext();

        return reader.readAll();
    }

    /**
     * Citste si parseaza datele dintr-un fisier CSV ce contine date despre cuvinte.
     * @param fileName Numele fisierului CSV.
     * @return Lista de cuvinte.
     */
    public static List<String> readAndParseWordCSVFile(String fileName) throws IOException, CsvException
    {
        List<String[]> content = readCSV(fileName);
        List<String> outputList = new ArrayList<>();

        for(String[] line : content)
        {
            if(!outputList.contains(line[0].toLowerCase(Locale.ROOT)))
                outputList.add(line[0].toLowerCase(Locale.ROOT));

            if(!outputList.contains(normalizeWord(line[0].toLowerCase(Locale.ROOT))))
                outputList.add(normalizeWord(line[0].toLowerCase(Locale.ROOT)));
        }

        return outputList;
    }

    private static String normalizeWord(String word)
    {
        word = Normalizer.normalize(word, Normalizer.Form.NFD);
        word = word.replaceAll("\\p{M}", "");

        return word;
    }

    /**
     * Parseaza lista de cuvinte si elimina cuvintele care nu sunt okay.
     * @param listOfWordsString Lista de cuvinte in format string.
     * @return Noua lista de cuvinte, in format Word.
     */
    public static List<Word> repairCSV(List<String> listOfWordsString)
    {
        List<Word> outputList = new ArrayList<>();
        int id = 0;
        Word word;

        for(String stringWord : listOfWordsString)
        {

            // Verific daca are sufix
            if((normalizeWord(stringWord).endsWith("-ul")) &&
                    (normalizeWord(stringWord).endsWith("-uri")) && (normalizeWord(stringWord).endsWith("-lea"))
                    && (normalizeWord(stringWord).endsWith("-ist")))
            {
                System.out.println(stringWord);
                continue;
            }

            if(stringWord.length() < 3)
                continue;

            // Verific daca nu este un cuvant de genul "nceput" (care provine de la expresii de genul "a-nceput")
            if((normalizeWord(stringWord).startsWith("n")  && (!"aeiouăîâ".contains(normalizeWord(stringWord).subSequence(1, 2))))
                    || ((normalizeWord(stringWord).startsWith("m")) && (!"aeiouăîâ".contains(normalizeWord(stringWord).subSequence(1, 2)))))
            {
                boolean ok = true;
                for(String str : listOfWordsString)
                {
                    if(normalizeWord(str).equals("i" + normalizeWord(stringWord)) ||
                            normalizeWord(str).equals("î" + normalizeWord(stringWord)))
                    {
                        System.out.println(str + " - " + stringWord);
                        ok = false; break;
                    }
                }

                if(!ok) continue;
            }

            id++;
            word = new Word(id, stringWord);
            outputList.add(word);
        }

        return outputList;
    }
}
