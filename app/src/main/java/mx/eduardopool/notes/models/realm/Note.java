package mx.eduardopool.notes.models.realm;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm note object.
 * Created by epool on 11/8/15.
 */
public class Note extends RealmObject {
    public static String ID = "id";
    public static String CREATE_DATE = "createDate";

    @PrimaryKey
    private String id;

    @Required
    private String title;

    @Required
    private String text;

    @Required
    private Date createDate;

    public Note() {
    }

    public Note(String title, String text) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.text = text;
        this.createDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
