package networkbook.logic.commands.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Person;
import networkbook.model.person.Specialisation;
import networkbook.model.util.UniqueList;
import networkbook.testutil.TypicalPersons;

public class DeleteSpecialisationActionTest {
    private static final Person JACK = TypicalPersons.JACK;
    private static final Index firstIndex = Index.fromOneBased(1);
    private static final Index tenthIndex = Index.fromOneBased(10);
    private static final DeletePersonDescriptor jackWithoutFirstSpecialisation = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            JACK.getPhones(),
            JACK.getEmails(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            new UniqueList<Specialisation>().setItems(List.of(new Specialisation("Artificial Intelligence"))),
            JACK.getTags(),
            JACK.getPriority().get()));

    private static final DeletePersonDescriptor jackWithoutAnySpecialisation = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            JACK.getPhones(),
            JACK.getEmails(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            new UniqueList<Specialisation>(),
            JACK.getTags(),
            JACK.getPriority().get()));

    @Test
    public void delete_deleteSpecialisationValidIndex_success() throws CommandException {
        DeletePersonDescriptor descriptor = new DeletePersonDescriptor(JACK);
        DeleteSpecialisationAction action = new DeleteSpecialisationAction(firstIndex);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutFirstSpecialisation);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutAnySpecialisation);
    }

    @Test
    public void delete_deleteSpecialisationInvalidIndex_commandException() throws CommandException {
        DeleteSpecialisationAction deleteFirstAction = new DeleteSpecialisationAction(firstIndex);
        DeletePersonDescriptor descriptorWithoutSpecialisation = new DeletePersonDescriptor(new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().get(),
                JACK.getCourses(),
                new UniqueList<Specialisation>(),
                JACK.getTags(),
                JACK.getPriority().get()));
        assertThrows(CommandException.class, () -> deleteFirstAction.delete(descriptorWithoutSpecialisation));

        DeleteSpecialisationAction deleteTenthAction = new DeleteSpecialisationAction(tenthIndex);
        DeletePersonDescriptor descriptorWithTwoSpecialisations = new DeletePersonDescriptor(JACK);
        assertThrows(CommandException.class, () -> deleteTenthAction.delete(descriptorWithTwoSpecialisations));
    }

    @Test
    public void delete_deleteNull_nullPointerException() {
        DeleteSpecialisationAction deleteFirstAction = new DeleteSpecialisationAction(firstIndex);
        assertThrows(NullPointerException.class, () -> deleteFirstAction.delete(null));
    }

    @Test
    public void equalsTest() {
        DeleteSpecialisationAction deleteFirstAction1 = new DeleteSpecialisationAction(firstIndex);
        DeleteSpecialisationAction deleteFirstAction2 = new DeleteSpecialisationAction(firstIndex);
        DeleteSpecialisationAction deleteTenthAction = new DeleteSpecialisationAction(tenthIndex);
        assertEquals(deleteFirstAction1, deleteFirstAction1);
        assertEquals(deleteFirstAction1, deleteFirstAction2);
        assertNotEquals(deleteFirstAction1, null);
        assertNotEquals(null, deleteFirstAction1);
        assertNotEquals(deleteFirstAction1, new DeletePriorityAction());
        assertNotEquals(deleteFirstAction1, deleteTenthAction);
    }
}
