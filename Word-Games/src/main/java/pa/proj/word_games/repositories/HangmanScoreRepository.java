package pa.proj.word_games.repositories;

import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.HangmanScore;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class HangmanScoreRepository implements AbstractRepository<HangmanScore> {
    private static HangmanScoreRepository instance = null;

    private HangmanScoreRepository() { }

    public static HangmanScoreRepository getInstance()
    {
        if(instance == null)
            instance = new HangmanScoreRepository();
        return instance;
    }

    /**
     * @return Urmatorul id disponibil.
     */
    public int getNextAvailableId() {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        String query = "SELECT MAX(hs.id) FROM HangmanScore hs";
        TypedQuery<Integer> typedQuery = entityManager.createQuery(query, Integer.class);

        Integer id = null;
        try
        {
            id = typedQuery.getSingleResult();
            entityManager.close();
            return id+1;
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return 1;
        }
    }

    @Override
    public HangmanScore findById(int id)
    {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT hs FROM HangmanScore hs WHERE hs.id=?1";
        TypedQuery<HangmanScore> typedQuery = entityManager.createQuery(query, HangmanScore.class);
        typedQuery.setParameter(1, id);

        HangmanScore hangmanScore = null;
        try
        {
            hangmanScore = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        entityManager.close();
        return hangmanScore;
    }

    @Override
    public HangmanScore findByText(String text) { return null; }

    @Override
    public HangmanScore save(HangmanScore hangmanScore) {
        if(hangmanScore == null)
            throw new NullPointerException();

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try
        {
            entityTransaction.begin();
            entityManager.persist(hangmanScore);
            entityTransaction.commit();
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return hangmanScore;
    }

    @Override
    public HangmanScore update(HangmanScore newHangmanScore) {
        if(newHangmanScore == null)
            throw new NullPointerException();

        String query = "SELECT hs FROM HangmanScore hs WHERE hs.id=?1";

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        TypedQuery<HangmanScore> typedQuery = entityManager.createQuery(query, HangmanScore.class);
        typedQuery.setParameter(1, newHangmanScore.getId());

        HangmanScore hangmanScore = null;
        try
        {
            entityTransaction.begin();
            hangmanScore = typedQuery.getSingleResult();
            hangmanScore.setScore(newHangmanScore.getScore());
            entityTransaction.commit();
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return newHangmanScore;
    }
}