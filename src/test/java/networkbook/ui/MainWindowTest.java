package networkbook.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import networkbook.MainApp;
import networkbook.logic.Messages;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.RedoCommand;
import networkbook.logic.commands.SaveCommand;
import networkbook.logic.commands.UndoCommand;
import networkbook.testutil.PersonBuilder;

@ExtendWith(ApplicationExtension.class)
public class MainWindowTest {
    @BeforeAll
    public static void setupForHeadlessTesting() {
        HeadlessHelper.setupForHeadlessTesting();
    }

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(MainApp.class);
    }

    @Test
    public void shortcut_robotPressesUndoRedoWhenUnfocused_success(FxRobot robot) {
        // NOTE: Ensure model contains no person with name "a" before running this test
        TextField textField = robot.lookup("#commandTextField").query();
        TextArea resultDisplay = robot.lookup("#resultDisplay").query();
        assertNotNull(textField);
        assertNotNull(resultDisplay);

        robot.clickOn(resultDisplay);
        robot.press(KeyCode.SHORTCUT, KeyCode.Z).release(KeyCode.SHORTCUT, KeyCode.Z);
        assertEquals(resultDisplay.getText(),
                UndoCommand.MESSAGE_UNDO_DISALLOWED);

        robot.press(KeyCode.SHORTCUT, KeyCode.Y).release(KeyCode.SHORTCUT, KeyCode.Y);
        assertEquals(resultDisplay.getText(),
                RedoCommand.MESSAGE_REDO_DISALLOWED);

        robot.clickOn(textField);
        robot.write(CreateCommand.COMMAND_WORD + " /name a").press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertEquals(resultDisplay.getText().substring(0, 20),
                String.format(CreateCommand.MESSAGE_SUCCESS, Messages.format(
                        new PersonBuilder().withoutOptionalFields().withName("a").build()))
                        .substring(0, 20)); // substring used to temporarily avoid the index

        robot.clickOn(resultDisplay);
        robot.press(KeyCode.SHORTCUT, KeyCode.Z).release(KeyCode.Z);
        assertEquals(resultDisplay.getText(),
                UndoCommand.MESSAGE_SUCCESS);

        robot.press(KeyCode.Z).release(KeyCode.Z);
        assertEquals(resultDisplay.getText(),
                UndoCommand.MESSAGE_UNDO_DISALLOWED);

        robot.press(KeyCode.Y).release(KeyCode.Y);
        assertEquals(resultDisplay.getText(),
                RedoCommand.MESSAGE_SUCCESS);

        robot.press(KeyCode.Y).release(KeyCode.Y);
        assertEquals(resultDisplay.getText(),
                RedoCommand.MESSAGE_REDO_DISALLOWED);

        robot.press(KeyCode.Z).release(KeyCode.Z); // clean up
    }

    @Test
    public void shortcut_robotPressesSave_success(FxRobot robot) {
        TextField textField = robot.lookup("#commandTextField").query();
        TextArea resultDisplay = robot.lookup("#resultDisplay").query();
        assertNotNull(textField);
        assertNotNull(resultDisplay);

        robot.clickOn(resultDisplay);
        robot.press(KeyCode.SHORTCUT, KeyCode.S).release(KeyCode.SHORTCUT, KeyCode.S);
        assertEquals(resultDisplay.getText(), SaveCommand.MESSAGE_SUCCESS);
    }
}
