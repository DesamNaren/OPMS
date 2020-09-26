package com.cgg.pps.model.response.report.truckchit;

import com.cgg.pps.model.response.report.Report;

import java.util.ArrayList;

public class TCDetailReportResponseList extends Report {

    private ArrayList<TCDetailReportResponse> TCReportResponse;

    public ArrayList<TCDetailReportResponse> getTCReportResponse() {
        return TCReportResponse;
    }

    public void setTCReportResponse(ArrayList<TCDetailReportResponse> TCReportResponse) {
        this.TCReportResponse = TCReportResponse;
    }

}
