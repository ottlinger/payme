package com.payme.service;

import com.payme.exception.NoNegativeBalanceAllowedException;
import com.payme.exception.NoSameAccountTransfersException;

import java.math.BigDecimal;

/**
 * Encapsulates the business operation to perform account transactions.
 */
public interface Transactionable {

    void book(String fromName, String toName, BigDecimal amount) throws NoNegativeBalanceAllowedException, NoSameAccountTransfersException;

}