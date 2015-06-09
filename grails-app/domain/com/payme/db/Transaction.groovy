package com.payme.db

/**
 * Encapsulates a transaction happening on accounts.
 * Is either a withdrawal (amount<0) or a deposit (amount>0).
 *
 * If owner and partner are the same it represents an initial account balance.
 *
 * Each transaction belongs to exactly one account, therefore the field is named owner instead of 'from'.
 * 'From' is the business logics perspective when doing bank transfers.
 */
class Transaction {

    Date dateCreated
    // Refers to AccountFrom
    Account owner
    // Refers to AccountTo
    Account partner
    BigDecimal amount

    static constraints = {
        owner(nullable: false)
        partner(nullable: false)
        amount(nullable: false)
    }

    static belongsTo = [owner: Account]
}
