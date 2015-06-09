package com.payme.service

import com.payme.db.Account
import com.payme.db.Transaction
import grails.transaction.Transactional

@Transactional
class TransactionService {

    /**
     * Dynamically calculates the balance of the given account by queuing the Transaction table for entries
     * related to that account.
     * @param a account
     * @return current account balance or <code>BigDecimal.ZERO</code>
     */
    @Transactional(readOnly = true)
    BigDecimal getBalance(Account a) {
        // if there were no interactions with that account something went wrong during Account creation
        // since each account has a default balance
        if (Transaction.findAllByOwner(a)) {
            def c = Transaction.createCriteria()
            return (BigDecimal) c.get {
                eq('owner', a)
                and
                        {
                            projections {
                                c.sum("amount")
                            }
                        }
            }
        } else {
            log.error 'No transactions on Account ${a} yet, this must not happen - you may see this message in tests.'
            return BigDecimal.ZERO
        }
    }

}
