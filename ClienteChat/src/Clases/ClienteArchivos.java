/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Nano
 */
public class ClienteArchivos extends Thread{
    Socket skCliente=null;
    DataInputStream in;
    DataOutputStream out;
    String pathArchivo;
    String pathGuardar;
    String ip;
    JInternalFrame internal;
    JProgressBar progresoBar;
    
    public ClienteArchivos(String pathArchivo, String pathGuardar, String ip, JInternalFrame internal)
    {
        this.pathArchivo=pathArchivo;
        this.pathGuardar=pathGuardar;
        this.ip=ip;
        this.internal=internal;
        start();
    }
    @Override
    public void run()
    {
        try
        {
            byte[] b=new byte[99999];
            skCliente=new Socket(ip, 4444);
            out=new DataOutputStream(skCliente.getOutputStream());
            out.writeUTF(pathArchivo);
            in=new DataInputStream(skCliente.getInputStream());
            if(in.readUTF().equals("El archivo Solicitado no existe"))
            {
                JOptionPane.showMessageDialog(null, "El archivo Solicitado no existe");
                in.close();
                out.close();
                skCliente.close();
                return;
            }
            FileOutputStream archivoNuevo=new FileOutputStream(pathGuardar);
            in=new DataInputStream(skCliente.getInputStream());
            long tamArchivo=in.readLong();
            int longitudArchivo=(int)(tamArchivo);
            int i=in.read(b);
            System.out.println(tamArchivo);
            progresoBar = new JProgressBar(0,longitudArchivo);
            progresoBar.setStringPainted(true);
            progresoBar.setSize(120, 30);
            internal.add(progresoBar);
            progresoBar.setVisible(true);
            int con=0;
            while(i!=-1)
            {
                archivoNuevo.write(b, 0, i);
                con+=i;
                progresoBar.setValue(con);
                i=in.read(b);
            }
            if(progresoBar.getValue()<tamArchivo)
            {
                JOptionPane.showMessageDialog(null, "No se pudo finalizar la transferencia", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Transferencia Finalizada Correctamente");
            }
            progresoBar.setVisible(false);
            in.close();
            out.close();
            skCliente.close();
        }
        catch(IOException | HeadlessException ex)
        {
            JOptionPane.showMessageDialog(null, "No se pudo finalizar la transferencia", "Error", JOptionPane.ERROR_MESSAGE);
            progresoBar.setVisible(false);
            System.out.println("HiloClienteArchivo: "+ex.getMessage());
        }
    }
    
}
