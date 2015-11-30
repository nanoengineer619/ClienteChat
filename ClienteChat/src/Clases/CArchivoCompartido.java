/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author Nano
 */
public class CArchivoCompartido {
    private String nombreArchivo;
    private String pathArchivo;
    private String receptor;
    
    public CArchivoCompartido(String nombreArchivo, String pathArchivo, String receptor)
    {
        this.nombreArchivo=nombreArchivo;
        this.pathArchivo=pathArchivo;
        this.receptor=receptor;
    }
    public String getNombreArchivo()
    {
        return this.nombreArchivo;
    }
    public String getPathArchivo()
    {
        return this.pathArchivo;
    }
    public String getReceptor()
    {
        return this.receptor;
    }
}
