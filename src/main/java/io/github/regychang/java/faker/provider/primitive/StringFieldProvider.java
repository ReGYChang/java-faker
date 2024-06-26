package io.github.regychang.java.faker.provider.primitive;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class StringFieldProvider extends PrimitiveProvider<String> {

    private static final String DEFAULT_CHARACTER_SET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final String regexPattern;

    private final int length;

    public StringFieldProvider(Field field, Options options) {
        super(field, String.class, options);
        regexPattern = Optional.ofNullable(annotation).map(JFaker::format).orElse(null);
        length = Optional.ofNullable(annotation).map(JFaker::length).orElse(options.getRandomStringLength());
    }

    @Override
    protected String provideInternal() {
        return regexPattern == null || regexPattern.isEmpty() ?
                generateRandomString() : generateWithRegexPattern();
    }

    @Override
    protected String cast(String value) {
        return value;
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder();
        String characterSet =
                Optional.ofNullable(options.getRandomStringCharacterSet())
                        .orElse(DEFAULT_CHARACTER_SET);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(characterSet.length());
            sb.append(characterSet.charAt(index));
        }

        return sb.toString();
    }

    private String generateWithRegexPattern() {
        StringBuilder sb = new StringBuilder();
        RegExp regExp = new RegExp(regexPattern);
        Automaton automaton = regExp.toAutomaton();
        State state = automaton.getInitialState();

        while (!state.isAccept()) {
            List<Transition> transitions = state.getSortedTransitions(false);
            Transition transition = transitions.get(RANDOM.nextInt(transitions.size()));
            char c =
                    (char) (
                            transition.getMin() +
                                    RANDOM.nextInt(transition.getMax() - transition.getMin() + 1));
            sb.append(c);
            state = transition.getDest();
        }

        return sb.toString();
    }
}
