package mx.eduardopool.notes.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.login.LoginManager;

import io.realm.Realm;
import mx.eduardopool.notes.R;
import mx.eduardopool.notes.models.realm.User;

/**
 * User model to handle user actions.
 * Created by epool on 11/9/15.
 */
public class UserModel {

    public static void signIn(Context context, String facebookId) {
        Realm realm = Realm.getDefaultInstance();

        User user = realm.where(User.class).equalTo(User.ID, facebookId).findFirst();

        if (user == null) {
            realm.beginTransaction();
            user = realm.createObject(User.class);
            user.setId(facebookId);
            realm.commitTransaction();
        }

        realm.close();

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_file_name), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(context.getString(R.string.current_user_id), facebookId)
                .apply();
    }

    public static String getCurrentUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_file_name), Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.current_user_id), null);
    }

    public static void signOut(Context context) {
        LoginManager.getInstance().logOut();

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_file_name), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(context.getString(R.string.current_user_id), null)
                .apply();
    }

}
