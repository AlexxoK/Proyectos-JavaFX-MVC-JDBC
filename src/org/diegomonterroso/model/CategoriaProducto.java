package org.diegomonterroso.model;

public class CategoriaProducto {
    private int categoriaProductoId;
    private String nombreCategoria;
    private String descripcionCategoria;

    public CategoriaProducto() {
    }

    public CategoriaProducto(int categoriaProductoId, String nombreCategoria, String descripcionCategoria) {
        this.categoriaProductoId = categoriaProductoId;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
    }

    public int getCategoriaProductoId() {
        return categoriaProductoId;
    }

    public void setCategoriaProductoId(int categoriaProductoId) {
        this.categoriaProductoId = categoriaProductoId;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    @Override
    public String toString() {
        return "Id: " + categoriaProductoId + " | " + nombreCategoria;
    }
    
    
}
