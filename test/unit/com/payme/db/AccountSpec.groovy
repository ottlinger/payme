package com.payme.db

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Account)
class AccountSpec extends Specification {

    void "Test account creation without name given"() {
        when: "Create account object"
        def a = new Account(email: "your@name.info")
        then: "Validation fails due to missing name"
        !a.validate()

        def errors = a.getErrors().allErrors
        assertEquals 1, errors.size()
        errors.get(0).codes.contains("com.payme.db.Account.name.nullable.error.com.payme.db.Account.name")
    }

    void "Test account creation without email given"() {
        when: "Create account object"
        def a = new Account(name: "my name is your name")
        then: "Validation fails due to missing email"
        !a.validate()

        def errors = a.getErrors().allErrors
        assertEquals 1, errors.size()
        errors.get(0).codes.contains("com.payme.db.Account.email.nullable.error.com.payme.db.Account.email")
    }

    void "Test account creation with invalid email given"() {
        when: "Create account object"
        def a = new Account(name: "my name is your name", email: "no.tld")
        then: "Validation fails due to wrong email"
        !a.validate()

        def errors = a.getErrors().allErrors
        assertEquals 1, errors.size()
        errors.get(0).codes.contains("com.payme.db.Account.email.email.error.email")
    }

    void "Test account creation while ensuring domain model constraints"() {
        when: "Create account object"
        def a = new Account(name: "my first account", email: "your@name.info")
        then: "Validation is okay"
        a.validate()
    }

    void "Test account creation with same mail address"() {
        when: "Example data is created"
        def a = new Account(name: "JustAName", email: "same@me.com")
        def b = new Account(name: "JustANameToo", email: "same@me.com")

        then: "Validation is okay if mail address is the same"
        a.validate() && b.validate()
        and: "Mail is the same"
        a.email == b.email
    }

    void "Test toString for UI integration"() {
        when: "Example account is created"
        def a = new Account(name: "name", email: "email@domain.com").save()

        then: "It has a nice toString representation"
        a.validate() && a.toString() == "name <email@domain.com>"
    }
}
