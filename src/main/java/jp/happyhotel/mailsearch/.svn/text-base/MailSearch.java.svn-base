/**
 * 
 */
package jp.happyhotel.mailsearch;


import jp.happyhotel.common.Logging;

import java.io.FileInputStream;
import java.util.*;

/**
 * @author sandeep-d1
 * 
 */
public class MailSearch {

	 private static Properties props;
	 private static String mailConfigFile = "mailsearch.properties";
	 private static EmailListener emailListener ;
	  
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		EmailMessage emailMesg;
		MailHandler mh;
		String replyMessage;
		
		
		try
		{
			Logging.info("**** Starting Mail Search ****");	
			init();
			
			// Process mails till eternity
			while ( true )
			{
				emailMesg = emailListener.getEmailMessage();
			
				if ( emailMesg != null)
				{
						if ( emailMesg.getFromMailAddr() != null)
						{
							// handle email
							mh = new MailHandler(emailMesg);
							replyMessage = mh.handleMessage();
							
							 //send reply
							emailMesg.sendReply(replyMessage, true);
							
						}
						else
						{
							Logging.error("Not a valid email : "+emailMesg.getMesgId());
							emailMesg.setStatus(EmailMessage.FAILURE);
							emailMesg.setErrorMessage("Invalid email-id");
						}
						
						// log email message (save in db or log file)
						emailMesg.save();
				}
				else
				{
					Logging.error("ERROR : Email Message is NULL ");
				}
						
			} // End of while loop
			
			
		}
		catch(Exception ex)
		{
			Logging.fatal(ex.getMessage());
		}
		
		Logging.info("**** Shutting down Mail Search****");
		
	}// End of main method
	
	
	private static void init() throws Exception
	{
		props = new Properties();
		FileInputStream propfile = new FileInputStream(mailConfigFile);
        
		//populate the 'Properties' object with the mail
	    //server address, so that the default 'Session'
	    //instance can use it.
	    
		props.load(propfile);
		propfile.close();
		emailListener = new EmailListener();
		
	}
	public static Properties getProperties()
	{
		return props;
	}
	

}
