package org.diegomonterroso.model;

public class DetalleCompra {
    private int detalleCompraId;
    private int cantidadCompra;
    
    private int productoId;
    private String producto;
    
    private int compraId;
    private String compra;

    public DetalleCompra() {
    }

    public DetalleCompra(int detalleCompraId, int cantidadCompra, String producto, String compra) {
        this.detalleCompraId = detalleCompraId;
        this.cantidadCompra = cantidadCompra;
        this.producto = producto;
        this.compra = compra;
    }

    public int getDetalleCompraId() {
        return detalleCompraId;
    }

    public void setDetalleCompraId(int detalleCompraId) {
        this.detalleCompraId = detalleCompraId;
    }

    public int getCantidadCompra() {
        return cantidadCompra;
    }

    public void setCantidadCompra(int cantidadCompra) {
        this.cantidadCompra = cantidadCompra;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCompraId() {
        return compraId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    @Override
    public String toString() {
        return "DetalleCompra{" + "detalleCompraId=" + detalleCompraId + ", cantidadCompra=" + cantidadCompra + ", productoId=" + productoId + ", producto=" + producto + ", compraId=" + compraId + ", compra=" + compra + '}';
    }
    
    
}
