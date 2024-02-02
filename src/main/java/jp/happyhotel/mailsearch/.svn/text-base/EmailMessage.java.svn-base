package jp.happyhotel.mailsearch;

import java.io.*;
import java.net.URLEncoder;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.*;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class EmailMessage {

	private String mesgId;
	private String fromMailAddr;
	private String toMailAddr;
	private String subject;
	private String mesgBody;
	private String recevdDate;
	private String server;
	private String status;
	private String errorMessage;
	final static String SUCCESS = "SUCCESS";
	final static String FAILURE = "FAILURE";
	private static Properties props;
	private static String  HEADER_TEXT_PLAIN_MESSEGE;
	
	Message msg;
	
	public EmailMessage(Message pop3Message)
	{
		try
		{
			fromMailAddr = ((InternetAddress) pop3Message.getFrom()[0]).getAddress();
			subject = pop3Message.getSubject();
			mesgId = String.valueOf(System.currentTimeMillis());
			java.util.Date dt = pop3Message.getReceivedDate();
			
			if ( dt == null)
			{
				dt = new Date(System.currentTimeMillis());
			}
			
			recevdDate = dt.toString();
		}
		catch(MessagingException msgEx)
		{
			Logging.error("Exception in creating EmailMessage ->"+msgEx.getMessage());
		}
		
		msg = pop3Message; 
		
	}
	/**
	 *  Log the message details into the database
	 *
	 */
	public void save()
	{
		// INSERT INTO DATABASE TABLE
		StringBuffer insertQuery = new StringBuffer("INSERT INTO mailsearch_log (msgid,sender,subject,recevd_date,status,errormsg) VALUES (")
							  			.append("'"+mesgId+"'")
							  			.append(",")
							  			.append("'"+fromMailAddr+"'")
							  			.append(",")
							  			.append("'"+subject+"'")
							  			.append(",")
							  			.append("'"+recevdDate+"'")
							  			.append(",")
							  			.append("'"+status+"'")
							  			.append(",")
							  			.append("'"+errorMessage+"'")
							  			.append(")");
		PreparedStatement pStmt ;
		Connection dbConn;
				
		try
		{
			 Logging.info("Saving email into database for ->"+mesgId);
			 dbConn = DBConnection.getConnection();
			 pStmt = dbConn.prepareStatement(insertQuery.toString());
			 pStmt.execute();
			
		}
		catch (Exception ex)
		{
			Logging.error("Could not save email into database ...."+mesgId);
			Logging.error("ERROR -> "+ex.getMessage());
			
		}
		
	}
	
	/**
	 * reading japanese character set.
	 * @param str
	 * @return
	 */
	private static String toCharSet(String str) {
		String result = null;
		if (str != null) {
			try {
				 result = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (Exception ex) {
				// do nothing
			}
		}

		return result;
	}
	/**
	 * Send back the response to the person from whom this message was received
	 * @param replyMessage response to the search keyword sent by mail in subject
	 * @param isHTML flag to indicate whether the response is a HTML or plain-text
	 * @return 
	 */
	public void sendReply(String replyMessage, boolean isHTML)
	{
		
		String replyFrom;
		
		try
		{ 
			Logging.info("Sending reply to ->"+fromMailAddr +":"+mesgId);
			
		    //	Logging.debug("Content Type :"+msg.getContentType());
			
			//replyFrom = "GASCHARGE-USER@ALMEX.JP";
			replyFrom   = MailSearch.getProperties().getProperty("mail.from"); 
			
			// Create a reply message
			MimeMessage reply = (MimeMessage)msg.reply(false);
			
			// Set the from field
	        reply.setFrom(new InternetAddress(replyFrom));
   	        
	        props = MailSearch.getProperties();
	        HEADER_TEXT_PLAIN_MESSEGE=toCharSet(props.getProperty("HEADER_TEXT_PLAIN_MESSEGE"));
	           	        
	        /*for sending multipart msg*/
	        String encodedSubject = URLEncoder.encode(subject, "Shift-JIS");
	        String Text=HEADER_TEXT_PLAIN_MESSEGE+encodedSubject;
            Multipart mp = new MimeMultipart("alternative");
            
            BodyPart b1 = new MimeBodyPart();
            b1.setContent(Text,"text/plain;charset=\"iso-2022-jp\"");

            BodyPart b2 = new MimeBodyPart();
            // Map is some binary postscript file
            
            b2.setContent(replyMessage,"text/html;charset=\"iso-2022-jp\"");
            
            mp.addBodyPart(b1);
            mp.addBodyPart(b2);
            
            
            reply.setContent(mp); // This form takes a Multipart object
	        
	        Logging.debug("------- Response to be sent -------\n"+replyMessage);
	        
	        // Send the message
	        Transport.send(reply);


		}
		catch(Exception ex)
		{
			Logging.error("Exception in sending message to ->"+fromMailAddr);
			Logging.error(ex.getMessage());
			status = FAILURE;
			errorMessage = ex.getMessage();
		}
		
	}
	
	public void displayPOP3Message()	throws MessagingException,
													   IOException 
	{
		Flags flags;
		flags = msg.getFlags();
		if (msg.getFrom()[0] instanceof InternetAddress) 
		{
					Logging.debug("Message received from: "
									+ ((InternetAddress) msg.getFrom()[0]).getAddress());
		}
		Logging.debug("Message received on: " + msg.getReceivedDate());

		Logging.debug("Message Subject: " + msg.getSubject());
		
	}// displayMessage


	public String getFromMailAddr() {
		return fromMailAddr;
	}
	public void setFromMailAddr(String fromMailAddr) {
		this.fromMailAddr = fromMailAddr;
	}
	public String getMesgBody() {
		return mesgBody;
	}
	public void setMesgBody(String mesgBody) {
		this.mesgBody = mesgBody;
	}
	public String getRecvdDateTime() {
		return recevdDate;
	}
	public void setRecvdDateTime(String recvdDateTime) {
		this.recevdDate = recvdDateTime;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getToMailAddr() {
		return toMailAddr;
	}
	public void setToMailAddr(String toMailAddr) {
		this.toMailAddr = toMailAddr;
	}
	public String getMesgId() {
		return mesgId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
