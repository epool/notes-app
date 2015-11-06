package mx.eduardopool.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doAuthentication(AccessToken.getCurrentAccessToken());
    }

    private void doAuthentication(AccessToken accessToken) {
        Class<? extends AppCompatActivity> clazz =
                accessToken == null || accessToken.isExpired() ?
                        FacebookLoginActivity.class :
                        NoteListActivity.class;

        Intent intent = new Intent(this, clazz);
        startActivity(intent);

        finish();
    }

}
