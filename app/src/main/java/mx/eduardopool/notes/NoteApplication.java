package mx.eduardopool.notes;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Base application class
 * Created by epool on 11/4/15.
 */
public class NoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
