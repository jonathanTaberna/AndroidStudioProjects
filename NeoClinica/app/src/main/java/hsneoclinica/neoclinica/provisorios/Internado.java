package hsneoclinica.neoclinica.provisorios;

public class Internado {

    private String egreso;
    private String lugar;
    private String paciente;
    private String mutual;
    private String profesional;
    private String motivo;
    private String edad;

    public Internado() {
        this.egreso = "";
        this.lugar = "";
        this.paciente = "";
        this.mutual = "";
        this.profesional = "";
        this.motivo = "";
        this.edad = "";
    }

    public Internado(String egreso, String lugar, String paciente, String mutual, String profesional, String motivo, String edad) {
        this.egreso = egreso;
        this.lugar = lugar;
        this.paciente = paciente;
        this.mutual = mutual;
        this.profesional = profesional;
        this.motivo = motivo;
        this.edad = edad;
    }

    public String getEgreso() {
        return egreso;
    }

    public void setEgreso(String egreso) {
        this.egreso = egreso;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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

    public String getProfesional() {
        return profesional;
    }

    public void setProfesional(String profesional) {
        this.profesional = profesional;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
}
