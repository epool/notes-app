package mx.eduardopool.notes.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import mx.eduardopool.notes.R;
import mx.eduardopool.notes.databinding.NoteItemBinding;
import mx.eduardopool.notes.models.realm.Note;
import mx.eduardopool.notes.models.wrappers.NoteWrapper;

/**
 * Adapter for note list.
 * Created by epool on 11/6/15.
 */
public class NoteListAdapter extends RealmBaseAdapter<Note> {

    public NoteListAdapter(Context context, RealmResults<Note> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            NoteItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.note_item, null, false);
            rowView = binding.getRoot();
            rowView.setTag(binding);
        }

        Note note = realmResults.get(position);

        NoteItemBinding binding = (NoteItemBinding) rowView.getTag();
        binding.setNoteWrapper(new NoteWrapper(note));

        return rowView;
    }
}
