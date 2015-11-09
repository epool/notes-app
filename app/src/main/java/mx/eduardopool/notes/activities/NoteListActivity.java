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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

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

        if (binding.frameLayout.findViewById(R.id.note_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            NoteListFragment noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.note_list);
            noteListFragment.setActivateOnItemClick(true);
        }
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
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(NoteDetailFragment.ARG_NOTE_ITEM, noteWrapper);
            NoteDetailFragment fragment = new NoteDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.note_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, NoteDetailActivity.class);
            detailIntent.putExtra(NoteDetailFragment.ARG_NOTE_ITEM, noteWrapper);
            startActivity(detailIntent);
        }
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
