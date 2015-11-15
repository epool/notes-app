package mx.eduardopool.notes.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.text.DateFormat;

import mx.eduardopool.notes.R;

/**
 * ViewUtil class.
 * Created by epool on 11/9/15.
 */
public class ViewUtil {
    public static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance();

    public static void showDeleteNotesConfirmationDialog(Context context, int notesNumber, DialogInterface.OnClickListener confirmationListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getQuantityString(R.plurals.title_delete_note, notesNumber));
        builder.setPositiveButton(android.R.string.ok, confirmationListener);
        builder.setNegativeButton(android.R.string.cancel, null);
        Dialog dialog = builder.create();
        dialog.show();
    }
}
