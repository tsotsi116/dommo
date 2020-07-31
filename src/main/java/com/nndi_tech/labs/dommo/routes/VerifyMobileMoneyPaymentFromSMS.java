package com.nndi_tech.labs.dommo.routes;

import com.google.gson.Gson;
import com.nndi_tech.labs.dommo.MobileMoneyService;
import com.nndi_tech.labs.dommo.dto.VerificationActionDTO;
import com.nndi_tech.labs.dommo.entities.MobileMoneyPayment;
import spark.Request;
import spark.Response;
import spark.Route;

public class VerifyMobileMoneyPaymentFromSMS implements Route {
    private final Gson gson;
    private final MobileMoneyService mobileMoneyService;

    public VerifyMobileMoneyPaymentFromSMS(Gson gson, MobileMoneyService mobileMoneyService) {
        this.gson = gson;
        this.mobileMoneyService = mobileMoneyService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return response;
    }
}