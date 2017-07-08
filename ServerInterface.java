package application;
/**
*
* @author Vishal
*/
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
* interface to implement the remote method invocation
* 
*/

/**
* version 1.0
* 
*/
public interface ServerInterface  extends Remote {
    
    void setClient(String s,String Name) throws RemoteException;
    Boolean canBePlaced(String loc,String ori,String player) throws RemoteException;
    void Place(String loc,String ori,String player) throws RemoteException;
    int getTotalShips() throws RemoteException;
     void setPlay() throws RemoteException;
     Boolean isHit1(String s)throws RemoteException;
    void PlayWithRMI1() throws RemoteException;

}


