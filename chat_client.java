/*
 * Nikhil Kumar
 * 1001644186
 */
import java.net.*;
import java.io.*;
import java.util.*;

//import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
//import com.sun.xml.internal.ws.resources.SenderMessages;
public class chat_client extends javax.swing.JFrame 
{ 
	
	String username, address = "localhost";//initialized  client as localhost but can be replaced with host IP e.g.127.0.0.1
	ArrayList<String> users = new ArrayList();//Mainitaining the list of users
	int port = 2222;//port number on which client will connect to server,initalized as 2222
	Boolean isConnected = false;//Initiazlizing the connection as false for termination of old ones    
	Socket sock;//creates client socket with given host
	private static BufferedReader reader;//reads data from socket sent by the client to the server
	private static PrintWriter writer;//Prints formatted representations of objects to a text-output stream

	//Integer counter;
	static Random rand = new Random(); //object of random
	// Obtain a number between [0 - 50].
	private static Integer counter = rand.nextInt(50); // Counter for client clock

	static Random rand2 = new Random();
	// Obtain a number between [0 - 50].
	private static Integer counterForMsg = rand2.nextInt(50); // Counter for messages

	static Random r = new Random();
	private static Integer timeTosend= (r.nextInt((10 - 2) + 1) + 2)*1000; // counter for generating random time to send randomly between  2 - 10 seconds
	
	static Random rand3 = new Random();
	// Obtain a number between [0 - 2].
	private static Integer indexOfClient; // randomly generate index of client to select a client randomly to send current clock

