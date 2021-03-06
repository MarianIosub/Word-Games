package pa.proj.word_games.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="words")
public class Word
{
    @Id
    @Column(name="id", unique = true)
    private int id;

    @Column(name="text")
    private String text;

    public Word() {
        text="";
    }

    public Word(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
}
