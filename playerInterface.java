package application;
import java.rmi.*;
/**
*
* @author Vishal
*/

/**
* interface to implement the remote method invocation
* 
*/

/**
* version 1.0
* 
*/
public interface playerInterface extends Remote {
    String getData() throws RemoteException;
    void displayText(String s) throws RemoteException;
    void Disable() throws RemoteException;
    void Enable() throws RemoteException;

}
