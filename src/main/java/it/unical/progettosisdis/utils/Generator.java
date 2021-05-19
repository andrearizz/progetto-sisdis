package it.unical.progettosisdis.utils;
import org.passay.*;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Generator {

    public static final String SPECIAL_CHARS = "!@#$%^&*()_+";

    public static final String ERROR_CODE = "ERRONEUS_SPECIAL_CHARS";

    public String passwordGenerator(int lenght, boolean containsChar, boolean containsDigit, boolean containsSymbols) {
        PasswordGenerator generator = new PasswordGenerator();
        List<CharacterRule> rules = new LinkedList<>();

        if (containsChar) {
            CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
            CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars,1);
            rules.add(lowerCaseRule);
            CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
            CharacterRule upperCaseRule = new CharacterRule(upperCaseChars,1);
            rules.add(upperCaseRule);
        }

        if(containsDigit) {
            CharacterData digitChars = EnglishCharacterData.Digit;
            CharacterRule digitRule = new CharacterRule(digitChars,1);
            rules.add(digitRule);
        }

        if(containsSymbols) {

            CharacterData specialChars = new CharacterData() {
                public String getErrorCode() {
                    return ERROR_CODE;
                }

                public String getCharacters() {
                    return SPECIAL_CHARS;
                }
            };

            CharacterRule splCharRule = new CharacterRule(specialChars,1);
            rules.add(splCharRule);
        }

        return generator.generatePassword(lenght, rules);
    }

}
