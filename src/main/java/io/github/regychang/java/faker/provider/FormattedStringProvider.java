package io.github.regychang.java.faker.provider;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import io.github.regychang.java.faker.FieldProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class FormattedStringProvider implements FieldProvider {

    private final String regexPattern;

    public FormattedStringProvider(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public Object generate(Field field) {
        RegExp regExp = new RegExp(regexPattern);
        Automaton automaton = regExp.toAutomaton();
        Random random = new Random();

        return generateString(automaton.getInitialState(), random);
    }

    private String generateString(State state, Random random) {
        StringBuilder sb = new StringBuilder();

        while (!state.isAccept()) {
            List<Transition> transitions = state.getSortedTransitions(false);
            Transition transition = transitions.get(random.nextInt(transitions.size()));
            char c =
                    (char) (
                            transition.getMin() +
                                    random.nextInt(transition.getMax() - transition.getMin() + 1));
            sb.append(c);
            state = transition.getDest();
        }

        return sb.toString();
    }
}
