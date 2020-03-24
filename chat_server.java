/*
 * Nikhil Kumar
 * 1001644186
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class chat_server extends javax.swing.JFrame 
{// Variables declaration for the GUI of server
  private javax.swing.JButton b_clear;
  private javax.swing.JButton b_end;
  private javax.swing.JButton b_start;
  private javax.swing.JButton b_users;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel lb_name;
  private javax.swing.JTextArea ta_chat;
  ArrayList clientOutputStreams;//Maintaining the list of users
  Map<String, ClientHandler> users;
  ArrayList<ClientHandler> clientsHand;
  String userNameS="";//String handling the number of clients
  ClientHandler ch;
  //Implementing the client connection
   public class ClientHandler implements Runnable	
   {BufferedReader reader;//Buffer reader for reading the data on the server
       Socket sock;
       PrintWriter clientWriter;
       String username;

       public ClientHandler(Socket clientSocket, PrintWriter userWriter, String userName) 
       {
            clientWriter = userWriter;
            username = userName;
            
            try 
            {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            }
            catch (Exception ex) 
            {
                ta_chat.append("Unexpected error... \n");
            }

       }

       @Override
       public void run() 
       {
            String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat" ;
            String[] data,arr;

            try 
            {
                while (true) 
                {
                	message = reader.readLine();
                    ta_chat.append("Received: " + message + "\n");
                    arr=message.split("Content:");
                    data = arr[1].split(":");
                    int a=data.length;
                    
                    for (String token:data) 
                    {
                        ta_chat.append(token + "\n");
                    }

                    if (data[a-1].equals(connect)) 
                    {	System.out.println("In chat with specific server2");
                    	if (message.contains("-"))
                    	{	System.out.println("In chat with specific server3");
                    		String[] msgContents = arr[1].split("-");
                    		String[] un = msgContents[1].split(":");
                    		//System.out.println(un[0]);
                    		System.out.println("In chat with specific server1");
                    		chatWithSpecificClient(un[0], arr[1]);
                    	}
                    	else
                    	{
                    		 tellEveryone((data[0] + ":" + data[1] + ":" + chat));
                             userNameS = data[0];
                             userAdd(data[0],ch);
                    	}
                       
                    } 
                    else if (data[a-1].equals(disconnect)) 
                    {
                        tellEveryone((data[0] + ":has disconnected." + ":" + chat));
                        userRemove(data[0]);
                    } 
                    else if (data[a-1].equals(chat)) 
                    {	System.out.println("In chat with specific server4");
                    	if (message.contains("-"))
                    	{	System.out.println("In chat with specific server5");
                    		String[] msgContents = arr[1].split("-");
                    		String[] un = msgContents[1].split(":");
                    		//System.out.println(un[0]);
                    		chatWithSpecificClient(un[0], arr[1]);
                    	}
                    	else
                    	{
                    		 tellEveryone((data[0] + ":" + data[1] + ":" + chat));
                             userNameS = data[0];
                             
                    	}
                    } 
                    else 
                    {
                        ta_chat.append("No Conditions were met. \n");
                    }
                } 
             } 
             catch (Exception ex) 
             {
                ta_chat.append("Lost a connection. \n");
                ex.printStackTrace();
                clientOutputStreams.remove(clientWriter);
             } 
	} 
    }

    public chat_server() 
    {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        b_start = new javax.swing.JButton();
        b_end = new javax.swing.JButton();
        b_users = new javax.swing.JButton();
        b_clear = new javax.swing.JButton();
        lb_name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server"); // NOI18N
        setResizable(false);

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_start.setText("START");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        b_end.setText("END");
        b_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_endActionPerformed(evt);
            }
        });

        b_users.setText("Online Users");
        b_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usersActionPerformed(evt);
            }
        });

        b_clear.setText("Clear");
        b_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_clearActionPerformed(evt);
            }
        });

        lb_name.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_users, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_name)
                .addGap(209, 209, 209))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_start)
                    .addComponent(b_users))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_clear)
                    .addComponent(b_end))
                .addGap(4, 4, 4)
                .addComponent(lb_name))
        );

        pack();
    }

    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {
        try 
        {
            Thread.sleep(5000);                 //5000 milliseconds is five second.
        } 
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
        
        tellEveryone("Server:is stopping and all users will be disconnected.\n:Chat");
        ta_chat.append("Server stopping... \n");
        
        ta_chat.setText("");
    }
    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {
        Thread starter = new Thread(new ServerStart());
        starter.start();
        
        ta_chat.append("Server started...\n");
    }

    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {
        ta_chat.append("\n Online users : \n");
        for (Map.Entry<String, ClientHandler> entry : users.entrySet())
        {
            ta_chat.append(entry.getKey());
            ta_chat.append("\n");
        }    
        
    }

    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {
        ta_chat.setText("");
    }

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new chat_server().setVisible(true);
            }
        });
    }
    
    public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();
            users = new HashMap<String,ClientHandler>();  

            try 
            {
                ServerSocket serverSock = new ServerSocket(2222);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				clientOutputStreams.add(writer);
				 ch = new ClientHandler(clientSock, writer, userNameS);
				//System.out.println(ch.username);
//				System.out.println(ch.);
//				System.out.println("ch");
				Thread listener = new Thread(ch);
				//clientsHand.add(ch);
				listener.start();
				ta_chat.append("Got a connection. \n");
                }
            }
            catch (Exception ex)
            {
            	ex.printStackTrace();
                ta_chat.append("Error making a connection. \n");
            }
        }
    }
    
    public void userAdd (String data, ClientHandler chn) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        ta_chat.append("Before " + name + " added. \n");
        users.put(name, chn);
        ta_chat.append("After " + name + " added. \n");
        String[] tempList = new String[(users.size())];
      // users.toArray(tempList);
        for (Map.Entry<String, ClientHandler> entry : users.entrySet())
        {
        	message = (entry.getKey() + add);
            tellEveryone(message);
        }
//
//        for (String token:tempList) 
//        {
//            
//        }
        tellEveryone(done);
    }
    
    public void userRemove (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.remove(name);
        String[] tempList = new String[(users.size())];
      //  users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void tellEveryone(String message) 
    {
	Iterator it = clientOutputStreams.iterator();

        while (it.hasNext()) 
        {
            try 
            {
                PrintWriter writer = (PrintWriter) it.next();
		writer.println(message);
		ta_chat.append("Sending: " + message + "\n");
                writer.flush();
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

            } 
            catch (Exception ex) 
            {
		ta_chat.append("Error telling everyone. \n");
            }
        } 
    }
    
    public void chatWithSpecificClient(String clientName, String message)
    {	System.out.println("In chat with specific server 1");
      	System.out.println("Client name is:"+clientName);
        for (Map.Entry<String, ClientHandler> entry : users.entrySet())
        {
        	System.out.println(entry.getKey());
        	System.out.println("In chat with specific server loop");
    		if(entry.getKey().matches(clientName))
    		{	System.out.println("In chat with specific server");
    			//ch.clientWriter.println(message);
    			long millis=System.currentTimeMillis();  
    			java.sql.Date date=new java.sql.Date(millis);   //Getting the current system date
    			int chat_lenght=message.length();
    			PrintWriter writer = (PrintWriter) entry.getValue().clientWriter;
    			writer.println(message);
    			System.out.println("message is:"+message);
    			ta_chat.append("Sending: POST Host localhost  HTTP/1.0 User-Agent HTTP/1.1 Date"+date+" Content-Type html/text Content-Length "+chat_lenght+" Content: " + message + "\n");
                writer.flush();
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

    			
    		}
//    		else
//    		{
//    			ta_chat.append("Client doesn't exist");
//    			ta_chat.append("please enter a valid client and try again");
//    		}
    	}
    }
    
    
}

/*References
Below are the links using which this project is developed
https://www.youtube.com/watch?v=hZgntu7889Q
https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams
https://www.javatpoint.com/java-get-current-date
https://www.jmarshall.com/easy/http/
*/