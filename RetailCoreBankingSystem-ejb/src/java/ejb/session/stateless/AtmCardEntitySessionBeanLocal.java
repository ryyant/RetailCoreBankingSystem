/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.DuplicateException;
import util.exception.InvalidAccountException;
import util.exception.InvalidPinException;

/**
 *
 * @author ryyant
 */
@Local
public interface AtmCardEntitySessionBeanLocal {

    public long insertAtmCard(String cardNumber, String pin) throws InvalidPinException;

    public void changeAtmCardPin(String cardNumber, String newPin);

    public long issueAtmCard(String identificationNumber, AtmCard atmCard, List<String> accountsToLink) throws CustomerNotFoundException, DuplicateException;
    
    public long issueReplacement(String idNumber) throws CustomerNotFoundException;
    
    public String checkBalance(String accountNumber) throws InvalidAccountException;

}
