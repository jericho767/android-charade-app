package data;

import java.util.ArrayList;
import java.util.Arrays;

// TODO: Search this place! Head to foot.

public class Garbage {
    public final static int ITEM_LENGTH_MAX = 60;
    public final static int ITEM_LENGTH_MIN = 1;

    public final static int NAME_LENGTH_MAX = 16;
    public final static int NAME_LENGTH_MIN = 1;

    private int mId;
    private String name;
    private ArrayList<String> items;

    public Garbage(String name, ArrayList<String> items) {
        this.name = name;
        this.items = items;
    }

    public Garbage(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    private Garbage(String name, ArrayList<String> items, int mId) {
        this.mId = mId;
        this.name = name;
        this.items = items;
    }

    protected void addItem(String item) {
        this.items.add(item);

    }

    protected void save() {
        Garbage.store(this);
    }

    protected void update() {
        // TODO: Implement
    }

    public static int store(Garbage garbage) {
        // TODO: Implement
        return 1;
    }

    public static Garbage getList(int mId) {
        // TODO: Implement

        // TODO: Remove test
        ArrayList<String> items = new ArrayList<>();
        items.add("Item 1");
        return new Garbage("Sample", items, mId);
    }

    public static ArrayList<Garbage> getLists() {
        // TODO: Implement

        // TODO: Remove test
        ArrayList<Garbage> garbage = new ArrayList<>();
        garbage.add(new Garbage("one", new ArrayList<>(Arrays.asList("we"))));
        garbage.add(new Garbage("two", new ArrayList<>(Arrays.asList(
                "one", "two"))));
        return garbage;
    }

    public static ArrayList<String> getPlayListsNames(ArrayList<Garbage> garbages) {
        ArrayList<String> names = new ArrayList<>();

        for (Garbage garbage : garbages) {
            names.add(garbage.getName());
        }

        return names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public int getId() {
        return mId;
    }

    public static final String ERROR_NAME_LENGTH() {
        return ERROR_LENGTH(NAME_LENGTH_MIN, NAME_LENGTH_MAX);
    }

    public static final String ERROR_ITEM_LENGTH() {
        return ERROR_LENGTH(ITEM_LENGTH_MIN, ITEM_LENGTH_MAX);
    }

    public static final String SUCCESS_ITEM_ADD() {
        return "Item added successfully.";
    }

    private static final String ERROR_LENGTH(int MIN, int MAX) {
        return "Name must be " + MIN + " to " +
                MAX + " characters.";
    }
}
