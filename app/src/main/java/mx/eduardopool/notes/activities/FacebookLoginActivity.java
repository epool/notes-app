package mx.eduardopool.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.databinding.ActivityFacebookLoginBinding;

public class FacebookLoginActivity extends BaseActivity implements FacebookCallback<LoginResult> {
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        ActivityFacebookLoginBinding binding = getBinding(ActivityFacebookLoginBinding.class);

        binding.loginButton.registerCallback(callbackManager, this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_facebook_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();

        // TODO Store user session
        System.out.println(String.format("========> %s %s", accessToken.getUserId(), accessToken.getToken()));

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onCancel() {
        System.out.println("========> Canceled!");
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }
}
