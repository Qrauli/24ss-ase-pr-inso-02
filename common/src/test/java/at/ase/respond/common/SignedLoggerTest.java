package at.ase.respond.common;


import at.ase.respond.common.dto.LogDTO;
import at.ase.respond.common.logging.LogFormatter;
import at.ase.respond.common.logging.LogSignatureException;
import at.ase.respond.common.logging.SignedLogger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.slf4j.MDC;

import java.security.SignatureException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SignedLoggerTest {

    private static final String SERVICE_NAME_PROPERTY = "serviceName";
    private static final String SERVICE_NAME = "Incident";
    private static final String USER = "testUser";
    private static final String PRIVATE_KEY_PATH_ENV = "LOG_KEY_PATH";
    private static final String PUBLIC_KEY_PATH_ENV = "PUBLIC_KEY_PATH";
    private static final String SERVICE_NAME_ENV = "SERVICE_NAME";
    private static final String PRIVATE_KEY_PATH = "src/test/resources/private-key.pem";
    private static final String PUBLIC_KEY_PATH = "src/test/resources/public-key.pem";
    private static final String INCIDENT_PRIVATE_KEY_PATH = "src/test/resources/incident-private.pem";
    private static final String INCIDENT_PUBLIC_KEY_PATH = "src/test/resources/incident-public.pem";
    private static final String WRONG_PUBLIC_KEY_PATH = "src/test/resources/wrong-public-key.pem";
    private SignedLogger signedLogger;
    // fix warning
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    public void setUp() {
        // Create the custom Appender
        listAppender = new ListAppender<>();
        listAppender.start();

        // Get the logger and add the custom Appender
        Logger logger = (Logger) LoggerFactory.getLogger(SignedLogger.class);
        logger.addAppender(listAppender);
        // set the service name
        logger.getLoggerContext().putProperty(SERVICE_NAME_PROPERTY, SERVICE_NAME);
        // Set the log level to TRACE such that all levels can be tested
        logger.setLevel(Level.TRACE);

        // Create the SignedLogger
        signedLogger = new SignedLogger();

        // Add the USER to the MDC
        MDC.put("user", USER);
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = "wrong-path")
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_createSignedLog_noExceptionButNoSignatureForInvalidPrivateKey() {
        // Try to create Signed log with invalid private key
        String message = "Test message with arg1: {} and arg2: {}";
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check error is detected and normal log still appears but without signature
        assertAll(
                () -> assertDoesNotThrow(() -> signedLogger.info(message, "test1", 2)),
                () -> assertTrue(listAppender.list.stream()
                        .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                                && log.getMDCPropertyMap().get("signature") == null
                                && log.getMDCPropertyMap().get("signatureTime") != null)
                )
        );
    }

    @Test
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_createSignedLogNullInput_noExceptionButNoSignature() {
        // Try to create Signed log with invalid private key
        String message = "Test message with arg1: {} and arg2: {}";
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check error is detected and normal log still appears but without signature
        assertAll(
                () -> assertDoesNotThrow(() -> signedLogger.info(message, "test1", 2)),
                () -> assertTrue(listAppender.list.stream()
                        .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                                && log.getMDCPropertyMap().get("signature") == null
                                && log.getMDCPropertyMap().get("signatureTime") != null)
                )
        );
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_logInfo_createsSignedInfoLog() {
        // Call the method to be tested
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check the log output
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyInfoSignature_succeedsForUnmodifiedLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertTrue(signedLogger.verifySignature(signature, SERVICE_NAME, signatureTime, logLevel, USER, formattedMessage));
    }


    // create tests for the rest of the logging methods
    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_logDebug_createsSignedDebugLog() {
        // Call the method to be tested
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.debug(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check the log output
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.DEBUG) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyDebugSignature_succeedsForUnmodifiedLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.debug(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.DEBUG) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.DEBUG) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertTrue(signedLogger.verifySignature(signature, SERVICE_NAME, signatureTime, logLevel, USER, formattedMessage));
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_logWarn_createsSignedWarnLog() {
        // Call the method to be tested
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.warn(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check the log output
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.WARN) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyWarnSignature_succeedsForUnmodifiedLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.warn(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.WARN) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.WARN) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertTrue(signedLogger.verifySignature(signature, SERVICE_NAME, signatureTime, logLevel, USER, formattedMessage));
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_logError_createsSignedErrorLog() {
        // Call the method to be tested
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.error(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check the log output
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.ERROR) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null
                        && log.getMDCPropertyMap().get("signatureTime") != null)
        );
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyErrorSignature_succeedsForUnmodifiedLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.error(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.ERROR) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.ERROR) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertTrue(signedLogger.verifySignature(signature, SERVICE_NAME, signatureTime, logLevel, USER, formattedMessage));
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_logTrace_createdSignedTraceLog() {
        // Call the method to be tested
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.trace(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check the log output
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.TRACE) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null
                        && log.getMDCPropertyMap().get("signatureTime") != null)
        );
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyTraceSignature_succeedsForUnmodifiedLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.trace(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.TRACE) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.TRACE) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertTrue(signedLogger.verifySignature(signature, SERVICE_NAME, signatureTime, logLevel, USER, formattedMessage));
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifySignature_failsForInvalidSignature() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertFalse(signedLogger.verifySignature("invalidSignature", SERVICE_NAME, signatureTime, logLevel, USER,
                formattedMessage));
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifySignature_failsForInvalidLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertFalse(signedLogger.verifySignature(signature, SERVICE_NAME, signatureTime, logLevel, USER,
                "modified message"));
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifySignature_failsForInvalidPublicKey() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        assertFalse(signedLogger.verifySignature(signature, SERVICE_NAME, signatureTime, logLevel, USER,
                formattedMessage));
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyDTOSignature_returnsTrueForUnmodifiedLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        LogDTO logDTO = new LogDTO(formattedMessage, logLevel, USER, SERVICE_NAME, signatureTime, signature);
        assertTrue(signedLogger.verifySignature(logDTO));
    }


    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyDTOSignature_throwsExceptionIfPublicKeyPathNotSet() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        LogDTO logDTO = new LogDTO(formattedMessage, logLevel, USER, SERVICE_NAME, signatureTime, signature);
        assertThrows(LogSignatureException.class, () -> signedLogger.verifySignature(logDTO));
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyDTOSignature_returnsFalseForModifiedLog() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        LogDTO logDTO = new LogDTO("modifiedMessage", logLevel, USER, SERVICE_NAME, signatureTime, signature);
        assertFalse(signedLogger.verifySignature(logDTO));
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = WRONG_PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyDTOSignature_returnsFalseForWrongPublicKey() {
        // Create signed log
        String message = "Test message with arg1: {} and arg2: {}";
        signedLogger.info(message, "test1", 2);
        String formattedMessage = LogFormatter.format(message, "test1", 2);

        // Check that the signed log output has been created
        assertTrue(listAppender.list.stream()
                .anyMatch(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null)
        );

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");

        // Verify the signature
        LogDTO logDTO = new LogDTO(formattedMessage, logLevel, USER, SERVICE_NAME, signatureTime, signature);
        assertThrows(LogSignatureException.class, () -> signedLogger.verifySignature(logDTO));
    }

    @Test
    @SetEnvironmentVariable(key = PRIVATE_KEY_PATH_ENV, value = INCIDENT_PRIVATE_KEY_PATH)
    @SetEnvironmentVariable(key = PUBLIC_KEY_PATH_ENV, value = INCIDENT_PUBLIC_KEY_PATH)
    @SetEnvironmentVariable(key = SERVICE_NAME_ENV, value = SERVICE_NAME)
    public void testSignedLogger_verifyDTOSignature_returnsTrueForRealIncidentLog() {
        signedLogger.info("Created new incident {} with code {} in session {}", "9d1721fc-5ffd-40df-886b-aedee90c961f",
                "01C00", "3ee5def4-99a5-4a08-896a-b8560b8aba07");
        String formattedMessage = LogFormatter.format("Created new incident {} with code {} in session {}",
                UUID.fromString("9d1721fc-5ffd-40df-886b-aedee90c961f"), "01C00",
                UUID.fromString("3ee5def4-99a5-4a08-896a-b8560b8aba07"));
        System.out.println(formattedMessage);

        // retrieve the signature and signed log data from the log output
        ILoggingEvent loggingEvent = listAppender.list.stream()
                .filter(log -> log.getLevel().equals(Level.INFO) && log.getFormattedMessage().equals(formattedMessage)
                        && log.getMDCPropertyMap().get("signature") != null).toList().get(0);
        String signature = loggingEvent.getMDCPropertyMap().get("signature");
        System.out.println(signature);
        String logLevel = loggingEvent.getMDCPropertyMap().get("logLevel");
        System.out.println(logLevel);
        String signatureTime = loggingEvent.getMDCPropertyMap().get("signatureTime");
        System.out.println(signatureTime);
        String serviceName = loggingEvent.getLoggerContextVO().getPropertyMap().get("serviceName");
        System.out.println(serviceName);
        SignedLogger signedLogger2 = new SignedLogger();

        // Verify the signature
        LogDTO logDTO = new LogDTO(formattedMessage, logLevel, USER, SERVICE_NAME, signatureTime, signature);
        assertTrue(signedLogger2.verifySignature(logDTO));
    }

}
