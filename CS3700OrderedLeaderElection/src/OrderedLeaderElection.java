import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

/*
 * David Hau
 * CS3700
 * 10/8/2018
 */

public class OrderedLeaderElection
{
	private static final Object RANKED_LOCK = new Object();
	private static final Object NOTIFY_LOCK = new Object();
	
	private static AtomicInteger electedThreadCount = new AtomicInteger(-2);
	private static AtomicBoolean wait = new AtomicBoolean(false);
	
	private static int threadCount;
	private static ArrayList<Thread> electedOfficialThreads = new ArrayList<>();
	private static Thread ranked = new Thread(new Ranked());
	
	private static ElectedOfficial currentElectedLeader = new ElectedOfficial("-1", Integer.MIN_VALUE);
	private static ElectedOfficial newElectedLeader = new ElectedOfficial("-1", Integer.MIN_VALUE);

	public static class Ranked implements Runnable
	{
		@Override
		public void run()
		{
			synchronized(RANKED_LOCK)
			{
				while (electedThreadCount.get() < threadCount)
				{
                    try
                    {
                        RANKED_LOCK.wait();
                        if (newElectedLeader.getValue() > currentElectedLeader.getValue())
                        {
                            System.out.println("New Current Leader: " + newElectedLeader.getName() + 
                            		", " + newElectedLeader.getValue());
                            currentElectedLeader = newElectedLeader;
                            while(!wait.get());
                            
                            synchronized(NOTIFY_LOCK)
                            {
                                for(int i = 0; i < electedThreadCount.get(); i++)
                                {
                                	NOTIFY_LOCK.notifyAll();
                                }
                            }
                        }
                        else
                        {
                            synchronized(NOTIFY_LOCK)
                            {
                                NOTIFY_LOCK.notify();
                            }
                        }
                    } catch (InterruptedException e) { System.out.println("Event in Ranked Run");}
                 }
			}
			
			for(int i = 0; i < electedThreadCount.get(); i++)
			{
				electedOfficialThreads.get(i).interrupt();
			}
			
		}
	}
	
	public static class ElectedOfficial implements Runnable
	{
		private String name;
		private ElectedOfficial electedLeader;
		private int value;
		
		public ElectedOfficial(String name, int value)
		{
			this.name = name;
			this.value = value;
			this.electedLeader = this;
			
			synchronized(RANKED_LOCK)
			{
				electedThreadCount.incrementAndGet();
				newElectedLeader = electedLeader;
				RANKED_LOCK.notify();
			}
			
		}
		
		public String getName() { return name; }
		public int getValue() { return value; }
		
		@Override
		public void run()
		{
			synchronized(NOTIFY_LOCK)
			{
				while(electedThreadCount.get() < threadCount)
				{
					try
					{
						wait.set(true);
						NOTIFY_LOCK.wait(1000);
						System.out.println("Name: " + getName() + ", Value: " + getValue() + " Leader: " + electedLeader.getName());
						electedLeader = currentElectedLeader;
					}
					catch (InterruptedException e)
					{
						electedLeader = currentElectedLeader;
						System.out.println("Name: " + getName() + ", Value: " + getValue() + " Leader: " + electedLeader.getName());
					} catch (Exception e) {}
				}
			}
			
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter number of elected official threads to run: ");
		threadCount = keyboard.nextInt();
		
		ranked.start();
		
		for (int i = 0; i < threadCount; i++)
		{
			wait.set(false);
            electedOfficialThreads.add(new Thread(new ElectedOfficial(Integer.toString(i), 
            		ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE))));
            electedOfficialThreads.get(i).start();
        }
		
		try
		{
			ranked.join();
			for(int i = 0; i < threadCount; i++)
			{
				electedOfficialThreads.get(i).join();
			}
		} catch(Exception e) { System.out.println("Event in Elected Official Join"); }
		System.out.println("The Leader is: " + currentElectedLeader.getName() + 
				", Value: " + currentElectedLeader.getValue());
	}
	
}
