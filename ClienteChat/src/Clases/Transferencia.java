/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 *
 * @author Nano
 */
public class Transferencia extends Thread{
    Socket skCliente;
    File ArchivoOrigen;
    
    public Transferencia(Socket skCliente, File ArchivoOrigen)
    {
        this.skCliente=skCliente;
        this.ArchivoOrigen=ArchivoOrigen;
        start();
    }
    @Override
    public void run()
    {
        try
        {
            DataOutputStream out=new DataOutputStream(skCliente.getOutputStream());
            out.writeUTF("Transfiriendo Archivo");
            System.out.println("Transferencia por el puerto 4444 al cliente: "+skCliente.getInetAddress());
            FileInputStream fin = new FileInputStream(ArchivoOrigen);
            out.writeLong(ArchivoOrigen.length());
            byte[] readData = new byte[99999];
            int i=fin.read(readData);
            while (i!=-1)
            {
                out.write(readData,0,i);
                i=fin.read(readData);
                
            }
            System.out.println("Transferencia Finalizada a: "+skCliente.getInetAddress());
            fin.close();
            out.close();
            skCliente.close();
        }
        catch(Exception ex)
        {
            System.out.println("Error en el hilo Transferencia: "+ex.getMessage());
        }
    }
    
}
