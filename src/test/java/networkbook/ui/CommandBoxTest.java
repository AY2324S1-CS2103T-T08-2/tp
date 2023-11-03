package networkbook.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import networkbook.MainApp;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.FindCommand;
import networkbook.logic.commands.RedoCommand;
import networkbook.logic.commands.UndoCommand;
import networkbook.logic.commands.edit.EditCommand;

@ExtendWith(ApplicationExtension.class)
public class CommandBoxTest {
    private static final String WHITESPACE = " ";

    @BeforeAll
    public static void setupForHeadlessTesting() {
        HeadlessHelper.setupForHeadlessTesting();
    }

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(MainApp.class);
    }

    @Test
    public void shortcut_robotPressesCtrlN_create(FxRobot robot) {
        TextField textField = robot.lookup("#commandTextField").query();
        assertNotNull(textField);
        robot.clickOn(textField);
        robot.press(KeyCode.SHORTCUT, KeyCode.N);
        assertEquals(textField.getText(), CreateCommand.COMMAND_WORD + WHITESPACE);
    }
    @Test
    public void shortcut_robotPressesCtrlG_edit(FxRobot robot) {
        TextField textField = robot.lookup("#commandTextField").query();
        assertNotNull(textField);
        robot.clickOn(textField);
        robot.press(KeyCode.SHORTCUT, KeyCode.G);
        assertEquals(textField.getText(), EditCommand.COMMAND_WORD + WHITESPACE);
    }

    @Test
    public void shortcut_robotPressesCtrlF_find(FxRobot robot) {
        TextField textField = robot.lookup("#commandTextField").query();
        assertNotNull(textField);
        robot.clickOn(textField);
        robot.press(KeyCode.SHORTCUT, KeyCode.F);
        assertEquals(textField.getText(), FindCommand.COMMAND_WORD + WHITESPACE);
    }

    @Test
    public void shortcut_robotPressesCtrlU_undo(FxRobot robot) {
        TextField textField = robot.lookup("#commandTextField").query();
        assertNotNull(textField);
        robot.clickOn(textField);
        robot.press(KeyCode.SHORTCUT, KeyCode.U);
        assertEquals(textField.getText(), UndoCommand.COMMAND_WORD);
    }

    @Test
    public void shortcut_robotPressesCtrlR_redo(FxRobot robot) {
        TextField textField = robot.lookup("#commandTextField").query();
        assertNotNull(textField);
        robot.clickOn(textField);
        robot.press(KeyCode.SHORTCUT, KeyCode.R);
        assertEquals(textField.getText(), RedoCommand.COMMAND_WORD);
    }

    @Test
    public void shortcut_robotPressesUpAndDown_navigateCommandHistory(FxRobot robot) {
        // NOTE: Ensure model contains no person with name "a" before running this test
        String createCommand = CreateCommand.COMMAND_WORD + " /name a";
        String undoCommand = UndoCommand.COMMAND_WORD;
        String invalidCommand = "input 3";
        TextField textField = robot.lookup("#commandTextField").query();
        assertNotNull(textField);
        robot.clickOn(textField);
        robot.write(createCommand).press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.write(undoCommand).press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.write(invalidCommand).press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.press(KeyCode.UP).release(KeyCode.UP).press(KeyCode.UP).release(KeyCode.UP);
        assertEquals(textField.getText(), undoCommand);
        robot.press(KeyCode.UP).release(KeyCode.UP);
        assertEquals(textField.getText(), createCommand);
        robot.press(KeyCode.DOWN).release(KeyCode.DOWN);
        assertEquals(textField.getText(), undoCommand);
        robot.press(KeyCode.DOWN).release(KeyCode.DOWN).press(KeyCode.DOWN).release(KeyCode.DOWN)
                .press(KeyCode.DOWN).release(KeyCode.DOWN);
        assertEquals(textField.getText(), invalidCommand);
    }

    @Test
    public void style_validAndInvalidCommands_changeStyle(FxRobot robot) {
        // NOTE: Ensure model contains no person with name "a" before running this test
        TextField textField = robot.lookup("#commandTextField").query();
        assertNotNull(textField);
        robot.clickOn(textField);
        robot.write(CreateCommand.COMMAND_WORD + " /name ").press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertTrue(textField.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
        robot.write("a").press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertFalse(textField.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
        robot.write(UndoCommand.COMMAND_WORD).press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertFalse(textField.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
    }
}
