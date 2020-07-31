package com.nndi_tech.labs.dommo.dto;

public record RequestVerificationDTO(
    String customerName,
    String phone,
    double amount,
    String transactionReference,
    String transactionDetail,
    String serviceNameID,
    // Below this line are fields required for the upstream service
    String productID,
    String customerID,
    String txnID,
    String failCallbackUrl,
    String verifiedCallbackUrl
) {}
