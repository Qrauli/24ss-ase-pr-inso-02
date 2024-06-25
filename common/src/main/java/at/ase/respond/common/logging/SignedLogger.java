package at.ase.respond.common.logging;

import at.ase.respond.common.dto.LogDTO;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.MDC;
import org.springframework.boot.logging.LogLevel;

import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * This class is used to log messages with a digital signature. For signature generation and verification, the class
 * expects the SERVICE_NAME, LOG_KEY_PATH, and PUBLIC_KEY_PATH environment variables to be set. The class uses the
 * BouncyCastle library for key handling and signature generation.
 */
@Slf4j
public class SignedLogger {

    private static final String TIMEZONE = "Europe/Vienna";
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final String SERVICE_NAME;
    private Signature PRIVATE_SIGNATURE;
    private Signature PUBLIC_SIGNATURE;


    /**
     * Constructs a new SignedLogger object and prepares the private key and signature objects for signing logs.
     */
    public SignedLogger() {

        // prepare bouncy castle provider
        Security.addProvider(new BouncyCastleProvider());

        // get service name from logback properties
        SERVICE_NAME = System.getenv("SERVICE_NAME");
        try {
            PRIVATE_SIGNATURE = Signature.getInstance("Ed25519", "BC");
            PRIVATE_SIGNATURE.initSign(getPrivateKey());
        } catch (Exception e) {
            log.error("Failed to initialize private-key", e);
            PRIVATE_SIGNATURE = null;
        }
        try {
            PUBLIC_SIGNATURE = Signature.getInstance("Ed25519", "BC");
            PUBLIC_SIGNATURE.initVerify(getPublicKey());
        } catch (Exception e) {
            log.error("Failed to initialize public-key", e);
            PUBLIC_SIGNATURE = null;
        }
    }

    /**
     * Retrieves the private key from the filepath specified in the env and constructs a private key object.
     * @throws IOException if an error occurs during the private key retrieval
     * @return the private key object
     */
    private static PrivateKey getPrivateKey() throws IOException {
        String privateKeyPath = System.getenv("LOG_KEY_PATH");
        if (privateKeyPath == null) {
            throw new IOException("LOG_KEY_PATH environment variable not set");
        }

        PEMParser pemParser = new PEMParser(new FileReader(privateKeyPath));
        Object object = pemParser.readObject();
        pemParser.close();

        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        PrivateKey privateKey = null;

        if (object instanceof PEMKeyPair) {
            privateKey = converter.getPrivateKey(((PEMKeyPair) object).getPrivateKeyInfo());
        } else if (object instanceof PrivateKeyInfo) {
            privateKey = converter.getPrivateKey((PrivateKeyInfo) object);
        }

        return privateKey;
    }


    /**
     * Retrieves the public key from the filepath specified in the env and constructs a public key object.
     * @throws IOException if an error occurs during the public key retrieval
     * @return the public key object
     */
    private static PublicKey getPublicKey() throws IOException {
        String publicKeyPath = System.getenv("PUBLIC_KEY_PATH");
        if (publicKeyPath == null) {
            throw new IOException("PUBLIC_KEY_PATH environment variable not set");
        }

        try {
            PemReader pemReader = new PemReader(new FileReader(publicKeyPath));
            PemObject pemObject = pemReader.readPemObject();
            pemReader.close();
            byte[] pemContent = pemObject.getContent();
            KeyFactory kf = KeyFactory.getInstance("Ed25519", "BC");

            X509EncodedKeySpec spec = new X509EncodedKeySpec(pemContent);
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchProviderException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new LogSignatureException("Failed to read public key", e);
        }
    }


    /**
     * Logs the given message with a digital signature using the given logger on the info log level.
     * @param message the message format for the log
     * @param args the arguments to format the log message
     */
    public void info(String message, Object... args) {
        prepareLog(LogLevel.INFO, message, args);
        log.info(message, args);
        clearMDCButKeepUser();
    }

    /**
     * Logs the given message with a digital signature using the given logger on the debug log level.
     * @param message the message format for the log
     * @param args the arguments to format the log message
     */
    public void debug(String message, Object... args) {
        prepareLog(LogLevel.DEBUG, message, args);
        log.debug(message, args);
        clearMDCButKeepUser();
    }

    /**
     * Logs the given message with a digital signature using the given logger on the warn log level.
     * @param message the message format for the log
     * @param args the arguments to format the log message
     */
    public void warn(String message, Object... args) {
        prepareLog(LogLevel.WARN, message, args);
        log.warn(message, args);
        clearMDCButKeepUser();
    }

