package networkbook.model.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import networkbook.model.NetworkBook;
import networkbook.model.ReadOnlyNetworkBook;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.GraduatingYear;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.tag.Tag;

/**
 * Contains utility methods for populating {@code NetworkBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"),
                    new UniqueList<Email>().setItems(List.of(new Email("alexyeoh@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("www.alexyeoh.net"))),
                    new GraduatingYear("2016"),
                    new Course("IS1128", new String[] {"1-02-2000", "12-08-2000"}),
                    new Specialisation("Financial Technology"),
                    getTagSet("friends"), new Priority("low")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"),
                    new UniqueList<Email>().setItems(List.of(new Email("berniceyu@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("github.com/bernfish"))),
                    new GraduatingYear("2020"),
                    new Course("CS1101S", new String[]{"12-03-2016"}),
                    new Specialisation("Artificial Intelligence"),
                    getTagSet("colleagues", "friends"), new Priority("high")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    new UniqueList<Email>().setItems(List.of(new Email("charlotte@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("facebook.com/Charlotte-Oliveiro"))),
                    new GraduatingYear("2000"),
                    new Course("CS1101S"), new Specialisation("Computer Security"),
                    getTagSet("neighbours"), new Priority("M")),
            new Person(new Name("David Li"), new Phone("91031282"),
                    new UniqueList<Email>().setItems(List.of(new Email("lidavid@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("wordpress.com/specialli-mine"))),
                    new GraduatingYear("2024"),
                    new Course("CS1101S"), new Specialisation("Database Systems"),
                    getTagSet("family"), new Priority("High")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                    new UniqueList<Email>().setItems(List.of(new Email("irfan@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("instagram.com/irfanny"))),
                    new GraduatingYear("2025"),
                    new Course("CS1101S"), new Specialisation("Parallel Computing"),
                    getTagSet("classmates"), new Priority("low")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                    new UniqueList<Email>().setItems(List.of(new Email("royb@example.com"))),
                    new UniqueList<Link>().setItems(List.of(new Link("incognito.com"))),
                    new GraduatingYear("2026"),
                    new Course("CS1101S"), new Specialisation("Computer Graphics"),
                    getTagSet("colleagues"), null)
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
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
