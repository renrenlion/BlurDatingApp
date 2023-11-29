package com.example.blurdatingapplication.data;

import com.example.blurdatingapplication.utils.FunctionUtil;
public class profitMonitor {
    private String paymentWeek;
    public profitMonitor(){}

    public profitMonitor(String paymentWeek){
        this.paymentWeek = paymentWeek;
    }

    public String getPaymentWeek(){ return paymentWeek;}

    public void setPaymentWeek(String paymentWeek){this.paymentWeek = paymentWeek;}
}
