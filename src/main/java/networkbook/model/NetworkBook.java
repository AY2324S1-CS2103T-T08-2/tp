package networkbook.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import networkbook.commons.core.index.Index;
import networkbook.commons.util.ToStringBuilder;
import networkbook.model.person.Email;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.util.UniqueList;

/**
 * Wraps all data at the network-book level
 * Duplicate contacts are not allowed (by .isSame comparison)
 */
public class NetworkBook implements ReadOnlyNetworkBook {

    private final UniqueList<Person> persons;
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> displayedPersons;
    /**
     * Creates a NetworkBook without any Persons.
     */
    public NetworkBook() {
        persons = new UniqueList<>();
        filteredPersons = new FilteredList<>(persons.asUnmodifiableObservableList());
        displayedPersons = new SortedList<>(filteredPersons,
                new PersonSortComparator(PersonSortComparator.SortField.NAME,
                                        PersonSortComparator.SortOrder.ASCENDING));
    }

    /**
     * Creates a NetworkBook using the Persons in the {@code toBeCopied}.
     */
    public NetworkBook(ReadOnlyNetworkBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setItems(List<Person> persons) {
        this.persons.setItems(persons);
    }

    /**
     * Resets the existing data of this {@code NetworkBook} with {@code newData}.
     */
    public void resetData(ReadOnlyNetworkBook newData) {
        requireNonNull(newData);
        setItems(newData.getPersonList());
        Optional.ofNullable(newData.getFilterPredicate()).ifPresent(this::setFilterPredicate);
        Optional.ofNullable(newData.getSortComparator()).ifPresent(this::setSortComparator);
    }

    /**
     * Updates the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    public void setFilterPredicate(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public Predicate<Person> getFilterPredicate() {
        return (Predicate<Person>) filteredPersons.getPredicate();
    }

    /**
     * Updates the sort of the filtered person list to sort by the given {@code comparator}.
     * @throws NullPointerException if {@code comparator} is null.
     */
    public void setSortComparator(Comparator<Person> comparator) {
        requireNonNull(comparator);
        displayedPersons.setComparator(comparator);
    }

    @Override
    public Comparator<Person> getSortComparator() {
        return (Comparator<Person>) displayedPersons.getComparator();
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the network book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the network book.
     * The person must not already exist in the network book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the network book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the network book.
     */
    public void setItem(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        persons.setItem(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code NetworkBook}.
     * {@code key} must exist in the network book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Checks if the indices for a link of a contact is valid.
     */
    public boolean isValidLinkIndex(Index personIndex, Index linkIndex) {
        return persons.test(personIndex.getZeroBased(), person -> person.isValidLinkIndex(linkIndex));
    }

    /**
     * Opens the link at {@code linkIndex} in the link list of the person
     * at index {@code personIndex}.
     * @return The link that has been opened.
     */
    public Link openLink(Index personIndex, Index linkIndex) throws IOException {
        return persons.consumeAndComputeItem(personIndex.getZeroBased(),
                person -> person.openLink(linkIndex), person -> person.getLink(linkIndex.getZeroBased()));
    }

    /**
     * Checks if the indices for a link of a contact is valid.
     */
    public boolean isValidEmailIndex(Index personIndex, Index emailIndex) {
        return persons.test(personIndex.getZeroBased(), person -> person.isValidEmailIndex(emailIndex));
    }

    /**
     * Opens the email at {@code emailIndex} in the email list of the person
     * at index {@code personIndex}.
     * @return The email that has been opened.
     */
    public Email openEmail(Index personIndex, Index emailIndex) throws IOException {
        return persons.consumeAndComputeItem(personIndex.getZeroBased(),
                person -> person.openEmail(emailIndex), person -> person.getEmail(emailIndex.getZeroBased()));
    }

    //// util methods
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }
    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Person> getDisplayedPersonList() {
        return displayedPersons;
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NetworkBook)) {
            return false;
        }

        NetworkBook otherNetworkBook = (NetworkBook) other;
        UniqueList<Person> localFilteredPersons = new UniqueList<>();
        UniqueList<Person> localDisplayedPersons = new UniqueList<>();
        UniqueList<Person> otherFilteredPersons = new UniqueList<>();
        UniqueList<Person> otherDisplayedPersons = new UniqueList<>();
        localFilteredPersons.setItems(this.filteredPersons);
        localDisplayedPersons.setItems(this.displayedPersons);
        otherFilteredPersons.setItems(otherNetworkBook.filteredPersons);
        otherDisplayedPersons.setItems(otherNetworkBook.displayedPersons);
        return this.persons.equals(otherNetworkBook.persons)
                && localFilteredPersons.equals(otherFilteredPersons)
                && localDisplayedPersons.equals(otherDisplayedPersons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
