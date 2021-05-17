package pa.proj.word_games.repositories;

import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.TypeFastScore;
import pa.proj.word_games.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class UserRepository implements AbstractRepository<User> {
    private static UserRepository instance = null;

    private UserRepository() { }

    public static UserRepository getInstance()
    {
        if(instance == null)
            instance = new UserRepository();
        return instance;
    }

    /**
     * Cauta in baza de date un utilizator dupa datele de conectare oferite de client.
     * @param username Numele de utilizator oferit.
     * @param password Parola oferita.
     * @return User-ul, daca exista unul cu aceste date; NULL, altfel
     */
    public User findByCredentials(String username, String password) {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT u FROM User u WHERE u.username=?1 AND u.password=?2";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter(1, username);
        typedQuery.setParameter(2, password);

        User user = null;
        try
        {
            user = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        entityManager.close();
        return user;
    }

    /**
     * Cauta in baza de date un User dupa un anumit username.
     * @param username Username-ul dupa care se face cautarea.
     * @return true, daca exista un User cu acest username; false, altfel
     */
    public boolean existsUserWithCertainUsername(String username) {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT u FROM User u WHERE u.username=?1";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter(1, username);

        try
        {
            typedQuery.getSingleResult();
            entityManager.close();
            return true;
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return false;
        }
    }

    /**
     * @return Urmatorul id disponibil (care poate fi folosit la crearea unui cont).
     */
    public int getNextAvailableId() {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        String query = "SELECT MAX(u.id) FROM User u";
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
    public User findById(int id)
    {
        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();

        String query = "SELECT u FROM User u WHERE u.id=?1";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter(1, id);

        User user = null;
        try
        {
            user = typedQuery.getSingleResult();
        }
        catch(Exception ignored)
        { }

        entityManager.close();
        return user;
    }

    @Override
    public User findByText(String text) { return null; }

    @Override
    public User save(User user) {
        if(user == null)
            throw new NullPointerException();

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try
        {
            entityTransaction.begin();
            entityManager.persist(user);
            entityTransaction.commit();
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return user;
    }

    @Override
    public User update(User newUser) {
        if(newUser == null)
            throw new NullPointerException();

        String query = "SELECT u FROM User u WHERE u.id=?1";

        EntityManager entityManager = EntityFactoryManager.getInstance().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter(1, newUser.getId());

        User user = null;
        try
        {
            entityTransaction.begin();
            user = typedQuery.getSingleResult();
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            entityTransaction.commit();
        }
        catch(Exception ignored)
        {
            entityManager.close();
            return null;
        }

        entityManager.close();
        return newUser;
    }
}
