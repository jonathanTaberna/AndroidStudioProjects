package com.example.windows_7.hstest.io.response;

import com.example.windows_7.hstest.model.User;
import com.google.gson.annotations.Expose;

public class ValidateUserResponse {
    @Expose
    private Integer salida;
    private User usuario;
    private String msj;

    public Integer getSalida() { return salida; }

    public User getUsuario() { return usuario; }

    public String getMsj() { return msj; }
}

