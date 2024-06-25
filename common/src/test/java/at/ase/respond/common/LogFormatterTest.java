package at.ase.respond.common;

import org.junit.jupiter.api.Test;

import at.ase.respond.common.logging.LogFormatter;

import static org.junit.jupiter.api.Assertions.*;


public class LogFormatterTest {


    @Test
    public void testLogFormatter_NoPlaceholders_InputDoesNotChange() {
        String template = "Hello, world!";
        String result = LogFormatter.format(template);
        assertEquals(template, result);
    }


    @Test
    public void testLogFormatter_OnePlaceholderOneArgument_FillsInArgument() {
        String template = "Hello, {}!";
        String result = LogFormatter.format(template, "world");
        assertEquals("Hello, world!", result);
    }


    @Test
    public void testLogFormatter_PlaceholderTwoArguments_OnlyFillsInFirst() {
        String template = "Hello, {}!";
        String result = LogFormatter.format(template, "world", "universe");
        assertEquals("Hello, world!", result);
    }


    @Test
    public void testLogFormatter_TwoPlaceholdersOneArgument_OnlyReplacesFirstPlaceholder() {
        String template = "Hello, {} and {}!";
        String result = LogFormatter.format(template, "world");
        assertEquals("Hello, world and {}!", result);
    }


    @Test
    public void testLogFormatter_TwoPlaceholdersTwoArguments_FillsInBoth() {
        String template = "Hello, {} and {}!";
        String result = LogFormatter.format(template, "world", "universe");
        assertEquals("Hello, world and universe!", result);
    }


    @Test
    public void testLogFormatter_IncompletePlaceholdersTwoArguments_DoesNotFillInArguments() {
        String template = "Hello, { and }!";
        String result = LogFormatter.format(template, "world", "universe");
        assertEquals("Hello, { and }!", result);
    }
}
