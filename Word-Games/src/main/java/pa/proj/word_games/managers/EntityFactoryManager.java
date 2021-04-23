package pa.proj.word_games.managers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Controleaza instanta de tipul EntityManagerFactory, folosita pentru a comunica cu baza de date.
 */
public class EntityFactoryManager
{
    private EntityManagerFactory entityManagerFactory;
    private static EntityFactoryManager instance = null;

    private EntityFactoryManager()
    {
        entityManagerFactory = Persistence.createEntityManagerFactory("WordGamesPersistenceUnit");
    }

    /**
     * @return Singura instanta de tip FactoryManager
     */
    public static EntityFactoryManager getInstance()
    {
        if(instance == null)
            instance = new EntityFactoryManager();
        return instance;
    }

    /**
     * Creeaza un EntityManager.
     * @return EntityManager-ul creat.
     */
    public EntityManager createEntityManager()
    {
        return entityManagerFactory.createEntityManager();
    }
}
