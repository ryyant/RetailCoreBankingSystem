/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtellermachineclient;

import ejb.session.stateless.AtmCardEntitySessionBeanRemote;
import java.util.Scanner;
import javax.ejb.EJB;
import util.exception.InvalidAccountException;
import util.exception.InvalidPinException;

/**
 *
 * @author ryyant
 */
public class Main {

    @EJB
    private static AtmCardEntitySessionBeanRemote atmCardEntitySessionBeanRemote;

    
    public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);
            
        OUTER:
        while (true) {
            System.out.println("*** Automated Teller Machine ***\n");


            String cardNumberInput = "";
            while (true) {
                try {
                    System.out.println("*** INSERT ATM CARD ***\n");
                    System.out.println("*** Enter ATM Card Number: ***\n");
                    cardNumberInput = scanner.nextLine().trim();
                    System.out.println("*** Enter Pin: ***\n");
                    String pinInput = scanner.nextLine().trim();
                    Long cardId = atmCardEntitySessionBeanRemote.insertAtmCard(cardNumberInput, pinInput);
                    System.out.println("*** Inserted! ***\n");
                    break;
                } 
                catch (InvalidPinException ex) {
                    System.out.println(ex.getMessage() + "\n");
                    System.out.println("*** Try Again? (Y/N) ***\n");
                    String tryAgain = scanner.nextLine().trim();
                    if (tryAgain.equalsIgnoreCase("N")) {
                        break OUTER;
                    }
                }
            }
            
            while (true) {
                System.out.println("*** What would you like to do? ***\n");
                System.out.println("*** 1. Change Pin ***\n");
                System.out.println("*** 2. Check Balance ***\n");
                System.out.println("*** 3. Exit ***\n");
                int response = scanner.nextInt();
                scanner.nextLine();

                switch (response) {
                    case 1:
                        System.out.println("*** CHANGE ATM CARD PIN ***\n");
                        System.out.println("*** Enter New Pin: ***\n");
                        String newPinInput = scanner.nextLine().trim();
                        atmCardEntitySessionBeanRemote.changeAtmCardPin(cardNumberInput, newPinInput);
                        System.out.println("Pin successfully changed!\n");
                        break;
                    case 2:
                        System.out.println("*** CHECK ACCOUNT BALANCE ***\n");
                        System.out.println("*** Enter account to check: ***\n");
                        String accountNumberInput = scanner.nextLine().trim();
                        try {
                            System.out.println(atmCardEntitySessionBeanRemote.checkBalance(accountNumberInput));
                        } catch(InvalidAccountException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                        break;
                    default:
                        break OUTER;
                }    
            }
            
        }
    
        
    }
    
}
