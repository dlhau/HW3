import java.util.Random;

/*
 * David Hau
 * CS3700
 * 10/8/2018
 */

public class Sock implements Runnable
{
	static int finished = 0;
	private Message message;
	String color = "BLANK";
	int amount = 0;
	
	public Sock (String color, Message message)
	{
		this.color = color;
		this.message = message;
	}

	@Override
	public void run()
	{
		Random random = new Random();
		amount = random.nextInt(100) + 1;
		for(int i = 1; i <= amount; i ++)
		{
			try
			{
				message.put(color);
				System.out.println(color + " Sock: Produced " + i + 
						" " + "of "+ amount + " " + color + " Socks");
				
			} catch (Exception e) {}
		}
		
		finished = finished + 1;
		message.put(Integer.toString(finished));
	}
	
	
}
