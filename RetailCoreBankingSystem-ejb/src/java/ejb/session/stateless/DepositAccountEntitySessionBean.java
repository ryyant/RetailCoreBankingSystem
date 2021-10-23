/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.DepositAccount;
import entity.DepositAccountTransaction;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.DepositAccountType;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class DepositAccountEntitySessionBean implements DepositAccountEntitySessionBeanRemote, DepositAccountEntitySessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createNewDepositAccount(String identificationNumber, DepositAccount depositAccount, DepositAccountTransaction initialTransaction) throws CustomerNotFoundException
    {
        try {
            Customer customer = (Customer) em.createQuery("SELECT c from Customer c WHERE c.identificationNumber LIKE :nric")
                    .setParameter("nric", identificationNumber)
                    .getSingleResult();

            depositAccount.setAvailableBalance(initialTransaction.getAmount());
            depositAccount.setHoldBalance(initialTransaction.getAmount());
            depositAccount.setAccountType(DepositAccountType.SAVINGS);
            depositAccount.getTransactions().add(initialTransaction);
            depositAccount.setCustomer(customer);

            customer.getDepositAccounts().add(depositAccount);

            initialTransaction.setDepositAccount(depositAccount);

            em.persist(depositAccount);
            em.persist(initialTransaction);
            em.flush();

            return depositAccount.getDepositAccountId();     
        }
        catch (Exception e) {
            throw new CustomerNotFoundException("Customer not registered!");
        }

    }

}
