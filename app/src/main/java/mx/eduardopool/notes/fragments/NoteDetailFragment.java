package mx.eduardopool.notes.fragments;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.activities.NoteDetailActivity;
import mx.eduardopool.notes.activities.NoteListActivity;
import mx.eduardopool.notes.databinding.FragmentNoteDetailBinding;
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

}
