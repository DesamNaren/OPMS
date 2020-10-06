package com.cgg.pps.network;

import com.cgg.pps.BuildConfig;
import com.cgg.pps.model.request.ChangePwdRequest;
import com.cgg.pps.model.request.ProRePrintRequest;
import com.cgg.pps.model.request.TCRePrintRequest;
import com.cgg.pps.model.request.TokenRePrintRequest;
import com.cgg.pps.model.request.devicemgmt.bb.bankbranch.BankBranchRequest;
import com.cgg.pps.model.request.devicemgmt.dmv.DMVRequest;
import com.cgg.pps.model.request.enablefaqrequest.EnableFAQRequest;
import com.cgg.pps.model.request.faq.faqsubmit.FAQRequest;
import com.cgg.pps.model.request.farmer.GetTokensStage1Request;
import com.cgg.pps.model.request.farmer.farmerUpdate.FarmerUpdateRequest;
import com.cgg.pps.model.request.farmer.farmerdetails.FarmerDetailsRequest;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerSubmitRequest;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.gunnydetails.GunnyDetailsRequest;
import com.cgg.pps.model.request.gunnydetails.GunnysSubmitRequest;
import com.cgg.pps.model.request.ppc_details.MappedPPCDetailsRequest;
import com.cgg.pps.model.request.procurement.IssuedGunnyDataRequest;
import com.cgg.pps.model.request.procurement.PaddyOTPRequest;
import com.cgg.pps.model.request.rejectedtokenrequest.RejectedTokenRequest;
import com.cgg.pps.model.request.reports.CommonReportRequest;
import com.cgg.pps.model.request.reports.PaymentInfoRequest;
import com.cgg.pps.model.request.reports.TransactionReportRequest;
import com.cgg.pps.model.request.truckchit.ManualTCRequest;
import com.cgg.pps.model.request.truckchit.MillerMasterRequest;
import com.cgg.pps.model.request.truckchit.OnlineTCRequest;
import com.cgg.pps.model.request.truckchit.TCFinalSubmitRequest;
import com.cgg.pps.model.request.truckchit.info.ROInfoRequest;
import com.cgg.pps.model.request.truckchit.transaction.TransactionRequest;
import com.cgg.pps.model.request.truckchit.transportdetails.TransportDetailsRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.MasterVehicleTypeRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.request.validateuser.login.ValidateUserRequest;
import com.cgg.pps.model.request.validateuser.logout.LogoutRequest;
import com.cgg.pps.model.request.devicemgmt.bb.masterbank.MasterBankRequest;
import com.cgg.pps.model.request.faq.paddytest.PaddyTestRequest;
import com.cgg.pps.model.request.farmer.socialstatus.SocialStatusRequest;
import com.cgg.pps.model.request.farmer.tokenGenerate.TokenGenerationRequest;
import com.cgg.pps.model.response.ChangePwdResponse;
import com.cgg.pps.model.response.devicemgmt.bankbranch.MasterBranchMainResponse;
import com.cgg.pps.model.response.enablefaqresponse.EnableFAQResponse;
import com.cgg.pps.model.response.faq.FAQSubmitResponse;
import com.cgg.pps.model.response.farmer.farmerUpdate.FarmerUpdateResponse;
import com.cgg.pps.model.response.farmer.farmerdetails.FarmerDetailsResponse;
import com.cgg.pps.model.response.farmer.farmerdetailsnew.FarRegNew;
import com.cgg.pps.model.response.farmer.farmersubmit.FarmerSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenDropDownDataResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenStage1Response;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.gunnydetails.GunnyDetailsResponse;
import com.cgg.pps.model.response.gunnydetails.gunnysubmit.GunnySubmitResponse;
import com.cgg.pps.model.response.ppc_details.MappedPPCDetailsResponse;
import com.cgg.pps.model.response.procurement.IssuedGunnyDataResponse;
import com.cgg.pps.model.request.procurement.PaddyProcurementSubmit;
import com.cgg.pps.model.response.procurement.OTPResponse;
import com.cgg.pps.model.response.procurement.ProcurementSubmitResponse;
import com.cgg.pps.model.response.rejectedtokenresponse.RejectedTokenResponse;
import com.cgg.pps.model.response.report.payment.ReportsDataResponce;
import com.cgg.pps.model.response.report.payment_report.PaddyPaymentInfoResponse;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponseList;
import com.cgg.pps.model.response.reprint.ProcRePrintResponse;
import com.cgg.pps.model.response.reprint.TokenRePrintResponse;
import com.cgg.pps.model.response.reprint.truckchit.TCRePrintResponse;
import com.cgg.pps.model.response.truckchit.info.ROInfoResponse;
import com.cgg.pps.model.response.truckchit.manual.ManualTCResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.TransportResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.truckchit.online.OnlineTCResponse;
import com.cgg.pps.model.response.truckchit.ro.MillerMainResponse;
import com.cgg.pps.model.response.truckchit.submit.TCSubmitResponse;
import com.cgg.pps.model.response.truckchit.transaction.GetTransactionResponse;
import com.cgg.pps.model.response.truckchit.vehicle_type.VehicleTypesResponse;
import com.cgg.pps.model.response.validateuser.login.ValidateUserResponse;
import com.cgg.pps.model.response.validateuser.logout.LogoutResponse;
import com.cgg.pps.model.response.devicemgmt.masterbank.MasterBankMainResponse;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.MasterDistrictMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MasterMandalMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastervillage.MasterVillageMainResponse;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyTestResponse;
import com.cgg.pps.model.response.socialstatus.SocialStatusResponse;
import com.cgg.pps.model.response.farmer.tokengenarate.TokenGenerateResponse;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OPMSService {
    class Factory {
        public static OPMSService create() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                // development build
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                // production build
                interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            }

            httpClient.addInterceptor(interceptor);
            httpClient.readTimeout(60,TimeUnit.SECONDS);
            httpClient.connectTimeout(60, TimeUnit.SECONDS);
            httpClient.writeTimeout(60, TimeUnit.SECONDS);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(OPMSURL.OPMS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
            return retrofit.create(OPMSService.class);
        }

    }

