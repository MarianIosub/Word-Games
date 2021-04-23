package pa.proj.word_games.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import pa.proj.word_games.database.models.Word;

import java.io.FileReader;
import java.io.IOException;
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
    public static List<Word> readAndParseWordCSVFile(String fileName) throws IOException, CsvException
    {
        List<String[]> content = readCSV(fileName);
        List<Word> outputList = new ArrayList<>();
        int id = 0;
        Word word;

        for(String[] line : content)
        {
            id++;
            word = new Word(id, line[0]);
            outputList.add(word);
        }

        return outputList;
    }
}
