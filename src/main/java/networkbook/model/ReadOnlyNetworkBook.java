package networkbook.model;

import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import networkbook.model.person.Person;

/**
 * Unmodifiable view of an network book
 */
public interface ReadOnlyNetworkBook {

    /**
     * Returns an unmodifiable view of all persons stored in the list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of all persons displayed in the list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getDisplayedPersonList();

    /**
     * Returns the predicate determining the filtering of the person list.
     */
    public Predicate<Person> getFilterPredicate();

    /**
     * Returns the comparator determining the sorting of the filtered person list.
     */
    public Comparator<Person> getSortComparator();
}
