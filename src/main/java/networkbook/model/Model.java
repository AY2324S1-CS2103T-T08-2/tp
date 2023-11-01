package networkbook.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import networkbook.commons.core.GuiSettings;
import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Link;
import networkbook.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' network book file path.
     */
    Path getNetworkBookFilePath();

    /**
     * Sets the user prefs' network book file path.
     */
    void setNetworkBookFilePath(Path networkBookFilePath);

    /**
     * Replaces network book data with the data in {@code networkBook}.
     */
    void setNetworkBook(ReadOnlyNetworkBook networkBook);

    /** Returns the NetworkBook */
    ReadOnlyNetworkBook getNetworkBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the network book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the network book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the network book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the network book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the network book.
     */
    void setItem(Person target, Person editedPerson);

    /**
     * Undoes the last command that changed the contacts stored in NetworkBook.
     * @throws CommandException if NetworkBook has no previous states stored to undo to.
     */
    void undoNetworkBook() throws CommandException;
    /**
     * Redoes a previously undone command that changed the contacts stored in NetworkBook.
     * @throws CommandException if NetworkBook has no subsequent states stored to redo to.
     */
    void redoNetworkBook() throws CommandException;

    /**
     * Checks if the indices for a link of a contact are valid.
     */
    boolean isValidLinkIndex(Index personIndex, Index linkIndex);

    /**
     * Opens the link at index {@code linkIndex} in the link list of the person
     * at index {@code personIndex}.
     */
    Link openLink(Index personIndex, Index linkIndex) throws IOException;

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the sort of the filtered person list to sort by the given {@code comparator}.
     * @throws NullPointerException if {@code comparator} is null.
     */
    void updateSortedPersonList(Comparator<Person> comparator);
}
