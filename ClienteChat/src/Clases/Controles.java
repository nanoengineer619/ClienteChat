/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Nano
 */
public class Controles {
    public void LONGITUD(int Long, JTextField txt, KeyEvent evt)
    {
        if(txt.getText().length()>=Long)
        {
            Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }
    public void MENSAJE(int x, String Texto, String Titulo){
        switch(x){
            case 1:
                JOptionPane.showOptionDialog(null, Texto, Titulo, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"OK"},"OK");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, Texto, Titulo, JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
    public boolean CONFIRMACION(String Texto, String Titulo){
        if(JOptionPane.showOptionDialog(null, Texto, Titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"SI","NO"},"SI")==0){
            return true;}
        else{
            return false;}
        }
}
