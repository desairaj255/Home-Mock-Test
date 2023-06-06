package com.home.mock.test.data.api.service

import com.home.mock.test.data.api.constants.ApiConstants.USERS_ENDPOINT
import com.home.mock.test.data.api.constants.ApiConstants.AUTH_TOKEN
import com.home.mock.test.data.api.response.GetOneUserResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Interface that defines API service methods for users
 */
interface UsersService {
    // Annotation specifying the HTTP GET method and the endpoint path
    // The endpoint path is obtained from a constant property defined elsewhere, likely in ApiConstants
    @GET(USERS_ENDPOINT)
    suspend fun fetchUsers(@Header("Authorization")authorization: String = AUTH_TOKEN, @Query("page") page: Int, @Query("per_page") perPage: Int = 20): ApiResponse<List<GetOneUserResponse>>
    // Method declaration for fetching users from the API
    // The suspend keyword indicates that this method can be suspended and called from a coroutine
    // The return type is ApiResponse<List<GetOneUserResponse>>, indicating that it will return an ApiResponse
    // object wrapping a List of GetOneUserResponse objects
}
