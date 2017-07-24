import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 12, 2017
 */

public class Test
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		char ch;
	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			while ((ch = (char) in.read()) != -1){
		        System.out.print(ch);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

}
