package it.aldolushkja.loginsystem.appuser;

import it.aldolushkja.loginsystem.registration.token.ConfirmationToken;
import it.aldolushkja.loginsystem.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "User with email %s non found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return appUserRepository.findAppUserByEmail(s).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, s)));
    }

    public String signUpUser(AppUser appUser) {
        final var userExists = appUserRepository.findAppUserByEmail(appUser.getEmail()).isPresent();
        if (userExists) {
            //TODO: check if properties are the same and
            //TODO: if email not confirmed send confirmation email.
            throw new IllegalStateException("email already taken");
        }
        final var encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        final var token = ConfirmationToken.buildConfirmationToken(appUser);
        confirmationTokenService.saveConfirmationToken(token);
        //TODO: send email
        return token.getToken();
    }

    public void enableAppUser(String email) {
        final var appUser = appUserRepository.findAppUserByEmail(email).orElseThrow(() -> new IllegalStateException("user not found"));
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
}
