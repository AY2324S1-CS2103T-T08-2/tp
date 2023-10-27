package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static networkbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.CollectionUtil;
import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
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
 * Adds new information about a contact.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds new information about a contact. "
            + "Parameters: [LIST INDEX OF CONTACT]"
            + "[" + CliSyntax.PREFIX_PHONE + " PHONE] "
            + "[" + CliSyntax.PREFIX_EMAIL + " EMAIL] "
            + "[" + CliSyntax.PREFIX_LINK + "LINK] "
            + "[" + CliSyntax.PREFIX_GRADUATION + " GRADUATION DATE] "
            + "[" + CliSyntax.PREFIX_COURSE + "COURSE OF STUDY] "
            + "[" + CliSyntax.PREFIX_SPECIALISATION + "SPECIALISATION] "
            + "[" + CliSyntax.PREFIX_TAG + " TAG] "
            + "[" + CliSyntax.PREFIX_PRIORITY + " PRIORITY]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + CliSyntax.PREFIX_PHONE + " 98765432 "
            + CliSyntax.PREFIX_EMAIL + " johnd@example.com "
            + CliSyntax.PREFIX_TAG + " friends "
            + CliSyntax.PREFIX_TAG + " owesMoney";

    public static final String MESSAGE_NO_INFO = "At least one field to add must be provided.";
    public static final String MESSAGE_MULTIPLE_UNIQUE_FIELD = "Some fields provided cannot have multiple values.\n"
            + "Please use the 'update' command instead.";
    public static final String MESSAGE_ADD_INFO_SUCCESS = "Added information to this contact: %1$s";

    private final Index index;
    private final AddPersonDescriptor addPersonDescriptor;

    /**
     * Creates an AddCommand to add information about the contact at {@code Index}
     *
     * @param index of the contact to add information about
     * @param addPersonDescriptor details to add to the contact
     */
    public AddCommand(Index index, AddPersonDescriptor addPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(addPersonDescriptor);

        this.index = index;
        this.addPersonDescriptor = addPersonDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model should not be null";
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToAddInfo = lastShownList.get(index.getZeroBased());
        Person personAfterAddingInfo = addInfoToPerson(personToAddInfo, addPersonDescriptor);
        assert !personToAddInfo.equals(personAfterAddingInfo)
                : "person after adding info must differ from person before adding info";
        model.setItem(personToAddInfo, personAfterAddingInfo);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_INFO_SUCCESS, Messages.format(personAfterAddingInfo)));
    }

    /**
     * Adds details from {@code addPersonDescriptor} and returns the {@code Person} after adding details.
     */
    private Person addInfoToPerson(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor)
            throws CommandException {
        assert personToAddInfo != null;
        Name currentName = personToAddInfo.getName(); // name cannot be added
        UniqueList<Phone> updatedPhones = addPhones(personToAddInfo, addPersonDescriptor);
        UniqueList<Email> updatedEmails = addEmails(personToAddInfo, addPersonDescriptor);
        UniqueList<Link> updatedLinks = addLinks(personToAddInfo, addPersonDescriptor);
        Graduation updatedGraduatingYear = addGraduation(personToAddInfo, addPersonDescriptor);
        UniqueList<Course> updatedCourses = addCourses(personToAddInfo, addPersonDescriptor);
        UniqueList<Specialisation> updatedSpecialisations = addSpecialisations(personToAddInfo, addPersonDescriptor);
        UniqueList<Tag> updatedTags = addTags(personToAddInfo, addPersonDescriptor);
        Priority updatedPriority = addPriority(personToAddInfo, addPersonDescriptor);

        return new Person(currentName, updatedPhones, updatedEmails, updatedLinks, updatedGraduatingYear,
                updatedCourses, updatedSpecialisations, updatedTags, updatedPriority);
    }

    private UniqueList<Phone> addPhones(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor) {
        UniqueList<Phone> phones = personToAddInfo.getPhones();
        addPersonDescriptor.getPhones().ifPresent(phones::addAllFromList);
        return phones;
    }
    private UniqueList<Email> addEmails(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor) {
        UniqueList<Email> emails = personToAddInfo.getEmails();
        addPersonDescriptor.getEmails().ifPresent(emails::addAllFromList);
        return emails;
    }
    private UniqueList<Link> addLinks(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor) {
        UniqueList<Link> links = personToAddInfo.getLinks();
        addPersonDescriptor.getLinks().ifPresent(links::addAllFromList);
        return links;
    }
    private Graduation addGraduation(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor)
            throws CommandException {
        Optional<Graduation> oldGraduation = personToAddInfo.getGraduation();
        Optional<Graduation> newGraduation = addPersonDescriptor.getGraduation();
        if (oldGraduation.isPresent() && newGraduation.isPresent()) {
            throw new CommandException(MESSAGE_MULTIPLE_UNIQUE_FIELD);
        }
        return newGraduation.orElse(oldGraduation.orElse(null));
    }
    private UniqueList<Course> addCourses(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor) {
        UniqueList<Course> courses = personToAddInfo.getCourses();
        addPersonDescriptor.getCourses().ifPresent(courses::addAllFromList);
        return courses;
    }
    private UniqueList<Specialisation> addSpecialisations(Person personToAddInfo,
                                                           AddPersonDescriptor addPersonDescriptor) {
        UniqueList<Specialisation> specs = personToAddInfo.getSpecialisations();
        addPersonDescriptor.getSpecialisations().ifPresent(specs::addAllFromList);
        return specs;

    }
    private UniqueList<Tag> addTags(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor) {
        UniqueList<Tag> tags = personToAddInfo.getTags();
        addPersonDescriptor.getTags().ifPresent(tags::addAllFromList);
        return tags;
    }

    private Priority addPriority(Person personToAddInfo, AddPersonDescriptor addPersonDescriptor)
            throws CommandException {
        Optional<Priority> oldPriority = personToAddInfo.getPriority();
        Optional<Priority> newPriority = addPersonDescriptor.getPriority();
        if (oldPriority.isPresent() && newPriority.isPresent()) {
            throw new CommandException(MESSAGE_MULTIPLE_UNIQUE_FIELD);
        }
        return newPriority.orElse(oldPriority.orElse(null));
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherCreateCommand = (AddCommand) other;
        return index.equals(otherCreateCommand.index)
                && addPersonDescriptor.equals(otherCreateCommand.addPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("addPersonDescriptor", addPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class AddPersonDescriptor {
        private UniqueList<Phone> phones;
        private UniqueList<Email> emails;
        private UniqueList<Link> links;
        private Graduation graduation;
        private UniqueList<Course> courses;
        private UniqueList<Specialisation> specialisations;
        private UniqueList<Tag> tags;
        private Priority priority;

        public AddPersonDescriptor() {}

        /**
         * Copy constructor.
         */
        public AddPersonDescriptor(AddPersonDescriptor toCopy) {
            setPhones(toCopy.getPhones().map(UniqueList::copy).orElse(null));
            setEmails(toCopy.getEmails().map(UniqueList::copy).orElse(null));
            setLinks(toCopy.getLinks().map(UniqueList::copy).orElse(null));
            setGraduation(toCopy.graduation);
            setCourses(toCopy.getCourses().map(UniqueList::copy).orElse(null));
            setSpecialisations(toCopy.getSpecialisations().map(UniqueList::copy).orElse(null));
            setTags(toCopy.getTags().map(UniqueList::copy).orElse(null));
            setPriority(toCopy.priority);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(graduation, tags, priority)
                    || (phones != null && !phones.isEmpty())
                    || (emails != null && !emails.isEmpty())
                    || (links != null && !links.isEmpty())
                    || (courses != null && !courses.isEmpty())
                    || (specialisations != null && !specialisations.isEmpty());
        }

        public void setPhones(UniqueList<Phone> phones) {
            this.phones = phones;
        }

        /**
         * Adds {@code phone} to the list of {@code Phone}s.
         */
        public void addPhone(Phone phone) {
            this.phones = Optional.ofNullable(this.phones).map(phones -> {
                phones.add(phone);
                return phones;
            }).or(() -> {
                UniqueList<Phone> uniqueList = new UniqueList<>();
                uniqueList.add(phone);
                return Optional.of(uniqueList);
            }).get();
        }

        public Optional<UniqueList<Phone>> getPhones() {
            return Optional.ofNullable(phones);
        }

        public void setEmails(UniqueList<Email> emails) {
            this.emails = emails;
        }

        /**
         * Adds {@code email} to the list of {@code emails}
         */
        public void addEmail(Email email) {
            this.emails = Optional.ofNullable(this.emails).map(emails -> {
                emails.add(email);
                return emails;
            }).or(() -> {
                UniqueList<Email> uniqueList = new UniqueList<>();
                uniqueList.add(email);
                return Optional.of(uniqueList);
            }).get();
        }

        public Optional<UniqueList<Email>> getEmails() {
            return Optional.ofNullable(emails);
        }

        public void setLinks(UniqueList<Link> links) {
            this.links = links;
        }

        /**
         * Adds {@code link} to the list of {@code links}.
         */
        public void addLink(Link link) {
            this.links = Optional.ofNullable(this.links).map(links -> {
                links.add(link);
                return links;
            }).or(() -> {
                UniqueList<Link> uniqueList = new UniqueList<>();
                uniqueList.add(link);
                return Optional.of(uniqueList);
            }).get();
        }

        public Optional<UniqueList<Link>> getLinks() {
            return Optional.ofNullable(links);
        }

        public void setGraduation(Graduation graduation) {
            this.graduation = graduation;
        }

        public Optional<Graduation> getGraduation() {
            return Optional.ofNullable(graduation);
        }

        public void setCourses(UniqueList<Course> courses) {
            this.courses = courses;
        }

        /**
         * Adds {@code specialisations} to the list of {@code specialisations}.
         */
        public void addCourse(Course course) {
            this.courses = Optional.ofNullable(this.courses).map(courses -> {
                courses.add(course);
                return courses;
            }).or(() -> {
                UniqueList<Course> uniqueList = new UniqueList<>();
                uniqueList.add(course);
                return Optional.of(uniqueList);
            }).get();
        }

        public Optional<UniqueList<Course>> getCourses() {
            return Optional.ofNullable(courses);
        }

        public void setSpecialisations(UniqueList<Specialisation> specialisations) {
            this.specialisations = specialisations;
        }

        /**
         * Adds {@code specialisations} to the list of {@code specialisations}.
         */
        public void addSpecialisation(Specialisation specialisation) {
            this.specialisations = Optional.ofNullable(this.specialisations).map(specialisations -> {
                specialisations.add(specialisation);
                return specialisations;
            }).or(() -> {
                UniqueList<Specialisation> uniqueList = new UniqueList<>();
                uniqueList.add(specialisation);
                return Optional.of(uniqueList);
            }).get();
        }

        public Optional<UniqueList<Specialisation>> getSpecialisations() {
            return Optional.ofNullable(specialisations);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(UniqueList<Tag> tags) {
            this.tags = tags;
        }

        /**
         * Adds {@code tag} to the {@code UniqueList<tag>} of the descriptor.
         * If the tags field is null, initialize it with an empty {@code UniqueList}.
         *
         * @param tag is the new tag to be added.
         */
        public void addTag(Tag tag) {
            if (this.tags == null) {
                this.tags = new UniqueList<>();
            }
            this.tags.add(tag);
        }

        public Optional<UniqueList<Tag>> getTags() {
            return Optional.ofNullable(tags);
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(this.priority);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof AddPersonDescriptor)) {
                return false;
            }

            AddPersonDescriptor otherAddPersonDescriptor = (AddPersonDescriptor) other;
            return Objects.equals(phones, otherAddPersonDescriptor.phones)
                    && Objects.equals(emails, otherAddPersonDescriptor.emails)
                    && Objects.equals(links, otherAddPersonDescriptor.links)
                    && Objects.equals(graduation, otherAddPersonDescriptor.graduation)
                    && Objects.equals(courses, otherAddPersonDescriptor.courses)
                    && Objects.equals(specialisations, otherAddPersonDescriptor.specialisations)
                    && Objects.equals(tags, otherAddPersonDescriptor.tags)
                    && Objects.equals(priority, otherAddPersonDescriptor.priority);
        }

        @Override
        public String toString() {
            ToStringBuilder tsb = new ToStringBuilder(this)
                    .add("phones", phones)
                    .add("emails", emails)
                    .add("links", links)
                    .add("graduation", graduation)
                    .add("courses", courses)
                    .add("specialisations", specialisations)
                    .add("priority", priority)
                    .add("tags", tags);
            return tsb.toString();
        }
    }
}
