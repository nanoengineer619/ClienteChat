/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Nano
 */
public class ServidorArchivos extends Thread{
    ServerSocket skServidor=null;
    Socket skCliente;
    File origenArchivo;
    DataOutputStream out;
    ArrayList<CArchivoCompartido> arrayCompartidos;
    
    public ServidorArchivos(ArrayList<CArchivoCompartido> arrayCompartidos)
    {
        this.arrayCompartidos=arrayCompartidos;
        start();
    }
    
    @Override
    public void run()
    {
        try
        {
            skServidor=new ServerSocket(4444);
            System.out.println("Servidor Escuchando en 4444");
        }
        catch(Exception ex)
        {
            System.out.println("Error: "+ex.getMessage());
        }
        while(true)
        {
            try
            {
                skCliente=skServidor.accept();
                System.out.println("Iniciando Proceso...");
                DataInputStream in=new DataInputStream(skCliente.getInputStream());
                String Solicitud=in.readUTF().toString();
                String Solicitudx;
                String nombreU="";
                if(Solicitud.length()>=14)
                {
                    nombreU=Solicitud.substring(Solicitud.indexOf("</VerArchivos>")+14,Solicitud.length());
                    Solicitudx=Solicitud.substring(0, 14);
                }
                else
                {
                    Solicitudx="cop";
                }
                if(Solicitudx.equals("</VerArchivos>"))
                {
                    String archivos="";
                    int i=0;
                    for(CArchivoCompartido valor:arrayCompartidos)
                    {
                        i++;
                        if(valor.getReceptor().toUpperCase().equals(nombreU.toUpperCase()))
                        {
                            archivos=archivos+valor.getNombreArchivo()+"::"+valor.getPathArchivo();
                            if(i<arrayCompartidos.size())
                            {
                                archivos=archivos+"</Separator>";
                            }
                        }
                    }
                    out=new DataOutputStream(skCliente.getOutputStream());
                    out.writeUTF(archivos);
                    out.close();
                    skCliente.close();
                    continue;
                }
                System.out.println(Solicitud);
                for(CArchivoCompartido datosArchivos:arrayCompartidos)
                {
                    if(datosArchivos.getNombreArchivo().equals(Solicitud))
                    {
                        Solicitud=datosArchivos.getPathArchivo();
                    }
                }
                origenArchivo=new File(Solicitud);
            }
            catch(Exception ex)
            {
                System.out.println("Error "+ex.getMessage());
            }
            if (!origenArchivo.exists()) 
            {
                try
                {
                    out=new DataOutputStream(skCliente.getOutputStream());
                    out.writeUTF("El archivo Solicitado no existe");
                    out.close();
                }
                catch(Exception ex)
                {
                    System.out.println("Error "+ex.getMessage());
                }
                finally
                {
                    if(!skCliente.isClosed())
                    {
                        try
                        {
                            skCliente.close();
                        }
                        catch(Exception ex)
                        {
                            System.out.println("Error: "+ex.getMessage());
                        }
                    }
                    continue;
                }
            }
            try 
            {
                Transferencia T=new Transferencia(skCliente, origenArchivo);
            }
            catch (Exception e) 
            {
                System.out.println("Error al Enviar: "+e.getMessage());
            }         
        }
    }
}
