package me.ichibot.hardware;

import com.fazecast.jSerialComm.SerialPort;

public class ConexionSerial {
    private SerialPort puerto;

    public boolean conectar(String nombre) {
        puerto = SerialPort.getCommPort(nombre);
        puerto.setComPortParameters(9600, 8, 1, 0);
        if (puerto.openPort()) {
            try { Thread.sleep(2000); } catch (Exception e) {}
            return true;
        }
        return false;
    }

    public void enviarComando(String c) {
        if (puerto != null && puerto.isOpen()) {
            puerto.writeBytes(c.getBytes(), c.length());
        }
    }

    public void cerrar() { if (puerto != null) puerto.closePort(); }
}