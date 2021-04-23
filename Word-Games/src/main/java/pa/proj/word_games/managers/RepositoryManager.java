package pa.proj.word_games.managers;

import pa.proj.word_games.repositories.WordRepository;

/**
 * Controleaza toate repository-urile.
 */
public class RepositoryManager
{
    private WordRepository wordRepository;

    private static RepositoryManager instance = null;

    private RepositoryManager()
    {
        wordRepository = WordRepository.getInstance();
    }

    public static RepositoryManager getInstance()
    {
        if(instance == null)
            instance = new RepositoryManager();
        return instance;
    }

    public WordRepository getWordRepository() {
        return wordRepository;
    }
}
