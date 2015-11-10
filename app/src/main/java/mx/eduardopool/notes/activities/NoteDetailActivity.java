package mx.eduardopool.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.databinding.ActivityNoteDetailBinding;
import mx.eduardopool.notes.fragments.NoteDetailFragment;
import mx.eduardopool.notes.fragments.NoteDialogFragment;
import mx.eduardopool.notes.models.NoteModel;
import mx.eduardopool.notes.models.wrappers.NoteWrapper;

/**
 * An activity representing a single Note detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item text are presented side-by-side with a list of items
 * in a {@link NoteListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link NoteDetailFragment}.
 */
public class NoteDetailActivity extends BaseActivity implements NoteDialogFragment.NoteDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNoteDetailBinding binding = getBinding(ActivityNoteDetailBinding.class);

        final NoteWrapper noteWrapper = getIntent().getParcelableExtra(NoteDetailFragment.ARG_NOTE_ITEM);
        binding.setNoteWrapper(noteWrapper);

        binding.fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialogFragment noteDialogFragment = NoteDialogFragment.newInstance(noteWrapper);
                noteDialogFragment.show(getSupportFragmentManager(), "NoteDialogFragment");
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            NoteDetailFragment noteDetailFragment = NoteDetailFragment.newInstance(noteWrapper);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.note_detail_container, noteDetailFragment)
                    .commit();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_note_detail;
    }

    @Override
    protected int getToolbarId() {
        return R.id.detail_toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more text, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                Intent intent = new Intent(this, NoteListActivity.class);
                NavUtils.navigateUpTo(this, intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(NoteWrapper noteWrapper) {
        NoteModel.updateNote(this, noteWrapper);
    }
}
