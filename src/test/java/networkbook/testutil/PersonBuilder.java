package networkbook.testutil;

import java.util.List;
import java.util.stream.Collectors;

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
import networkbook.model.util.SampleDataUtil;
import networkbook.model.util.UniqueList;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_LINK = "github.com/amybeez";
    public static final String DEFAULT_GRADUATION = "AY0001-S1";
    public static final String DEFAULT_COURSE = "Computer Science";
    public static final String DEFAULT_SPECIALISATION = "Game Development";
    public static final String DEFAULT_PRIORITY = null;

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
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phones = new UniqueList<Phone>().setItems(List.of(new Phone(DEFAULT_PHONE)));
        emails = new UniqueList<Email>().setItems(List.of(new Email(DEFAULT_EMAIL)));
        links = new UniqueList<Link>().setItems(List.of(new Link(DEFAULT_LINK)));
        graduation = new Graduation(DEFAULT_GRADUATION);
        courses = new UniqueList<Course>().setItems(List.of(new Course(DEFAULT_COURSE)));
        specialisations = new UniqueList<Specialisation>()
                .setItems(List.of(new Specialisation(DEFAULT_SPECIALISATION)));
        tags = new UniqueList<>();
        priority = null;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phones = personToCopy.getPhones();
        emails = personToCopy.getEmails();
        links = personToCopy.getLinks();
        graduation = personToCopy.getGraduation().orElse(null);
        courses = personToCopy.getCourses();
        specialisations = personToCopy.getSpecialisations();
        tags = personToCopy.getTags();
        priority = personToCopy.getPriority().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code UniqueList<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagList(tags);
        return this;
    }

    /**
     * Adds a link to the person we are building.
     */
    public PersonBuilder addLink(String link) {
        this.links.add(new Link(link));
        return this;
    }

    /**
     * Sets the list of link of the person that we are building.
     */
    public PersonBuilder withLinks(List<String> links) {
        this.links = new UniqueList<Link>().setItems(links.stream().map(Link::new).collect(Collectors.toList()));
        return this;
    }

    /**
     * Sets the {@code Graduation} of the {@code Person} that we are building.
     */
    public PersonBuilder withGraduation(String graduation) {
        this.graduation = new Graduation(graduation);
        return this;
    }

    /**
     * Adds a course to the person we are building.
     */
    public PersonBuilder addCourse(String course) {
        this.courses.add(new Course(course));
        return this;
    }

    /**
     * Adds a course with a start date to the person we are building.
     */
    public PersonBuilder addCourse(String course, String startDate) {
        this.courses.add(new Course(course, startDate));
        return this;
    }
    /**
     * Adds a course with a start and end date to the person we are building.
     */
    public PersonBuilder addCourse(String course, String startDate, String endDate) {
        this.courses.add(new Course(course, startDate, endDate));
        return this;
    }

    /**
     * Sets the {@code Course} of the {@code Person} that we are building.
     */
    public PersonBuilder withCourses(List<String> courses) {
        this.courses = new UniqueList<Course>()
                .setItems(courses.stream().map(Course::new).collect(Collectors.toList()));
        return this;
    }

    /**
     * Adds a specialisation to the person we are building.
     */
    public PersonBuilder addSpecialisation(String specialisation) {
        this.specialisations.add(new Specialisation(specialisation));
        return this;
    }

    /**
     * Sets the {@code Specialisation} of the {@code Person} that we are building.
     */
    public PersonBuilder withSpecialisations(List<String> specialisations) {
        this.specialisations = new UniqueList<Specialisation>()
                .setItems(specialisations.stream().map(Specialisation::new).collect(Collectors.toList()));
        return this;
    }

    /**
     * Adds a phone to the person we are building.
     */
    public PersonBuilder addPhone(String phone) {
        this.phones.add(new Phone(phone));
        return this;
    }
    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhones(List<String> phones) {
        this.phones = new UniqueList<Phone>().setItems(phones.stream().map(Phone::new).collect(Collectors.toList()));
        return this;
    }

    /**
     * Adds an email to the person we are building.
     */
    public PersonBuilder addEmail(String email) {
        this.emails.add(new Email(email));
        return this;
    }
    /**
     * Sets the emails of the person that we are building.
     */
    public PersonBuilder withEmails(List<String> emails) {
        this.emails = new UniqueList<Email>().setItems(emails.stream().map(Email::new).collect(Collectors.toList()));
        return this;
    }

    /**
     * Sets the {@code Priority} of the {@code Person} that we are building.
     */
    public PersonBuilder withPriority(String priority) {
        this.priority = new Priority(priority);
        return this;
    }

    /**
     * Sets all fields of the {@code Person} that we are building to null.
     */
    public PersonBuilder withoutOptionalFields() {
        this.phones = new UniqueList<>();
        this.emails = new UniqueList<>();
        this.links = new UniqueList<>();
        this.graduation = null;
        this.courses = new UniqueList<>();
        this.specialisations = new UniqueList<>();
        this.tags = new UniqueList<>();
        this.priority = null;
        return this;
    }

    public Person build() {
        return new Person(name, phones, emails, links, graduation, courses, specialisations, tags, priority);
    }

}
