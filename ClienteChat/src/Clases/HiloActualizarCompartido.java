/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nano
 */
public class HiloActualizarCompartido extends Thread{
    MiUsuario miUsuario=new MiUsuario();
    String receptor;
    List<String> ipContacto;
    DefaultListModel modeloListaArchivos;
    JList ListaArchivos;
    
    public HiloActualizarCompartido(String receptor, List<String> ipContacto, DefaultListModel modeloListaArchivos, JList ListaArchivos)
    {
        this.receptor=receptor;
        this.ipContacto=ipContacto;
        this.modeloListaArchivos=modeloListaArchivos;
        this.ListaArchivos=ListaArchivos;
        start();
    }
    @Override
    public void run()
    {
        try
        {
            Socket skClientex = new Socket("192.168.0.13", 7777);
            DataOutputStream outx = new DataOutputStream(skClientex.getOutputStream());
            outx.writeUTF(
                        "<|emisor:|>"+miUsuario.getUsuario()+"</|emisor:|>"+
                        "<|receptor:|>"+receptor+"</|receptor:|>"+
                        "<|mensaje:|></VerArchivosCompartidos>");
            DataInputStream inx = new DataInputStream(skClientex.getInputStream());
            ipContacto.add(inx.readUTF().toString());
            skClientex.close();
            
            if(ipContacto.get(0).equals("Contacto Desconectado"))
            {
                JOptionPane.showMessageDialog(null, "Contacto Desconectado");
                modeloListaArchivos=new DefaultListModel();
                ListaArchivos.setModel(modeloListaArchivos);
                return;
            }
            Socket skCliente = new Socket(ipContacto.get(0), 4444);
            DataOutputStream out = new DataOutputStream(skCliente.getOutputStream());
            String miUsuarioAux=miUsuario.getUsuario();
            out.writeUTF("</VerArchivos>"+miUsuarioAux);
            DataInputStream in = new DataInputStream(skCliente.getInputStream());
            String[] datos = in.readUTF().toString().split("</Separator>");
            modeloListaArchivos = new DefaultListModel();
            for(String valor : datos) {
                modeloListaArchivos.addElement(valor.substring(0, valor.indexOf("::")));
            }
            ListaArchivos.setModel(modeloListaArchivos);
            out.close();
            in.close();
            skCliente.close();
        }
        catch(Exception ex)
        {
            if(ex.getMessage().equals("String index out of range: -1"))
            {
                JOptionPane.showMessageDialog(null, "No hay Archivos Compartidos");
                modeloListaArchivos=new DefaultListModel();
                ListaArchivos.setModel(modeloListaArchivos);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "HiloActualizaCompartido: " + ex.getMessage());
            }
        }
    }
}
