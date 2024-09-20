package com.learning.expencetracker.Utils

import com.learning.expencetracker.Model.AddMoneyTrans.AddMoneyTransInputModel
import com.learning.expencetracker.Model.AddMoneyTrans.AddMoneyTransOutputModel
import com.learning.expencetracker.Model.ChangePasswordModel.ChangePasswordInputModel
import com.learning.expencetracker.Model.ChangePasswordModel.ChangePasswordOutputModel
import com.learning.expencetracker.Model.CreateNewBook.CreateNewBookInputModel
import com.learning.expencetracker.Model.CreateNewBook.CreateNewBookOutputModel
import com.learning.expencetracker.Model.DeleteBookModel.DeleteBookOutputModel
import com.learning.expencetracker.Model.DeleteTrans.DeleteTransOutputModel
import com.learning.expencetracker.Model.ForgottenPassword.ForgottenPasswordInputModel
import com.learning.expencetracker.Model.ForgottenPassword.ForgottenPasswordOutputModel
import com.learning.expencetracker.Model.GetAllTrans.GetAllTransOutputModel
import com.learning.expencetracker.Model.GetBooks.GetBooksOutputModel
import com.learning.expencetracker.Model.GetDataBasedOnCategory.GetDataBasedOnCatOutputModel
import com.learning.expencetracker.Model.GetMe.GetMeOutputModel
import com.learning.expencetracker.Model.GetTransFilters.GetTransFilterOutputModel
import com.learning.expencetracker.Model.KeysModel.GetKeysOutputModel
import com.learning.expencetracker.Model.LoginUser.LoginUserInputModel
import com.learning.expencetracker.Model.LoginUser.LoginUserOutputModel
import com.learning.expencetracker.Model.PaymentHistory.PaymentHistoryOutputModel
import com.learning.expencetracker.Model.ResendVerificationOTP.ResendVerificationCodeInputModel
import com.learning.expencetracker.Model.ResendVerificationOTP.ResendVerificationOTPOutputModel
import com.learning.expencetracker.Model.ResetPassword.ResetPasswordInputModel
import com.learning.expencetracker.Model.ResetPassword.ResetPasswordOutputModel
import com.learning.expencetracker.Model.SignUp.SignUpInputModel
import com.learning.expencetracker.Model.SignUp.SignUpOutputModel
import com.learning.expencetracker.Model.UpdateBookModel.UpdateBookInputModel
import com.learning.expencetracker.Model.UpdateBookModel.UpdateBookOutputModel
import com.learning.expencetracker.Model.UpdateUser.UpdateUserInputModel
import com.learning.expencetracker.Model.UpdateSingleTrans.UpdateSingleTransInputModel
import com.learning.expencetracker.Model.UpdateSingleTrans.UpdateSingleTransOutputModel
import com.learning.expencetracker.Model.UpdateUser.UpdateUserOutputModel
import com.learning.expencetracker.Model.VerifyOTP.VerifyOTPInputModel
import com.learning.expencetracker.Model.VerifyOTP.VerifyOTPOutputModel
import com.learning.expencetracker.Model.VerifyTransAndAddToDB.VerifyTransAndAddToDBInputModel
import com.learning.expencetracker.Model.VerifyTransAndAddToDB.VerifyTransIdAndAddToDBOutputModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming


