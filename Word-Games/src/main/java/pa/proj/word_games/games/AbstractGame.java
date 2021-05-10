package pa.proj.word_games.games;

import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.util.List;

public interface AbstractGame {
    public void startGame() throws IOException;

    public void initialize(List<ClientThread> clientThreads);
}
