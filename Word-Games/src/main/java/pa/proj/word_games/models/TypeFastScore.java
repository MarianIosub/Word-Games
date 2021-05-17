package pa.proj.word_games.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="typefast_scores")
public class TypeFastScore {
    @Id
    @Column(name="id", unique = true)
    private int id;

    @Column(name="user_id", unique = true)
    private int userId;

    @Column(name="score")
    private int score;

    public TypeFastScore() { }

    public TypeFastScore(int id, int userId, int score) {
        this.id = id;
        this.userId = userId;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
