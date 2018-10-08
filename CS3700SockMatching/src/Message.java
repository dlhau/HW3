/*
 * David Hau
 * CS3700
 * 10/8/2018
 */

public class Message
{
	String message;
	
	private boolean empty = true;
	
	public synchronized String take()
    { 
        while(empty)
        { 
            try
            { 
                wait(); 
            }
            catch (InterruptedException e) {} 
        } 
        
        empty = true; 

        notifyAll(); 
        return message; 
    } 
	
	public synchronized void put(String message)
    { 
        while(!empty)
        { 
            try
            {  
                wait(); 
            }
            catch (InterruptedException e) {} 
        } 

        empty = false; 

        this.message = message; 

        notifyAll(); 
    } 
}
