package com.payme.service

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class PaymentServiceSpec extends Specification {
    void "Test outgoing message"() {
        when: "Outgoing params are given"
        def text = PaymentService.getMailText("RECPT", "SENDR", BigDecimal.TEN.negate())
        println text

        then: "Text should be correct"
        text.contains(BigDecimal.TEN.toString()) && text.contains("transferred") && text.contains("to")
    }

    void "Test incoming message"() {
        when: "Incoming params are given"
        def text = PaymentService.getMailText("RECPT", "SENDR", BigDecimal.TEN)
        println text

        then: "Text should be correct"
        text.contains(BigDecimal.TEN.toString()) && text.contains("received") && text.contains("from")
    }
}