//    static OkHttpClient.Builder getUnsafeOkHttpClient() {
//
//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.connectTimeout(60, TimeUnit.SECONDS)
//                    .writeTimeout(60, TimeUnit.SECONDS)
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .build();
//            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//            return builder;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    //------------------- Login & Logout ----------------------------------------

    @POST("MasterData/ValidateUser")
    Observable<ValidateUserResponse> getValidateUserResponse(@Body ValidateUserRequest validateUserRequest);


    @POST("MasterData/LogoutService")
    Observable<LogoutResponse> getLogoutResponse(@Body LogoutRequest logoutRequest);

    @POST("Token/GetTokens_DDL")
    Observable<GetTokensDDLResponse> getTokensDdlResponse(@Body GetTokensRequest getTokensFarmerRequest);

    @POST("Token/GetTokens_RegNo_ddl")
    Observable<GetTokenStage1Response> getTokensStage1Response(@Body GetTokensStage1Request getTokensStage1Request);

    //------------------- Registration ----------------------------------------


    @POST("MasterData/Get_MappedPPCDetails")
    Observable<MappedPPCDetailsResponse> getMappedPPCDetailsResponse(@Body MappedPPCDetailsRequest mappedPpcDetailsRequest);

    @POST("Farmer/GetFarmerDetailsData")
    Observable<FarmerDetailsResponse> getFarRegResponse(@Body FarmerDetailsRequest farmerDetailsRequest);

    @POST("Token/GetTokenStat1BindData")
    Observable<GetTokenDropDownDataResponse> getTokensDropDownDataResponse(@Body GetTokensRequest getTokensFarmerRequest);

    @POST("Procurement/SaveTokenRegistrationData")
    Observable<FarmerSubmitResponse> farmerSubmitResponse(@Body FarmerSubmitRequest farmerSubmitRequest);

    @POST("Token/SaveTokenData")
    Observable<TokenGenerateResponse> tokenGenerateResponse(@Body TokenGenerationRequest tokenGenerationRequest);

    @POST("Farmer/SaveandUpdateFarmerDetails")
    Observable<FarmerUpdateResponse> getFarUpdateResponse(@Body FarmerUpdateRequest farmerUpdateRequest);

    @POST("Farmer/GetFarmerDetailsData_New")
    Observable<FarRegNew> getFarRegResponseNew(@Body FarmerDetailsRequest farmerDetailsRequest);

    //------------------- Downloads ----------------------------------------


    @POST("MasterData/Get_MasterBankDetails")
    Observable<MasterBankMainResponse> getMasterBankResponse(@Body MasterBankRequest masterBankRequest);

    @POST("MasterData/Get_MasterBankBranchDetails")
    Observable<MasterBranchMainResponse> getMasterBranchResponse(@Body BankBranchRequest bankBranchRequest);

    @POST("MasterData/Get_MasterDistrictDetails")
    Observable<MasterDistrictMainResponse> getMasterDistrictResponse(@Body DMVRequest dmvRequest);

    @POST("MasterData/Get_MasterMandalDetails")
    Observable<MasterMandalMainResponse> getMasterMandalResponse(@Body DMVRequest dmvRequest);

    @POST("MasterData/Get_MasterVillageDetails")
    Observable<MasterVillageMainResponse> getMasterVillageResponse(@Body DMVRequest dmvRequest);

    @POST("MasterData/Get_PaddyDetailsMaster")
    Observable<PaddyTestResponse> getPaddyTestResponse(@Body PaddyTestRequest paddyTestRequest);

    @POST("MasterData/GetSocialStatusDetails")
    Observable<SocialStatusResponse> getSocialStatusResponse(@Body SocialStatusRequest socialStatusRequest);

    //------------------- Rejected Tokens ----------------------------------------
    @POST("Token/GetRejectTokens")
    Observable<RejectedTokenResponse> getRejectedTokens(@Body RejectedTokenRequest rejectedTokenRequest);

    @POST("FAQ/EnableToFAQ")
    Observable<EnableFAQResponse> EnableToFAQ(@Body EnableFAQRequest enableFAQRequest);

    //----------------- FAQ ----------------------------------------------------------


    @POST("Token/GetTokens")
    Observable<GetTokensResponse> getSelectedFAQResponse(@Body GetTokensRequest getTokensGunnysRequest);

    @POST("FAQ/SaveToFAQ")
    Observable<FAQSubmitResponse> getFAQSubmitResponse(@Body FAQRequest faqRequest);

    //----------------- Gunnys to Farmer ----------------------------------------------------------

    @POST("Token/GetTokens")
    Observable<GetTokensResponse> getTokensGunnyResponse(@Body GetTokensRequest getTokensGunnysRequest);

    @POST("MasterData/Get_GunnyOrderDetails")
    Observable<GunnyDetailsResponse> getGunnyDetails(@Body GunnyDetailsRequest gunnyDetailsRequest);

    @POST("MasterData/SaveIssuingGunnyData")
    Observable<GunnySubmitResponse> getGunnySubmitResponse(@Body GunnysSubmitRequest gunnysSubmitRequest);

    //----------------- Paddy Procurement ----------------------------------------------------------

    @POST("Token/GetTokens")
    Observable<GetTokensResponse> getTokensProcurementResponse(@Body GetTokensRequest getTokensProcurementRequest);

    @POST("MasterData/GetIssuedGunnyData")
    Observable<IssuedGunnyDataResponse> GetIssuedGunnyDataResponse(@Body IssuedGunnyDataRequest issuedGunnyDataRequest);

    @POST("Procurement/SaveProcurementData")
    Observable<ProcurementSubmitResponse> SaveProcurementData(@Body PaddyProcurementSubmit paddyProcurementSubmit);



    //----------------- Truckchit ----------------------------------------------------------


    @POST("Procurement/GetOnlineTruckChitNo")
    Observable<OnlineTCResponse> getOnlineTCResponse(@Body OnlineTCRequest onlineTCRequest);

    @POST("Procurement/UniqueManualTruckchit")
    Observable<ManualTCResponse> getManualTCResponse(@Body ManualTCRequest manualTCRequest);

    @POST("MasterData/Get_MillerDetails")
    Observable<MillerMainResponse> getMillerMasterResponse(@Body MillerMasterRequest millerMasterRequest);

    @POST("Farmer/GetTransactions")
    Observable<GetTransactionResponse> getTransactionResponse(@Body TransactionRequest transactionRequest);

    @POST("MasterData/Get_TransportationDetails")
    Observable<TransportResponse> getTransportResponse(@Body TransportDetailsRequest transportDetailsRequest);

    @POST("MasterData/Get_VehicleDetails")
    Observable<VehicleResponse> getVehicleResponse(@Body VehicleDetailsRequest vehicleDetailsRequest);

    @POST("MasterData/Get_MasterVechicletypeDetails")
    Observable<VehicleTypesResponse> getMasterVehicleTypesResponse(@Body MasterVehicleTypeRequest masterVehicleTypeRequest);

    @POST("Procurement/SaveTruckchitData")
    Observable<TCSubmitResponse> getTCSubmitResponse(@Body TCFinalSubmitRequest finalSubmitRequest);

    @POST("MillerRO/GetROdetails")
    Observable<ROInfoResponse> getROInfoResponse(@Body ROInfoRequest roInfoRequest);

    //----------------- RePrint ----------------------------------------------------------


    @POST("Procurement/PrintProcurementData")
    Observable<ProcRePrintResponse> getProcRePrintResponse(@Body ProRePrintRequest proRePrintRequest);

    @POST("Procurement/PrintTruckchitData")
    Observable<TCRePrintResponse> getTCRePrintResponse(@Body TCRePrintRequest tcRePrintRequest);

    @POST("Token/PrintToken")
    Observable<TokenRePrintResponse> getTokenRePrintResponse(@Body TokenRePrintRequest tokenRePrintRequest);

    //----------------- Reports ----------------------------------------------------------

    @POST("Token/GetTokens_Report")
    Observable<GetTokensResponse> getTokensReportsResponse(@Body GetTokensRequest getTokensFarmerRequest);

    @POST("Report/GetReportData")
    Observable<ReportsDataResponce> getCommanReportsResponse(@Body CommonReportRequest commonReportRequest);

    @POST("Farmer/GetTransactions")
    Observable<TransactionReportResponseList> GetTransactionsResponse(@Body TransactionReportRequest transactionReportRequest);

    @POST("MasterData/ChangePassword")
    Observable<ChangePwdResponse> UpdatePwdResponse(@Body ChangePwdRequest changePwdRequest);


    @POST("Report/GetPaddyPaymentReport")
    Observable<PaddyPaymentInfoResponse> GetPaddyPaymentResponse(@Body PaymentInfoRequest paymentInfoRequest);

    @POST("MasterData/GetMobileOtp")
    Observable<OTPResponse> GetPaddyOTPtResponse(@Body PaddyOTPRequest paddyOTPRequest);

}


