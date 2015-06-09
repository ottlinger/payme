package com.payme.db

import grails.test.spock.IntegrationSpec

class TransactionIntegrationSpec extends IntegrationSpec {

    def transactionService

    void "Test object creation and linking with multiple transactions"() {
        when: "Example data is created"
        def owner = new Account(name: "TestMyself", email: "testmyself@escape.com")
        def partner = new Account(name: "TestPartner", email: "testPartner@escape.com")
        assert owner.validate() && partner.validate()
        owner.save(flush:true)
        partner.save(flush:true)

        // do the double-bookkeeping that happens from within the service layer
        owner.addToTransactions(new Transaction(owner: owner, partner: partner, amount: new BigDecimal(-100)))
        owner.addToTransactions(new Transaction(owner: owner, partner: partner, amount: new BigDecimal(100)))

        partner.addToTransactions(new Transaction(owner: partner, partner: owner, amount: new BigDecimal(100)))
        partner.addToTransactions(new Transaction(owner: partner, partner: owner, amount: new BigDecimal(-100)))

        assert Account.findByName("TestMyself")
        assert Account.findByName("TestPartner")

        then: "Number of transactions is correct since there's an initial balance set"
        Account.get(owner.id).transactions.size() == 3
        and:
        Account.get(partner.id).transactions.size() == 3

        when: "Transactions are properly calculated"
        println "Balance Owner: " + transactionService.getBalance(owner)
        println "Balance Partn: " + transactionService.getBalance(partner)
        then: "Source balances is correct"
        Account.findByName("TestMyself").getBalance() == new BigDecimal(200)
        and: "Target balance is correct"
        Account.findByName("TestPartner").getBalance() == new BigDecimal(200)
    }

    void "Test object creation and basic linking"() {
        when: "Dummy data exists"
        def from = new Account(name: "TestFrom", email: "testfrom@escape.com").save()
        def to = new Account(name: "TestTo", email: "testto@escape.com").save()
        def transaction = new Transaction(owner: from, partner: to, amount: new BigDecimal(100)).save()
        // real balance calculation is done from within the service layer

        then: "Validation is okay"
        transaction.validate()
    }
}
