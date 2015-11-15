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

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.broadcastreceivers.NoteStatusReceiver;
import mx.eduardopool.notes.services.NoteIntentService;

/**
 * Base activity
 * Created by epool on 11/5/15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ViewDataBinding binding;
    private NoteStatusReceiver noteStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutResourceId = getLayoutResourceId();

        binding = DataBindingUtil.setContentView(this, layoutResourceId);

        Toolbar toolbar = (Toolbar) findViewById(getToolbarId());
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        noteStatusReceiver = new NoteStatusReceiver();
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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NoteIntentService.ACTION_NOTE_ADDED);
        intentFilter.addAction(NoteIntentService.ACTION_NOTE_UPDATED);
        intentFilter.addAction(NoteIntentService.ACTION_NOTES_DELETED);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(this.noteStatusReceiver, intentFilter);
    }

    protected void unregisterNoteStatusReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(noteStatusReceiver);
    }

    protected abstract int getLayoutResourceId();

    protected int getToolbarId() {
        return R.id.toolbar;
    }

    public <T extends ViewDataBinding> T getBinding(Class<T> clazz) {
        return clazz.cast(binding);
    }
}
