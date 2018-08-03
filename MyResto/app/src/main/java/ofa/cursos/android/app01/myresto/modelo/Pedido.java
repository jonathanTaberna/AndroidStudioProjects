package ofa.cursos.android.app01.myresto.modelo;

import java.util.Objects;

public class Pedido {

    private Integer id;
    private String nombre;
    private String pedido;
    private boolean envioDomicilio;
    private boolean bebidaXL;
    private boolean permiteCancelar;
    private boolean incluyePropina;
    private boolean enviarNotificaciones;
    private boolean pagoAuotomatico;
    private static int idGenerator = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public boolean isEnvioDomicilio() {
        return envioDomicilio;
    }

    public void setEnvioDomicilio(boolean envioDomicilio) {
        this.envioDomicilio = envioDomicilio;
    }

    public boolean isBebidaXL() {
        return bebidaXL;
    }

    public void setBebidaXL(boolean bebidaXL) {
        this.bebidaXL = bebidaXL;
    }

    public boolean isPermiteCancelar() {
        return permiteCancelar;
    }

    public void setPermiteCancelar(boolean permiteCancelar) {
        this.permiteCancelar = permiteCancelar;
    }

    public boolean isIncluyePropina() {
        return incluyePropina;
    }

    public void setIncluyePropina(boolean incluyePropina) {
        this.incluyePropina = incluyePropina;
    }

    public boolean isEnviarNotificaciones() {
        return enviarNotificaciones;
    }

    public void setEnviarNotificaciones(boolean enviarNotificaciones) {
        this.enviarNotificaciones = enviarNotificaciones;
    }

    public boolean isPagoAuotomatico() {
        return pagoAuotomatico;
    }

    public void setPagoAuotomatico(boolean pagoAuotomatico) {
        this.pagoAuotomatico = pagoAuotomatico;
    }

    public Pedido(){
        this.id = ++Pedido.idGenerator;
    }

    @Override
    public String toString() {
        return nombre + " - " + pedido;
    }
    /*
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pedido='" + pedido + '\'' +
                ", envioDomicilio=" + envioDomicilio +
                ", bebidaXL=" + bebidaXL +
                ", permiteCancelar=" + permiteCancelar +
                ", incluyePropina=" + incluyePropina +
                ", enviarNotificaciones=" + enviarNotificaciones +
                ", pagoAuotomatico=" + pagoAuotomatico +
                '}';
    }
    */
    /*
    //PARA API >= 19
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    */
}
