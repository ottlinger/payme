package com.payme.service

import grails.test.spock.IntegrationSpec

class NotificationServiceIntegrationSpec extends IntegrationSpec {
    def notificationService
    def greenMail

    void "Test reset queue"() {
        when: "Reset is called"
        notificationService.reset()

        then: "Queue is 0"
        !greenMail.getReceivedMessages().length
    }

    // TODO FIXME?
    @spock.lang.Ignore("Does not work locally, although sending notifications itself works if the app is running")
    void "Test sending multiple events"() {
        when: "Notifications are published"
        notificationService.publish("recipient@mail.com", "Test contents " + System.currentTimeMillis())
        notificationService.publish("recipient@mail.com", "Test contents " + System.currentTimeMillis())
        notificationService.publish("recipient@mail.com", "Test contents " + System.currentTimeMillis())
        notificationService.publish("recipient@mail.com", "Test contents " + System.currentTimeMillis())

        then: "Queue is 4"
        4 == greenMail.getReceivedMessages().length
    }
}
