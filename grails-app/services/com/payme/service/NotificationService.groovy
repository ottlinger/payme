package com.payme.service

import grails.transaction.Transactional

/**
 * Encapsulates the notification in case of transaction or other business actions and sends mails.
 */
@Transactional(readOnly = true)
class NotificationService implements Notifiable {

    def greenMail
    def mailService

    @Override
    void publish(final String recipient, final String contents) {
        log.info 'Will send to $recipient ...'

        def m = mailService.sendMail {
            subject "Notification from PayMe - simple payment app"
            to recipient
            from "payme@me.com"
            body contents
        }
        if (m) {
            log.info 'Sent a message.'
        } else {
            log.error 'Could not send a message.'
        }
        log.info greenMail.getReceivedMessages().length + " messages in the queue"
    }

    void reset() {
        log.info "Currently " + greenMail.getReceivedMessages().length + " messages in the queue"
        greenMail.deleteAllMessages()
        log.info "Reset to " + greenMail.getReceivedMessages().length
    }
}
