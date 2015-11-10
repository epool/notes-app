package mx.eduardopool.notes.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.eduardopool.notes.models.NoteItem;

/**
 * Helper class for providing sample title for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    private static final int COUNT = 25;
    /**
     * An array of sample (dummy) items.
     */
    public static List<NoteItem> ITEMS = new ArrayList<>();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, NoteItem> ITEM_MAP = new HashMap<>();

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static NoteItem getNoteItemById(String noteItemId) {
        for (int i = 0; i < ITEMS.size(); i++) {
            NoteItem noteItem = ITEMS.get(i);
            if (noteItem.getId().equals(noteItemId)) {
                return noteItem;
            }
        }
        return null;
    }

    private static void addItem(NoteItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private static NoteItem createDummyItem(int position) {
        return new NoteItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore text information here.");
        }
        return builder.toString();
    }

}
