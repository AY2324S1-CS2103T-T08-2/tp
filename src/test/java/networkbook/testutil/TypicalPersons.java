package networkbook.testutil;

import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATION_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATION_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_LINK_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_SPECIALISATION_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_SPECIALISATION_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static networkbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import networkbook.model.NetworkBook;
import networkbook.model.person.Link;
import networkbook.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withEmails(List.of("alice@example.com"))
            .withPhones(List.of("94351253"))
            .withGraduation("AY2324-S1")
            .withLinks(List.of("nknguyenhc.github.io"))
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withEmails(List.of("johnd@example.com"))
            .withPhones(List.of("98765432"))
            .withLinks(List.of("www.facebook.com/john-d"))
            .withTags("owesMoney", "friends")
            .withGraduation("AY0001-S1")
            .build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
            .withPhones(List.of("95352563"))
            .withEmails(List.of("heinz@example.com"))
            .withGraduation("AY3031-S2")
            .build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
        .withPhones(List.of("87652533"))
            .withEmails(List.of("cornelia@example.com"))
            .withTags("friends")
            .withGraduation("AY9899-S1")
            .build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
            .withPhones(List.of("9482224"))
            .withEmails(List.of("werner@example.com"))
            .withGraduation("AY8990-S1")
            .build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
            .withPhones(List.of("9482427"))
            .withEmails(List.of("lydia@example.com"))
            .withGraduation("AY2122-S2")
            .build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best")
            .withPhones(List.of("9482442"))
            .withEmails(List.of("anna@example.com"))
            .withGraduation("AY2324-S2")
            .build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhones(List.of("8482424"))
            .withEmails(List.of("stefan@example.com")).build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhones(List.of("8482131"))
            .withEmails(List.of("hans@example.com")).build();
    public static final Person JACK = new PersonBuilder().withName("Jacky")
            .withPhones(List.of("12345678", "61828284"))
            .withEmails(List.of("jack@gmail.com", "jacky@test.com"))
            .withLinks(List.of("github.com", "https://nknguyenhc.github.io"))
            .withGraduation("AY2526-S2")
            .withCourses(List.of("CS2103T", "CS2109S"))
            .withSpecialisations(List.of("Software Engineering", "Artificial Intelligence"))
            .withPriority("High")
            .withTags("software eng enthusiast", "AI enthusiast").build();
    public static final Link JACK_FIRST_LINK = new Link("github.com");

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhones(List.of(VALID_PHONE_AMY))
            .withEmails(List.of(VALID_EMAIL_AMY)).withLinks(List.of(VALID_LINK_AMY))
            .withGraduation(VALID_GRADUATION_AMY)
            .withCourses(List.of(VALID_COURSE_AMY))
            .withSpecialisations(List.of(VALID_SPECIALISATION_AMY))
            .withTags(VALID_TAG_FRIEND)
            .withPriority("High").build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhones(List.of(VALID_PHONE_BOB))
            .withEmails(List.of(VALID_EMAIL_BOB)).withLinks(List.of(VALID_LINK_BOB))
            .withGraduation(VALID_GRADUATION_BOB)
            .withCourses(List.of(VALID_COURSE_BOB)).withSpecialisations(List.of(VALID_SPECIALISATION_BOB))
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code NetworkBook} with all the typical persons.
     */
    public static NetworkBook getTypicalNetworkBook() {
        NetworkBook ab = new NetworkBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
