package com.smartaink.smart_home_assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class BillingToolsImpl implements BillingTools {

    @Override
    @Tool("Check current user's subscription plan Basic/Pro/Enterprise and renewal date")
    public String getCurrentPlan() {
        String plan = "Pro";
        String renewalDate = "3 Nov 2025";
        return "Your current plan is: " + plan + " (renews on " + renewalDate + ").";
    }

    @Override
    @Tool("Subscribe a specified plan Basic/Pro/Enterprise")
    public String subscribePlan(String plan) {
        if (plan == null || plan.isBlank()) {
            return "Please specify a valid plan: Basic, Pro, or Enterprise.";
        }
        return "Subscription updated successfully. You are now on the " + plan + " plan.";
    }

    @Override
    @Tool("Unsubscribe current plan")
    public String unsubscribePlan() {
        return "You have successfully unsubscribed from the PRO plan. Your access will remain active until the end of the billing cycle.";
    }

    @Override
    @Tool("Check remaining days before next billing cycle")
    public String getBillingCycleInfo() {
        return "Your next billing date is 3 Nov 2025. You have 1 days remaining in the current cycle.";
    }

    @Override
    @Tool("List all recent invoices and their statuses and amounts")
    public String listInvoices() {
        return """
            Recent invoices:
            - INV-2025-1001 | 29 Sep 2025 | $12.99 | Paid
            - INV-2025-0901 | 29 Aug 2025 | $12.99 | Paid
            - INV-2025-0801 | 29 Jul 2025 | $12.99 | Refunded
            """;
    }

    @Override
    @Tool("Get all data for given invoice ID")
    public String getInvoiceData(String invoiceId) {
        if (invoiceId == null || invoiceId.isBlank()) {
            return "Please provide a valid invoice ID.";
        }

        switch (invoiceId.toUpperCase()) {
            case "INV-2025-1001":
                return """
                    Invoice ID: INV-2025-1001
                    Date: 29 Sep 2025
                    Amount: $12.99
                    Status: Paid
                    Payment Method: Visa (**** 4421)
                    """;
            case "INV-2025-0801":
                return """
                    Invoice ID: INV-2025-0801
                    Date: 29 Jul 2025
                    Amount: $12.99
                    Status: Refunded
                    Payment Method: Visa (**** 4421)
                    """;
            default:
                return "No data found for invoice ID: " + invoiceId;
        }
    }

    @Override
    @Tool("Generate refund form for a specified invoice ID")
    public String generateRefundForm(String invoiceId) {
        if (invoiceId == null || invoiceId.isBlank()) {
            return "Please provide a valid invoice ID to generate a refund form.";
        }

        return """
            Refund Form Template:
            -----------------------
            Invoice ID: %s
            Customer Name: [Enter your name]
            Reason for refund: [Enter reason]
            Preferred refund method: [Bank transfer / Original payment method]
            -----------------------
            """.formatted(invoiceId);

    }

    @Override
    @Tool("Submit refund form with a given reason")
    public String submitRefundForm(String refundForm) {
        //checking if form correctly filled etc........
        if (refundForm == null || refundForm.isBlank()) {
            return "Missing refund form. Please fill the form and submit again.";
        }

        String ticketId = "RFD-" + (int) (Math.random() * 9000 + 1000);
        return "Refund request submitted for invoice 6772466233. Ticket ID: " + ticketId + ". Estimated review time: 3–5 business days.";
    }

    @Override
    @Tool("Check refund status of given refund ticket ID")
    public String checkRefundStatus(String ticketId) {
        if (ticketId == null || ticketId.isBlank()) {
            return "Please provide a valid refund ticket ID.";
        }

        switch (ticketId.toUpperCase()) {
            case "RFD-1234":
                return "Refund ticket RFD-1234: Approved — refund issued on 1 Nov 2025.";
            case "RFD-5678":
                return "Refund ticket RFD-5678: Under review — estimated completion in 2 business days.";
            default:
                return "Refund ticket " + ticketId + ": No matching record found.";
        }
    }
}
