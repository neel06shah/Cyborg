package com.example.cyborg.Models;

public class OutstandingModel {

    private String voucherDate;
    private String voucherRef;
    private String ledgerName;
    private String balanceAmount;
    private String balanceDueOn;
    private String balanceOverDue;
    private String balanceArea;
    private String balanceMobile;

    public OutstandingModel(String voucherDate, String voucherRef, String ledgerName, String balanceAmount, String balanceDueOn, String balanceOverDue, String balanceArea, String balanceMobile) {
        this.voucherDate = voucherDate;
        this.voucherRef = voucherRef;
        this.ledgerName = ledgerName;
        this.balanceAmount = balanceAmount;
        this.balanceDueOn = balanceDueOn;
        this.balanceOverDue = balanceOverDue;
        this.balanceArea = balanceArea;
        this.balanceMobile = balanceMobile;
    }

    public String getBalanceMobile() {
        return balanceMobile;
    }

    public String getBalanceArea() {
        return balanceArea;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public String getVoucherRef() {
        return voucherRef;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public String getBalanceDueOn() {
        return balanceDueOn;
    }

    public String getBalanceOverDue() {
        return balanceOverDue;
    }
}
