package pa.proj.word_games.repositories;

import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.Word;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

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

        entityManager.close();
        return word;
    }

    @Override
    public Word save(Word word)
    {
        if(word == null)
            throw new NullPointerException();

        Word temp = findById(word.getId());
        if(temp != null)
            return null;

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
