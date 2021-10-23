/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DepositAccount;
import entity.DepositAccountTransaction;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidAccountException;

/**
 *
 * @author ryyant
 */
@Remote
public interface DepositAccountEntitySessionBeanRemote {
    
    public long createNewDepositAccount(String identificationNumber, DepositAccount depositAccount, DepositAccountTransaction initialTransaction) throws CustomerNotFoundException;
    
}
