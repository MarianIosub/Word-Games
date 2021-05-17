package pa.proj.word_games.repositories;

public interface AbstractRepository<T>
{
    /**
     * Adauga in baza de date un anumit obiect.
     * @param object Obiectul care va fi adaugat.
     * @return Obiectul, daca acesta a fost adaugat; NULL, altfel.
     */
    public T save(T object);

    /**
     * Updateaza obiectul din baza de date cu datele din noul obiect
     * @param newObject Obiectul, cu datele actualizate.
     * @return Obiectul, daca acesta a fost actualizat; NULL, altfel.
     */
    public T update(T newObject);

    /**
     * Cauta un obiect dupa un anumit id.
     * @param id Id-ul dupa care se face cautarea
     * @return Obiectul gasit; NULL, daca nu exista un obiect cu acest id in baza de date
     */
    public T findById(int id);

    /**
     * Cauta un obiect dupa un anumit text.
     * @param text Textul dupa care se face cautarea
     * @return Obiectul gasit; NULL, daca nu exista un obiect cu acest id in baza de date
     */
    public T findByText(String text);
}
