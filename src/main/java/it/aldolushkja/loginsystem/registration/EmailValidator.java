package it.aldolushkja.loginsystem.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    //TODO: add email regex
    @Override
    public boolean test(String s) {
        return true;
    }
}
