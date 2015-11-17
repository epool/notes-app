package mx.eduardopool.notes.di.modules;

import android.content.IntentFilter;

import com.facebook.FacebookSdk;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import mx.eduardopool.notes.NoteApplication;
import mx.eduardopool.notes.broadcastreceivers.NoteStatusReceiver;
import mx.eduardopool.notes.services.NoteIntentService;

/**
 * Notes application module.
 * Created by epool on 11/16/15.
 */
@Module
public class NoteApplicationModule {

    public NoteApplicationModule(NoteApplication noteApplication) {
        RealmConfiguration config = new RealmConfiguration.Builder(noteApplication.getApplicationContext())
                .name("notes.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);

        FacebookSdk.sdkInitialize(noteApplication.getApplicationContext());
    }

    @Provides
    public NoteStatusReceiver provideNoteStatusReceiver() {
        return new NoteStatusReceiver();
    }

    @Singleton
    @Provides
    public IntentFilter providesNoteIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NoteIntentService.ACTION_NOTE_ADDED);
        intentFilter.addAction(NoteIntentService.ACTION_NOTE_UPDATED);
        intentFilter.addAction(NoteIntentService.ACTION_NOTES_DELETED);
        return intentFilter;
    }
}
