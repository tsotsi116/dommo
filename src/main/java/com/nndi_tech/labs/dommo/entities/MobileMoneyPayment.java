package com.nndi_tech.labs.dommo.entities;

import com.activepersistence.model.Base;
import com.nndi_tech.labs.dommo.dto.VerificationActionDTO;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="mobile_money_payments")
public class MobileMoneyPayment extends Base<String> {
    @Id
    private String id;

    @Column(name="website_url")
    private String websiteUrl;

    @Column(name="txn_id")
    private String upstreamTransactionID;

    @Column(name="customer_id")
    private String upstreamCustomerID;

    @Column(name="product_id")
    private String upstreamProductID;

    @Column(name="depositor_name")
    private String paidBy;

    @Column(name="phone_or_account_number")
    private String phoneAccountNo;

    @ManyToOne
    @JoinColumn(name="mobile_money_service_id")
    private Service service;

    @Column(name="service_txn_reference")
    private String serviceTransactionRef;

    @Column(name="service_txn_details")
    private String serviceTransactionDetails;

    @Column(name="amount")
    private double amount;

    @Column(name="ip_address")
    private String ipAddress;

    @Column(name="user_agent")
    private String requestUserAgent;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name="is_verified")
    private boolean verified;

    @Column(name="is_cancelled")
    private boolean cancelled;

    @ManyToOne
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name="failed_callback_url")
    private String onFailedCallbackUrl;

    @Column(name="verified_callback_url")
    private String onVerifiedCallbackUrl;

    @Column(name="created")
    private LocalDateTime createdAt;

    @Column(name="modified")
    private LocalDateTime updatedAt;

    @Column(name="deleted")
    private LocalDateTime deleted;

    public void verifyOrCancel(VerificationActionDTO action, User user) {
        assert action.actionBy().equalsIgnoreCase(user.getUsername());
        switch (action.actionType()) {
            case VERIFY_PAYMENT:
                this.setVerified(true);
                this.setCancelled(false);
                this.setVerifiedBy(user);
                break;
            case CANCEL_PAYMENT:
                this.setVerified(false);
                this.setCancelled(true);
                this.setCancelledBy(user);
                break;
            default:
                throw new UnsupportedOperationException("Invalid ActionType specified");
        }
    }
}
