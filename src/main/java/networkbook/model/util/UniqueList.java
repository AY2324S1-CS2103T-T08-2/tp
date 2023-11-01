package networkbook.model.util;

import static networkbook.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networkbook.commons.util.ThrowingIoExceptionConsumer;

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
     * This makes use of {@code T::isSame}.
     * @param toCheck The element to check.
     */
    public boolean contains(T toCheck) {
        assert toCheck != null : "T toCheck should not be null";
        return internalList.stream().anyMatch(toCheck::isSame);
    }

    /**
     * Checks whether the list contains the element {@code toCheck} not at the specified {@code index}.
     * Meaning that element at the specified {@code index} is ignored.
     */
    public boolean containsNotAtIndex(T toCheck, int index) {
        assert toCheck != null;
        assert index >= 0;
        assert index < this.size();

        for (int i = 0; i < internalList.size(); i++) {
            if (i != index && internalList.get(i).isSame(toCheck)) {
                return true;
            }
        }
        return false;
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
        return this.internalList.isEmpty();
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

    /**
     * Tests if the item at the specified {@code index} satisfies the {@code predicate}.
     */
    public boolean test(int index, Predicate<T> predicate) {
        assert predicate != null;
        assert index >= 0;
        assert index < this.internalList.size();
        return predicate.test(this.internalList.get(index));
    }

    /**
     * Provides the item at the given {@code index} to the {@code consumer}.
     * The consumer may throw an exception.
     * @throws IOException If the consumer throws {@code IOException}.
     */
    public void consumeItem(int index, ThrowingIoExceptionConsumer<T> consumer) throws IOException {
        assert consumer != null;
        assert index >= 0;
        assert index < this.internalList.size();
        consumer.accept(this.internalList.get(index));
    }

    /**
     * Provides the item at the given {@code index} and
     * returns the result of applying the provided {@code function} on the item at the given {@code index}.
     * @throws IOException If the consumer throws an IOException.
     */
    public <U> U consumeAndComputeItem(int index, ThrowingIoExceptionConsumer<T> consumer,
                                       Function<T, U> function) throws IOException {
        assert consumer != null;
        assert function != null;
        assert index >= 0;
        assert index < this.internalList.size();
        T item = this.internalList.get(index);
        consumer.accept(item);
        return function.apply(item);
    }

    public T get(int index) {
        return this.internalList.get(index);
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
