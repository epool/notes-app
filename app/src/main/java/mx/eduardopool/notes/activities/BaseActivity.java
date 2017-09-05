package mx.eduardopool.notes.activities;

import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import mx.eduardopool.notes.NoteApplication;
import mx.eduardopool.notes.R;
import mx.eduardopool.notes.broadcastreceivers.NoteStatusReceiver;
import mx.eduardopool.notes.di.components.NoteApplicationComponent;

/**
 * Base activity
 * Created by epool on 11/5/15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    protected NoteStatusReceiver noteStatusReceiver;
    @Inject
    protected IntentFilter intentFilter;
    private ViewDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutResourceId = getLayoutResourceId();

        binding = DataBindingUtil.setContentView(this, layoutResourceId);

        Toolbar toolbar = (Toolbar) findViewById(getToolbarId());
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        injectComponent(((NoteApplication) getApplication()).getNoteApplicationComponent());
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        super.onSupportActionModeStarted(mode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        super.onSupportActionModeFinished(mode);
    }

    protected void registerNoteStatusReceiver(NoteStatusReceiver.Callback callback) {
        noteStatusReceiver.setCallback(callback);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(noteStatusReceiver, intentFilter);
    }

    protected void unregisterNoteStatusReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(noteStatusReceiver);
    }

    protected abstract int getLayoutResourceId();

    protected void injectComponent(NoteApplicationComponent component) {
        component.inject(this);
    }

    protected int getToolbarId() {
        return R.id.toolbar;
    }

    public <T extends ViewDataBinding> T getBinding(Class<T> clazz) {
        return clazz.cast(binding);
    }
}
