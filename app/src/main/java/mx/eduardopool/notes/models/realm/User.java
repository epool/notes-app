package mx.eduardopool.notes.models.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Real user object.
 * Created by epool on 11/8/15.
 */
public class User extends RealmObject {
    public static String ID = "id";

    @PrimaryKey
    private String id;
    private RealmList<Note> notes;

    public User() {
    }

    public User(String facebookId) {
        this.id = facebookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

    public void setNotes(RealmList<Note> notes) {
        this.notes = notes;
    }

}
