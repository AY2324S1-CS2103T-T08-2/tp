package networkbook.logic.commands.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.util.UniqueList;
import networkbook.testutil.TypicalPersons;
public class DeletePersonDescriptorTest {
    private static final Person JACK_PERSON = TypicalPersons.JACK;
    private static final DeletePersonDescriptor JACK_DESCRIPTOR = new DeletePersonDescriptor(JACK_PERSON);
    private static final Person EMPTY_PERSON = new Person(
        new Name("empty"),
        new UniqueList<>(),
        new UniqueList<>(),
        new UniqueList<>(),
        null,
        new UniqueList<>(),
        new UniqueList<>(),
        new UniqueList<>(),
        null
    );
    private static final DeletePersonDescriptor EMPTY_DESCRIPTOR = new DeletePersonDescriptor(EMPTY_PERSON);
    @Test
    public void equalsTest() {
        assertEquals(JACK_DESCRIPTOR, JACK_DESCRIPTOR);
        DeletePersonDescriptor jackDescriptor2 = new DeletePersonDescriptor(JACK_PERSON);
        assertEquals(JACK_DESCRIPTOR, jackDescriptor2);
        assertNotEquals(JACK_DESCRIPTOR, EMPTY_DESCRIPTOR);
        assertNotEquals(JACK_DESCRIPTOR, null);
        assertNotEquals(null, JACK_DESCRIPTOR);
        assertNotEquals(JACK_DESCRIPTOR, "string");
    }

    @Test
    public void toPersonTest() {
        assertEquals(JACK_PERSON, JACK_DESCRIPTOR.toPerson());
        assertEquals(EMPTY_PERSON, EMPTY_DESCRIPTOR.toPerson());
        assertNotEquals(JACK_PERSON, EMPTY_DESCRIPTOR.toPerson());
        assertNotEquals(EMPTY_PERSON, JACK_DESCRIPTOR.toPerson());
    }
}
