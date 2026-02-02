import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import cherry.CherryException;
import cherry.Parser;
import cherry.command.AddCommand;
import cherry.command.ByeCommand;
import cherry.command.Command;
import cherry.command.DeleteCommand;
import cherry.command.ListCommand;
import cherry.command.MarkCommand;
import cherry.command.UnmarkCommand;


public class ParserTest {

    @Test
    public void parse_byeCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("bye");
        assertInstanceOf(ByeCommand.class, command);
        command = parser.parse("BYE");
        assertInstanceOf(ByeCommand.class, command);
    }

    @Test
    public void parse_listCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("list");
        assertInstanceOf(ListCommand.class, command);
        command = parser.parse("LIST");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_todoCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("todo read book");
        assertInstanceOf(AddCommand.class, command);
        command = parser.parse("TODO read book");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_todoCommandNoDescription_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("todo");
            fail(); // Should not reach this line
        } catch (CherryException e) {
            assertEquals("No task description", e.getMessage());
        }
    }

    @Test
    public void parse_deadlineCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("deadline return book /by 2024-12-31");
        assertInstanceOf(AddCommand.class, command);
        command = parser.parse("DEADLINE return book /by 2024-12-31");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_deadlineCommandNoBy_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("deadline return book");
            fail();
        } catch (CherryException e) {
            assertEquals("/by not found in command", e.getMessage());
        }
    }

    @Test
    public void parse_deadlineCommandNoDescription_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("deadline /by 2024-12-31");
            fail();
        } catch (CherryException e) {
            assertEquals("Please give a deadline description", e.getMessage());
        }
    }

    @Test
    public void parse_deadlineCommandInvalidDate_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("deadline return book /by 2024-13-45");
            fail();
        } catch (CherryException e) {
            assertTrue(e.getMessage().contains("Invalid date format"));
        }
    }

    @Test
    public void parse_eventCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("event meeting /from 2pm /to 4pm");
        assertInstanceOf(AddCommand.class, command);
        command = parser.parse("EVENT meeting /from 2pm /to 4pm");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_eventCommandNoFrom_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("event meeting /to 4pm");
            fail();
        } catch (CherryException e) {
            assertEquals("/from not found in command", e.getMessage());
        }
    }

    @Test
    public void parse_eventCommandNoTo_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("event meeting /from 2pm");
            fail();
        } catch (CherryException e) {
            assertEquals("/to not found in command", e.getMessage());
        }
    }

    @Test
    public void parse_eventCommandWrongOrder_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("event meeting /to 4pm /from 2pm");
            fail();
        } catch (CherryException e) {
            assertEquals("/from must come before /to", e.getMessage());
        }
    }

    @Test
    public void parse_markCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("mark 3");
        assertInstanceOf(MarkCommand.class, command);
        command = parser.parse("MARK 3");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_markCommandNoNumber_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("mark");
            fail();
        } catch (CherryException e) {
            assertEquals("Missing task number", e.getMessage());
        }
    }

    @Test
    public void parse_markCommandInvalidNumber_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("mark abc");
            fail();
        } catch (CherryException e) {
            assertEquals("Invalid task number", e.getMessage());
        }
    }

    @Test
    public void parse_deleteCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("delete 2");
        assertInstanceOf(DeleteCommand.class, command);
        command = parser.parse("DELETE 2");
        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    public void parse_unmarkCommand_success() throws CherryException {
        Parser parser = new Parser();
        Command command = parser.parse("unmark 1");
        assertInstanceOf(UnmarkCommand.class, command);
        command = parser.parse("UNMARK 1");
        assertInstanceOf(UnmarkCommand.class, command);
    }

    @Test
    public void parse_emptyInput_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("");
            fail();
        } catch (CherryException e) {
            assertEquals("No user input :(", e.getMessage());
        }
    }

    @Test
    public void parse_unknownCommand_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parse("unknown command");
            fail();
        } catch (CherryException e) {
            assertEquals("Sorry, the task cafe can't help with that yet!", e.getMessage());
        }
    }

    @Test
    public void getDate_validDate_success() throws CherryException {
        Parser parser = new Parser();
        LocalDate date = parser.getDate("2024-12-31");
        assertEquals(LocalDate.of(2024, 12, 31), date);
        date = parser.getDate("2024-02-29");
        assertEquals(LocalDate.of(2024, 02, 29), date);
    }

    @Test
    public void getDate_invalidDate_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.getDate("invalid-date");
            fail();
        } catch (CherryException e) {
            assertTrue(e.getMessage().contains("Invalid date format"));
        }
    }
}
