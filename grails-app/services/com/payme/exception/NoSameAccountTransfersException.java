package com.payme.exception;

/**
 * Transferring money to oneself is not supported, because it's magic :-D
 */
public class NoSameAccountTransfersException extends Exception {
}
