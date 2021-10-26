/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tellerterminalclient;

import ejb.session.stateless.AtmCardEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.DepositAccountEntitySessionBeanRemote;
import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import entity.DepositAccountTransaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import util.enumeration.TransactionType;
import util.exception.CustomerNotFoundException;
import util.exception.DuplicateException;

/**
 *
 * @author ryyant
 */
public class Main {

    @EJB
    private static DepositAccountEntitySessionBeanRemote depositAccountEntitySessionBeanRemote;

    @EJB
    private static AtmCardEntitySessionBeanRemote atmCardEntitySessionBeanRemote;
    
    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
            
        OUTER:
        while (true) {
            System.out.println("*** Teller Terminal ***\n");
            System.out.println("*** What would you like to do? ***\n");
            System.out.println("*** 1. Register ***\n");
            System.out.println("*** 2. Open Account ***\n");
            System.out.println("*** 3. Issue ATM Card ***\n");
            System.out.println("*** 4. Issue Replacement ATM Card ***\n");
            System.out.println("*** 5. Exit ***\n");
            int response = scanner.nextInt();
            scanner.nextLine();
            
            switch (response) {
                case 1:
                    System.out.println("*** What is your first name? ***\n");
                    String firstNameInput = scanner.nextLine().trim();
                    System.out.println("*** What is your last name? ***\n");
                    String lastNameInput = scanner.nextLine().trim();
                    System.out.println("*** What is your address (line 1)? ***\n");
                    String address1Input = scanner.nextLine().trim();
                    System.out.println("*** What is your address (line 2)? ***\n");
                    String address2Input = scanner.nextLine().trim();
                    System.out.println("*** What is your postal code? ***\n");
                    String postalCodeInput = scanner.nextLine().trim();
                    System.out.println("*** What is your contact number? ***\n");
                    String contactNumberInput = scanner.nextLine().trim();
                    System.out.println("*** What is your nric? ***\n");
                    String nricInput = scanner.nextLine().trim();
                    Customer customer = new Customer(firstNameInput, lastNameInput, nricInput, contactNumberInput, address1Input, address2Input, postalCodeInput);
                    try {
                        customerEntitySessionBeanRemote.createNewCustomer(customer);
                        System.out.println("Customer successfully created!\n");
                    } catch (DuplicateException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }
                    break;
                case 2:
                    {
                        System.out.println("*** What is your identification number? ***\n");
                        String idNumber = scanner.nextLine().trim();
                        
                        System.out.println("*** Transaction Type? ***\n");
                        String transTypeString = scanner.nextLine().trim();
                        TransactionType transType;
                        if (transTypeString.equalsIgnoreCase("credit")) {
                            transType = TransactionType.CREDIT;
                        }
                        else {
                            transType = TransactionType.DEBIT;
                        }
                        
                        System.out.println("*** How much do you want to deposit? ***\n");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();
                        BigDecimal bigAmount = BigDecimal.valueOf(amount);
                        
                        Date currentDate = new Date();
                        DepositAccountTransaction initialTransaction = new DepositAccountTransaction(currentDate, bigAmount, transType);
                        //generate random number
                        String accountNumber = Integer.toString((int)(Math.floor(Math.random()*1000)));
                        DepositAccount depositAccount = new DepositAccount(accountNumber);
                        
                        try {
                            depositAccountEntitySessionBeanRemote.createNewDepositAccount(idNumber, depositAccount, initialTransaction);
                            System.out.println("Account successfully created!\n");
                        } catch (CustomerNotFoundException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                        break;                        
                    }
                case 3:
                    {
                        System.out.println("*** What is your identification number? ***\n");
                        String idNumber = scanner.nextLine().trim();
                        System.out.println("*** Set your Pin: ***\n");
                        String pinNumber = scanner.nextLine().trim();
                        System.out.println("*** Name on Card: ***\n");
                        String nameOnCard = scanner.nextLine().trim();
                        System.out.println("*** Which deposit accounts do you wish to link it to? ***\n");
                        List<String> accountsToLink = new ArrayList<>();
                        while (true)
                        {
                            System.out.println("DepositAccount Number:");
                            String accountNumber = scanner.nextLine().trim();
                            accountsToLink.add(accountNumber);
                            System.out.println("Anymore? (Y/N)");
                            String answer = scanner.nextLine().trim();
                            if (answer.equalsIgnoreCase("N"))
                            {
                                System.out.println("Processing..");
                                break;
                            }
                        }       
                        //generate random number
                        String cardNumber = Integer.toString((int)(Math.floor(Math.random()*1000)));
                        AtmCard atmCard = new AtmCard(cardNumber, pinNumber, nameOnCard);
                        try {
                            atmCardEntitySessionBeanRemote.issueAtmCard(idNumber, atmCard, accountsToLink);
                            System.out.println("ATM Card issued!");
                        } catch (CustomerNotFoundException | DuplicateException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                        break;
                    }
                    case 4:
                    {
                        System.out.println("*** What is your identification number? ***\n");
                        String idNumber = scanner.nextLine().trim();   
                        try {
                            atmCardEntitySessionBeanRemote.issueReplacement(idNumber);
                            System.out.println("Replacement ATM Card issued!");
                        } catch (CustomerNotFoundException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                        break;
                    }
                    
                default:
                    break OUTER;
            }
        }
        
        
    }
    
}
