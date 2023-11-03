package networkbook.logic.parser;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import networkbook.commons.core.LogsCenter;
import networkbook.logic.Messages;
import networkbook.logic.commands.AddCommand;
import networkbook.logic.commands.ClearCommand;
import networkbook.logic.commands.Command;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.ExitCommand;
import networkbook.logic.commands.FindCommand;
import networkbook.logic.commands.HelpCommand;
import networkbook.logic.commands.ListCommand;
import networkbook.logic.commands.OpenEmailCommand;
import networkbook.logic.commands.OpenLinkCommand;
import networkbook.logic.commands.RedoCommand;
import networkbook.logic.commands.SaveCommand;
import networkbook.logic.commands.SortCommand;
import networkbook.logic.commands.UndoCommand;
import networkbook.logic.commands.delete.DeletePersonCommand;
import networkbook.logic.commands.edit.EditCommand;
import networkbook.logic.commands.filter.FilterCommand;
import networkbook.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class NetworkBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(NetworkBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case CreateCommand.COMMAND_WORD:
            return new CreateCommandParser().parse(arguments);

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeletePersonCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case SortCommand.COMMAND_WORD:
            return new SortCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case SaveCommand.COMMAND_WORD:
            return new SaveCommand();

        case FilterCommand.COMMAND_WORD:
            return new FilterCommandParser().parse(arguments);

        case OpenLinkCommand.COMMAND_WORD:
            return new OpenLinkCommandParser().parse(arguments);

        case OpenEmailCommand.COMMAND_WORD:
            return new OpenEmailCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
