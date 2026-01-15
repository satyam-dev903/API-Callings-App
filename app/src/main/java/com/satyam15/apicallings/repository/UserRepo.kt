package com.satyam15.apicallings.repository

import com.satyam15.apicallings.api.ApiResult
import com.satyam15.apicallings.api.RetrofitClient
import com.satyam15.apicallings.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception

class UserRepo {

    suspend fun getUsersRepo(): ApiResult<List<User>> =
        withContext(Dispatchers.IO){
            try {
                val response= RetrofitClient.api.getUsers()
                if(response.isSuccessful){
                    ApiResult.Success(response.body()?:emptyList())
                }else{
                    ApiResult.Error(handleHttpError(response.code()))
                }
            }catch (e: IOException){
                ApiResult.Error("No internet connection")
            }catch (e:Exception){
                ApiResult.Error("Something went wrong")
            }
        }


    suspend fun addUserRepo(user: User) : ApiResult<User> =
        withContext(Dispatchers.IO){
             try {
                val response = RetrofitClient.api.addUser(user)
                if (response.isSuccessful){
                    ApiResult.Success(response.body()!!)
                }else{
                    ApiResult.Error(handleHttpError(response.code()))
                }
            }catch (e: Exception){
                ApiResult.Error("Unable to add user")
            }
        }


    suspend fun updateUserRepo(id: Int, user: User) : ApiResult<User> =
        withContext(Dispatchers.IO){
            try {
                val response= RetrofitClient.api.updateUser(id,user)
                if (response.isSuccessful && response.body() != null){
                    ApiResult.Success(response.body()!!)
                }else{
                    ApiResult.Error(handleHttpError(response.code()))
                }

            }catch (e: Exception){
                ApiResult.Error("unable to update")
            }
        }



    suspend fun deleteUserRepo(id: Int) : ApiResult<Unit> = withContext(Dispatchers.IO){
        try {
            val response= RetrofitClient.api.deleteUser(id)
            if (response.isSuccessful){
                ApiResult.Success(Unit)
            }else{
                ApiResult.Error(handleHttpError(response.code()))
            }
        }catch (e: Exception) {
            ApiResult.Error("Unable to delete")
        }
    }
    private fun handleHttpError(code:Int): String {
        return when(code){
            400 -> "Bad request"
            401 -> "Unauthorized"
            403 -> "Forbidden"
            404 -> "Resource not found"
            500 -> "Server error"
            else -> "Something went wrong (Error $code)"
        }
    }
}