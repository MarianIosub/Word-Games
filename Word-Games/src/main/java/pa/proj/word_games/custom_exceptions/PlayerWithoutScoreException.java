package pa.proj.word_games.custom_exceptions;

public class PlayerWithoutScoreException extends Exception{
    public PlayerWithoutScoreException(String message) {
        super(message);
    }
}
