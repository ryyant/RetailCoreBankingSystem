/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidAccountException;
import util.exception.InvalidPinException;

/**
 *
 * @author ryyant
 */
@Stateless
public class AtmCardEntitySessionBean implements AtmCardEntitySessionBeanRemote, AtmCardEntitySessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public long issueAtmCard(String identificationNumber, AtmCard atmCard, List<String> accountsToLink) throws CustomerNotFoundException
    {
        try {
            // customers matching the NRIC.
            Customer customer = (Customer) em.createQuery("SELECT c from Customer c WHERE c.identificationNumber LIKE :nric")
                    .setParameter("nric", identificationNumber)
                    .getSingleResult();

            for (String acc:accountsToLink)
            {
                DepositAccount depAccount = (DepositAccount) em.createQuery("SELECT d from DepositAccount d WHERE d.accountNumber LIKE :accNum")
                    .setParameter("accNum", acc)
                    .getSingleResult();
                atmCard.getDepositAccounts().add(depAccount);
            }

            atmCard.setCustomer(customer);
            customer.setAtmCard(atmCard);

            em.persist(atmCard);
            em.flush();

            return atmCard.getAtmCardId();
        } 
        catch (Exception e) {
            throw new CustomerNotFoundException("Customer / Accounts not registered!");
        }
    }
    
    @Override
    public long insertAtmCard(String cardNumber, String pin) throws InvalidPinException
    {
        try
        {
            AtmCard atmCard = (AtmCard) em.createQuery("SELECT a from AtmCard a WHERE a.cardNumber LIKE ?1")
                    .setParameter(1, cardNumber)
                    .getSingleResult();
            
            System.out.println(atmCard);
            return atmCard.getAtmCardId();            
        }
        catch(Exception e)
        {
            throw new InvalidPinException("Invalid pin!");
        }
              

    }
    
    @Override
    public long changeAtmCardPin(Long cardId, String newPin)
    {
        return 1;
    }
    
    @Override
    public String checkBalance(String accountNumber) throws InvalidAccountException
    {
        try
        {
            DepositAccount account = (DepositAccount) em.createQuery("SELECT a from DepositAccount a WHERE a.accountNumber LIKE ?1")
                    .setParameter(1, accountNumber)
                    .getSingleResult();
 
            return "Available Balance: " + account.getAvailableBalance();            
        }
        catch(Exception e)
        {
            throw new InvalidAccountException("Account number not linked to this ATM Card!");
        }
    }
 
}
