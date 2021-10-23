/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author ryyant
 */
public class InvalidAccountException extends Exception {
    
    public InvalidAccountException()
    {
    }
    
    
    public InvalidAccountException(String msg)
    {
        super(msg);
    }
        
}
