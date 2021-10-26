/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.DuplicateException;

/**
 *
 * @author ryyant
 */
@Stateless
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;


    @Override
    public long createNewCustomer(Customer customer) throws DuplicateException
    {
        try {
            List<Customer> customers = em.createQuery("SELECT c from Customer c WHERE c.identificationNumber LIKE :nric")
                .setParameter("nric", customer.getIdentificationNumber())
                .getResultList();

            // check if already exists.
            if (customers.size() > 0) {
                throw new Exception();
            }
                    
            em.persist(customer);
            em.flush();
            return customer.getCustomerId();
        } catch (Exception e) {
            throw new DuplicateException("Customer already exists!");
        }
    }

}
