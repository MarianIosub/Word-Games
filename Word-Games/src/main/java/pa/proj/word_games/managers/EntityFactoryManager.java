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
     * @return Singura instanta de tip FactoryManager, fara a o initializa in cazul in care este null.
     */
    public static EntityFactoryManager getInstanceWithoutInitialization()
    {
        return instance;
    }

    /**
     * @return Singura instanta de tip FactoryManager.
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

    /**
     * Inchide obiectul de tip EntityManagerFactory.
     */
    public void close()
    {
        if(entityManagerFactory.isOpen())
            entityManagerFactory.close();
    }
}
