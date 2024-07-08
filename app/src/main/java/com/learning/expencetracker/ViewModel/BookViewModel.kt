package com.learning.expencetracker.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.expencetracker.Fragments.HomeFragment
import com.learning.expencetracker.Model.CreateNewBook.CreateNewBookInputModel
import com.learning.expencetracker.Model.CreateNewBook.CreateNewBookOutputModel
import com.learning.expencetracker.Model.DeleteBookModel.DeleteBookOutputModel
import com.learning.expencetracker.Model.GetBooks.GetBooksOutputModel
import com.learning.expencetracker.Model.UpdateBookModel.UpdateBookInputModel
import com.learning.expencetracker.Model.UpdateBookModel.UpdateBookOutputModel
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.Utils.RetrofitApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewModel : ViewModel() {
    //    create new book
    var resultOfCreateNewBook: MutableLiveData<CreateNewBookOutputModel> = MutableLiveData()
    fun createNewBook(
        input: CreateNewBookInputModel,
        context: Context,
        fragment: HomeFragment,
        token: String
    ) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.createNewBook(input, token)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfCreateNewBook.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                fragment.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForCreateNewBook(): LiveData<CreateNewBookOutputModel> = resultOfCreateNewBook
    fun clearCreateNewBook() {
        resultOfCreateNewBook.value = null
    }

    // get book for particular user
    var resultOfGetBooks: MutableLiveData<GetBooksOutputModel> = MutableLiveData()
    fun getBooks(context: Context, fragment: HomeFragment, token: String) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.getBooks(token)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetBooks.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                fragment.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }

    fun observerForGetBooks(): LiveData<GetBooksOutputModel> = resultOfGetBooks
    fun clearGetBookForUser() {
        resultOfGetBooks.value = null
    }

    // update books details
    var resultOfUpdateBook: MutableLiveData<UpdateBookOutputModel> = MutableLiveData()
    fun updateBook(
        context: Context,
        fragment: HomeFragment,
        token: String,
        id: String,
        input: UpdateBookInputModel
    ) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.updateBook(token, id, input)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfUpdateBook.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                fragment.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }

    fun observerForUpdateBooks(): LiveData<UpdateBookOutputModel> = resultOfUpdateBook
    fun clearUpdateBook() {
        resultOfUpdateBook.value = null
    }

    // delete book
    var resultOfDeleteBook: MutableLiveData<DeleteBookOutputModel> = MutableLiveData()
    fun deleteBook(context: Context, fragment: HomeFragment, token: String, id: String) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.deleteBook(token, id)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfDeleteBook.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                fragment.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForDeleteBooks(): LiveData<DeleteBookOutputModel> = resultOfDeleteBook
    fun clearDeleteBook() {
        resultOfDeleteBook.value = null
    }
}