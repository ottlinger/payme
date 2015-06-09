package com.payme.service

import com.payme.db.Account
import com.payme.db.Transaction
import com.payme.exception.NoNegativeBalanceAllowedException
import com.payme.exception.NoSameAccountTransfersException
import grails.transaction.Transactional

/**
 * Encapsulates the actual booking of account transactions and takes care of the correct state in the database.
 *
 * Each business transaction (money transfer) results in two transaction data rows to correctly
 * do double-entry bookkeeping.
 */
@Transactional
class PaymentService implements Transactionable {
    def notificationService

    // since all service methods are transactional as well, the transfer happens atomically
    @Override
    void book(final String fromName, final String toName, final BigDecimal amount) {

        assert fromName
        assert toName
        assert amount

        // wrong usage if called with negative amounts (to and from need to be reversed!)
        if (amount < BigDecimal.ZERO) {
            throw new IllegalArgumentException("No negative bookings allowed, reverse to and from accounts.")
        }

        Account from = Account.findByName fromName
        Account to = Account.findByName toName
        log.info 'Booking ' + amount + ' from ' + from + ' to ' + to
        assert from
        assert to

        if (to == from) {
            throw new NoSameAccountTransfersException()
        }

        // prevent overdrafts
        if (from.balance.subtract(amount) < BigDecimal.ZERO) {
            throw new NoNegativeBalanceAllowedException()
        }

        // START to actually change data - since the whole method is transactional, the transfer should be atomic
        // update balances
        to.balance.add(amount);
        from.balance.subtract(amount)

        // create transaction objects to do accounting correctly
        def inverted = amount.negate()
        to.addToTransactions(new Transaction(owner: to, partner: from, amount: amount))
        from.addToTransactions(new Transaction(owner: from, partner: to, amount: inverted))

        to.save(flush: true)
        from.save(flush: true)

        // send out notifications to recipients
        notificationService.publish(to.email, getMailText(toName, fromName, amount))
        notificationService.publish(from.email, getMailText(fromName, toName, inverted))
    }


    static String getMailText(String recipient, String sender, BigDecimal amount) {
        "Hi $recipient!\nYou've just " + ((amount > BigDecimal.ZERO) ? "received " : "transferred ") +
                amount.abs() + " " + Account.DEFAULT_CURRENCY +
                ((amount > BigDecimal.ZERO) ? " from " : " to ") +
                "$sender.\n\nYours,\nPayMe :-)\n"
    }
}
