package serverConnection;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class client {
    
	public static void main(String[] args) {
        
        String responseLine;
        int i=0;
		Hashtable<String,String> ht=new Hashtable<String,String>();
		ArrayList<String> alist=new ArrayList<String>();
        try {
            
            Socket clntsock = new Socket("localhost", 9776);
            BufferedReader is = new BufferedReader(new InputStreamReader (clntsock.getInputStream()));
            PrintWriter os = new PrintWriter(clntsock.getOutputStream(), true);
            
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                
                while ((responseLine = stdIn.readLine()) != null) {
                    if(responseLine.equals("HELLO") || responseLine.equals("LIST"))
                    {
                        os.println(responseLine);
                    }
                    else if(responseLine.matches("[0-9]+"))
                    {
                        String sindex=null;
                        int option=Integer.parseInt(responseLine);
                        String filename=alist.get(option-1);
                        for(Map.Entry entry: ht.entrySet()){
                            if(entry.getValue().equals(filename)){
                                sindex=(String)entry.getKey();
                                break;
                            }
                        }
                        os.println("GET, "+sindex);
                    }
                    else
                    {
                        os.println(responseLine);
                    }
                    String messagefromserver=is.readLine();
                    if(messagefromserver.contains("LIST"))
                    {
                        String[] sarray=messagefromserver.split(", ");
                        int length=sarray.length-1;
                        if(sarray[0].equals("LIST"))
                        {
                            while(i<length)
                            {
                                ht.put(sarray[i+1],sarray[i+2]);
                                alist.add(sarray[i+2]);
                                i+=2;
                            }
                            int count=1;
                            for(String file:alist)
                            {
                                System.out.println(count+"  "+file);
                                count++;
                            }
                        }
                        else
                            System.out.println("Invalid");
                    }
                    else
                    {
                        if(messagefromserver.contains("GET, "))
                        {
                            String[] sarray=messagefromserver.split(", ");
                            int length=sarray.length-1;
                        System.out.println("message from server: " + sarray[length]);
                        }
                        else
                        {
                           System.out.println("message from server: " + messagefromserver); 
                        }
                    }
                }
            os.close();
            is.close();
            clntsock.close();
        }
        
        catch (Exception e) {
			System.out.println(e);
		}
	}
}
