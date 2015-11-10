package mx.eduardopool.notes;

import android.app.Application;

import com.facebook.FacebookSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Base application class
 * Created by epool on 11/4/15.
 */
public class NoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                .name("notes.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);

        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
