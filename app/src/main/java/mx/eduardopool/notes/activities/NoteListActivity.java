package mx.eduardopool.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.broadcastreceivers.NoteStatusReceiver;
import mx.eduardopool.notes.databinding.ActivityNoteAppBarBinding;
import mx.eduardopool.notes.fragments.NoteDetailFragment;
import mx.eduardopool.notes.fragments.NoteDialogFragment;
import mx.eduardopool.notes.fragments.NoteListFragment;
import mx.eduardopool.notes.models.NoteModel;
import mx.eduardopool.notes.models.UserModel;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.models.viewmodels.NoteViewModel;


/**
 * An activity representing a list of Notes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NoteDetailActivity} representing
 * item text. On tablets, the activity presents the list of items and
 * item text side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link NoteListFragment} and the item text
 * (if present) is a {@link NoteDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link NoteListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class NoteListActivity extends BaseActivity
        implements NoteListFragment.Callbacks, NoteDialogFragment.NoteDialogListener {
    private ActivityNoteAppBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getBinding(ActivityNoteAppBarBinding.class);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialogFragment noteDialogFragment = NoteDialogFragment.newInstance(null);
                noteDialogFragment.show(getSupportFragmentManager(), "NoteDialogFragment");
            }
        });
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        binding.fab.hide();

        super.onSupportActionModeStarted(mode);
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        super.onSupportActionModeFinished(mode);

        binding.fab.show();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_note_app_bar;
    }

    /**
     * Callback method from {@link NoteListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onNoteSelected(Note note) {
        NoteViewModel noteViewModel = new NoteViewModel(note);
        // In single-pane mode, simply start the detail activity
        // for the selected item ID.
        Intent detailIntent = new Intent(this, NoteDetailActivity.class);
        detailIntent.putExtra(NoteDetailFragment.ARG_NOTE_ITEM, noteViewModel);
        startActivity(detailIntent);
    }

    @Override
    public void onNotesDeleted(ArrayList<String> noteIds) {
        NoteModel.deleteNotes(this, noteIds);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

        registerNoteStatusReceiver(new NoteStatusReceiver.Callback() {
            @Override
            public void onNoteAdded(NoteViewModel noteViewModel) {
                Snackbar.make(binding.getRoot(), R.string.message_note_added, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNotesDeleted(int notesDeletedCount) {
                String message = getResources().getQuantityString(R.plurals.message_notes_deleted, notesDeletedCount, notesDeletedCount);
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);

        unregisterNoteStatusReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutMenuId:

                UserModel.signOut(this);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(NoteViewModel noteViewModel) {
        NoteModel.addNewNote(this, noteViewModel);
    }
}
