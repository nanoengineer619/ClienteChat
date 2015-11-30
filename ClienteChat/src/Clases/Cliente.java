/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;



import Componentes.KPanel;
import Formularios.Compartir;
import Formularios.CopiarCompartido;
import Formularios.VentanaChat;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIDefaults;

/**
 *
 * @author Nano
 */
public class Cliente extends Thread
{
    JInternalFrame internal;
    int[] valorBorrar=new int[500];
    boolean existeContacto;
    String btnCrearVentanaRec="";
    int posY=0;
    private String mensajeTotal;
    private String auxCLinea;
    private String[] CLinea;
    private String Usuario;
    private String mensaje[];
    private JTabbedPane TabedPane;
    private String Emisor="";
    private Font LetraM = new Font("Dialog", Font.ITALIC, 16);
    final private Usuario miUsuario=new Usuario();
    ClaseVentanaChat VC=new ClaseVentanaChat();
    Controles control=new Controles();
    Container grupoListaContactos;
    String auxMensajeWrite="";
    DataInputStream in;
    DataOutputStream out;
    ArrayList<CArchivoCompartido> arrayListArchivos;
    
    public Cliente(String Usuario, JTabbedPane TabedPane, Container C, ArrayList<CArchivoCompartido> arrayListArchivos, JInternalFrame internal)
    {
        this.Usuario=Usuario;
        this.TabedPane=TabedPane;
        this.grupoListaContactos=C;
        this.arrayListArchivos=arrayListArchivos;
        this.internal=internal;
    }
    private void sonido()
    {
        String data = "09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azaZ";
        data += "09azaZ09azaZ09azaZ09azaZ09azAZ";
        data += "09aZAz09aZAz09aZAz09aZAz09aZAz";
        data += "09aZAz09aZAz09aZAz09aZAz09aZAz";
        String[] cmd = {"sh", "-c", "echo " + data + " | aplay -r 4"};
        try
        {
            Runtime.getRuntime().exec(cmd);
        }
        catch(Exception ex)
        {
            try
            {
                Toolkit.getDefaultToolkit().beep();
            }
            catch(Exception exx){}
        }
    }
    
