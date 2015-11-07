package mx.eduardopool.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;

import mx.eduardopool.notes.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccessToken.refreshCurrentAccessTokenAsync();
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
