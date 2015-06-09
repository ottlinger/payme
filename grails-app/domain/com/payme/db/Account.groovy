package com.payme.db

class Account {

    def transactionService

    static DEFAULT_CURRENCY = "£"
    static DEFAULT_BALANCE = new BigDecimal(200.00)

    String name
    String email

    // will be calculated dynamically based on existing transactions of that account
    BigDecimal balance = BigDecimal.ZERO

    // Grails defaults
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name(nullable: false, unique: true)
        email(nullable: false, email: true)
    }

    static transients = ['balance']

    static mappedBy = [transactions: 'owner']
    static hasMany = [transactions: Transaction]

    /**
     * The balance is (intentionally) recalculated upon each access to keep things simple.
     *
     * @return recalculated current account balance.
     */
    def getBalance() {
        balance = transactionService.getBalance(this)
        balance
    }

    // upon first object insertion we need to populate the default balance
    // http://grails.github.io/grails-doc/3.0.x/guide/GORM.html#eventsAutoTimestamping
    def afterInsert() {
        addToTransactions(new Transaction(owner: this, partner: this, amount: DEFAULT_BALANCE))
    }

    String toString() {
        name + " <" + email + ">"
    }
}
