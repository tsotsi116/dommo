package com.nndi_tech.labs.dommo.routes;

import com.google.gson.Gson;
import com.nndi_tech.labs.dommo.MobileMoneyService;
import com.nndi_tech.labs.dommo.NotificationsService;
import com.nndi_tech.labs.dommo.dto.VerificationActionDTO;
import com.nndi_tech.labs.dommo.entities.MobileMoneyPayment;
import spark.Request;
import spark.Response;
import spark.Route;

public class VerifyMobileMoneyPaymentByTxn implements Route {
    private final Gson gson;
    private final MobileMoneyService mobileMoneyService;
    private final NotificationsService notificationsService;

    public VerifyMobileMoneyPaymentByTxn(Gson gson, MobileMoneyService mobileMoneyService, NotificationsService notificationsService) {
        this.gson = gson;
        this.mobileMoneyService = mobileMoneyService;
        this.notificationsService = notificationsService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String paymentId = request.params("paymentId");
        VerificationActionDTO action = gson.fromJson(request.body(), VerificationActionDTO.class);

        MobileMoneyPayment payment = mobileMoneyService.find(paymentId);

        mobileMoneyService.verifyOrCancel(payment, action);

        notificationsService.notifyCustomer(payment, action);

        return response;
    }
}
