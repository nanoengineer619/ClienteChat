/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author Nano
 */
public class Enviar extends Thread {
    
    private String Emisor;
    private String Receptor;
    private String xMensaje;
    private JTextArea txtAMensajes;
    
    public Enviar(String Emisor, String Receptor, String xMensaje, JTextArea txtAMensajes)
    {
        this.Emisor=Emisor.trim();
        this.Receptor=Receptor;
        this.xMensaje=xMensaje;
        this.txtAMensajes=txtAMensajes;
    }
    
    @Override
    public void run()
    {
        try
        {
                Socket socket = new Socket("192.168.0.13",7777);
                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                out.writeUTF(
                        "<|emisor:|>"+Emisor+"</|emisor:|>"+
                        "<|receptor:|>"+Receptor+"</|receptor:|>"+
                        "<|mensaje:|>"+xMensaje);
                socket.close();
                
                txtAMensajes.append("|::::::::::"+Emisor+"::::::::::| dice:\n"+xMensaje+"\n");
                txtAMensajes.setCaretPosition(txtAMensajes.getDocument().getLength());
        }
        catch(Exception ex)
        {
            System.out.println("Error "+ex.getMessage());
        }
    }
}
