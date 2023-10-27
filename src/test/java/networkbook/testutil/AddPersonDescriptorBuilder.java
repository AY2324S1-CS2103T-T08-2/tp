package networkbook.testutil;


import networkbook.logic.commands.AddCommand;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;

/**
 * A utility class to help with building AddPersonDescriptor objects.
 */
public class AddPersonDescriptorBuilder {

    private AddCommand.AddPersonDescriptor descriptor;

    public AddPersonDescriptorBuilder() {
        descriptor = new AddCommand.AddPersonDescriptor();
    }

    public AddPersonDescriptorBuilder(AddCommand.AddPersonDescriptor descriptor) {
        this.descriptor = new AddCommand.AddPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code AddPersonDescriptor} with fields containing {@code person}'s details
     */
    public AddPersonDescriptorBuilder(Person person) {
        descriptor = new AddCommand.AddPersonDescriptor();
        descriptor.setPhones(person.getPhones());
        descriptor.setEmails(person.getEmails());
        descriptor.setLinks(person.getLinks());
        person.getGraduation().ifPresent((Graduation g) -> descriptor.setGraduation(g));
        descriptor.setCourses(person.getCourses());
        descriptor.setSpecialisations(person.getSpecialisations());
        descriptor.setTags(person.getTags());
        person.getPriority().ifPresent((Priority p) -> descriptor.setPriority(p));
    }

    /**
     * Sets the {@code Phone} of the {@code AddPersonDescriptor} that we are building.
     */
    public AddPersonDescriptorBuilder withPhone(String phone) {
        descriptor.addPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code AddPersonDescriptor} that we are building.
     */
    public AddPersonDescriptorBuilder withEmail(String email) {
        descriptor.addEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Link} of the {@code AddPersonDescriptor} that we are building.
     */
    public AddPersonDescriptorBuilder withLink(String link) {
        descriptor.addLink(new Link(link));
        return this;
    }

    /**
     * Sets the {@code Graduation} of the {@code AddPersonDescriptor} that we are building.
     */
    public AddPersonDescriptorBuilder withGraduation(String graduation) {
        descriptor.setGraduation(new Graduation(graduation));
        return this;
    }

    /**
     * Sets the {@code Course} of the {@code AddPersonDescriptor} that we are building.
     */
    public AddPersonDescriptorBuilder withCourse(String course) {
        descriptor.addCourse(new Course(course));
        return this;
    }

    /**
     * Sets the {@code Specialisation} of the {@code AddPersonDescriptor} that we are building.
     */
    public AddPersonDescriptorBuilder withSpecialisation(String specialisation) {
        descriptor.addSpecialisation(new Specialisation(specialisation));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code AddPersonDescriptor}
     * that we are building.
     */
    public AddPersonDescriptorBuilder withTags(String... tags) {
        descriptor.setTags(new UniqueList<>());
        for (String tag : tags) {
            descriptor.addTag(new Tag(tag));
        }
        return this;
    }

    /**
     * Sets the {@code Priority} of the {@code AddPersonDescriptor} that we are building.
     */
    public AddPersonDescriptorBuilder withPriority(String priority) {
        descriptor.setPriority(new Priority(priority));
        return this;
    }

    public AddCommand.AddPersonDescriptor build() {
        return descriptor;
    }
}
