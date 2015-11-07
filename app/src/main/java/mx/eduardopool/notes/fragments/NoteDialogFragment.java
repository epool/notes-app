package mx.eduardopool.notes.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.databinding.AddEditNoteBinding;
import mx.eduardopool.notes.models.NoteItem;

/**
 * Dialog fragment to add and edit notes.
 * Created by epool on 11/6/15.
 */
public class NoteDialogFragment extends DialogFragment {
    private static String NOTE_ITEM_KEY = "NOTE_ITEM_KEY";
    private AddEditNoteBinding binding;

    // Use this instance of the interface to deliver action events
    private NoteDialogListener listener;

    public static NoteDialogFragment newInstance(NoteItem noteItem) {

        Bundle args = new Bundle();
        args.putParcelable(NOTE_ITEM_KEY, noteItem != null ? noteItem : new NoteItem());

        NoteDialogFragment fragment = new NoteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.add_edit_note, null, false);

        NoteItem noteItem = getArguments().getParcelable(NOTE_ITEM_KEY);

        binding.setNoteItem(noteItem);
        binding.executePendingBindings();
        binding.titleEditText.setSelection(binding.titleEditText.getText().length());
        binding.textEditText.setSelection(binding.textEditText.getText().length());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(binding.getRoot())
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        NoteItem noteItem = binding.getNoteItem();
                        noteItem.setTitle(binding.titleEditText.getText().toString());
                        noteItem.setText(binding.textEditText.getText().toString());
                        listener.onDialogPositiveClick(noteItem);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NoteDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoteDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoteDialogListener");
        }
    }

    public interface NoteDialogListener {
        void onDialogPositiveClick(NoteItem noteItem);
    }
}