	//Wrapping the object thread
    public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
    }
    
	//Method used for adding new users to the server
    public void userAdd(String data) 
    {
         users.add(data);
         //System.out.println("size of array:"+users.size());
    }
    
  //Method used when the client sign out from the server
    public void userRemove(String data) 
    {
         ta_chat.append(data + " is now offline.\n");
    }
   
  //Method used to add the new users to the server 
    public void writeUsers() 
    {
         String[] tempList = new String[(users.size())];
         users.toArray(tempList);
         for (String token:tempList) 
         {
             //users.append(token + "\n");
         }
    }
    
	//Method used for disconnecting the user
    public void sendDisconnect() 
    {
        String bye = (username + ": :Disconnect");
        try
        {
            writer.println(bye); 
            writer.flush(); 
        } catch (Exception e) 
        {
            ta_chat.append("Could not send Disconnect message.\n");
        }
    }

  //Disconnected User
    public void Disconnect() 
    {
        try 
        {
            ta_chat.append("Disconnected.\n");
            sock.close();
        } catch(Exception ex) {
            ta_chat.append("Failed to disconnect. \n");
        }
        isConnected = false;
        tf_username.setEditable(true);

    }
    
  //Method called for initialization of components
    public chat_client() 
    {
//    	  lc = new LamportClock();
//    	  counter= lc.getCounter(); 
    	   initComponents();
//    	   System.out.println(counter);
    	   
    	// Incrementing clock counter every second 
       	Timer timer = new Timer(); 
       	timer.scheduleAtFixedRate(new TimerTask(){ 
       	public void run() { 
       	counter++; 
       	ta_chat.append("clock:"+counter.toString()+"\n"); 
       	} 
       	}, new Date(), 1000); // every second
    	   ta_chat.append("CLock Counter of  given client:"+counter);     
    }
  
    
    public class IncomingReader implements Runnable
    {
        @Override
        public void run() 
        {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

            try 
            {
                while ((stream = reader.readLine()) != null) 
                {
                    if(users.size() > 1)
                    {
                   	 sendMSgAfterTwoToTenSeconds(); //sends clock time to others randomly
                    }
                
                	System.out.println(stream);
                     data = stream.split(":"); //Spliting the string for checking the type of connection
     				//Condition used for sending the message to all
                 
                     
                     if (data[2].equals(chat)) 
                     {
                    	   	String[] arr1 = stream.split("-");
                        	String[] arr2 = arr1[0].split(":");
                        	String[] arr3 = data[1].split("-");
                        	System.out.println("sender:"+data[0]);
                    
                        if(stream.contains("-"))
                        {
                            ta_chat.append(data[0] + ": " + data[1] + "\n");
                            ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                        	Integer clockOfOtherClient = Integer.parseInt(arr2[1]);
                        	System.out.println("clockOfOtherClient:"+clockOfOtherClient);
                        	if(clockOfOtherClient > counter )
                        	{
                        		counter= clockOfOtherClient+1; // updates clock
                        	}
                        }
                     } 
                 	//Condition used for checking the connection of the client and server
                     else if (data[2].equals(connect))
                     {
                        ta_chat.removeAll();
                        System.out.println("someone connected:"+data[0]);
                        userAdd(data[0]);               
                     } 
                     
                 	//Condition used for checking the disconnection of the client and server
                     else if (data[2].equals(disconnect)) 
                     {
                         userRemove(data[0]);
                     } 
                     
                     else if (data[2].equals(done)) 
                     {
                        //users.setText("");
                        writeUsers();
                       // users.clear();
                     }
                 	//Send logical clock time to a client randomly
//                  	Thread.sleep(30000);
//                  	writer.println("c:chat:b");
                  	
                     
                }
           }catch(Exception ex) { }
        }


    }

    // Method that sends clock to other clients randomly between 2-10 seconds
	public void sendMSgAfterTwoToTenSeconds() {
		// TODO Auto-generated method stub
		Timer timer2 = new Timer(); 
      	int arraySize = users.size();
      	
      	System.out.println("Array size of users"+arraySize);
      	timer2.scheduleAtFixedRate(new TimerTask(){ 
      	public void run() { 
      		
      		indexOfClient = rand3.nextInt(arraySize);
      		if(arraySize >= indexOfClient)
      		{
      		counterForMsg++; 
      	System.out.println(counter); 
      	System.out.println("time to send"+timeTosend);
      	String clientname =  users.get(indexOfClient);
//      	c:hi-b:Chat
      	String message = username +":"+counter.toString()+"-"+clientname+":Chat";
    	long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);   //Getting the current system date
		int chat_lenght=ta_chat.getText().length(); //Getting the length of the string
    
      	if(!username.equals(clientname))
      	{
      		//System.out.println(message);
//      	   writer.println("POST Host localhost  HTTP/1.0 User-Agent HTTP/1.1 Date "+date+
//        		   " Content-Type html/text Content-Length "+chat_lenght+" Content: "+username+
//        		   ":"+ta_chat .getText()+":"+"Chat");
      		writer.println("POST Host localhost  HTTP/1.0 User-Agent HTTP/1.1 Date "+date+" Content-Type html/text Content-Length "+chat_lenght+" Content: "+message);
      	//	writer.println(message);
            writer.flush();
      	}
      
      		}	
      	} 
      	}, new Date(), timeTosend); 
	}
   
    
    @SuppressWarnings("unchecked")
   
    private void initComponents() {

        lb_address = new javax.swing.JLabel();
        tf_address = new javax.swing.JTextField();
        lb_port = new javax.swing.JLabel();
        tf_port = new javax.swing.JTextField();
        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        b_connect = new javax.swing.JButton();
        b_disconnect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        tf_chat = new javax.swing.JTextField();
        b_send = new javax.swing.JButton();
        lb_name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's frame");
        setName("client"); 
        setResizable(false);

        lb_address.setText("Address : ");

        tf_address.setText("localhost");
        tf_address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_addressActionPerformed(evt);
            }
        });

        lb_port.setText("Port :");

        tf_port.setText("2222");
        tf_port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_portActionPerformed(evt);
            }
        });

        lb_username.setText("Username :");

        tf_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_usernameActionPerformed(evt);
            }
        });

       // lb_password.setText("Password : ");

        b_connect.setText("Connect");
        b_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_connectActionPerformed(evt);
            }
        });

        b_disconnect.setText("Disconnect");
        b_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_disconnectActionPerformed(evt);
            }
        });

