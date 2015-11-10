package mx.eduardopool.notes.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import io.realm.Realm;
import mx.eduardopool.notes.models.UserModel;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.models.realm.User;
import mx.eduardopool.notes.models.wrappers.NoteWrapper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class NoteIntentService extends IntentService {
    private static final String ACTION_ADD_NOTE = "mx.eduardopool.notes.services.action.ADD_NOTE";
    private static final String ACTION_UPDATE_NOTE = "mx.eduardopool.notes.services.action.UPDATE_NOTE";
    private static final String ACTION_DELETE_NOTE = "mx.eduardopool.notes.services.action.DELETE_NOTE";

    private static final String EXTRA_NOTE_WRAPPER = "mx.eduardopool.notes.services.extra.NOTE_WRAPPER";

    public NoteIntentService() {
        super("NoteIntentService");
    }

    /**
     * Starts this service to perform action ADD_NOTE with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionAddNote(Context context, NoteWrapper noteWrapper) {
        Intent intent = new Intent(context, NoteIntentService.class);
        intent.setAction(ACTION_ADD_NOTE);
        intent.putExtra(EXTRA_NOTE_WRAPPER, noteWrapper);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action UPDATE_NOTE with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateNote(Context context, NoteWrapper noteWrapper) {
        Intent intent = new Intent(context, NoteIntentService.class);
        intent.setAction(ACTION_UPDATE_NOTE);
        intent.putExtra(EXTRA_NOTE_WRAPPER, noteWrapper);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DELETE_NOTE with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionDeleteNote(Context context, NoteWrapper noteWrapper) {
        Intent intent = new Intent(context, NoteIntentService.class);
        intent.setAction(ACTION_DELETE_NOTE);
        intent.putExtra(EXTRA_NOTE_WRAPPER, noteWrapper);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action != null) {
                Realm realm = Realm.getDefaultInstance();
                NoteWrapper noteWrapper = intent.getParcelableExtra(EXTRA_NOTE_WRAPPER);
                switch (action) {
                    case ACTION_ADD_NOTE:
                        handleActionAddNote(realm, noteWrapper);
                        break;
                    case ACTION_UPDATE_NOTE:
                        handleActionUpdateNote(realm, noteWrapper);
                        break;
                    case ACTION_DELETE_NOTE:
                        handleActionDeleteNote(realm, noteWrapper);
                        break;
                }
                realm.close();
            }
        }
    }

    /**
     * Handle action ADD_NOTE in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAddNote(Realm realm, NoteWrapper noteWrapper) {
        Note note = noteWrapper.toRealm(true);

        realm.beginTransaction();
        Note noteAdded = realm.copyToRealmOrUpdate(note);
        realm.where(User.class)
                .equalTo(User.ID, UserModel.getCurrentUserId(this))
                .findFirst()
                .getNotes()
                .add(noteAdded);
        realm.commitTransaction();
    }

    /**
     * Handle action UPDATE_NOTE in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateNote(Realm realm, NoteWrapper noteWrapper) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(noteWrapper.toRealm(false));
        realm.commitTransaction();
    }

    /**
     * Handle action DELETE_NOTE in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDeleteNote(Realm realm, NoteWrapper noteWrapper) {
        realm.beginTransaction();
        realm.where(User.class)
                .equalTo(User.ID, UserModel.getCurrentUserId(this))
                .findFirst()
                .getNotes()
                .where()
                .equalTo(Note.ID, noteWrapper.getId())
                .findAll()
                .clear();
        realm.commitTransaction();
    }
}
