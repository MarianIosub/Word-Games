import pa.proj.word_games.csv.CSVFileParser;
import pa.proj.word_games.database.DbConnection;
import pa.proj.word_games.database.models.Word;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Initializez conexiunea...");
            DbConnection dbConnection = DbConnection.getInstance();

            System.out.println("Citesc si parsez continutul fisierului CSV...");
            String filePath = System.getProperty("user.dir") + "\\data\\cuvinte_romana.csv";
            List<Word> listOfWords = CSVFileParser.readAndParseWordCSVFile(filePath);

            System.out.println("Adaug continutul in baza de date...");
            dbConnection.addWordsInDatabase(listOfWords);

            System.out.println("Continutul a fost adaugat in baza de date...");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            try
            {
                System.out.println("Fac commit si inchid conexiunea cu baza de date...");
                DbConnection.getInstance().commitAndClose();
            }
            catch(Exception ignored)
            { }
        }
    }
}
