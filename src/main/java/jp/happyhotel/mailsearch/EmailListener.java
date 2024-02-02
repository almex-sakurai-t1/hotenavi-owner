package jp.happyhotel.mailsearch;


import java.util.Properties;
import javax.mail.*;
import jp.happyhotel.common.Logging;
/**
 * @author sandeep-d1
 * 
 */
public class EmailListener {

	private String EMAIL_POP3_SERVER_IP;
	private String EMAIL_SMTP_SERVER_IP;
	private String USERNAME;
	private String PASSWORD;
	private String debug = "false";
	private int numOfMessages=0;
	private int numOfMessagesProcessed=0;
	private int retryCount = 10;
	
	private long totalNumOfMessagesProcessed;
	long checkForNewMailsSleepInterval = 10000; //10 secs
	long reconnectTimeInterval = 5000; // 5 secs
	
	private boolean cleanupMails = false;
	
	private Message []pop3Messages;
	
	private Properties props ;
	private Store popStore = null;
	private Folder folder = null;
	
	// Initialize
	public EmailListener() throws Exception
	{
		init();
	}
	/**
	 * Initialization 
	 *  - Read the properties file for configuration details
	 *  - Establish connection with the the POP3 server
	 */
	private void init() throws Exception
	{
		//props = new Properties();
		props  = MailSearch.getProperties();
		
//		MAIL_KEY        = props.getProperty("mail.key");
		EMAIL_POP3_SERVER_IP = props.getProperty("mail.pop3.host");
		EMAIL_SMTP_SERVER_IP = props.getProperty("mail.smtp.host");
        USERNAME 		= props.getProperty("mail.user");
        PASSWORD		= props.getProperty("mail.pass");
        try
        {
        	reconnectTimeInterval = Long.parseLong(props.getProperty("mail.pop3.reconnect_time_interval"));
        }
        catch (NumberFormatException nfEx)
        {
        	// set default value
        	reconnectTimeInterval = 5000;
        }
        try
        {
        	retryCount  = Integer.parseInt(props.getProperty("mail.pop3.retrycount"));
        }
        catch ( NumberFormatException nfEx)
        {
        	// set default value
        	retryCount = 10;
        }
        
        try
        {
        	checkForNewMailsSleepInterval  = Integer.parseInt(props.getProperty("mail.fetch.interval"));
        }
        catch ( NumberFormatException nfEx)
        {
        	// set default value
        	checkForNewMailsSleepInterval = 10000;
        }
        
       
        if (props.getProperty("mail.debug") != null) 
        	debug = props.getProperty("mail.debug"); 
        	
        
        Logging.info("EmailListener : mail.pop3.host ->"+EMAIL_POP3_SERVER_IP);
        Logging.info("EmailListener : mail.smtp.host ->"+EMAIL_SMTP_SERVER_IP);
        Logging.info("EmailListener : mail.user ->"+USERNAME);
        Logging.info("EmailListener : mail.pass ->"+PASSWORD);
        Logging.info("EmailListener : mail.pop3.reconnect_time_interval ->"+reconnectTimeInterval);
        Logging.info("EmailListener : mail.pop3.retrycount ->"+retryCount);
        Logging.info("EmailListener : mail.fetch.interval ->"+checkForNewMailsSleepInterval);
        Logging.info("EmailListener : debug ->"+debug);
        
        		
		connect();
        
	}
	/**
	 *  Establish connection with the POP3 mail server
	 * 
	 */
	private void connect() throws MessagingException, Exception
	{
		Logging.info("EmailListener : Connecting to POP3 ->"+EMAIL_POP3_SERVER_IP);

		Properties systemProps = System.getProperties(); 
		systemProps.put("mail.debug", debug);
		systemProps.put("mail.smtp.host", EMAIL_SMTP_SERVER_IP);

		Session session = Session.getDefaultInstance(systemProps, null); 
		
		popStore = session.getStore("pop3");

	    popStore.connect(EMAIL_POP3_SERVER_IP, USERNAME, PASSWORD);
	    
	  /*  folder = popStore.getFolder("INBOX");
	    
	    
	    if (!folder.exists())
	    {
	        throw new Exception
	        ("An 'INBOX' folder does not exist for the user ["+USERNAME+"]");
	    }
	  */  
	    Logging.info("EmailListener : ** Connection established with POP3 server **");
	}

