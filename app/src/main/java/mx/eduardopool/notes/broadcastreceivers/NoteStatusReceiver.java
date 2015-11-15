package mx.eduardopool.notes.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import mx.eduardopool.notes.models.wrappers.NoteWrapper;
import mx.eduardopool.notes.services.NoteIntentService;

public class NoteStatusReceiver extends BroadcastReceiver {
    private Callback callback;

    public NoteStatusReceiver() {
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && callback != null) {
            NoteWrapper noteWrapper = intent.getParcelableExtra(NoteIntentService.EXTRA_NOTE_WRAPPER);
            switch (action) {
                case NoteIntentService.ACTION_NOTE_ADDED:
                    callback.onNoteAdded(noteWrapper);
                    break;
                case NoteIntentService.ACTION_NOTE_UPDATED:
                    callback.onNoteUpdated(noteWrapper);
                    break;
                case NoteIntentService.ACTION_NOTES_DELETED:
                    int notesDeletedCount = intent.getIntExtra(NoteIntentService.EXTRA_NOTES_DELETED_COUNT, 0);
                    if (notesDeletedCount != 0) {
                        callback.onNotesDeleted(notesDeletedCount);
                    }
                    break;
            }
        }
    }

    public abstract static class Callback {
        public void onNoteAdded(NoteWrapper noteWrapper) {
        }

        public void onNoteUpdated(NoteWrapper noteWrapper) {
        }

        public void onNotesDeleted(int notesDeletedCount) {
        }
    }
}
