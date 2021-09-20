package it.aldolushkja.loginsystem.registration;

import it.aldolushkja.loginsystem.appuser.AppUser;
import it.aldolushkja.loginsystem.appuser.AppUserService;
import it.aldolushkja.loginsystem.email.EmailSender;
import it.aldolushkja.loginsystem.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
@Validated
@RequiredArgsConstructor
public class RegistrationService {

    private Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final ResourceLoader resourceLoader;

    public String register(@Valid RegistrationRequest request) {
        var token = "";
        try {
            final var isValidEmail = emailValidator.test(request.getEmail());
            if (!isValidEmail) {
                throw new IllegalStateException("Email not valid");
            }
            token = appUserService.signUpUser(AppUser.buildRegisteredAppUser(request));
            final var link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
            emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link));
            return token;
        } catch (IOException e) {
            LOGGER.error("Unable to register user", e);
            throw new IllegalStateException("Unable to register user, please try again..");
        }
    }

    private String buildEmail(String firstName, String link) throws IOException {
        final var resource = loadEmailTemplate();
        final var html = new String(Files.readAllBytes(Path.of(resource.getURI())));
        return html.replace("{firstName}", firstName).replace("{link}", link);
    }

    public Resource loadEmailTemplate() {
        return resourceLoader.getResource("classpath:confirmUserEmailTemplate.html");
    }

    @Transactional
    public String confirmToken(String token) {
        final var confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }
        final var expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "enabled";
    }


}
