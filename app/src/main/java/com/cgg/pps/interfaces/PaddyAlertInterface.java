package com.cgg.pps.interfaces;

import android.app.Dialog;

import com.cgg.pps.model.response.rejectedtokenresponse.TokenRejectionOutput;

public interface PaddyAlertInterface {
    void cancelPaddySubmit(Dialog dialog);
    void proceedPaddySubmit(Double finalProcLand, Double finalProcQty);
}
