package networkbook.logic.commands.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Email;
import networkbook.model.person.Person;
import networkbook.model.util.UniqueList;
import networkbook.testutil.TypicalPersons;

public class DeleteEmailActionTest {
    private static final Person JACK = TypicalPersons.JACK;
    private static final Index firstIndex = Index.fromOneBased(1);
    private static final Index tenthIndex = Index.fromOneBased(10);
    private static final DeletePersonDescriptor jackWithoutFirstEmail = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            JACK.getPhones(),
            new UniqueList<Email>().setItems(List.of(new Email("jacky@test.com"))),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            JACK.getSpecialisations(),
            JACK.getTags(),
            JACK.getPriority().get()));

    private static final DeletePersonDescriptor jackWithoutAnyEmail = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            JACK.getPhones(),
            new UniqueList<Email>(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            JACK.getSpecialisations(),
            JACK.getTags(),
            JACK.getPriority().get()));

    @Test
    public void delete_deleteEmailValidIndex_success() throws CommandException {
        DeletePersonDescriptor descriptor = new DeletePersonDescriptor(JACK);
        DeleteEmailAction action = new DeleteEmailAction(firstIndex);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutFirstEmail);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutAnyEmail);
    }

    @Test
    public void delete_deleteEmailInvalidIndex_commandException() throws CommandException {
        DeleteEmailAction deleteFirstAction = new DeleteEmailAction(firstIndex);
        DeletePersonDescriptor descriptorWithoutEmail = new DeletePersonDescriptor(new Person(
                JACK.getName(),
                JACK.getPhones(),
                new UniqueList<Email>(),
                JACK.getLinks(),
                JACK.getGraduation().get(),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().get()));
        assertThrows(CommandException.class, () -> deleteFirstAction.delete(descriptorWithoutEmail));

        DeleteEmailAction deleteTenthAction = new DeleteEmailAction(tenthIndex);
        DeletePersonDescriptor descriptorWithTwoEmails = new DeletePersonDescriptor(JACK);
        assertThrows(CommandException.class, () -> deleteTenthAction.delete(descriptorWithTwoEmails));
    }

    @Test
    public void delete_deleteNull_nullPointerException() {
        DeleteEmailAction deleteFirstAction = new DeleteEmailAction(firstIndex);
        assertThrows(NullPointerException.class, () -> deleteFirstAction.delete(null));
    }

    @Test
    public void equalsTest() {
        DeleteEmailAction deleteFirstAction1 = new DeleteEmailAction(firstIndex);
        DeleteEmailAction deleteFirstAction2 = new DeleteEmailAction(firstIndex);
        DeleteEmailAction deleteTenthAction = new DeleteEmailAction(tenthIndex);
        assertEquals(deleteFirstAction1, deleteFirstAction1);
        assertEquals(deleteFirstAction1, deleteFirstAction2);
        assertNotEquals(deleteFirstAction1, null);
        assertNotEquals(null, deleteFirstAction1);
        assertNotEquals(deleteFirstAction1, new DeletePriorityAction());
        assertNotEquals(deleteFirstAction1, deleteTenthAction);
    }
}
