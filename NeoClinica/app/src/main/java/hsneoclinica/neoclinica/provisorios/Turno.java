package hsneoclinica.neoclinica.provisorios;

import android.graphics.Bitmap;

public class Turno {

    private String hora;
    private String paciente;
    private String mutual;
    private String obs;
    private String color;


    public Turno(String hora, String paciente, String mutual, String obs, String color) {
        this.hora = hora;
        this.paciente = paciente;
        this.mutual = mutual;
        this.obs = obs;
        this.color = color;
    }

    public Turno() {
        this.hora = "";
        this.paciente = "";
        this.mutual = "";
        this.obs = "";
        this.color = "";
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getMutual() {
        return mutual;
    }

    public void setMutual(String mutual) {
        this.mutual = mutual;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
