package mx.eduardopool.notes;

import android.app.Application;

import mx.eduardopool.notes.di.components.DaggerNoteApplicationComponent;
import mx.eduardopool.notes.di.components.NoteApplicationComponent;
import mx.eduardopool.notes.di.modules.NoteApplicationModule;

/**
 * Base application class
 * Created by epool on 11/4/15.
 */
public class NoteApplication extends Application {
    private NoteApplicationComponent noteApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        noteApplicationComponent = DaggerNoteApplicationComponent.builder()
                .noteApplicationModule(new NoteApplicationModule(this))
                .build();
    }

    public NoteApplicationComponent getNoteApplicationComponent() {
        return noteApplicationComponent;
    }
}
