package at.ase.respond.incident;

import at.ase.respond.common.dto.*;
import at.ase.respond.common.logging.LogSignatureException;
import at.ase.respond.common.logging.SignedLogger;
import at.ase.respond.incident.presentation.controller.SignatureController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SignatureControllerTest {


    SignatureController signatureController = new SignatureController();

    private static final String USER = "testUser";

    @Mock
    private SignedLogger signedLogger;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(signatureController, "signedLogger", signedLogger);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    // test verifySignature

    @Test
    public void testVerifySignature_verified_shouldReturnTrue() {
        LogDTO logDTO = new LogDTO("Test", "INFO", USER, "Test", "test", "signature");
        when(signedLogger.verifySignature(logDTO)).thenReturn(true);
        ResponseEntity<Boolean> responseEntity = signatureController.verifySignature(logDTO);

        assertAll(
                () ->  assertEquals(200, responseEntity.getStatusCode().value()),
                () ->  assertEquals(true, responseEntity.getBody())
        );
    }

    @Test
    public void testVerifySignature_notVerified_shouldReturnFalse() {
        LogDTO logDTO = new LogDTO("Test", "INFO", USER, "Test", "test", "signature");
        when(signedLogger.verifySignature(logDTO)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = signatureController.verifySignature(logDTO);

        assertAll(
                () ->  assertEquals(200, responseEntity.getStatusCode().value()),
                () ->  assertEquals(false, responseEntity.getBody())
        );
    }

    @Test
    public void testVerifySignature_LogSignatureException_shouldReturnBadRequestFalse() {
        LogDTO logDTO = new LogDTO("Test", "INFO", USER, "Test", "test", "signature");
        when(signedLogger.verifySignature(logDTO)).thenThrow(LogSignatureException.class);

        ResponseEntity<Boolean> responseEntity = signatureController.verifySignature(logDTO);

        assertAll(
                () ->  assertEquals(400, responseEntity.getStatusCode().value()),
                () ->  assertEquals(false, responseEntity.getBody())
        );
    }

    @Test
    public void testVerifySignature_throwsException_shouldReturnInternalServerErrorFalse() {
        LogDTO logDTO = new LogDTO("Test", "INFO", USER, "Test", "test", "signature");
        when(signedLogger.verifySignature(logDTO)).thenThrow(RuntimeException.class);

        ResponseEntity<Boolean> responseEntity = signatureController.verifySignature(logDTO);

        assertAll(
                () ->  assertEquals(500, responseEntity.getStatusCode().value()),
                () ->  assertEquals(false, responseEntity.getBody())
        );
    }

}
