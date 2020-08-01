package com.nndi_tech.labs.dommo.routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nndi_tech.labs.dommo.MobileMoneyService;
import com.nndi_tech.labs.dommo.dto.ActionType;
import com.nndi_tech.labs.dommo.dto.VerificationActionDTO;
import com.nndi_tech.labs.dommo.entities.MobileMoneyPayment;
import lib.gintec_rdl.momo.model.MpambaCashInTransaction;
import lib.gintec_rdl.momo.model.MpambaDebitTransaction;
import lib.gintec_rdl.momo.model.MpambaDepositTransaction;
import lib.gintec_rdl.momo.model.Transaction;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.HttpURLConnection;
import java.util.Optional;

import static spark.Spark.halt;

// Adapted from: https://raw.githubusercontent.com/SharkFourSix/momopoc/master/web-app/src/main/java/app/gintec_rdl/momopoc/MomoPoc.java
public class MpambaTransactionRoute implements Route {
    private MobileMoneyService mobileMoneyService;
    private static final Gson mGson;

    static {
        mGson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
    }

    public MpambaTransactionRoute(MobileMoneyService mobileMoneyService) {
        this.mobileMoneyService = mobileMoneyService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Transaction transaction;
        switch (request.params("type")) {
            case "cash-in":
                transaction = tx(request, MpambaCashInTransaction.class);
                break;
            case "deposit":
                transaction = tx(request, MpambaDepositTransaction.class);
                break;
            case "debit":
                transaction = tx(request, MpambaDebitTransaction.class);
                break;
            case "credit": // transaction = tx(request, MpambaCreditTransaction.class);
            case "cash-out": // transaction = tx(request, MpambaCashOutTransaction.class);
            default:
                return halt(HttpURLConnection.HTTP_NO_CONTENT, "Can't process Transaction as payment");
        }
        // store(transaction);
        Optional<MobileMoneyPayment> mobileMoneyPayment = Optional.ofNullable(mobileMoneyService.findBy(
            "mobileMoneyPayment.transactionID=?", transaction.getTransactionId()));
        MobileMoneyPayment payment = null;
        if (mobileMoneyPayment.isEmpty()) {
            MobileMoneyPayment newPayment = new MobileMoneyPayment();

        } else {
            payment = mobileMoneyPayment.get();
        }
        // TODO: fix all of this
        VerificationActionDTO action = new VerificationActionDTO(
            payment.getId(), "system", ActionType.VERIFY_PAYMENT
        );

        mobileMoneyService.verifyOrCancel(payment, action);
        return response;
    }


    private static <T extends Transaction> T tx(Request request, Class<T> type) {
        return mGson.fromJson(request.body(), type);
    }
}
