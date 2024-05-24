package com.example.e_journal.utlis;

public class Event<T> {
    private final T value;
    private boolean handled;

    public Event(T value) {
        this.value = value;
    }

    public T getValue() {
        if (handled) return null;
        handled = true;
        return value;
    }
}

