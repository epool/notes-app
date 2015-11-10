package mx.eduardopool.notes.adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import mx.eduardopool.notes.R;
import mx.eduardopool.notes.databinding.NoteItemBinding;
import mx.eduardopool.notes.models.NoteItem;

/**
 * Adapter for note list.
 * Created by epool on 11/6/15.
 */
public class NoteListAdapter extends BaseAdapter {
    private Activity context;
    private List<NoteItem> noteItems = new ArrayList<>();

    public NoteListAdapter(Activity context, List<NoteItem> noteItems) {
        this.context = context;
        this.noteItems = noteItems;
    }

    public void addNewNote(NoteItem noteItem) {
        noteItems.add(0, noteItem);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return noteItems.size();
    }

    @Override
    public NoteItem getItem(int position) {
        return noteItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(noteItems.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            NoteItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.note_item, null, false);
            rowView = binding.getRoot();
            rowView.setTag(binding);
        }

        NoteItem noteItem = noteItems.get(position);

        NoteItemBinding binding = (NoteItemBinding) rowView.getTag();
        binding.titleTextView.setText(noteItem.getTitle());

        return rowView;
    }
}
