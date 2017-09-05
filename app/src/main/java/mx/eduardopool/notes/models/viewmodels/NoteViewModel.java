package mx.eduardopool.notes.models.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import mx.eduardopool.notes.BR;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.utils.ViewUtil;

/**
 * A dummy item representing a piece of title.
 */
public class NoteViewModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<NoteViewModel> CREATOR = new Parcelable.Creator<NoteViewModel>() {
        public NoteViewModel createFromParcel(Parcel source) {
            return new NoteViewModel(source);
        }

        public NoteViewModel[] newArray(int size) {
            return new NoteViewModel[size];
        }
    };
    private String id;
    private String title;
    private String text;
    private Date createDate;
    private String createDateFormatted;

    public NoteViewModel() {
    }

    public NoteViewModel(Note note) {
        id = note.getId();
        title = note.getTitle();
        text = note.getText();
        createDate = note.getCreateDate();
        this.createDateFormatted = ViewUtil.DATE_FORMAT.format(this.createDate);
    }

    protected NoteViewModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.text = in.readString();
        this.createDate = new Date(in.readLong());
        this.createDateFormatted = ViewUtil.DATE_FORMAT.format(this.createDate);
    }

    @Override
    public String toString() {
        return title;
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
        this.createDateFormatted = ViewUtil.DATE_FORMAT.format(this.createDate);
        notifyPropertyChanged(BR.createDate);
        notifyPropertyChanged(BR.createDateFormatted);
    }

    @Bindable
    public String getCreateDateFormatted() {
        return createDateFormatted;
    }

    public void setCreateDateFormatted(String createDateFormatted) {
        this.createDateFormatted = createDateFormatted;
        notifyPropertyChanged(BR.createDateFormatted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.text);
        dest.writeLong(this.createDate.getTime());
    }

    public Note toRealm(boolean isCreate) {
        Note note = new Note(title, text);
        if (!isCreate) {
            note.setId(id);
        }
        return note;
    }
}
