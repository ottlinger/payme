import com.payme.db.Account
import com.payme.db.Transaction

class BootStrap {

    def init = { servletContext ->
        // Create ACCOUNTS
        def income = new Account(name: "Income", email: "income@me.com").save(failOnErrors: true)
        def outcome = new Account(name: "Outcome", email: "outcome@me.com").save(failOnErrors: true)
        def defaultAccount = new Account(name: "Default", email: "default@me.com").save(failOnErrors: true)
        def bank = new Account(name: "Bank", email: "bank@me.com").save(failOnErrors: true)

        // perform some BOOKING on accounts
        // withdraw floating-point number from bank to see full potential of BigDecimal
        def amount = new BigDecimal("11.83")
        defaultAccount.addToTransactions(new Transaction(owner: defaultAccount, partner: bank, amount: amount))
        bank.addToTransactions(new Transaction(owner: bank, partner: defaultAccount, amount: amount.negate()))
        defaultAccount.save()
        bank.save()

        // deduct money from bank
        for (Account a : [income, outcome, defaultAccount]) {
            // withdraw from bank
            bank.addToTransactions(new Transaction(owner: bank, partner: a, amount:BigDecimal.TEN.negate()))
            // add to account
            a.addToTransactions(new Transaction(owner:a, partner:bank, amount: BigDecimal.TEN))
        }
    }
    def destroy = {
    }
}
