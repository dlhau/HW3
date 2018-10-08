/*
 * David Hau
 * CS3700
 * 10/8/2018
 */

public class Washer implements Runnable 
{
	
	private Message message;
	
	public Washer (Message message)
	{
		this.message = message;
	}
	
	@Override
	public void run()
	{
		for (String message = this.message.take(); !message.equals("MATCHING"); message = this.message.take())
        {	
            try
            {
            	System.out.println("Destroyed " + message + " socks");
            }
            catch (Exception e) {} 
        }
		
	}
}