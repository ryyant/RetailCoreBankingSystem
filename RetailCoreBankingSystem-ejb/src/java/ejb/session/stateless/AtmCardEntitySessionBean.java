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
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerNotFoundException;
import util.exception.DuplicateException;
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
    public long issueAtmCard(String identificationNumber, AtmCard atmCard, List<String> accountsToLink) throws CustomerNotFoundException, DuplicateException
    {
        try {
            // customers matching the NRIC.
            Customer customer = (Customer) em.createQuery("SELECT c from Customer c WHERE c.identificationNumber LIKE :nric")
                    .setParameter("nric", identificationNumber)
                    .getSingleResult();
            
            // check if already got card.
            if (customer.getAtmCard() != null) {
                throw new DuplicateException();
            }     
            
            atmCard.setCustomer(customer);
            em.persist(atmCard);
            em.flush();
            
            for (String acc:accountsToLink)
            {
                DepositAccount depAccount = (DepositAccount) em.createQuery("SELECT d from DepositAccount d WHERE d.accountNumber = ?1")
                    .setParameter(1, acc)
                    .getSingleResult();
                
                // check if account is not his, so CANNOT link to atmCard
                if (!Objects.equals(depAccount.getCustomer().getCustomerId(), customer.getCustomerId())) {
                    throw new Exception();
                }
                
                atmCard.getDepositAccounts().add(depAccount);
                depAccount.setAtmCard(atmCard);
            }
            
            customer.setAtmCard(atmCard);

            return atmCard.getAtmCardId();
        }
        catch (DuplicateException d) {
            throw new DuplicateException("Already have a card!");
        }
        catch (Exception e) {
            throw new CustomerNotFoundException("Customer not registered / Account can't be linked!");
        }
    }
    
    @Override
    public long issueReplacement(String idNumber) throws CustomerNotFoundException
    {
        try
        {
            // customer matching the NRIC
            Customer customer = (Customer) em.createQuery("SELECT c from Customer c WHERE c.identificationNumber LIKE :nric")
                    .setParameter("nric", idNumber)
                    .getSingleResult();
            
            // create new card and persist
            String pin = customer.getAtmCard().getPin();
            String nameOnCard = customer.getAtmCard().getNameOnCard();
            String cardNumber = Integer.toString((int)(Math.floor(Math.random()*1000)));
            AtmCard newCard = new AtmCard(cardNumber, pin, nameOnCard);    
            newCard.setCustomer(customer);
            em.persist(newCard);
            em.flush();
            
            // associate new card and accounts
            List<DepositAccount> depositAccounts = customer.getAtmCard().getDepositAccounts();
            for (DepositAccount acc : depositAccounts) {
                newCard.getDepositAccounts().add(acc);
                acc.setAtmCard(newCard);
            }

            // delete away old card record
            em.remove(customer.getAtmCard());
            em.flush();
            System.out.println("old customer atm card: " + customer.getAtmCard());
            
            // associate new card and customer
            customer.setAtmCard(newCard);
            
            return newCard.getAtmCardId();            
        }
        catch(Exception e)
        {
            throw new CustomerNotFoundException("Customer no card!");
        }
    }    
    
    @Override
    public long insertAtmCard(String cardNumber, String pin) throws InvalidPinException
    {
        try
        {
            AtmCard atmCard = (AtmCard) em.createQuery("SELECT a from AtmCard a WHERE a.cardNumber LIKE ?1 AND a.pin LIKE ?2")
                    .setParameter(1, cardNumber)
                    .setParameter(2, pin)
                    .getSingleResult();
            
            return atmCard.getAtmCardId();            
        }
        catch(Exception e)
        {
            throw new InvalidPinException("Invalid card number / pin!");
        }
    }
    
    @Override
    public void changeAtmCardPin(String cardNumber, String newPin)
    {
        int update = em.createQuery("UPDATE AtmCard a SET a.pin = ?1 WHERE a.cardNumber = ?2")
                .setParameter(1, newPin)
                .setParameter(2, cardNumber)
                .executeUpdate();
    }
    
    @Override
    public String checkBalance(String cardNumber, String accountNumber) throws InvalidAccountException
    {
        try
        {
            AtmCard atmCard = (AtmCard) em.createQuery("SELECT a from AtmCard a WHERE a.cardNumber LIKE ?1")
                    .setParameter(1, cardNumber)
                    .getSingleResult();
            
            DepositAccount account = (DepositAccount) em.createQuery("SELECT d from DepositAccount d WHERE d.accountNumber LIKE ?1")
                    .setParameter(1, accountNumber)
                    .getSingleResult();
            
            if (!Objects.equals(account.getAtmCard().getAtmCardId(), atmCard.getAtmCardId())) {
                throw new InvalidAccountException();
            }

            return "Available Balance: " + account.getAvailableBalance();            
        }
        catch(Exception e)
        {
            throw new InvalidAccountException("Account number invalid! (Does not exist / Not linked to AtmCard)");
        }
    }
 
}
