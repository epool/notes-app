package mx.eduardopool.notes.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import mx.eduardopool.notes.BR;

/**
 * A dummy item representing a piece of title.
 */
public class NoteItem extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<NoteItem> CREATOR = new Parcelable.Creator<NoteItem>() {
        public NoteItem createFromParcel(Parcel source) {
            return new NoteItem(source);
        }

        public NoteItem[] newArray(int size) {
            return new NoteItem[size];
        }
    };
    private String id;
    private String title;
    private String text;

    public NoteItem() {
    }

    public NoteItem(String id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    protected NoteItem(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.text = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.text);
    }
}
