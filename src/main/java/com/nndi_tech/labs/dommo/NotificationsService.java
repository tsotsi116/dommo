package com.nndi_tech.labs.dommo;

import com.nndi_tech.labs.dommo.dto.VerificationActionDTO;
import com.nndi_tech.labs.dommo.entities.MobileMoneyPayment;

public interface NotificationsService {
    /**
     * Notify personnel responsible for verifying payments about a new
     * mobile money payment verification request
     *
     * @param payment
     */
    void notifyVerifiers(MobileMoneyPayment payment);

    /**
     * Notify the customer about the state of their mobile money verification
     * request; whether it was accepted or cancelled.
     *
     * @param payment
     * @param action
     */
    void notifyCustomer(MobileMoneyPayment payment, VerificationActionDTO action);
}
