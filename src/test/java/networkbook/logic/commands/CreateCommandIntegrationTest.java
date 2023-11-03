package networkbook.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import networkbook.logic.Messages;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.UserPrefs;
import networkbook.model.person.Person;
import networkbook.testutil.PersonBuilder;
import networkbook.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class CreateCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getNetworkBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        CommandTestUtil.assertCommandSuccess(new CreateCommand(validPerson), model,
                String.format(CreateCommand.MESSAGE_SUCCESS, Messages.format(validPerson))
                    + String.format(CreateCommand.MESSAGE_SUCCESS_INDEX, expectedModel.getDisplayedPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getNetworkBook().getPersonList().get(0);
        CommandTestUtil.assertCommandFailure(new CreateCommand(personInList), model,
                CreateCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
