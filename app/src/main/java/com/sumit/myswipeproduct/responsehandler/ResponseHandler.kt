package com.sumit.myswipeproduct.responsehandler


import android.content.Context
import com.sumit.myswipeproduct.R
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


class ResponseHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun <T : Any> callAPI(call: suspend () -> Response<T>): APIResponse<T> {
        try {
            val apiResponse = call()
            return if (apiResponse.isSuccessful && apiResponse.body() != null) {
                APIResponse.Success(apiResponse.body()!!)
            } else {
                val errorObj = apiResponse.errorBody()!!.charStream().readText()
                APIResponse.Error(
                    message =
                    getErrorMessage(
                        errorBody = errorObj
                    )
                )
            }
        } catch (e: IOException) {
            return APIResponse.Error(saySomethingWentWrong())
        } catch (socketTimeOutException: SocketTimeoutException) {
            return APIResponse.Error(saySomethingWentWrong())
        } catch (e: Exception) {
            return APIResponse.Error(getErrorMessage(null))
        }
    }

    fun getErrorMessage(errorBody: String?): String {
        return errorBody ?: saySomethingWentWrong()
    }


    private fun saySomethingWentWrong(): String {
        return context.resources.getString(R.string.text_something_went_wrong_error)
    }

}