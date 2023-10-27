package networkbook.logic.commands.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.util.UniqueList;
import networkbook.testutil.TypicalPersons;

public class DeletePhoneActionTest {
    private static final Person JACK = TypicalPersons.JACK;
    private static final Index firstIndex = Index.fromOneBased(1);
    private static final Index tenthIndex = Index.fromOneBased(10);
    private static final DeletePersonDescriptor jackWithoutFirstPhone = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            new UniqueList<Phone>().setItems(List.of(new Phone("61828284"))),
            JACK.getEmails(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            JACK.getSpecialisations(),
            JACK.getTags(),
            JACK.getPriority().get()));

    private static final DeletePersonDescriptor jackWithoutAnyPhone = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            new UniqueList<Phone>(),
            JACK.getEmails(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            JACK.getSpecialisations(),
            JACK.getTags(),
            JACK.getPriority().get()));

    @Test
    public void delete_deletePhoneValidIndex_success() throws CommandException {
        DeletePersonDescriptor descriptor = new DeletePersonDescriptor(JACK);
        DeletePhoneAction action = new DeletePhoneAction(firstIndex);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutFirstPhone);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutAnyPhone);
    }

    @Test
    public void delete_deletePhoneInvalidIndex_commandException() throws CommandException {
        DeletePhoneAction deleteFirstAction = new DeletePhoneAction(firstIndex);
        DeletePersonDescriptor descriptorWithoutPhone = new DeletePersonDescriptor(new Person(
                JACK.getName(),
                new UniqueList<Phone>(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().get(),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().get()));
        assertThrows(CommandException.class, () -> deleteFirstAction.delete(descriptorWithoutPhone));

        DeletePhoneAction deleteTenthAction = new DeletePhoneAction(tenthIndex);
        DeletePersonDescriptor descriptorWithTwoPhones = new DeletePersonDescriptor(JACK);
        assertThrows(CommandException.class, () -> deleteTenthAction.delete(descriptorWithTwoPhones));
    }

    @Test
    public void delete_deleteNull_nullPointerException() {
        DeletePhoneAction deleteFirstAction = new DeletePhoneAction(firstIndex);
        assertThrows(NullPointerException.class, () -> deleteFirstAction.delete(null));
    }

    @Test
    public void equalsTest() {
        DeletePhoneAction deleteFirstAction1 = new DeletePhoneAction(firstIndex);
        DeletePhoneAction deleteFirstAction2 = new DeletePhoneAction(firstIndex);
        DeletePhoneAction deleteTenthAction = new DeletePhoneAction(tenthIndex);
        assertEquals(deleteFirstAction1, deleteFirstAction1);
        assertEquals(deleteFirstAction1, deleteFirstAction2);
        assertNotEquals(deleteFirstAction1, null);
        assertNotEquals(null, deleteFirstAction1);
        assertNotEquals(deleteFirstAction1, new DeletePriorityAction());
        assertNotEquals(deleteFirstAction1, deleteTenthAction);
    }
}
