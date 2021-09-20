package it.aldolushkja.loginsystem.registration.token;

import it.aldolushkja.loginsystem.appuser.AppUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    public static final String CONFIRMATION_TOKEN_SEQUENCE = "confirmation_token_sequence";

    @Id
    @SequenceGenerator(name = CONFIRMATION_TOKEN_SEQUENCE, sequenceName = CONFIRMATION_TOKEN_SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CONFIRMATION_TOKEN_SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

    public static ConfirmationToken buildConfirmationToken(AppUser appUser){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setAppUser(appUser);
        return confirmationToken;
    }
}
