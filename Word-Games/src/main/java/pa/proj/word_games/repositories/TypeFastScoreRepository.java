package pa.proj.word_games.repositories;

import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.HangmanScore;
import pa.proj.word_games.models.TypeFastScore;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class TypeFastScoreRepository implements AbstractRepository<TypeFastScore> {
    private static TypeFastScoreRepository instance = null;

    private TypeFastScoreRepository() { }

    public static TypeFastScoreRepository getInstance()
    {
        if(instance == null)
            instance = new TypeFastScoreRepository();
        return instance;
    }

    /**
     * @return Urmatorul id disponibil.
     */
    public int getNextAvailableId() {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        String query = "SELECT MAX(tfs.id) FROM TypeFastScore tfs";
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
    public TypeFastScore findById(int id)
    {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT tfs FROM TypeFastScore tfs WHERE tfs.id=?1";
        TypedQuery<TypeFastScore> typedQuery = entityManager.createQuery(query, TypeFastScore.class);
        typedQuery.setParameter(1, id);

        TypeFastScore typeFastScore = null;
        try
        {
            typeFastScore = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        entityManager.close();
        return typeFastScore;
    }

    @Override
    public TypeFastScore findByText(String text) { return null; }

    @Override
    public TypeFastScore save(TypeFastScore typeFastScore) {
        if(typeFastScore == null)
            throw new NullPointerException();

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try
        {
            entityTransaction.begin();
            entityManager.persist(typeFastScore);
            entityTransaction.commit();
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return typeFastScore;
    }

    @Override
    public TypeFastScore update(TypeFastScore newTypeFastScore) {
        if(newTypeFastScore == null)
            throw new NullPointerException();

        String query = "SELECT tfs FROM TypeFastScore tfs WHERE tfs.id=?1";

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        TypedQuery<TypeFastScore> typedQuery = entityManager.createQuery(query, TypeFastScore.class);
        typedQuery.setParameter(1, newTypeFastScore.getId());

        TypeFastScore typeFastScore = null;
        try
        {
            entityTransaction.begin();
            typeFastScore = typedQuery.getSingleResult();
            typeFastScore.setScore(newTypeFastScore.getScore());
            entityTransaction.commit();
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return newTypeFastScore;
    }
}