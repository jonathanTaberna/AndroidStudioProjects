package hsneoclinica.neoclinica.provisorios;

public class Internado {

    private String egreso;
    private String lugar;
    private String icono;
    private String paciente;
    private String mutual;
    private String profesional;
    private String motivo;
    private String fechaIng;
    private String fechaEgr;
    private String edad;

    public Internado() {
        this.egreso = "";
        this.lugar = "";
        this.icono = "";
        this.paciente = "";
        this.mutual = "";
        this.profesional = "";
        this.motivo = "";
        this.fechaIng = "";
        this.fechaEgr = "";
        this.edad = "";
    }

    public Internado(String egreso, String lugar, String icono, String paciente, String mutual, String profesional, String motivo, String fechaIng, String fechaEgr, String edad) {
        this.egreso = egreso;
        this.lugar = lugar;
        this.icono = icono;
        this.paciente = paciente;
        this.mutual = mutual;
        this.profesional = profesional;
        this.motivo = motivo;
        this.fechaIng = fechaIng;
        this.fechaEgr = fechaEgr;
        this.edad = edad;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getFechaEgr() {
        return fechaEgr;
    }

    public void setFechaEgr(String fechaEgr) {
        this.fechaEgr = fechaEgr;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
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
