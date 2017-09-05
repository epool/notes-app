package mx.eduardopool.notes.models;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.models.realm.User;
import mx.eduardopool.notes.models.viewmodels.NoteViewModel;
import mx.eduardopool.notes.services.NoteIntentService;

/**
 * Note model to handle note actions.
 * Created by epool on 11/9/15.
 */
public class NoteModel {

    /**
     * Obtains all notes from the current user.
     *
     * @param context Context where this method is invoked.
     * @param realm   Realm connection to get the notes. Don't close it here, this is managed from the Activity/Fragment life cycle.
     * @return Current user notes.
     */
    public static RealmResults<Note> getCurrentUserNotes(Context context, Realm realm) {
        String userId = UserModel.getCurrentUserId(context);

        User user = realm.where(User.class)
                .equalTo(User.ID, userId)
                .findFirst();

        return user
                .getNotes()
                .where()
                .findAllSorted(Note.CREATE_DATE, false);
    }

    /**
     * Adds a new note to the current user. This is handled in a intent service to avoid blocking the UI.
     *
     * @param context       Context where this method is invoked.
     * @param noteViewModel Note to be added.
     */
    public static void addNewNote(Context context, NoteViewModel noteViewModel) {
        NoteIntentService.startActionAddNote(context, noteViewModel);
    }

    /**
     * Updates an existing note from the current user. This is handled in a intent service to avoid blocking the UI.
     *
     * @param context       Context where this method is invoked.
     * @param noteViewModel Note to be edited.
     */
    public static void updateNote(Context context, NoteViewModel noteViewModel) {
        NoteIntentService.startActionUpdateNote(context, noteViewModel);
    }

    /**
     * Deletes existing notes from the current user. This is handled in a intent service to avoid blocking the UI.
     *
     * @param context Context where this method is invoked.
     * @param noteIds Note ids to be deleted.
     */
    public static void deleteNotes(Context context, ArrayList<String> noteIds) {
        NoteIntentService.startActionDeleteNotes(context, noteIds);
    }

}
