package networkbook.model;

import java.util.ArrayList;

/**
 * Behaves like a wrapper class around the `NetworkBook` class that facilitates the undo/redo mechanism by storing
 * version history.
 */
public class VersionedNetworkBook extends NetworkBook {

    private final ArrayList<NetworkBook> networkBookStateList;
    private int currentStatePointer;

    /**
     * Constructs a new instance of VersionedNetworkBook.
     */
    public VersionedNetworkBook() {
        super();
        this.networkBookStateList = new ArrayList<>();
        this.networkBookStateList.add(new NetworkBook(this));
        this.currentStatePointer = 0;
    }

    /**
     * Constructs a new `VersionedNetworkBook` object by copying the data from the given `ReadOnlyNetworkBook` object.
     *
     * @param toBeCopied The `ReadOnlyNetworkBook` object to copy data from.
     */
    public VersionedNetworkBook(ReadOnlyNetworkBook toBeCopied) {
        super();
        super.resetData(toBeCopied);
        this.networkBookStateList = new ArrayList<>();
        this.currentStatePointer = -1;
        commit();
    }

    /**
     * Commits the current state of the network book to the version history such that this state
     * will be the most recent version in the version history.
     */
    public void commit() {
        networkBookStateList.subList(currentStatePointer + 1, networkBookStateList.size()).clear();
        networkBookStateList.add(new NetworkBook(this));
        currentStatePointer++;
    }

    /**
     * Undoes the last committed change to the state of the network book.
     */
    public void undo() {
        super.resetData(networkBookStateList.get(--currentStatePointer));
    }

    /**
     * Redoes the last undone change to the network book.
     */
    public void redo() {
        super.resetData(networkBookStateList.get(++currentStatePointer));
    }

    /**
     * Returns a boolean that reflects whether it is possible to undo the last committed change to the network book.
     *
     * @return `true` if it is possible to undo the last committed change, `false` otherwise.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns a boolean that reflects whether it is possible to redo the last undone change to the network book.
     *
     * @return `true` if it is possible to redo the last undone change, `false` otherwise.
     */
    public boolean canRedo() {
        return currentStatePointer < (networkBookStateList.size() - 1);
    }

    /**
     * Returns a list of all versions of the network book in chronological order,
     * with the most recent version at the end.
     *
     * @return A list of all versions of the network book.
     */
    public ArrayList<NetworkBook> getNetworkBookStateList() {
        return networkBookStateList;
    }

    /**
     * Returns the pointer to the current version of the network book in the `networkBookStateList`.
     *
     * @return The pointer to the current version of the network book in the `networkBookStateList`.
     */
    public int getCurrentStatePointer() {
        return currentStatePointer;
    }
}
