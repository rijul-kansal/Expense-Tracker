package com.learning.expencetracker.Model.VerifyTransAndAddToDB

data class TransSuccessModel(
    var Error_Message: String? = null, // No Error
    var PG_TYPE: String? = null, // NB-PG
    var addedon: String? = null, // 2024-07-24 09:19:06
    var amount: String? = null, // 600.00
    var bank_ref_no: String? = null, // 21f41135-dc87-4a23-a386-2d905ef67acd
    var discount: String? = null, // 0.00
    var email: String? = null, // a@gmail.com
    var error_code: String? = null, // E000
    var field9: String? = null, // Transaction Completed Successfully
    var firstname: String? = null, // Rijul
    var furl: String? = null, // https://cbjs.payu.in/sdk/failure
    var hash: String? = null, // 91cb0e0cf6a9214e8aa0f6f3f2f8c3bb43a59400fad870d905eee0b577a867141afa1895150b9f9441b226b2a62d7fcad13b40fe351c40f62fc39485639ec839
    var ibibo_code: String? = null, // SBIB
    var id: Long? = null, // 403993715531989346
    var is_seamless: Int? = null, // 1
    var key: String? = null, // RzpfQl
    var mode: String? = null, // NB
    var payment_source: String? = null, // payu
    var phone: String? = null, // 7123456789
    var productinfo: String? = null, // AccessAppFeatures
    var status: String? = null, // success
    var surl: String? = null, // https://cbjs.payu.in/sdk/success
    var transaction_fee: String? = null, // 600.00
    var txnid: String? = null, // paymentForTest-1721792931761
    var unmappedstatus: String? = null // captured
)