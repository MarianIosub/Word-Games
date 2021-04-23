package pa.proj.word_games.database;
import pa.proj.word_games.database.models.Word;

import java.sql.*;
import java.util.List;

public class DbConnection
{
    private Connection conn;
    private static DbConnection instance = null;

    /**
     * Constructor privat: Crearea unei conexiuni + dezactivarea auto commit-ului.
     * @throws SQLException
     */
    private DbConnection() throws SQLException
    {
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "usrwordgames", "pwdwordgames");
        conn.setAutoCommit(false);
    }

    public Connection getConnection() {
        return conn;
    }

    /**
     * Folosind o functie si un camp static, putem crea o singura instanta de tip DbConnection.
     * @return Instanta conexiunii.
     * @throws SQLException
     */
    public static DbConnection getInstance() throws SQLException
    {
        if (instance == null)
            instance = new DbConnection();

        if(instance.getConnection().isClosed())
            instance = new DbConnection();

        return instance;
    }

    /**
     * Adauga o lista de cuvinte in baza de date.
     * @param listOfWords Lista de cuvinte.
     */
    public void addWordsInDatabase(List<Word> listOfWords) throws SQLException
    {
        if(conn.isClosed())
            return;

        for(Word word : listOfWords)
        {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO words VALUES(" + word.getId() + ", '" + word.getText() + "')");
            statement.close();
        }
    }

    public void commitAndClose() throws SQLException
    {
        if(conn.isClosed())
            return;

        conn.commit();
        conn.close();
    }
}
