package jt.cotitagliero.provisorios;

public class Cliente {

    private String fecha;
    private double peso;
    private double pesoInicial;
    private String imc;
    private String gVisceral;
    private String mGrasa;
    private String mMuscular;
    private String edadMeta;
    private String tipo;

    public Cliente(String fecha, double peso, double pesoInicial, String imc, String gVisceral, String mGrasa, String mMuscular, String edadMeta, String tipo) {
        this.fecha = fecha;
        this.peso = peso;
        this.pesoInicial = pesoInicial;
        this.imc = imc;
        this.gVisceral = gVisceral;
        this.mGrasa = mGrasa;
        this.mMuscular = mMuscular;
        this.edadMeta = edadMeta;
        this.tipo = tipo;
    }

    public Cliente() {
        this.fecha = null;
        this.peso = 0;
        this.pesoInicial = 0;
        this.imc = null;
        this.gVisceral = null;
        this.mGrasa = null;
        this.mMuscular = null;
        this.edadMeta = null;
        this.tipo = null;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getPesoInicial() {
        return pesoInicial;
    }

    public void setPesoInicial(double pesoInicial) {
        this.pesoInicial = pesoInicial;
    }

    public String getImc() {
        return imc;
    }

    public void setImc(String imc) {
        this.imc = imc;
    }

    public String getgVisceral() {
        return gVisceral;
    }

    public void setgVisceral(String gVisceral) {
        this.gVisceral = gVisceral;
    }

    public String getmGrasa() {
        return mGrasa;
    }

    public void setmGrasa(String mGrasa) {
        this.mGrasa = mGrasa;
    }

    public String getmMuscular() {
        return mMuscular;
    }

    public void setmMuscular(String mMuscular) {
        this.mMuscular = mMuscular;
    }

    public String getEdadMeta() {
        return edadMeta;
    }

    public void setEdadMeta(String edadMeta) {
        this.edadMeta = edadMeta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
