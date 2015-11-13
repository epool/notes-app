package mx.eduardopool.notes.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.activities.NoteDetailActivity;
import mx.eduardopool.notes.activities.NoteListActivity;
import mx.eduardopool.notes.databinding.FragmentNoteDetailBinding;
import mx.eduardopool.notes.models.NoteModel;
import mx.eduardopool.notes.models.wrappers.NoteWrapper;

/**
 * A fragment representing a single Note detail screen.
 * This fragment is either contained in a {@link NoteListActivity}
 * in two-pane mode (on tablets) or a {@link NoteDetailActivity}
 * on handsets.
 */
public class NoteDetailFragment extends BaseFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_NOTE_ITEM = "ARG_NOTE_ITEM";

    /**
     * The dummy title this fragment is presenting.
     */
    private NoteWrapper noteWrapper;

    private FragmentNoteDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteDetailFragment() {
    }

    public static NoteDetailFragment newInstance(NoteWrapper noteWrapper) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE_ITEM, noteWrapper);

        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_NOTE_ITEM)) {
            // Load the dummy title specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load title from a title provider.
            noteWrapper = getArguments().getParcelable(ARG_NOTE_ITEM);
            if (noteWrapper != null) {
                setTitle(noteWrapper.getTitle());
            }
        }
    }

    public void setTitle(String title) {
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_detail, container, false);

        binding.setNoteWrapper(noteWrapper);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteNoteMenuId:

                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
                builder.setTitle(R.string.title_delete_note);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteModel.deleteNote(getBaseActivity(), noteWrapper);

                        Intent intent = new Intent(getBaseActivity(), NoteListActivity.class);
                        NavUtils.navigateUpTo(getBaseActivity(), intent);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                Dialog dialog = builder.create();
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
