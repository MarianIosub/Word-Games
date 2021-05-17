package pa.proj.word_games.repositories;

import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.Word;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Repository pentru clasa "Word" (respectiv, tabela "words").
 */
public class WordRepository implements AbstractRepository<Word>
{
    private static WordRepository instance = null;

    private WordRepository() { }

    public static WordRepository getInstance()
    {
        if(instance == null)
            instance = new WordRepository();
        return instance;
    }

    /**
     * Calculeaza numarul de intrari din tabela "words" (numarul de randuri).
     * @return Numarul de randuri din tabela "words".
     */
    public Long getNumberOfEntries()
    {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT COUNT(w) FROM Word w";
        TypedQuery<Long> typedQuery = entityManager.createQuery(query, Long.class);

        Long numberOfEntries = 0L;
        try
        {
            numberOfEntries = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        entityManager.close();
        return numberOfEntries;
    }

    /**
     * Cauta in baza de date cuvinte care incep cu un anumit pattern.
     * @param pattern Pattern-ul.
     * @return 1, daca exista cel putin un cuvant; 0, altfel
     */
    public Integer findByStartPattern(String pattern)
    {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT 1 FROM Word w WHERE SUBSTR(w.text, 1, 2) = ?1";
        TypedQuery<Integer> typedQuery = entityManager.createQuery(query, Integer.class);
        typedQuery.setParameter(1,  pattern);

        Integer result = 0;
        try
        {
            result = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        // Caut si fara diacritici
        if(result == 0)
        {
            typedQuery.setParameter(1,  WordController.stringWithoutDiacritics(pattern) + "%");
            try
            {
                result = typedQuery.getSingleResult();
            }
            catch(Exception ignored)
            { }
        }

        entityManager.close();
        return result;
    }

    @Override
    public Word findById(int id)
    {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT w FROM Word w WHERE w.id=?1";
        TypedQuery<Word> typedQuery = entityManager.createQuery(query, Word.class);
        typedQuery.setParameter(1, id);

        Word word = null;
        try
        {
            word = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        entityManager.close();
        return word;
    }

    @Override
    public Word findByText(String text)
    {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT w FROM Word w WHERE w.text=?1";
        TypedQuery<Word> typedQuery = entityManager.createQuery(query, Word.class);
        typedQuery.setParameter(1, text);

        Word word = null;
        try
        {
            word = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        // Caut fara diacritici
        if(word == null)
        {
            typedQuery.setParameter(1, WordController.stringWithoutDiacritics(text));
            try
            {
                word = typedQuery.getSingleResult();
            }
            catch(Exception ignored)
            { }
        }

        entityManager.close();
        return word;
    }

    @Override
    public Word save(Word word)
    {
        if(word == null)
            throw new NullPointerException();

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try
        {
            entityTransaction.begin();
            entityManager.persist(word);
            entityTransaction.commit();
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return word;
    }
}
