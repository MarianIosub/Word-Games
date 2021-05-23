package pa.proj.word_games.repositories;

import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.FazanScore;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class FazanScoreRepository implements AbstractRepository<FazanScore> {
    private static FazanScoreRepository instance = null;

    private FazanScoreRepository() {
    }

    public static FazanScoreRepository getInstance() {
        if (instance == null) {
            instance = new FazanScoreRepository();
        }
        return instance;
    }

    /**
     * @return Urmatorul id disponibil.
     */
    public int getNextAvailableId() {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        String query = "SELECT MAX(fs.id) FROM FazanScore fs";
        TypedQuery<Integer> typedQuery = entityManager.createQuery(query, Integer.class);

        Integer id = null;
        try {
            id = typedQuery.getSingleResult();
            entityManager.close();
            return id + 1;
        } catch (Exception ignored) {
            entityManager.close();
            return 1;
        }
    }

    @Override
    public FazanScore findById(int id) {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT fs FROM FazanScore fs WHERE fs.id=?1";
        TypedQuery<FazanScore> typedQuery = entityManager.createQuery(query, FazanScore.class);
        typedQuery.setParameter(1, id);

        FazanScore fazanScore = null;
        try {
            fazanScore = typedQuery.getSingleResult();
        } catch (Exception ignored) {
        }

        entityManager.close();
        return fazanScore;
    }

    @Override
    public FazanScore findByText(String text) {
        return null;
    }

    @Override
    public FazanScore save(FazanScore fazanScore) {
        if (fazanScore == null) {
            throw new NullPointerException();
        }

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(fazanScore);
            entityTransaction.commit();
        } catch (Exception ignored) {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return fazanScore;
    }

    @Override
    public FazanScore update(FazanScore newFazanScore) {
        if (newFazanScore == null) {
            throw new NullPointerException();
        }
        String query = "SELECT fs FROM FazanScore fs WHERE fs.id=?1";

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        TypedQuery<FazanScore> typedQuery = entityManager.createQuery(query, FazanScore.class);
        typedQuery.setParameter(1, newFazanScore.getId());

        FazanScore fazanScore = null;
        try {
            entityTransaction.begin();
            fazanScore = typedQuery.getSingleResult();
            fazanScore.setScore(newFazanScore.getScore());
            entityTransaction.commit();
        } catch (Exception ignored) {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return newFazanScore;
    }
}
