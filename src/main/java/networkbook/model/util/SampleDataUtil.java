package networkbook.model.util;

import java.util.List;

import networkbook.model.NetworkBook;
import networkbook.model.ReadOnlyNetworkBook;
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

/**
 * Contains utility methods for populating {@code NetworkBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"),
                    new UniqueList<Phone>().setItems(List.of(new Phone("87438807"))),
                    new UniqueList<Email>().setItems(List.of(new Email("alexyeoh@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("www.alexyeoh.net"))),
                    new Graduation("AY1617-S1"),
                    new UniqueList<Course>().setItems(List.of(new Course("Information Systems"))),
                    new UniqueList<Specialisation>()
                            .setItems(List.of(
                                    new Specialisation("Financial Technology"),
                                    new Specialisation("Quantitative Finance"))),
                    getTagList("friends"), new Priority("low")),
            new Person(new Name("Bernice Yu"),
                    new UniqueList<Phone>().setItems(List.of(new Phone("99272758"))),
                    new UniqueList<Email>().setItems(List.of(new Email("berniceyu@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("github.com/bernfish"))),
                    new Graduation("AY2021-S1"),
                    new UniqueList<Course>().setItems(List.of(new Course("Computer Science"))),
                    new UniqueList<Specialisation>()
                            .setItems(List.of(new Specialisation("Artificial Intelligence"))),
                    getTagList("colleagues", "friends"), new Priority("high")),
            new Person(new Name("Charlotte Oliveiro"),
                    new UniqueList<Phone>().setItems(List.of(new Phone("93210283"))),
                    new UniqueList<Email>().setItems(List.of(new Email("charlotte@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("facebook.com/Charlotte-Oliveiro"))),
                    new Graduation("AY9900-S2"),
                    new UniqueList<Course>().setItems(List.of(new Course("Computer Science"))),
                    new UniqueList<Specialisation>()
                            .setItems(List.of(new Specialisation("Computer Security"))),
                    getTagList("neighbours"), new Priority("M")),
            new Person(new Name("David Li"),
                    new UniqueList<Phone>().setItems(List.of(new Phone("91031282"))),
                    new UniqueList<Email>().setItems(List.of(new Email("lidavid@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("wordpress.com/specialli-mine"))),
                    new Graduation("AY2425-S1"),
                    new UniqueList<Course>().setItems(List.of(new Course("Computer Science"))),
                    new UniqueList<Specialisation>()
                            .setItems(List.of(new Specialisation("Database Systems"))),
                    getTagList("family"), new Priority("High")),
            new Person(new Name("Irfan Ibrahim"),
                    new UniqueList<Phone>().setItems(List.of(new Phone("92492021"))),
                    new UniqueList<Email>().setItems(List.of(new Email("irfan@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("instagram.com/irfanny"))),
                    new Graduation("AY2425-S2"),
                    new UniqueList<Course>().setItems(List.of(new Course("Computer Science"))),
                    new UniqueList<Specialisation>()
                            .setItems(List.of(new Specialisation("Parallel Computing"))),
                    getTagList("classmates"), new Priority("low")),
            new Person(new Name("Roy Balakrishnan"),
                    new UniqueList<Phone>().setItems(List.of(new Phone("92624417"))),
                    new UniqueList<Email>().setItems(List.of(new Email("royb@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("incognito.com"))),
                    new Graduation("AY2627-S1"),
                    new UniqueList<Course>().setItems(List.of(new Course("Computer Science"))),
                    new UniqueList<Specialisation>()
                            .setItems(List.of(new Specialisation("Computer Graphics"))),
                    getTagList("colleagues"), null)
        };
    }

    public static ReadOnlyNetworkBook getSampleNetworkBook() {
        NetworkBook sampleAb = new NetworkBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a {@code UniqueList} of tags containing the list of strings given.
     */
    public static UniqueList<Tag> getTagList(String... strings) {
        UniqueList<Tag> tagList = new UniqueList<>();
        for (String s : strings) {
            tagList.add(new Tag(s));
        }
        return tagList;
    }

}
