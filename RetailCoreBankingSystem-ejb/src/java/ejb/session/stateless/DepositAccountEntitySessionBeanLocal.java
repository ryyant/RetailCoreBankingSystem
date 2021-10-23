/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DepositAccount;
import entity.DepositAccountTransaction;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidAccountException;

/**
 *
 * @author ryyant
 */
@Local
public interface DepositAccountEntitySessionBeanLocal {

    public long createNewDepositAccount(String identificationNumber, DepositAccount depositAccount, DepositAccountTransaction initialTransaction) throws CustomerNotFoundException;
    
}
