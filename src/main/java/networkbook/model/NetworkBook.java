package networkbook.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;

import javafx.collections.ObservableList;
import networkbook.commons.core.index.Index;
import networkbook.commons.util.ToStringBuilder;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.model.util.Identifiable;
import networkbook.model.util.UniqueList;

/**
 * Wraps all data at the network-book level
 * Duplicate contacts are not allowed (by .isSame comparison)
 */
public class NetworkBook implements ReadOnlyNetworkBook, Identifiable<NetworkBook> {

    private final UniqueList<Person> persons;
    public NetworkBook() {
        this.persons = new UniqueList<>();
    }

    /**
     * Creates an NetworkBook using the Persons in the {@code toBeCopied}
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
     */
    public Link openLink(Index personIndex, Index linkIndex) throws IOException {
        return persons.consumeAndComputeItem(personIndex.getZeroBased(),
                person -> person.openLink(linkIndex), person -> person.getLink(linkIndex.getZeroBased()));
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
    public boolean isSame(NetworkBook another) {
        return this == another;
    }

    @Override
    public String getValue() {
        return "";
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
        return persons.equals(otherNetworkBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
