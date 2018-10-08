/*
 * David Hau
 * CS3700
 * 10/8/2018
 */

public class Matching implements Runnable
{
	
	private Message message;
	int total = 0;
	int redPile = 0;
	int greenPile = 0;
	int bluePile = 0;
	int orangePile = 0;
	
	public Matching (Message message)
	{
		this.message = message;
	}
	
	
	@Override
	public void run()
	{
		total = redPile + greenPile + bluePile + orangePile;
		
		for (String message = this.message.take(); !message.equals("4"); message = this.message.take())
        {
			if(message.equals("Red"))
			{
				redPile = redPile + 1;
				total  = total + 1;
			}
			else if(message.equals("Green"))
			{
				greenPile = greenPile + 1;
				total  = total + 1;
			}
			else if(message.equals("Blue"))
			{
				bluePile = bluePile + 1;
				total  = total + 1;
			}
			else if(message.equals("Orange"))
			{
				orangePile = orangePile + 1;
				total  = total + 1;
			}

            try
            {
            	int colorQueue = 0;
            	if(redPile >= 2)
            	{
            		redPile = redPile - 2;
            		this.message.put("Red");
            	}
            	
            	if(greenPile >= 2)
            	{
            		greenPile = greenPile - 2;
            		this.message.put("Green");
            	}
            	
            	if(bluePile >= 2)
            	{
            		bluePile = bluePile - 2;
            		this.message.put("Blue");
            	}
            	
            	if(orangePile >= 2)
            	{
            		orangePile = orangePile - 2;
            		this.message.put("Orange");
            	}
            	
            	colorQueue = redPile + greenPile + bluePile + orangePile;
            	System.out.println("Matching Thread: Sent " + message + 
            			" Socks to Washer. Total socks " + total + ". Total inside queue " + colorQueue);
            }
            catch (Exception e) {} 
        }
		
		message.put("MATCHING");
	}

}
