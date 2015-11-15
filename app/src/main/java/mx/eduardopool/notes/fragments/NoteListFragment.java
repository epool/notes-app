package mx.eduardopool.notes.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.eduardopool.notes.R;
import mx.eduardopool.notes.adapters.NoteListAdapter;
import mx.eduardopool.notes.databinding.FragmentNoteListBinding;
import mx.eduardopool.notes.models.NoteModel;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.utils.ViewUtil;

/**
 * A list fragment representing a list of Notes. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link NoteDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class NoteListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onNoteSelected(Note note) {
        }

        @Override
        public void onNotesDeleted(ArrayList<String> noteIds) {

        }
    };
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;
    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private NoteListAdapter noteListAdapter;

    private FragmentNoteListBinding binding;

    private Realm realm;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        RealmResults<Note> notes = NoteModel.getCurrentUserNotes(getBaseActivity(), realm);
        noteListAdapter = new NoteListAdapter(getBaseActivity(), notes, true);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_list, null, false);

        binding.noteList.setEmptyView(binding.getRoot().findViewById(android.R.id.empty));

        binding.noteList.setAdapter(noteListAdapter);

        binding.noteList.setOnItemClickListener(this);

        binding.noteList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        binding.noteList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Update title
                int count = binding.noteList.getCheckedItemCount();
                if (count > 0) {
                    mode.setTitle(getResources().getQuantityString(R.plurals.title_selected_notes, count, count));

                    noteListAdapter.checkItemAtPosition(checked, position);
                }
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.deleteNotesMenuId:
                        int count = binding.noteList.getCheckedItemCount();
                        ViewUtil.showDeleteNotesConfirmationDialog(getBaseActivity(), count, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteModel.deleteNotes(getBaseActivity(), noteListAdapter.getSelectedNoteIds());

                                mode.finish(); // Action picked, so close the CAB
                            }
                        });
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.notes_cab_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
                binding.noteList.clearChoices();
                noteListAdapter.getSelectedNoteIds().clear();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement its callbacks.
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        realm.close();
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            binding.noteList.setItemChecked(mActivatedPosition, false);
        } else {
            binding.noteList.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        Note note = (Note) parent.getItemAtPosition(position);
        mCallbacks.onNoteSelected(note);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onNoteSelected(Note note);

        void onNotesDeleted(ArrayList<String> noteIds);
    }
}
