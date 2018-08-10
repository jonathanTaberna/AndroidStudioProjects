package com.example.windows_7.hstest.io;

import com.example.windows_7.hstest.io.response.ValidateUserResponse;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HSTestApiService {
    @POST("ValidarUsuario")
    Call<ValidateUserResponse> validateUser(@Body HashMap<String, Object> body);
}