interface RetrofitApis {
//    @POST("/api/api/v1/Users/signUp")
//    suspend fun signUp(@Body body : SignUpInputModel) : Response<SignUpOutputModel>
//    @POST("/api/api/v1/Users/verifyEmail")
//    suspend fun verifyOTP(@Body body : VerifyOTPInputModel) : Response<VerifyOTPOutputModel>
//    @PATCH("/api/api/v1/Users/resendVerificationcode")
//    suspend fun resendVerificationCode(@Body body : ResendVerificationCodeInputModel) : Response<ResendVerificationOTPOutputModel>
//    @POST("/api/api/v1/Users/login")
//    suspend fun loginUser(@Body body : LoginUserInputModel) : Response<LoginUserOutputModel>
//    @POST("/api/api/v1/Users/forgottenPassword")
//    suspend fun forgottenPassword(@Body body : ForgottenPasswordInputModel) : Response<ForgottenPasswordOutputModel>
//    @PATCH("/api/api/v1/Users/resetPassword")
//    suspend fun resetPassword(@Body body : ResetPasswordInputModel) : Response<ResetPasswordOutputModel>
//    @POST("/api/api/v1/bookname")
//    suspend fun createNewBook(@Body body : CreateNewBookInputModel, @Header("authorization") authHeader:String) : Response<CreateNewBookOutputModel>
//    @GET("/api/api/v1/bookname")
//    suspend fun getBooks(@Header("authorization") authHeader:String) : Response<GetBooksOutputModel>
//    @PATCH("/api/api/v1/bookname/{id}")
//    suspend fun updateBook(@Header("authorization") authHeader:String,@Path("id") otp: String?, @Body body : UpdateBookInputModel) : Response<UpdateBookOutputModel>
//    @DELETE("/api/api/v1/bookname/{id}")
//    suspend fun deleteBook(@Header("authorization") authHeader:String,@Path("id") otp: String?) : Response<DeleteBookOutputModel>
//    @POST("/api/api/v1/moneyTrans/{id}")
//    suspend fun addMoneyTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?,@Body body : AddMoneyTransInputModel) : Response<AddMoneyTransOutputModel>
//    @GET("/api/api/v1/moneyTrans/{id}")
//    suspend fun getAllTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?) : Response<GetAllTransOutputModel>
//    @PATCH("/api/api/v1/moneyTrans/{id}")
//    suspend fun updateSingleTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?,@Body body : UpdateSingleTransInputModel) : Response<UpdateSingleTransOutputModel>
//    @DELETE("/api/api/v1/moneyTrans/{id}")
//    suspend fun deleteSingleTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?) : Response<DeleteTransOutputModel>
//    @GET("/api/api/v1/moneyTrans/filter/{id}?")
//    suspend fun getTransFilter(@Header("authorization") authHeader: String, @Path("id") id: String, @Query("type") type: String?, @Query("members") members: String?, @Query("date") date: String?, @Query("category") category: String?): Response<GetTransFilterOutputModel>
//    @GET("/api/api/v1/moneyTrans/basedOnCat/{id}?")
//    suspend fun getDataBasedOnCategory(@Header("authorization") authHeader: String, @Path("id") id: String, ): Response<GetDataBasedOnCatOutputModel>
//    @PATCH("/api/api/v1/Users/updatePassword")
//    suspend fun changePassword(@Header("authorization") authHeader: String, @Body body : ChangePasswordInputModel): Response<ChangePasswordOutputModel>
//    @PATCH("/api/api/v1/users")
//    suspend fun updateMe(@Header("authorization") authHeader: String, @Body body: UpdateUserInputModel?, ): Response<UpdateUserOutputModel>
//    @GET("/api/api/v1/users")
//    suspend fun getMe(@Header("authorization") authHeader: String, ): Response<GetMeOutputModel>
//    @GET("/api/api/v1/moneyTrans/download/{id}")
//    @Streaming
//    suspend fun downloadExcel( @Header("authorization") authHeader: String,@Path("id") id: String): Response<ResponseBody>
//    @GET("/api/api/v1/payment/getKeys/{amt}")
//    suspend fun getKeys( @Header("authorization") authHeader: String,@Path("amt") amt: String): Response<GetKeysOutputModel>
//
//    @POST("/api/api/v1/payment")
//    suspend fun verifyAndAddToDB( @Header("authorization") authHeader: String,@Body body: VerifyTransAndAddToDBInputModel): Response<VerifyTransIdAndAddToDBOutputModel>
//
//    @GET("/api/api/v1/payment")
//    suspend fun paymentHistory( @Header("authorization") authHeader: String): Response<PaymentHistoryOutputModel>











