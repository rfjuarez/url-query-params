package core.domain;

@FunctionalInterface
public interface Translator <I, O>{
    O translate(I input);
}
