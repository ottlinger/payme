package com.payme.service

import com.payme.db.Account
import com.payme.db.Transaction
import com.payme.exception.NoNegativeBalanceAllowedException
import com.payme.exception.NoSameAccountTransfersException
import grails.test.spock.IntegrationSpec

class PaymentServiceIntegrationSpec extends IntegrationSpec {
    def greenMail
    def paymentService

    def cleanup() {
        greenMail.deleteAllMessages()
    }

    void "Test overdraft prevention"() {
        when: "Example accounts exist"
        new Account(name: "source", email: "source@domain.de").save()
        new Account(name: "target", email: "source@domain.de").save()
        then: "Default balances are persisted"
        Account.findByName("source").getBalance() == Account.DEFAULT_BALANCE && Account.findByName("target").getBalance() == Account.DEFAULT_BALANCE

        when: "A booking is performed"
        paymentService.book("source", "target", Account.DEFAULT_BALANCE.add(BigDecimal.ONE))

        then: "I am unable to cause an overdraft"
        thrown NoNegativeBalanceAllowedException
    }

    void "Test interaccount transfer"() {
        when: "Example accounts exist with default and specific balance"
        new Account(name: "source", email: "source@domain.de").save()
        new Account(name: "target", email: "source@domain.de").save()

        def source = Account.findByName("source");
        source.addToTransactions(new Transaction(owner: source, partner: source, amount: Account.DEFAULT_BALANCE))
        paymentService.book("source", "target", BigDecimal.TEN)

        then: "Balances are correct on source"
        Account.findByName("source").getBalance() == new BigDecimal(390)
        and: "and target account"
        Account.findByName("target").getBalance() == new BigDecimal(210)
    }

    void "Test interaccount transfer with negative amount"() {
        when: "Example accounts exist"
        new Account(name: "source", email: "source@domain.de", balance: BigDecimal.TEN).save()
        new Account(name: "target", email: "source@domain.de", balance: BigDecimal.ONE).save()
        paymentService.book("source", "target", BigDecimal.TEN.negate())

        then: "and I cause an IAE"
        thrown IllegalArgumentException
    }

    void "Test sameaccount transfer"() {
        when: "Example accounts exist"
        new Account(name: "source", email: "source@domain.de", balance: BigDecimal.TEN).save()
        then: "Account can be retrieved properly"
        assert Account.findByName("source")

        when: "A booking on itself is done"
        paymentService.book("source", "source", BigDecimal.TEN)
        then: "and I cause an exception"
        thrown NoSameAccountTransfersException
    }
}
