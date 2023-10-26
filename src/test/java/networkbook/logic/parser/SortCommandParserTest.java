package networkbook.logic.parser;

import static networkbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static networkbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import networkbook.logic.Messages;
import networkbook.logic.commands.SortCommand;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(Messages.MESSAGE_MISSING_FIELD, CliSyntax.PREFIX_SORT_FIELD));
    }

    @Test
    public void parse_validArgs_returnsSortCommand() {
        PersonSortComparator cmp = new PersonSortComparator(SortField.GRAD, SortOrder.ASCENDING);
        SortCommand expectedSortCommand = new SortCommand(cmp);
        assertParseSuccess(parser, " \n /by  grad  \n \t /order   asc  \t", expectedSortCommand);
        assertParseSuccess(parser, " /by graduation /order ascending", expectedSortCommand);
        assertParseSuccess(parser, " /by grad", expectedSortCommand);
    }

}
