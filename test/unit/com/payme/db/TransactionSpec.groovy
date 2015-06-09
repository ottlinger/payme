package com.payme.db

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(Transaction)
@Mock(Account)
class TransactionSpec extends Specification {

    void "Test transaction creation without owner given"() {
        when: "Create account and transaction"
        def partner = new Account(name: "partner", email: "your@name.info")
        def t = new Transaction(partner: partner, amount: BigDecimal.TEN.negate())

        then: "Validation fails due to missing owner"
        partner.validate() && !t.validate()

        def errors = t.getErrors().allErrors
        assertEquals 1, errors.size()
        errors.get(0).codes.contains("com.payme.db.Transaction.owner.nullable.error.com.payme.db.Transaction.owner")
    }

    void "Test transaction creation without partner given"() {
        when: "Create account and transaction"
        def owner = new Account(name: "owner", email: "your@name.info")
        def t = new Transaction(owner: owner, amount: BigDecimal.TEN.negate())

        then: "Validation fails due to missing partner"
        owner.validate() && !t.validate()

        def errors = t.getErrors().allErrors
        assertEquals 1, errors.size()
        errors.get(0).codes.contains("com.payme.db.Transaction.partner.nullable.error.com.payme.db.Transaction.partner")
    }

    void "Test transaction creation without amount given"() {
        when: "Create account and transaction"
        def owner = new Account(name: "owner", email: "your@name.info")
        def partner = new Account(name: "partner", email: "your@name.info")
        def t = new Transaction(owner: owner, partner: partner)

        then: "Accounts are valid"
        partner.validate()&&owner.validate()
        and:  "Transaction validation fails due to missing amount"
        !t.validate()

        def errors = t.getErrors().allErrors
        assertEquals 1, errors.size()
        errors.get(0).codes.contains("com.payme.db.Transaction.amount.nullable.error.com.payme.db.Transaction.amount")
    }

    void "Test successfull account and transaction creation"() {
        when: "Create account and transaction"
        def owner = new Account(name: "owner", email: "your@name.info")
        def partner = new Account(name: "partner", email: "your@name.info")
        def t = new Transaction(owner: owner, partner: owner, amount: BigDecimal.TEN)

        then: "Accounts are valid"
        partner.validate()&&owner.validate()
        and:  "Transaction is valid too"
        t.validate()
    }
}
