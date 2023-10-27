package networkbook.logic.commands.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Person;
import networkbook.testutil.TypicalPersons;

public class DeletePriorityActionTest {
    private static final Person JACK = TypicalPersons.JACK;
    private static final DeletePersonDescriptor jackWithoutPriority = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            JACK.getPhones(),
            JACK.getEmails(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            JACK.getSpecialisations(),
            JACK.getTags(),
            null));

    @Test
    public void delete_deletePriority_success() throws CommandException {
        DeletePersonDescriptor descriptor = new DeletePersonDescriptor(JACK);
        DeletePriorityAction action = new DeletePriorityAction();
        action.delete(descriptor);
        assertEquals(jackWithoutPriority, descriptor);
    }

    @Test
    public void delete_deletePriorityOfPersonWithoutPriority_success() throws CommandException {
        DeletePriorityAction action = new DeletePriorityAction();
        DeletePersonDescriptor descriptorWithoutPriority = new DeletePersonDescriptor(new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().get(),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                null));
        action.delete(descriptorWithoutPriority);
        assertEquals(jackWithoutPriority, descriptorWithoutPriority);
    }

    @Test
    public void delete_deleteNull_nullPointerException() {
        DeletePriorityAction deleteFirstAction = new DeletePriorityAction();
        assertThrows(NullPointerException.class, () -> deleteFirstAction.delete(null));
    }

    @Test
    public void equalsTest() {
        DeletePriorityAction action1 = new DeletePriorityAction();
        DeletePriorityAction action2 = new DeletePriorityAction();
        assertEquals(action1, action1);
        assertEquals(action1, action2);
        assertNotEquals(action1, null);
        assertNotEquals(null, action1);
        assertNotEquals(action1, new DeleteCourseAction(Index.fromOneBased(1)));
    }
}
