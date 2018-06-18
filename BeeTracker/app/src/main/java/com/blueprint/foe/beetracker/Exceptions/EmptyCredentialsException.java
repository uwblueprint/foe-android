package com.blueprint.foe.beetracker.Exceptions;

public class EmptyCredentialsException extends Exception {
    public EmptyCredentialsException() {
        super("The username or password cannot be empty");
    }
}
