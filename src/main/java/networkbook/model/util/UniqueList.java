package networkbook.model.util;

import static networkbook.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a list of items that need to be unique.
 * @param <T> The class representing the item.
 */
public class UniqueList<T extends Identifiable<T>> implements Iterable<T> {
    private final ObservableList<T> internalList = FXCollections.observableArrayList();
    private final ObservableList<T> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Checks whether the list contains the element.
     * This makes use of {@code T::equals}.
     * @param toCheck The element to check.
     */
    public boolean contains(T toCheck) {
        assert toCheck != null : "T toCheck should not be null";
        return internalList.stream().anyMatch(toCheck::isSame);
    }

    /**
     * Adds a new element to the list.
     * The element must not already exist in the list.
     * @param toAdd The element to add.
     */
    public void add(T toAdd) {
        assert toAdd != null : "T toAdd should not be null";
        assert !contains(toAdd) : "toAdd should not exist in UniqueList beforehand.";
        internalList.add(toAdd);
    }

    /**
     * Adds all elements from the specified list to this list.
     * If there are items in the specified list that are already in this list, they are simply ignored.
     */
    public void addAllFromList(UniqueList<T> listToAddFrom) {
        assert listToAddFrom != null : "listToAddFrom should not be null";
        listToAddFrom.internalList.forEach(toAdd -> {
            if (!contains(toAdd)) {
                internalList.add(toAdd);
            }
        });
    }

    /**
     * Replaces the element {@code target} in the list with {@code edited}.
     * {@code target} must exist in the list.
     * @param target The target to replace.
     * @param edited The new value of the element.
     */
    public void setItem(T target, T edited) {
        requireAllNonNull(target, edited);

        int index = internalList.indexOf(target);
        assert index != -1 : "Target item should be in list.";

        assert target.isSame(edited) || !contains(edited) : "Edited item is already in the list.";

        internalList.set(index, edited);
    }

    /**
     * Replaces the element of zero-based {@code index} with the new element.
     */
    public void setItem(int index, T edited) {
        assert edited != null : "edited item should not be null";
        assert index >= 0 : "index should be non-negative";
        assert index < this.internalList.size() : "index should not be out of bound";

        for (int i = 0; i < internalList.size(); i++) {
            assert i == index || !edited.isSame(internalList.get(i))
                    : "edited item should not already exist in UniqueList";
        }

        this.internalList.set(index, edited);
    }

    /**
     * Removes the equivalent element from the list.
     * The element must exist in the list.
     * @param toRemove The element to remove.
     */
    public void remove(T toRemove) {
        assert toRemove != null : "T toRemove should not be null";
        boolean isRemoved = internalList.remove(toRemove);
        assert isRemoved : "T toRemove should exists in UniqueList";
    }

    /**
     * Removes the element at the index in the list.
     * @param index The zero-based index of the element to remove.
     */
    public void removeAtIndex(int index) {
        assert(index < this.internalList.size());
        this.internalList.remove(index);
    }

    /**
     * Checks if this list is empty.
     */
    public boolean isEmpty() {
        return this.internalList.size() == 0;
    }

    /**
     * Returns the size of this list.
     */
    public int size() {
        return this.internalList.size();
    }

    /**
     * Replaces the contents of this list with {@code items}.
     * {@code items} must not contain duplicate items.
     * @param items Items to replace.
     * @return This list itself.
     */
    public UniqueList<T> setItems(List<T> items) {
        requireAllNonNull(items);
        assert itemsAreUnique(items) : "All items in the list should be unique.";
        internalList.setAll(items);
        return this;
    }

    public UniqueList<T> setItems(UniqueList<T> items) {
        assert items != null : "items should not be null";
        internalList.setAll(items.internalList);
        return this;
    }

    private boolean itemsAreUnique(List<T> items) {
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                if (items.get(i).isSame(items.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public ObservableList<T> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    public Stream<T> stream() {
        return this.internalList.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return internalList.iterator();
    }

    /**
     * Creates a copy of this list.
     * This copy can be used to avoid modifying this list.
     */
    public UniqueList<T> copy() {
        UniqueList<T> newUniqueList = new UniqueList<>();
        newUniqueList.addAllFromList(this);
        return newUniqueList;
    }

    @Override
    public boolean equals(Object another) {
        if (another == this) {
            return true;
        }

        if (!(another instanceof UniqueList<?>)) {
            return false;
        }

        UniqueList<?> anotherList = (UniqueList<?>) another;
        return internalList.equals(anotherList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }
}
