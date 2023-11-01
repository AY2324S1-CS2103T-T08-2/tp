package networkbook.logic.parser;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.ClearCommand;
import networkbook.logic.commands.Command;
import networkbook.logic.commands.CommandTestUtil;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.ExitCommand;
import networkbook.logic.commands.FindCommand;
import networkbook.logic.commands.HelpCommand;
import networkbook.logic.commands.ListCommand;
import networkbook.logic.commands.OpenLinkCommand;
import networkbook.logic.commands.RedoCommand;
import networkbook.logic.commands.SortCommand;
import networkbook.logic.commands.UndoCommand;
import networkbook.logic.commands.delete.DeletePersonCommand;
import networkbook.logic.commands.edit.EditCommand;
import networkbook.logic.commands.edit.EditNameAction;
import networkbook.logic.commands.filter.FilterCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Name;
import networkbook.model.person.NameContainsKeyTermsPredicate;
import networkbook.model.person.Person;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;
import networkbook.model.person.filter.CourseContainsKeyTermsPredicate;
import networkbook.model.person.filter.CourseIsStillBeingTakenPredicate;
import networkbook.testutil.PersonBuilder;
import networkbook.testutil.PersonUtil;
import networkbook.testutil.TypicalIndexes;

public class NetworkBookParserTest {

    private final NetworkBookParser parser = new NetworkBookParser();

    @Test
    public void parseCommand_create() throws Exception {
        Person person = new PersonBuilder().build();
        CreateCommand command = (CreateCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new CreateCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeletePersonCommand command = (DeletePersonCommand) parser.parseCommand(
                DeletePersonCommand.COMMAND_WORD + " " + TypicalIndexes.INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeletePersonCommand(TypicalIndexes.INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_NAME + " " + CommandTestUtil.VALID_NAME_AMY);
        EditNameAction expectedAction = new EditNameAction(new Name(CommandTestUtil.VALID_NAME_AMY));
        assertEquals(new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction), command);
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD + " 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_redo() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD + " 3") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeyTermsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_sort() throws Exception {
        SortCommand command = (SortCommand) parser.parseCommand(
                SortCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_SORT_FIELD + " NAMe "
                + CliSyntax.PREFIX_SORT_ORDER + " desCending");
        PersonSortComparator expectedCmp = new PersonSortComparator(SortField.NAME, SortOrder.DESCENDING);
        assertEquals(new SortCommand(expectedCmp), command);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FilterCommand command = (FilterCommand) parser.parseCommand(
                FilterCommand.COMMAND_WORD + " "
                        + CliSyntax.PREFIX_FILTER_FIELD + " "
                        + keywords.stream().collect(Collectors.joining(" "))
        );
        assertEquals(new FilterCommand(
                new CourseContainsKeyTermsPredicate(keywords),
                new CourseIsStillBeingTakenPredicate(LocalDate.now()),
                false), command);
    }

    @Test
    public void parseCommand_openLink() throws Exception {
        String userInput = OpenLinkCommand.COMMAND_WORD + " 1 " + CliSyntax.PREFIX_INDEX + " 1 ";
        Command actualCommand = parser.parseCommand(userInput);
        Command expectedCommand = new OpenLinkCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                        -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, Messages.MESSAGE_UNKNOWN_COMMAND, ()
                -> parser.parseCommand("unknownCommand"));
    }
}
