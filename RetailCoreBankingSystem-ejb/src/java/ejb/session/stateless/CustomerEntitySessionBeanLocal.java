/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.DuplicateException;

/**
 *
 * @author ryyant
 */
@Local
public interface CustomerEntitySessionBeanLocal {

    public long createNewCustomer(Customer customer) throws DuplicateException;
    
}
