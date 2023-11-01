package networkbook.logic.parser;

import static networkbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static networkbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.CommandTestUtil;
import networkbook.logic.commands.OpenLinkCommand;
import networkbook.testutil.TypicalIndexes;

public class OpenLinkCommandParserTest {
    private static final OpenLinkCommandParser PARSER = new OpenLinkCommandParser();

    @Test
    public void parse_missingPreamble_failure() {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, OpenLinkCommand.MESSAGE_USAGE);
        assertParseFailure(PARSER, "", expectedMessage);
        assertParseFailure(PARSER, CommandTestUtil.VALID_INDEX_DESC, expectedMessage);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, OpenLinkCommand.MESSAGE_USAGE);
        assertParseFailure(PARSER, "-1", expectedMessage);
        assertParseFailure(PARSER, "0", expectedMessage);
        assertParseFailure(PARSER, "1 what", expectedMessage);
        assertParseFailure(PARSER, "lol", expectedMessage);
    }

    @Test
    public void parse_duplicateIndexPrefix_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_INDEX);
        String userInput = String.format("1 %s %s", CommandTestUtil.VALID_INDEX_DESC, CommandTestUtil.VALID_INDEX_DESC);
        assertParseFailure(PARSER, userInput, expectedMessage);
    }

    @Test
    public void parse_validPreamble_success() {
        OpenLinkCommand expectedCommand = new OpenLinkCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, Index.fromOneBased(1));
        assertParseSuccess(PARSER, "1", expectedCommand);
    }

    @Test
    public void parse_validPreambleAndIndex_success() {
        OpenLinkCommand expectedCommand = new OpenLinkCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, Index.fromOneBased(1));
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + CommandTestUtil.VALID_INDEX_DESC;
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }
}
