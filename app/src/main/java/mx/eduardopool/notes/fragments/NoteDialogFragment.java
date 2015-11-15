package mx.eduardopool.notes.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.Date;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.databinding.FragmentDialogAddEditNoteBinding;
import mx.eduardopool.notes.models.wrappers.NoteWrapper;

/**
 * Dialog fragment to add and edit notes.
 * Created by epool on 11/6/15.
 */
public class NoteDialogFragment extends DialogFragment {
    private static String NOTE_KEY = "NOTE_KEY";
    private FragmentDialogAddEditNoteBinding binding;

    // Use this instance of the interface to deliver action events
    private NoteDialogListener listener;

    public static NoteDialogFragment newInstance(NoteWrapper noteWrapper) {

        Bundle args = new Bundle();
        args.putParcelable(NOTE_KEY, noteWrapper != null ? noteWrapper : new NoteWrapper());

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_add_edit_note, null, false);

        NoteWrapper noteWrapper = getArguments().getParcelable(NOTE_KEY);
        binding.setNoteWrapper(noteWrapper);
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
                        // This will be overwritten in onStart()
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
    public void onStart() {
        super.onStart();

        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.titleInputLayout.setError(null);
                    binding.textInputLayout.setError(null);

                    String title = binding.titleEditText.getText().toString();
                    String text = binding.textEditText.getText().toString();

                    String fieldRequiredErrorMessage = getResources().getString(R.string.error_field_required);

                    if (TextUtils.isEmpty(title) && TextUtils.isEmpty(text)) {
                        binding.titleInputLayout.setError(fieldRequiredErrorMessage);
                        binding.textInputLayout.setError(fieldRequiredErrorMessage);
                        return;
                    } else if (TextUtils.isEmpty(title)) {
                        binding.titleInputLayout.setError(fieldRequiredErrorMessage);
                        return;
                    } else if (TextUtils.isEmpty(text)) {
                        binding.textInputLayout.setError(fieldRequiredErrorMessage);
                        return;
                    }

                    NoteWrapper noteWrapper = binding.getNoteWrapper();
                    noteWrapper.setTitle(title);
                    noteWrapper.setText(text);
                    noteWrapper.setCreateDate(new Date());

                    listener.onDialogPositiveClick(noteWrapper);

                    alertDialog.dismiss();
                }
            });
        }
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
        void onDialogPositiveClick(NoteWrapper noteWrapper);
    }
}
