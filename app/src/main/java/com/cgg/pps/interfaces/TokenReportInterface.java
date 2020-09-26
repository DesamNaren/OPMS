package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.report.payment.PaymentReportResponse;
import com.cgg.pps.model.response.report.payment.ReportsDataResponce;
import com.cgg.pps.model.response.report.payment_report.PaddyPaymentInfoResponse;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponseList;

public interface TokenReportInterface extends BaseView {
    void getPaymentStatusReportData(ReportsDataResponce paymentReportResponseList);
    void getTokensResponseData(GetTokensResponse getTokensFarmerResponse);
    void getTransactionsReportData(TransactionReportResponseList transactionReportResponseList);
}
