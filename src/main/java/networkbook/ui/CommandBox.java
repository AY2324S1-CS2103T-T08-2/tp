package networkbook.ui;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import networkbook.logic.Logic;
import networkbook.logic.commands.CommandResult;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.FindCommand;
import networkbook.logic.commands.RedoCommand;
import networkbook.logic.commands.UndoCommand;
import networkbook.logic.commands.edit.EditCommand;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final KeyCombination SHORTCUT_FIND =
            new KeyCharacterCombination("F", KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination SHORTCUT_CREATE =
            new KeyCharacterCombination("N", KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination SHORTCUT_EDIT =
            new KeyCharacterCombination("G", KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination SHORTCUT_UNDO =
            new KeyCharacterCombination("U", KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination SHORTCUT_REDO =
            new KeyCharacterCombination("R", KeyCombination.SHORTCUT_DOWN);
    private static final KeyCode SHORTCUT_UP = KeyCode.UP;
    private static final KeyCode SHORTCUT_DOWN = KeyCode.DOWN;
    private static final String WHITESPACE = " ";

    private final CommandExecutor commandExecutor;
    private final ArrayList<String> commandHistory;
    private int pointer;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        commandHistory = new ArrayList<>();
        pointer = 0;
        setCommandBoxShortcuts();
    }

    private void setCommandBoxShortcuts() {
        commandTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (SHORTCUT_FIND.match(event)) {
                    autoFillCommandIfEmpty(FindCommand.COMMAND_WORD + WHITESPACE);
                    event.consume();
                } else if (SHORTCUT_CREATE.match(event)) {
                    autoFillCommandIfEmpty(CreateCommand.COMMAND_WORD + WHITESPACE);
                    event.consume();
                } else if (SHORTCUT_EDIT.match(event)) {
                    autoFillCommandIfEmpty(EditCommand.COMMAND_WORD + WHITESPACE);
                    event.consume();
                } else if (SHORTCUT_UNDO.match(event)) {
                    autoFillCommandIfEmpty(UndoCommand.COMMAND_WORD);
                    event.consume();
                } else if (SHORTCUT_REDO.match(event)) {
                    autoFillCommandIfEmpty(RedoCommand.COMMAND_WORD);
                    event.consume();
                } else if (noModifier(event) && event.getCode() == SHORTCUT_UP) {
                    navigateCommandHistory(true);
                    event.consume();
                } else if (noModifier(event) && event.getCode() == SHORTCUT_DOWN) {
                    navigateCommandHistory(false);
                    event.consume();
                }
            }

            private boolean noModifier(KeyEvent event) {
                return !(event.isShortcutDown() || event.isAltDown() || event.isShiftDown());
            }
        });
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.isEmpty()) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        } finally {
            commandHistory.add(commandText);
            pointer = commandHistory.size();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

    public boolean isTextFieldFocused() {
        return commandTextField.isFocused();
    }

    /**
     * Automatically fills the command box if there's no user input.
     * @param command the command to fill
     */
    private void autoFillCommandIfEmpty(String command) {
        if (commandTextField.getText().isEmpty()) {
            commandTextField.setText(command);
            commandTextField.positionCaret(commandTextField.getLength());
        }
    }

    /**
     * Sets the command to be an entry in a command history, if the history is still navigable in the given direction.
     * @param isOlderCommand whether it navigates to an older command (true when "up" key is pressed)
     */
    private void navigateCommandHistory(boolean isOlderCommand) {
        int newPointer = isOlderCommand ? pointer - 1 : pointer + 1;

        if (newPointer < 0 || newPointer > commandHistory.size() - 1) {
            return;
        }

        String newText = commandHistory.get(newPointer);
        assert newText != null && !newText.isEmpty();
        commandTextField.setText(newText);
        commandTextField.positionCaret(newText.length());
        pointer = newPointer;
    }
}
