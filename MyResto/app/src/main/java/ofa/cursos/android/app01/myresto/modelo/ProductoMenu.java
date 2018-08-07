package ofa.cursos.android.app01.myresto.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductoMenu implements Parcelable {
    private int id;
    private String nombre;
    private double precio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " - " + precio;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeDouble(precio);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductoMenu> CREATOR = new
            Creator<ProductoMenu>() {
                @Override
                public ProductoMenu createFromParcel(Parcel in) {
                    ProductoMenu aux = new ProductoMenu();
                    aux.id = in.readInt();
                    aux.nombre = in.readString();
                    aux.precio = in.readDouble();
                    return aux;
                }
                @Override
                public ProductoMenu[] newArray(int size) {
                    return new ProductoMenu[size];
                }
            };


}
