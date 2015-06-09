package com.payme.service;

/**
 * Encapsulates sending/publishing of notifications.
 * Implementations decide the semantics of recipient and contents.
 */
public interface Notifiable {

    void publish(String recipient, String contents);
}
