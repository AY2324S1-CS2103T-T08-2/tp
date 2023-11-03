package networkbook.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import networkbook.commons.core.GuiSettings;
import networkbook.commons.core.LogsCenter;
import networkbook.logic.commands.Command;
import networkbook.logic.commands.CommandResult;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.NetworkBookParser;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.Model;
import networkbook.model.ReadOnlyNetworkBook;
import networkbook.model.person.Person;
import networkbook.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final NetworkBookParser networkBookParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        networkBookParser = new NetworkBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = networkBookParser.parseCommand(commandText);
        commandResult = command.execute(model);

        if (!command.getIsChangingData()) {
            return commandResult;
        }

        try {
            storage.saveNetworkBook(model.getNetworkBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyNetworkBook getNetworkBook() {
        return model.getNetworkBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getDisplayedPersonList();
    }

    @Override
    public Path getNetworkBookFilePath() {
        return model.getNetworkBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
