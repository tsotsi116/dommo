package com.nndi_tech.labs.dommo.routes;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.CustomConstraint;
import am.ik.yavi.core.Validator;
import am.ik.yavi.core.ViolationMessage;
import com.google.gson.Gson;
import com.nndi_tech.labs.dommo.MobileMoneyService;
import com.nndi_tech.labs.dommo.NotificationsService;
import com.nndi_tech.labs.dommo.dto.RequestVerificationDTO;
import com.nndi_tech.labs.dommo.entities.MobileMoneyPayment;
import com.nndi_tech.labs.dommo.entities.Service;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class RequestMobileMoneyPaymentVerificationRoute implements Route {
    private final Gson gson;
    private final MobileMoneyService mobileMoneyService;
    private final NotificationsService notificationsService;

    private static final Validator<RequestVerificationDTO> validator = ValidatorBuilder.of(RequestVerificationDTO.class)
        ._string(RequestVerificationDTO::phone, "phone", c -> c.notNull() //
            .lessThanOrEqual(20))
        ._double(RequestVerificationDTO::amount, "amount", c -> c.greaterThanOrEqual(0d))
        ._string(RequestVerificationDTO::transactionReference, "transactionReference", c -> c.notNull())
        ._string(RequestVerificationDTO::serviceNameID, "service",
            c -> c.predicate(Service.supportedServices()::contains,
                ViolationMessage.of("invalid service type", "%s")))
        .build();

    public RequestMobileMoneyPaymentVerificationRoute(Gson gson, MobileMoneyService mobileMoneyService, NotificationsService notificationsService) {
        this.gson = gson;
        this.mobileMoneyService = mobileMoneyService;
        this.notificationsService = notificationsService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        RequestVerificationDTO requestData = gson.fromJson(request.body(), RequestVerificationDTO.class);
        ConstraintViolations violations = validator.validate(requestData);

        // violations.forEach(x -> System.out.println(x.message()));

        if (!violations.isValid()) {
            response.status(HttpStatus.BAD_REQUEST_400);
            return response;
        }
        MobileMoneyPayment payment = new MobileMoneyPayment();

        payment.setId(UUID.randomUUID().toString());

        payment.setPaidBy(requestData.customerName());
        payment.setPhoneAccountNo(requestData.phone());
        payment.setServiceTransactionRef(requestData.transactionReference());
        payment.setAmount(requestData.amount());

        payment.setServiceTransactionDetails(requestData.transactionDetail());

        payment.setUpstreamProductID(requestData.productID());
        payment.setUpstreamCustomerID(requestData.customerID());
        payment.setUpstreamTransactionID(requestData.txnID());

        payment.setIpAddress(request.ip());
        payment.setRequestUserAgent(request.headers("User-Agent"));
        payment.setService(Service.findSupportedByName(requestData.serviceNameID()));

        mobileMoneyService.recordUnverifiedPayment(payment);
        notificationsService.notifyVerifiers(payment);

        return response;
    }
}
