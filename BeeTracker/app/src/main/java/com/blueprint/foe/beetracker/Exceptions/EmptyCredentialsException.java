package com.blueprint.foe.beetracker.Exceptions;

public class EmptyCredentialsException extends Exception {
    public EmptyCredentialsException(String field) {
        super("The " + field + " cannot be empty.");
    }
}
