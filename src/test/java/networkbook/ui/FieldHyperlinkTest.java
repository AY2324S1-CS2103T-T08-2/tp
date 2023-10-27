package networkbook.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import networkbook.MainApp;

@ExtendWith(ApplicationExtension.class)
public class FieldHyperlinkTest {
    //NOTE: for GUI headless tests, running this single file or test case has issue on Windows,
    //possibly due to issue discussed in https://github.com/Santulator/Santulator/issues/14
    //To avoid the issue, either run gradle test command to perform all tests,
    //or use 'gradlew check coverage' in command line in root folder

    @BeforeAll
    public static void setupForHeadlessTesting() {
        HeadlessHelper.setupForHeadlessTesting();
    }

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(MainApp.class);
    }

    @Test
    public void constructor_hasValidLabel_showsValidLabel() {
        FieldHyperlink fh1 = new FieldHyperlink("", () -> {});
        FieldHyperlink fh2 = new FieldHyperlink("test", () -> {});
        FieldHyperlink fh3 = new FieldHyperlink("test@test.com", () -> {});
        assertEquals("", fh1.getText());
        assertEquals("test", fh2.getText());
        assertEquals("test@test.com", fh3.getText());
    }

    @Test
    public void constructor_nullParams_exceptionThrown() {
        assertThrows(AssertionError.class, () -> {
            FieldHyperlink f = new FieldHyperlink("test", null);
        });

        assertThrows(AssertionError.class, () -> {
            FieldHyperlink f = new FieldHyperlink(null, null);
        });

        assertThrows(AssertionError.class, () -> {
            FieldHyperlink f = new FieldHyperlink(null, () -> {});
        });
    }

}
