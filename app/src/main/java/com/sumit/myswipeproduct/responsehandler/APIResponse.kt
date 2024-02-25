package com.sumit.myswipeproduct.responsehandler


sealed class APIResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val errorBody: String? = null
) {

    class Success<T>(data: T) : APIResponse<T>(data)
    class Error<T>(message: String?, data: T? = null,
                   errorBody: String? = null) : APIResponse<T>(data, message, errorBody)
    class Loading<T> : APIResponse<T>()

}

fun <X, R> APIResponse<X>.map(transform: (X) -> R): APIResponse<R?> {
    return when (this) {
        is APIResponse.Success -> APIResponse.Success(data?.let { transform(it) })
        is APIResponse.Error -> APIResponse.Error(
            data = data?.let { transform(it) },
            message = message,
            errorBody = errorBody
        )
        is APIResponse.Loading -> APIResponse.Loading()
    }
}