    @POST("/api/v1/Users/signUp")
    suspend fun signUp(@Body body : SignUpInputModel) : Response<SignUpOutputModel>
    @POST("/api/v1/Users/verifyEmail")
    suspend fun verifyOTP(@Body body : VerifyOTPInputModel) : Response<VerifyOTPOutputModel>
    @PATCH("/api/v1/Users/resendVerificationcode")
    suspend fun resendVerificationCode(@Body body : ResendVerificationCodeInputModel) : Response<ResendVerificationOTPOutputModel>
    @POST("/api/v1/Users/login")
    suspend fun loginUser(@Body body : LoginUserInputModel) : Response<LoginUserOutputModel>
    @POST("/api/v1/Users/forgottenPassword")
    suspend fun forgottenPassword(@Body body : ForgottenPasswordInputModel) : Response<ForgottenPasswordOutputModel>
    @PATCH("/api/v1/Users/resetPassword")
    suspend fun resetPassword(@Body body : ResetPasswordInputModel) : Response<ResetPasswordOutputModel>
    @POST("/api/v1/bookname")
    suspend fun createNewBook(@Body body : CreateNewBookInputModel, @Header("authorization") authHeader:String) : Response<CreateNewBookOutputModel>
    @GET("/api/v1/bookname")
    suspend fun getBooks(@Header("authorization") authHeader:String) : Response<GetBooksOutputModel>
    @PATCH("/api/v1/bookname/{id}")
    suspend fun updateBook(@Header("authorization") authHeader:String,@Path("id") otp: String?, @Body body : UpdateBookInputModel) : Response<UpdateBookOutputModel>
    @DELETE("/api/v1/bookname/{id}")
    suspend fun deleteBook(@Header("authorization") authHeader:String,@Path("id") otp: String?) : Response<DeleteBookOutputModel>
    @POST("/api/v1/moneyTrans/{id}")
    suspend fun addMoneyTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?,@Body body : AddMoneyTransInputModel) : Response<AddMoneyTransOutputModel>
    @GET("/api/v1/moneyTrans/{id}")
    suspend fun getAllTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?) : Response<GetAllTransOutputModel>
    @PATCH("/api/v1/moneyTrans/{id}")
    suspend fun updateSingleTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?,@Body body : UpdateSingleTransInputModel) : Response<UpdateSingleTransOutputModel>
    @DELETE("/api/v1/moneyTrans/{id}")
    suspend fun deleteSingleTrans(@Header("authorization") authHeader:String,@Path("id") otp: String?) : Response<DeleteTransOutputModel>
    @GET("/api/v1/moneyTrans/filter/{id}?")
    suspend fun getTransFilter(@Header("authorization") authHeader: String, @Path("id") id: String, @Query("type") type: String?, @Query("members") members: String?, @Query("date") date: String?, @Query("category") category: String?): Response<GetTransFilterOutputModel>
    @GET("/api/v1/moneyTrans/basedOnCat/{id}?")
    suspend fun getDataBasedOnCategory(@Header("authorization") authHeader: String, @Path("id") id: String, ): Response<GetDataBasedOnCatOutputModel>
    @PATCH("/api/v1/Users/updatePassword")
    suspend fun changePassword(@Header("authorization") authHeader: String, @Body body : ChangePasswordInputModel): Response<ChangePasswordOutputModel>
    @PATCH("/api/v1/users")
    suspend fun updateMe(@Header("authorization") authHeader: String, @Body body: UpdateUserInputModel?, ): Response<UpdateUserOutputModel>
    @GET("/api/v1/users")
    suspend fun getMe(@Header("authorization") authHeader: String, ): Response<GetMeOutputModel>
    @GET("/api/v1/moneyTrans/download/{id}")
    @Streaming
    suspend fun downloadExcel( @Header("authorization") authHeader: String,@Path("id") id: String): Response<ResponseBody>
    @GET("/api/v1/payment/getKeys/{amt}")
    suspend fun getKeys( @Header("authorization") authHeader: String,@Path("amt") amt: String): Response<GetKeysOutputModel>

    @POST("/api/v1/payment")
    suspend fun verifyAndAddToDB( @Header("authorization") authHeader: String,@Body body: VerifyTransAndAddToDBInputModel): Response<VerifyTransIdAndAddToDBOutputModel>

    @GET("/api/v1/payment")
    suspend fun paymentHistory( @Header("authorization") authHeader: String): Response<PaymentHistoryOutputModel>

}