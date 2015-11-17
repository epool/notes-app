package mx.eduardopool.notes.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import mx.eduardopool.notes.models.UserModel;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.models.realm.User;
import mx.eduardopool.notes.models.viewmodels.NoteViewModel;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class NoteIntentService extends IntentService {
    public static final String EXTRA_NOTE_WRAPPER = "mx.eduardopool.notes.services.extra.NOTE_WRAPPER";
    public static final String ACTION_NOTE_ADDED = "mx.eduardopool.notes.services.action.NOTE_ADDED";
    public static final String ACTION_NOTE_UPDATED = "mx.eduardopool.notes.services.action.NOTE_UPDATED";
    public static final String ACTION_NOTES_DELETED = "mx.eduardopool.notes.services.action.NOTES_DELETED";
    public static final String EXTRA_NOTES_DELETED_COUNT = "mx.eduardopool.notes.services.extra.NOTES_DELETED_COUNT";
    private static final String ACTION_ADD_NOTE = "mx.eduardopool.notes.services.action.ADD_NOTE";
    private static final String ACTION_UPDATE_NOTE = "mx.eduardopool.notes.services.action.UPDATE_NOTE";
    private static final String ACTION_DELETE_NOTES = "mx.eduardopool.notes.services.action.DELETE_NOTES";
    private static final String EXTRA_NOTE_IDS = "mx.eduardopool.notes.services.extra.NOTE_IDS";
    private LocalBroadcastManager localBroadcastManager;

    public NoteIntentService() {
        super("NoteIntentService");
    }

    /**
     * Starts this service to perform action ADD_NOTE with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context       Context where this method is invoked.
     * @param noteViewModel Note to be added.
     */
    public static void startActionAddNote(Context context, NoteViewModel noteViewModel) {
        Intent intent = new Intent(context, NoteIntentService.class);
        intent.setAction(ACTION_ADD_NOTE);
        intent.putExtra(EXTRA_NOTE_WRAPPER, noteViewModel);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action UPDATE_NOTE with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context       Context where this method is invoked.
     * @param noteViewModel Note to be updated.
     */
    public static void startActionUpdateNote(Context context, NoteViewModel noteViewModel) {
        Intent intent = new Intent(context, NoteIntentService.class);
        intent.setAction(ACTION_UPDATE_NOTE);
        intent.putExtra(EXTRA_NOTE_WRAPPER, noteViewModel);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DELETE_NOTE with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context Context where this method is invoked.
     * @param noteIds Note ids to be deleted.
     */
    public static void startActionDeleteNotes(Context context, ArrayList<String> noteIds) {
        Intent intent = new Intent(context, NoteIntentService.class);
        intent.setAction(ACTION_DELETE_NOTES);
        intent.putExtra(EXTRA_NOTE_IDS, noteIds);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action != null) {
                Realm realm = Realm.getDefaultInstance();
                NoteViewModel noteViewModel = intent.getParcelableExtra(EXTRA_NOTE_WRAPPER);
                switch (action) {
                    case ACTION_ADD_NOTE:
                        handleActionAddNote(realm, noteViewModel);
                        break;
                    case ACTION_UPDATE_NOTE:
                        handleActionUpdateNote(realm, noteViewModel);
                        break;
                    case ACTION_DELETE_NOTES:
                        ArrayList<String> noteIds = intent.getStringArrayListExtra(EXTRA_NOTE_IDS);
                        handleActionDeleteNotes(realm, noteIds);
                        break;
                }
                realm.close();
            }
        }
    }

    /**
     * Handle action ADD_NOTE in the provided background thread with the provided
     * parameters.
     *
     * @param realm         Realm instance to do realm transactions.
     * @param noteViewModel Note to be added.
     */
    private void handleActionAddNote(Realm realm, NoteViewModel noteViewModel) {
        Note note = noteViewModel.toRealm(true);

        realm.beginTransaction();
        Note noteAdded = realm.copyToRealmOrUpdate(note);
        realm.where(User.class)
                .equalTo(User.ID, UserModel.getCurrentUserId(this))
                .findFirst()
                .getNotes()
                .add(noteAdded);
        realm.commitTransaction();

        notifyNoteAdded(new NoteViewModel(noteAdded));
    }

    /**
     * Notifies note added.
     *
     * @param noteViewModel Note added.
     */
    private void notifyNoteAdded(NoteViewModel noteViewModel) {
        Intent intent = new Intent(ACTION_NOTE_ADDED);
        intent.putExtra(EXTRA_NOTE_WRAPPER, noteViewModel);
        localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * Handle action UPDATE_NOTE in the provided background thread with the provided
     * parameters.
     *
     * @param realm         Realm instance to do realm transactions.
     * @param noteViewModel Note to be updated.
     */
    private void handleActionUpdateNote(Realm realm, NoteViewModel noteViewModel) {
        Note note = noteViewModel.toRealm(false);

        realm.beginTransaction();
        Note noteUpdated = realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();

        notifyNoteUpdated(new NoteViewModel(noteUpdated));
    }

    /**
     * Notifies note updated.
     *
     * @param noteViewModel Note updated.
     */
    private void notifyNoteUpdated(NoteViewModel noteViewModel) {
        Intent intent = new Intent(ACTION_NOTE_UPDATED);
        intent.putExtra(EXTRA_NOTE_WRAPPER, noteViewModel);
        localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * Handle action DELETE_NOTE in the provided background thread with the provided
     * parameters.
     *
     * @param realm   Realm instance to do realm transactions.
     * @param noteIds Note ids to be deleted.
     */
    private void handleActionDeleteNotes(Realm realm, ArrayList<String> noteIds) {
        realm.beginTransaction();
        RealmList<Note> notes = realm.where(User.class)
                .equalTo(User.ID, UserModel.getCurrentUserId(this))
                .findFirst()
                .getNotes();

        int notesDeletedCount = 0;
        for (String noteId : noteIds) {
            Note note = notes.where()
                    .equalTo(Note.ID, noteId)
                    .findFirst();
            if (note != null) {
                note.removeFromRealm();
                notesDeletedCount += 1;
            }
        }

        realm.commitTransaction();

        notifyNotesDeleted(notesDeletedCount);
    }

    /**
     * Notifies notes deleted.
     *
     * @param notesDeletedCount Number of notes that were deleted.
     */
    private void notifyNotesDeleted(int notesDeletedCount) {
        Intent intent = new Intent(ACTION_NOTES_DELETED);
        intent.putExtra(EXTRA_NOTES_DELETED_COUNT, notesDeletedCount);
        localBroadcastManager.sendBroadcast(intent);
    }
}
