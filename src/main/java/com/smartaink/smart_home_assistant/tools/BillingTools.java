package com.smartaink.smart_home_assistant.tools;

public interface BillingTools {

    String getCurrentPlan();

    String subscribePlan(String plan);

    String unsubscribePlan();

    String getBillingCycleInfo();

    String listInvoices();

    String getInvoiceData(String invoiceId);

    String generateRefundForm(String invoiceId);

    String submitRefundForm(String refundForm);

    String checkRefundStatus(String ticketId);
}