	/**
	 * Retrieves email message from the POP3 mail server 
	 * 
	 * @return EmailMessage received from the user ( PC or Mobile )
	 * 		   NULL : returns null in case of a connection failure	
	 */
	public EmailMessage getEmailMessage()
	{
		boolean connStatus;
		EmailMessage emailMessage = null;
		Message pop3Message;
		
		// check if we are connected to the pop3 server
			if (!popStore.isConnected())
			{
				 connStatus  = reconnect();
				 if (!connStatus)
				 {
					 return null; 
				 }
				 
			}
		
			while ( (numOfMessages == 0) ||
					(numOfMessagesProcessed == numOfMessages)
				   )
			{
				
					if ( cleanupMails)
					{
						cleanupMailsFromServer();
					}
					
					try
					{
						Logging.info("Going to fetch mails from server ..");
						folder = popStore.getFolder("INBOX");
						
						getMessagesToProcess();
						Logging.debug("Number of Messages to process ->"+numOfMessages);
						numOfMessagesProcessed = 0;
					
						if (numOfMessages == 0)
						{
							cleanupMails = false;
							try
							{
								Logging.debug("Going to sleep for "+
											   	(checkForNewMailsSleepInterval/1000)+
												"secs ...");
								Thread.sleep(checkForNewMailsSleepInterval);
							}
							catch(Exception ex)
							{
								// do nothing
							}
						}
						
					}
					catch(Exception ex)
					{
						Logging.error("Exception while getting inbox folder ->"+ex.getMessage());
						break;
					}
					
					
			}// End of while
			
			if ( numOfMessages > 0)
			{
				cleanupMails = true;
				pop3Message = pop3Messages[numOfMessagesProcessed];
				++numOfMessagesProcessed; 
						
				emailMessage = createEmailMessage(pop3Message);
			}
		
		return emailMessage;
	}
	
	private Message[] getMessagesToProcess()
	{
		try{
	
			numOfMessages = 0;
			
			if (!folder.isOpen())
			{	
				Logging.debug("Open FOLDER");
				folder.open(Folder.READ_WRITE);
			}
			//Flags flags = folder.getPermanentFlags();
				
			pop3Messages = folder.getMessages();
			
			numOfMessages = pop3Messages.length;
			
			totalNumOfMessagesProcessed = totalNumOfMessagesProcessed + numOfMessages;
			
			if (numOfMessages == 0)
			{
				//System.out.println("The INBOX folder does not yet contain any email messages");
			}
			
		}catch(Exception ex)
		{
			Logging.error("Exception in getMessages -> "+ex.getMessage());
			// do nothing
		}

		return pop3Messages;
	}
	
	private EmailMessage createEmailMessage(Message pop3Message)
	{
		EmailMessage em = null;
				
		try{
			em = new EmailMessage(pop3Message);
		}
		catch(Exception msgEx)
		{
			// do nothing
		}
		
		return em;
	}
	
	private void cleanupMailsFromServer()
	{
		try{
			
			Logging.info("Going to cleanup mails from the server ->"+numOfMessages);
			for ( int index = 0; index < numOfMessages; index ++)
			{
				pop3Messages[index].setFlag(Flags.Flag.DELETED, true);
			}
			
			if ( folder != null)	folder.close(true);
			
			Logging.debug("Close FOLDER");
		}
		catch(Exception ex)
		{
			Logging.error("Excepion while cleaning up mails ->"+ex.getMessage());
		}
		
	}
	private boolean reconnect()
	{
		int count = 0;
		boolean isConnected = false;
		
		while ( count < retryCount)
		{
			try{
				++count;
				Logging.info("EmailListener : reconnect() "+count+
									"-> Trying to reconnect POP3 server "+
									EMAIL_POP3_SERVER_IP);
				connect();
				isConnected = true;
				break;
			}catch(MessagingException msgEx)
			{
				
				Logging.error("EmailListener : reconnect() ERROR ->"+msgEx.getMessage());
				Logging.error("EmailListener : reconnect() Trying to reconnect again after "+reconnectTimeInterval);
				try{
					Thread.sleep(reconnectTimeInterval);
				}catch(Exception ex)
				{
					// do nothing
				}
			}catch(Exception ex)
			{
				Logging.error("EmailListener : reconnect() FATAL ->"+ex.getMessage());
				Logging.error("EmailListener : reconnect() -> Exiting , could not connect to server");
				break;
			}
		}// End of while
		
		return isConnected;
	}
		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		EmailMessage em;
		
		try{
			EmailListener el = new EmailListener();
			while (true)
			{	
				em = el.getEmailMessage();
				if (em != null)
				{
					System.out.println("Email : FROM ->"+em.getFromMailAddr());
					System.out.println("Email : SUBJECT ->"+em.getSubject());
					em.sendReply(null, false);
				}
			}
			
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			ex.getMessage();
		}

	}

}
