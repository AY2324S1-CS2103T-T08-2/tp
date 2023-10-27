package networkbook.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;

/**
 * Class that contains temporary information about a person whose field is being deleted.
 */
public class DeletePersonDescriptor {
    public static final String MESSAGE_INVALID_PHONE_INDEX = "The phone index provided is invalid.";
    public static final String MESSAGE_INVALID_EMAIL_INDEX = "The email index provided is invalid.";
    public static final String MESSAGE_INVALID_LINK_INDEX = "The link index provided is invalid.";
    public static final String MESSAGE_INVALID_COURSE_INDEX = "The course index provided is invalid.";
    public static final String MESSAGE_INVALID_SPECIALISATION_INDEX = "The specialisation index provided is invalid.";
    public static final String MESSAGE_INVALID_TAG_INDEX = "The tag index provided is invalid.";
    private Name name;
    private UniqueList<Phone> phones;
    private UniqueList<Email> emails;
    private UniqueList<Link> links;
    private Graduation graduation;
    private UniqueList<Course> courses;
    private UniqueList<Specialisation> specialisations;
    private UniqueList<Tag> tags;
    private Priority priority;

    /**
     * Constructs a new person editor from the original {@code person}.
     */
    public DeletePersonDescriptor(Person person) {
        this.name = person.getName();
        this.phones = person.getPhones();
        this.emails = person.getEmails();
        this.links = person.getLinks();
        this.graduation = person.getGraduation().orElse(null);
        this.courses = person.getCourses();
        this.specialisations = person.getSpecialisations();
        this.tags = person.getTags();
        this.priority = person.getPriority().orElse(null);
    }

    /**
     * Deletes the phone entry at {@code index} in the descriptor's list of phones.
     * @param index is the zero-based index of the entry to delete.
     * @throws CommandException when the index is invalid in the list.
     */
    public void deletePhone(Index index) throws CommandException {
        requireNonNull(index);
        if (index.getZeroBased() >= this.phones.size()) {
            throw new CommandException(MESSAGE_INVALID_PHONE_INDEX);
        }
        this.phones.removeAtIndex(index.getZeroBased());
    }

    /**
     * Deletes the email entry at {@code index} in the descriptor's list of emails.
     * @param index is the zero-based index of the entry to delete.
     * @throws CommandException when the index is invalid in the list.
     */
    public void deleteEmail(Index index) throws CommandException {
        requireNonNull(index);
        if (index.getZeroBased() >= this.emails.size()) {
            throw new CommandException(MESSAGE_INVALID_EMAIL_INDEX);
        }
        this.emails.removeAtIndex(index.getZeroBased());
    }

    /**
     * Deletes the link entry at {@code index} in the descriptor's list of links.
     * @param index is the zero-based index of the entry to delete.
     * @throws CommandException when the index is invalid in the list.
     */
    public void deleteLink(Index index) throws CommandException {
        requireNonNull(index);
        if (index.getZeroBased() >= this.links.size()) {
            throw new CommandException(MESSAGE_INVALID_LINK_INDEX);
        }
        this.links.removeAtIndex(index.getZeroBased());
    }

    /**
     * Deletes the course entry at {@code index} in the descriptor's list of courses.
     * @param index is the zero-based index of the entry to delete.
     * @throws CommandException when the index is invalid in the list.
     */
    public void deleteCourse(Index index) throws CommandException {
        requireNonNull(index);
        if (index.getZeroBased() >= this.courses.size()) {
            throw new CommandException(MESSAGE_INVALID_COURSE_INDEX);
        }
        this.courses.removeAtIndex(index.getZeroBased());
    }

    /**
     * Deletes the specialisation entry at {@code index} in the descriptor's list of specialisations.
     * @param index is the zero-based index of the entry to delete.
     * @throws CommandException when the index is invalid in the list.
     */
    public void deleteSpecialisation(Index index) throws CommandException {
        requireNonNull(index);
        if (index.getZeroBased() >= this.specialisations.size()) {
            throw new CommandException(MESSAGE_INVALID_SPECIALISATION_INDEX);
        }
        this.specialisations.removeAtIndex(index.getZeroBased());
    }

    /**
     * Deletes the tag entry at {@code index} in the descriptor's list of tags.
     * @param index is the zero-based index of the entry to delete.
     * @throws CommandException when the index is invalid in the list.
     */
    public void deleteTag(Index index) throws CommandException {
        requireNonNull(index);
        if (index.getZeroBased() >= this.tags.size()) {
            throw new CommandException(MESSAGE_INVALID_TAG_INDEX);
        }
        this.tags.removeAtIndex(index.getZeroBased());
    }

    public void deleteGraduation() {
        this.graduation = null;
    }

    public void deletePriority() {
        this.priority = null;
    }

    public Person toPerson() {
        return new Person(name, phones, emails, links, graduation, courses, specialisations, tags, priority);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeletePersonDescriptor)) {
            return false;
        }

        DeletePersonDescriptor otherDescriptor = (DeletePersonDescriptor) object;
        return this.name.equals(otherDescriptor.name)
                && this.phones.equals(otherDescriptor.phones)
                && this.emails.equals(otherDescriptor.emails)
                && this.links.equals(otherDescriptor.links)
                && Objects.equals(this.graduation, otherDescriptor.graduation)
                && this.courses.equals(otherDescriptor.courses)
                && this.specialisations.equals(otherDescriptor.specialisations)
                && this.tags.equals(otherDescriptor.tags)
                && Objects.equals(this.priority, otherDescriptor.priority);
    }
}
