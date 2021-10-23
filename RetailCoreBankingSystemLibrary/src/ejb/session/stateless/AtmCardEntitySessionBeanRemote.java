/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidAccountException;
import util.exception.InvalidPinException;

/**
 *
 * @author ryyant
 */
@Remote
public interface AtmCardEntitySessionBeanRemote {
    
    public long insertAtmCard(String cardNumber, String pin) throws InvalidPinException;

    public long changeAtmCardPin(Long cardId, String newPin);

    public long issueAtmCard(String identificationNumber, AtmCard atmCard, List<String> accountsToLink) throws CustomerNotFoundException;
    
    public String checkBalance(String accountNumber) throws InvalidAccountException;
}