    /**
     * Logs the given message with a digital signature using the given logger on the error log level.
     * @param message the message format for the log
     * @param args the arguments to format the log message
     */
    public void error(String message, Object... args) {
        prepareLog(LogLevel.ERROR, message, args);
        log.error(message, args);
        clearMDCButKeepUser();
    }

    /**
     * Logs the given message with a digital signature using the given logger on the trace log level.
     * @param message the message format for the log
     * @param args the arguments to format the log message
     */
    public void trace(String message, Object... args) {
        prepareLog(LogLevel.TRACE, message, args);
        log.trace(message, args);
       clearMDCButKeepUser();
    }

    private void clearMDCButKeepUser() {
        String user = MDC.get("user");
        MDC.clear();
        MDC.put("user", user);
    }

    /**
     * Creates a log signature and prepares the log data for the given log level and message.
     *
     * @param logLevel log level at which the message should be logged
     * @param message  the message format to log
     * @param args     the arguments to format the LOG message
     * @throws LogSignatureException if an error occurs during the log signature generation
     */
    public void prepareLog(LogLevel logLevel, String message, Object... args) {
        // compute message
        message = LogFormatter.format(message, args);

        // generate current timestamp
        String timestamp = java.time.LocalDateTime.now(java.time.ZoneId.of(TIMEZONE))
                .format(java.time.format.DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN));

        // read user from MDC
        String user = MDC.get("user");

        try {
            // create signature object if it does not exist yet
            if (PRIVATE_SIGNATURE != null) {
                // sign log data and add signature to MDC
                String signature = signData(timestamp, logLevel.name(), user, message);
                MDC.put("signature", signature);
            }
            MDC.put("signatureTime", timestamp);
            MDC.put("logLevel", logLevel.name());
            MDC.put("logMessage", message);
        } catch (Exception e) {
            log.error("Log signature failed for message {}", message, e);
            MDC.put("signature", null);
            MDC.put("signatureTime", timestamp);
            MDC.put("logLevel", logLevel.name());
        }
    }

    /**
     * Computes a uniform log format for the given log data that can be used for creating and verifying log signatures.
     * @param timestamp the timestamp of the log event
     * @param logLevel the log level of the log event
     * @param message the message of the log event
     * @return the log format for the given log data
     */
    private String computeSignatureLogFormat(String timestamp, String serviceName, String logLevel, String user, String message) {
        return String.format("%s|%s|%s|%s|%s", timestamp, serviceName, logLevel, user, message);
    }

    /**
     * Signs the given log data using the service's private key.
     * @param timestamp the timestamp of the signature
     * @param logLevel the log level of the log event
     * @param user the user that created the log event
     * @param message the message of the log event
     * @return the digital signature of the log data
     */
    public String signData(String timestamp, String logLevel, String user, String message) throws SignatureException {
        String logData = computeSignatureLogFormat(timestamp, SERVICE_NAME, logLevel, user, message);
        PRIVATE_SIGNATURE.update(logData.getBytes());
        byte[] digitalSignature = PRIVATE_SIGNATURE.sign();
        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    /**
     * Verifies the given signature for the given log data
     * @param signature the signature to verify
     * @param serviceName the name of the service that created the log
     * @param timestamp the timestamp of the log event
     * @param logLevel the log level of the log event
     * @param user the user that created the log event
     * @param message the message of the log event
     * @return true if the signature is valid, false otherwise
     */
    public Boolean verifySignature(String signature, String serviceName, String timestamp, String logLevel, String user, String message) {
        try {
            String originalData = computeSignatureLogFormat(timestamp, serviceName, logLevel, user, message);
            PUBLIC_SIGNATURE.update(originalData.getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return PUBLIC_SIGNATURE.verify(signatureBytes);
        } catch (Exception e) {
            log.error("Signature verification failed for message {}", message, e);
            return false;
        }
    }

    /**
     * Verifies the given signature for the given log data
     * PUBLIC_KEY_PATH environment variable must be set to the path of the public key file.
     * @param logDTO the log data to verify
     * @return true if the signature is valid, false otherwise
     */
    public Boolean verifySignature(LogDTO logDTO) throws LogSignatureException {
        if (PUBLIC_SIGNATURE == null) {
            throw new LogSignatureException("Public key not set for signature verification");
        }
        return verifySignature(logDTO.signature(), logDTO.service(), logDTO.timestamp(), logDTO.level(), logDTO.user(), logDTO.message());
    }

}