//        b_anonymous.setText("Anonymous Login");
//        b_anonymous.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                b_anonymousActionPerformed(evt);
//            }
//        });

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_send.setText("SEND");
        b_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendActionPerformed(evt);
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tf_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lb_username, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(lb_address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_address, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(tf_username))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            
                            .addComponent(lb_port, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            
                            .addComponent(tf_port, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b_connect)
                                .addGap(2, 2, 2)
                                .addComponent(b_disconnect)
                                .addGap(0, 0, Short.MAX_VALUE))
                            )))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_name)
                .addGap(201, 201, 201))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_address)
                    .addComponent(tf_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_port)
                    .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    )
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_username)
                    
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_username)
                       
                        .addComponent(b_connect)
                        .addComponent(b_disconnect)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_chat)
                    .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_name))
        );

        pack();
    }

    private void tf_addressActionPerformed(java.awt.event.ActionEvent evt) {
       
    }

    private void tf_portActionPerformed(java.awt.event.ActionEvent evt) {
   
    }

    private void tf_usernameActionPerformed(java.awt.event.ActionEvent evt) {
    
    }

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {
        if (isConnected == false) 
        {
            username = tf_username.getText();
            tf_username.setEditable(false);

            try 
            {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());
//                writer.println("POST Host localhost  HTTP/1.0 User-Agent HTTP/1.1 Date "+date+
//             		   " Content-Type html/text Content-Length "+chat_lenght+" Content: "+username+
//             		   ":has connected.:Connect");
                writer.println("Content:"+username + ":has connected.:Connect");
                writer.flush(); 
                isConnected = true; 
            } 
            catch (Exception ex) 
            {
                ta_chat.append("Cannot Connect! Try Again. \n");
                tf_username.setEditable(true);
            }
            
            ListenThread();
            
        } else if (isConnected == true) 
        {
            ta_chat.append("You are already connected. \n");
        }
    }

    private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt) {
        sendDisconnect();
        Disconnect();
    }

    private void b_anonymousActionPerformed(java.awt.event.ActionEvent evt) {
        tf_username.setText("");
        if (isConnected == false) 
        {
            String anon="anon";
            Random generator = new Random(); 
            int i = generator.nextInt(999) + 1;
            String is=String.valueOf(i);
            anon=anon.concat(is);
            username=anon;
            
            tf_username.setText(anon);
            tf_username.setEditable(false);

            try 
            {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(anon + ":has connected.:Connect");
                writer.flush(); 
                isConnected = true; 
            } 
            catch (Exception ex) 
            {
                ta_chat.append("Cannot Connect! Try Again. \n");
                tf_username.setEditable(true);
            }
            
            ListenThread();
            
        } else if (isConnected == true) 
        {
            ta_chat.append("You are already connected. \n");
        }        
    }

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {
        String nothing = "";
        if ((tf_chat.getText()).equals(nothing)) {
            tf_chat.setText("");
            tf_chat.requestFocus();
        } else {
            try {
            	long millis=System.currentTimeMillis();  
				java.sql.Date date=new java.sql.Date(millis);   //Getting the current system date
				int chat_lenght=ta_chat.getText().length(); //Getting the length of the string
               writer.println("POST Host localhost  HTTP/1.0 User-Agent HTTP/1.1 Date "+date+
            		   " Content-Type html/text Content-Length "+chat_lenght+" Content: "+username+
            		   ":"+ta_chat .getText()+":"+"Chat");
               writer.flush(); // flushes the buffer
            } catch (Exception ex) {
                ta_chat.append("Message was not sent. \n");
            }
            tf_chat.setText("");
            tf_chat.requestFocus();
        }

        tf_chat.setText("");
        tf_chat.requestFocus();
    }

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {       	             	    
                new chat_client().setVisible(true);
            }
        });
        
    	
    }
    
 // Variables declaration for the GUI of client
    private javax.swing.JButton b_connect;
    private javax.swing.JButton b_disconnect;
    private javax.swing.JButton b_send;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_address;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_port;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_address;
    private javax.swing.JTextField tf_chat;
    private javax.swing.JTextField tf_port;
    private javax.swing.JTextField tf_username;
    // End of variables declaration
}

/*Referencessssss
Below are the links using which this project is developed
https://www.youtube.com/watch?v=hZgntu7889Q
https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams
https://www.javatpoint.com/java-get-current-date
https://www.jmarshall.com/easy/http/
*/
