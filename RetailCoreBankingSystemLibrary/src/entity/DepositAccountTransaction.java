/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.TransactionStatus;
import util.enumeration.TransactionType;

/**
 *
 * @author ryyant
 */
@Entity
public class DepositAccountTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositAccountTransactionId;
    private Date transactionDateTime;
    private TransactionType type;
    private String code;
    private String reference;
    private BigDecimal amount;
    private TransactionStatus status;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private DepositAccount depositAccount;
    
    @OneToOne(fetch = FetchType.LAZY)
    private DepositAccountTransaction sourceTransaction;
    
    @OneToOne(fetch = FetchType.LAZY)
    private DepositAccountTransaction destinationTransaction;

    public DepositAccountTransaction() {
        this.status = TransactionStatus.COMPLETED;
    }

    public DepositAccountTransaction(Date transactionDateTime, BigDecimal amount, TransactionType transType) {
        this();
        this.transactionDateTime = transactionDateTime;
        this.amount = amount;
        this.type = transType;
    }
    
    public Long getDepositAccountTransactionId() {
        return depositAccountTransactionId;
    }

    public void setDepositAccountTransactionId(Long depositAccountTransactionId) {
        this.depositAccountTransactionId = depositAccountTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depositAccountTransactionId != null ? depositAccountTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the depositAccountTransactionId fields are not set
        if (!(object instanceof DepositAccountTransaction)) {
            return false;
        }
        DepositAccountTransaction other = (DepositAccountTransaction) object;
        if ((this.depositAccountTransactionId == null && other.depositAccountTransactionId != null) || (this.depositAccountTransactionId != null && !this.depositAccountTransactionId.equals(other.depositAccountTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DepositAccountTransaction[ id=" + depositAccountTransactionId + " ]";
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public DepositAccount getDepositAccount() {
        return depositAccount;
    }

    public void setDepositAccount(DepositAccount depositAccount) {
        this.depositAccount = depositAccount;
    }

    public DepositAccountTransaction getSourceTransaction() {
        return sourceTransaction;
    }

    public void setSourceTransaction(DepositAccountTransaction sourceTransaction) {
        this.sourceTransaction = sourceTransaction;
    }

    public DepositAccountTransaction getDestinationTransaction() {
        return destinationTransaction;
    }

    public void setDestinationTransaction(DepositAccountTransaction destinationTransaction) {
        this.destinationTransaction = destinationTransaction;
    }
    
}
