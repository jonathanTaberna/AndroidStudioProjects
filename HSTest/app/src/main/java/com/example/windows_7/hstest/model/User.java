package com.example.windows_7.hstest.model;

public class User {
    String nombre_usu;
    String apellido_usu;
    String dni_usu;
    String pass_usu;
    String mail_usu;
    String direccion_usu;
    String entre_calle_1_usu;
    String entre_calle_2_usu;
    int numero_usu;
    int piso_usu;
    String departamento_usu;
    String localidad_usu;
    int codpos_usu;

    public String getNombre_usu() {
        return nombre_usu;
    }

    public void setNombre_usu(String nombre_usu) {
        this.nombre_usu = nombre_usu;
    }

    public String getApellido_usu() {
        return apellido_usu;
    }

    public void setApellido_usu(String apellido_usu) {
        this.apellido_usu = apellido_usu;
    }

    public String getDni_usu() {
        return dni_usu;
    }

    public void setDni_usu(String dni_usu) {
        this.dni_usu = dni_usu;
    }

    public String getPass_usu() {
        return pass_usu;
    }

    public void setPass_usu(String pass_usu) {
        this.pass_usu = pass_usu;
    }

    public String getMail_usu() {
        return mail_usu;
    }

    public void setMail_usu(String mail_usu) {
        this.mail_usu = mail_usu;
    }

    public String getDireccion_usu() {
        return direccion_usu;
    }

    public void setDireccion_usu(String direccion_usu) {
        this.direccion_usu = direccion_usu;
    }

    public String getEntre_calle_1_usu() {
        return entre_calle_1_usu;
    }

    public void setEntre_calle_1_usu(String entre_calle_1_usu) {
        this.entre_calle_1_usu = entre_calle_1_usu;
    }

    public String getEntre_calle_2_usu() {
        return entre_calle_2_usu;
    }

    public void setEntre_calle_2_usu(String entre_calle_2_usu) {
        this.entre_calle_2_usu = entre_calle_2_usu;
    }

    public int getNumero_usu() {
        return numero_usu;
    }

    public void setNumero_usu(int numero_usu) {
        this.numero_usu = numero_usu;
    }

    public int getPiso_usu() {
        return piso_usu;
    }

    public void setPiso_usu(int piso_usu) {
        this.piso_usu = piso_usu;
    }

    public String getDepartamento_usu() {
        return departamento_usu;
    }

    public void setDepartamento_usu(String departamento_usu) {
        this.departamento_usu = departamento_usu;
    }

    public String getLocalidad_usu() {
        return localidad_usu;
    }

    public void setLocalidad_usu(String localidad_usu) {
        this.localidad_usu = localidad_usu;
    }

    public int getCodpos_usu() {
        return codpos_usu;
    }

    public void setCodpos_usu(int codpos_usu) {
        this.codpos_usu = codpos_usu;
    }

    @Override
    public String toString() {
        return "User{" +
                "nombre_usu='" + nombre_usu + '\'' +
                ", apellido_usu='" + apellido_usu + '\'' +
                ", dni_usu='" + dni_usu + '\'' +
                ", pass_usu='" + pass_usu + '\'' +
                ", mail_usu='" + mail_usu + '\'' +
                ", direccion_usu='" + direccion_usu + '\'' +
                ", entre_calle_1_usu='" + entre_calle_1_usu + '\'' +
                ", entre_calle_2_usu='" + entre_calle_2_usu + '\'' +
                ", numero_usu=" + numero_usu +
                ", piso_usu=" + piso_usu +
                ", departamento_usu='" + departamento_usu + '\'' +
                ", localidad_usu='" + localidad_usu + '\'' +
                ", codpos_usu=" + codpos_usu +
                '}';
    }
}
