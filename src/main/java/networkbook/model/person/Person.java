package networkbook.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import networkbook.commons.util.ToStringBuilder;
import networkbook.model.util.Identifiable;
import networkbook.model.util.UniqueList;

/**
 * Represents a Person in the network book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person implements Identifiable<Person> {
    private final Name name;
    private final UniqueList<Phone> phones;
    private final UniqueList<Email> emails;
    private final UniqueList<Link> links;
    private final Graduation graduation;
    private final UniqueList<Course> courses;
    private final UniqueList<Specialisation> specialisations;
    private final UniqueList<Tag> tags;
    private final Priority priority;

    /**
     * Name must be present and not null.
     * Other fields are nullable.
     */
    public Person(Name name,
                  UniqueList<Phone> phones,
                  UniqueList<Email> emails,
                  UniqueList<Link> links,
                  Graduation graduation,
                  UniqueList<Course> courses,
                  UniqueList<Specialisation> specialisations,
                  UniqueList<Tag> tags,
                  Priority priority) {
        requireNonNull(name);
        this.name = name;
        this.phones = phones;
        this.emails = emails.copy();
        this.links = links.copy();
        this.graduation = graduation;
        this.courses = courses.copy();
        this.specialisations = specialisations.copy();
        this.tags = tags.copy();
        this.priority = priority;
    }

    public Name getName() {
        return name;
    }

    public UniqueList<Phone> getPhones() {
        return phones.copy();
    }

    public UniqueList<Email> getEmails() {
        return emails.copy();
    }
    public UniqueList<Link> getLinks() {
        return links.copy();
    }
    public Optional<Graduation> getGraduation() {
        return Optional.ofNullable(graduation);
    }
    public UniqueList<Course> getCourses() {
        return courses.copy();
    }
    public UniqueList<Specialisation> getSpecialisations() {
        return specialisations.copy();
    }
    public UniqueList<Tag> getTags() {
        return this.tags.copy();
    }

    public Optional<Priority> getPriority() {
        return Optional.ofNullable(priority);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSame(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns string for Json storage.
     * However, a person cannot be converted to a simple string for Json storage.
     * Hence, this method is unsupported here.
     */
    public String getValue() {
        throw new UnsupportedOperationException("Person does not have String representation for Json storage");
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && Objects.equals(phones, otherPerson.phones)
                && Objects.equals(emails, otherPerson.emails)
                && Objects.equals(links, otherPerson.links)
                && Objects.equals(graduation, otherPerson.graduation)
                && Objects.equals(courses, otherPerson.courses)
                && Objects.equals(specialisations, otherPerson.specialisations)
                && Objects.equals(tags, otherPerson.tags)
                && Objects.equals(priority, otherPerson.priority);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phones, emails, links, graduation, courses, specialisations, tags, priority);
    }

    @Override
    public String toString() {
        ToStringBuilder tsb = new ToStringBuilder(this)
                .add("name", name);
        if (!Objects.equals(phones, new UniqueList<Phone>())) {
            tsb.add("phones", phones);
        }
        if (!Objects.equals(emails, new UniqueList<Email>())) {
            tsb.add("emails", emails);
        }
        if (!Objects.equals(links, new UniqueList<Link>())) {
            tsb.add("links", links);
        }
        if (graduation != null) {
            tsb.add("graduation", graduation);
        }
        if (courses != null) {
            tsb.add("courses", courses);
        }
        if (!Objects.equals(specialisations, new UniqueList<Specialisation>())) {
            tsb.add("specialisations", specialisations);
        }
        if (!Objects.equals(tags, new UniqueList<Tag>())) {
            tsb.add("tags", tags);
        }
        if (priority != null) {
            tsb.add("priority", priority);
        }
        return tsb.toString();
    }

}
