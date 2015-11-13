package mx.eduardopool.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.databinding.ActivityNoteAppBarBinding;
import mx.eduardopool.notes.fragments.NoteDetailFragment;
import mx.eduardopool.notes.fragments.NoteDialogFragment;
import mx.eduardopool.notes.fragments.NoteListFragment;
import mx.eduardopool.notes.models.NoteModel;
import mx.eduardopool.notes.models.UserModel;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.models.wrappers.NoteWrapper;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNoteAppBarBinding binding = getBinding(ActivityNoteAppBarBinding.class);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialogFragment noteDialogFragment = NoteDialogFragment.newInstance(null);
                noteDialogFragment.show(getSupportFragmentManager(), "NoteDialogFragment");
            }
        });
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
    public void onItemSelected(Note note) {
        NoteWrapper noteWrapper = new NoteWrapper(note);
        // In single-pane mode, simply start the detail activity
        // for the selected item ID.
        Intent detailIntent = new Intent(this, NoteDetailActivity.class);
        detailIntent.putExtra(NoteDetailFragment.ARG_NOTE_ITEM, noteWrapper);
        startActivity(detailIntent);
    }

    @Override
    public void onItemDeleted(Note note) {
        NoteModel.deleteNote(this, new NoteWrapper(note));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
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

                LoginManager.getInstance().logOut();
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
    public void onDialogPositiveClick(NoteWrapper noteWrapper) {
        NoteModel.addNewNote(this, noteWrapper);
    }
}
