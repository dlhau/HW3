/*
 * David Hau
 * CS3700
 * 10/8/2018
 */

public class SockMatching
{
	public static void main(String[] args)
	{
		Message message = new Message();
		
		Thread redSock = new Thread(new Sock("Red", message));
		Thread greenSock = new Thread(new Sock("Green", message));
		Thread blueSock = new Thread(new Sock("Blue", message));
		Thread orangeSock = new Thread(new Sock("Orange", message));
		Thread sockMatching = new Thread(new Matching(message));
		Thread washer = new Thread(new Washer(message));
		
		redSock.start();
		greenSock.start();
		blueSock.start();
		orangeSock.start();
		sockMatching.start();
		washer.start();
	}
	
}