    private void LanzarConversacion(String NombreContacto)
    {
        boolean valor=false;
        VentanaChat Vc=VC.getVentanaChat();
        Component componenteTabscala1=Vc.getComponent(0);
        Component componenteTabscala2=((javax.swing.JRootPane) componenteTabscala1).getComponent(1);
        Component componenteTabscala3=((javax.swing.JLayeredPane) componenteTabscala2).getComponent(0);
        Component componenteTabscala4=((javax.swing.JPanel) componenteTabscala3).getComponent(0);
        Component[] Componentes=((javax.swing.JTabbedPane) componenteTabscala4).getComponents();
        for(int j=0;j<Componentes.length;j++)
        {
            if(Componentes[j].getClass().getName().equals("Componentes.KPanel"))
            {
                if(((javax.swing.JPanel) Componentes[j]).getName().toUpperCase().equals(NombreContacto.toUpperCase()))
                {
                    valor=true;
                    break;
                }
            }
        }
        if(!valor)
        {
            Font LetraMM = new Font("Dialog", Font.ITALIC, 16);
            String EmisorX=NombreContacto;
            final KPanel Panel=new KPanel();
            Panel.setName(EmisorX);
            Component ccomp1=Panel.getComponent(0);
            Component ccomp2=((javax.swing.JScrollPane) ccomp1).getComponent(0);
            final Component ccomp3=((javax.swing.JViewport) ccomp2).getComponent(0);
            ((javax.swing.JTextArea) ccomp3).setName("txtA"+EmisorX);
            ((javax.swing.JTextArea) ccomp3).setFont(LetraMM);
            ((javax.swing.JTextArea) ccomp3).setEditable(false);
            ((javax.swing.JTextArea) ccomp3).setLineWrap(true);
            ((javax.swing.JTextArea) ccomp3).setWrapStyleWord(true);

            final Component cccomp1=Panel.getComponent(1);
            ((javax.swing.JTextField) cccomp1).addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    control.LONGITUD(1000, ((javax.swing.JTextField) cccomp1), e);
                    if(((javax.swing.JTextField) cccomp1).getText().length()>1000)
                    {
                        ((javax.swing.JTextField) cccomp1).setText(((javax.swing.JTextField) cccomp1).getText().substring(0, 1000));
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    String msj=((javax.swing.JTextField) cccomp1).getText();
                    if(!msj.trim().equals(""))
                    {
                        if (e.getKeyCode()==KeyEvent.VK_ENTER)
                        {
                            Enviar env=new Enviar(miUsuario.getUsuario(), Panel.getName(), msj, ((javax.swing.JTextArea) ccomp3));
                            env.start();
                            ((javax.swing.JTextField) cccomp1).setText("");
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    
                }
            });
            ((javax.swing.JTabbedPane) componenteTabscala4).addTab(EmisorX+"   ", null, Panel, null);
            Component[] taComponentes=((javax.swing.JTabbedPane) componenteTabscala4).getComponents();
            for(int j=0;j<taComponentes.length;j++)
            {
                if(((javax.swing.JPanel)taComponentes[j]).getName().toUpperCase().trim().equals(EmisorX.toUpperCase()))
                {
                    ((javax.swing.JTabbedPane) componenteTabscala4).setSelectedIndex(j);
                    break;
                }
            }
            
            final Component compBtnCompartir=Panel.getComponent(3);
            ((javax.swing.JButton) compBtnCompartir).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Compartir frmCompartir=new Compartir(arrayListArchivos);
                    frmCompartir.setTitle(Panel.getName());
                    internal.getDesktopPane().add(frmCompartir);
                    frmCompartir.setVisible(true);
                }
            });
            final Component compBtnRecibir=Panel.getComponent(2);
            ((javax.swing.JButton) compBtnRecibir).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    CopiarCompartido frmCopiar=new CopiarCompartido(Panel.getName());
                    frmCopiar.setTitle(Panel.getName());
                    internal.getDesktopPane().add(frmCopiar);
                    frmCopiar.setVisible(true);
                }
            });
        }
    }
    
    @Override
    public void run()
    {
        Socket socket = null;
        for(;;)
        {
            try
            {
                socket = new Socket("192.168.0.13",7777); // 192.168.1.7
                out=new DataOutputStream(socket.getOutputStream());
                out.writeUTF(
                        "<|emisor:|>"+Usuario+"</|emisor:|>"+
                        "<|receptor:|></|receptor:|>"+
                        "<|mensaje:|>");
                
                in=new DataInputStream(socket.getInputStream());
                mensajeTotal=in.readUTF();
                socket.close();
                auxCLinea=mensajeTotal.substring(mensajeTotal.indexOf(
                            "<|CenLinea|>")+12, mensajeTotal.indexOf("</|CenLinea|>"));
                CLinea=auxCLinea.split(";");
                for(String va:CLinea)
                {
                    existeContacto=false;
                    for(int cantBoton=0;cantBoton<grupoListaContactos.getComponentCount();cantBoton++)
                    {
                        Component btnscala0=(JButton)grupoListaContactos.getComponent(cantBoton);
                        if(btnscala0.getName().toUpperCase().equals(va.toUpperCase()))
                        {
                            existeContacto=true;
                            break;
                        }
                    }
                    
                    if(!existeContacto  && !va.equals(miUsuario.getUsuario()) && !va.equals("KAccesoServidorK"))
                    {
                        JButton btnContacto=new JButton();
                        btnContacto.setText(va);
                        btnContacto.setName(va);
                        btnContacto.setBounds(0, posY, 188, 40);
                        posY+=36;
                        grupoListaContactos.setPreferredSize(new Dimension(185,posY+7));
                        btnCrearVentanaRec=va;
                        
                        btnContacto.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    if(evt.getClickCount()==2)
                                    {
                                        LanzarConversacion(((JButton) evt.getSource()).getName());
                                        try
                                        {
                                            internal.setSelected(true);
                                        }
                                        catch(Exception ex){}
                                        ((JButton) evt.getSource()).setBackground(Color.orange);
                                        for(int SelectTab=0;SelectTab<TabedPane.getComponentCount();SelectTab++)
                                        {
                                            if(TabedPane.getComponent(SelectTab).getName().toUpperCase().equals(((JButton) evt.getSource()).getName().toUpperCase()))
                                            {
                                                TabedPane.setSelectedIndex(SelectTab);
                                                if(TabedPane.getComponentCount()>0)
                                                {
                                                    ((javax.swing.JTextField) ((javax.swing.JPanel) TabedPane.getComponent(SelectTab)).getComponent(1)).grabFocus();
                                                }
                                            }
                                        }
                                    }
                                }
                        });
                        UIDefaults enLineaTheme = new UIDefaults();
                        enLineaTheme.put("Button.background", Color.orange);
                        btnContacto.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
                        btnContacto.putClientProperty("Nimbus.Overrides", enLineaTheme);
                        
                        grupoListaContactos.add(btnContacto);
                        btnContacto.setVisible(false);
                        btnContacto.setVisible(true);
                    }
                }
                int auxcontainBorrar=grupoListaContactos.getComponentCount();
                for(int cantBoton=0;cantBoton<auxcontainBorrar;cantBoton++)
                {
                    valorBorrar[cantBoton]=1;
                    Component borrarbtnscala0=(JButton)(grupoListaContactos.getComponent(cantBoton));
                    for(String auxValor:CLinea)
                    {
                        if(auxValor.toUpperCase().equals(borrarbtnscala0.getName().toUpperCase()))
                        {
                            valorBorrar[cantBoton]=0;
                            break;
                        }
                    }
                }
                int restPos=0;
                for(int cantBoton=0;cantBoton<auxcontainBorrar;cantBoton++)
                {
                    Component borrarbtnscala0=(JButton)(grupoListaContactos.getComponent(cantBoton-restPos));
                    if(valorBorrar[cantBoton]==1)
                    {
                        borrarbtnscala0.setVisible(false);
                        grupoListaContactos.remove(cantBoton-restPos);
                        restPos++;
                        posY=posY-36;
                        grupoListaContactos.setPreferredSize(new Dimension(185,posY+7));
                        for(int xcantBoton=0;xcantBoton<grupoListaContactos.getComponentCount();xcantBoton++)
                        {
                            Component xOrdenarbtn=(JButton)(grupoListaContactos.getComponent(xcantBoton));
                            if(xcantBoton>=cantBoton)
                            {
                                xOrdenarbtn.move(0, xOrdenarbtn.getY()-36);
                            }
                        }
                    }
                }
                
                mensajeTotal=mensajeTotal.substring(mensajeTotal.indexOf(
                            "</|CenLinea|>")+13, mensajeTotal.length());
                
                if(mensajeTotal.length()>0)
                {
                    mensaje=mensajeTotal.split("</sepatadorArray>");

                    int i=0;
                    for(String inMensaje:mensaje)
                    {
                        if((i % 2)==0)
                        {
                            boolean valor=false;
                            String auxEmisor=inMensaje.substring(inMensaje.indexOf("|::::::::::")+11, inMensaje.indexOf("::::::::::|"));
                                Emisor=auxEmisor;
                                Component[] Componentes=TabedPane.getComponents();
                                for(int j=0;j<Componentes.length;j++)
                                {
                                    if(Componentes[j].getClass().getName().equals("Componentes.KPanel"))
                                    {
                                        if(((javax.swing.JPanel) Componentes[j]).getName().toUpperCase().equals(Emisor.toUpperCase()))
                                        {
                                            valor=true;
                                            break;
                                        }
                                    }
                                }
                                if(!valor)
                                {
                                    final KPanel Panel=new KPanel();
                                    Panel.setName(Emisor);
                                    Component ccomp1=Panel.getComponent(0);
                                    Component ccomp2=((javax.swing.JScrollPane) ccomp1).getComponent(0);
                                    final Component ccomp3=((javax.swing.JViewport) ccomp2).getComponent(0);
                                    ((javax.swing.JTextArea) ccomp3).setName("txtA"+Emisor);
                                    ((javax.swing.JTextArea) ccomp3).setFont(LetraM);
                                    ((javax.swing.JTextArea) ccomp3).setEditable(false);
                                    ((javax.swing.JTextArea) ccomp3).setLineWrap(true);
                                    ((javax.swing.JTextArea) ccomp3).setWrapStyleWord(true);

                                    final Component cccomp1=Panel.getComponent(1);
                                    ((javax.swing.JTextField) cccomp1).addKeyListener(new KeyListener() {

                                        @Override
                                        public void keyTyped(KeyEvent e) {
                                            control.LONGITUD(1000, ((javax.swing.JTextField) cccomp1), e);
                                            if(((javax.swing.JTextField) cccomp1).getText().length()>1000)
                                            {
                                                ((javax.swing.JTextField) cccomp1).setText(((javax.swing.JTextField) cccomp1).getText().substring(0, 1000));
                                            }
                                        }

                                        @Override
                                        public void keyPressed(KeyEvent e) {
                                            String msj=((javax.swing.JTextField) cccomp1).getText();
                                            if(!msj.trim().equals(""))
                                            {
                                                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                                                {
                                                    Enviar env=new Enviar(miUsuario.getUsuario(), Panel.getName(), msj, ((javax.swing.JTextArea) ccomp3));
                                                    env.start();
                                                    ((javax.swing.JTextField) cccomp1).setText("");
                                                }
                                            }
                                        }

                                        @Override
                                        public void keyReleased(KeyEvent e) {

                                        }
                                    });
                                    
                                    final Component compBtnCompartir=Panel.getComponent(3);
                                    ((javax.swing.JButton) compBtnCompartir).addActionListener(new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            Compartir frmCompartir=new Compartir(arrayListArchivos);
                                            frmCompartir.setTitle(Panel.getName());
                                            internal.getDesktopPane().add(frmCompartir);
                                            frmCompartir.setVisible(true);
                                        }
                                    });
                                    final Component compBtnRecibir=Panel.getComponent(2);
                                    ((javax.swing.JButton) compBtnRecibir).addActionListener(new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            CopiarCompartido frmCopiar=new CopiarCompartido(Panel.getName());
                                            frmCopiar.setTitle(Panel.getName());
                                            internal.getDesktopPane().add(frmCopiar);
                                            frmCopiar.setVisible(true);
                                        }
                                    });                                    
                                    
                                    TabedPane.addTab(Emisor+"   ", null, Panel, null);
                                }
                        }
                        Component[] taComponentes=TabedPane.getComponents();
                        if((i % 2)==0)
                        {
                            auxMensajeWrite="";
                            String auxKMensaje;
                            if(i!=0)
                            {
                                auxKMensaje=inMensaje.substring(inMensaje.indexOf("</|CenLinea|>")+13,inMensaje.length()) +"\n";
                            }
                            else
                            {
                                auxKMensaje=inMensaje.substring(0,inMensaje.length()) +"\n";
                            }
                            auxMensajeWrite+=auxKMensaje;
                            i++;
                            continue;
                        }
                        else
                        {
                            auxMensajeWrite+=inMensaje+"\n";
                        }
                        for(int j=0;j<taComponentes.length;j++)
                        {
                            if(taComponentes[j].getClass().getName().equals("Componentes.KPanel"))
                            {
                                Component comp1=((javax.swing.JPanel) taComponentes[j]).getComponent(0);
                                Component comp2=((javax.swing.JScrollPane) comp1).getComponent(0);
                                Component comp3=((javax.swing.JViewport) comp2).getComponent(0);
                                if(((javax.swing.JTextArea) comp3).getName().toUpperCase().equals(("txtA"+Emisor).toUpperCase()))
                                {
                                    ((javax.swing.JTextArea) comp3).append(auxMensajeWrite);
                                    ((javax.swing.JTextArea) comp3).setCaretPosition(((javax.swing.JTextArea) comp3).getDocument().getLength());
                                    for(int btnPintar=0;btnPintar<grupoListaContactos.getComponentCount();btnPintar++)
                                    {
                                        if(grupoListaContactos.getComponent(btnPintar).getName().toUpperCase().equals(Emisor.toUpperCase()) && !TabedPane.getComponent(TabedPane.getSelectedIndex()).getName().toUpperCase().equals(Emisor.toUpperCase()))
                                        {
                                            grupoListaContactos.getComponent(btnPintar).setBackground(Color.red);
                                        }
                                    }
                                    sonido();
                                    break;
                                }
                            }
                        }
                        i++;
                    }
                }
            }
            catch(Exception ex)
            {
                try 
                {
                    socket.close();
                } 
                catch (IOException ex1) 
                {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            try 
            {
                Thread.sleep(20);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}