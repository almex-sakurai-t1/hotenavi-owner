/*
 * @(#)SendMail.java 1.00 2007/09/07 Copyright (C) ALMEX Inc. 2007 メール送信クラス
 */
package jp.happyhotel.common;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * メール送信クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2007/09/07
 */
public class SendMail implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -153460213816034844L;

    /**
     * メールを送信する
     * 
     * @param from 送信元メールアドレス
     * @param to 送信先メールアドレス
     * @param subject 題名
     * @param body 本文
     * 
     */
    public static void send(String from, String to, String subject, String body)
    {
        String hostkey;
        String hostname;
        String mailfrom = "";
        String erroraddr = "";
        Properties config = new Properties();
        Properties props = new Properties();
        FileInputStream propfile = null;
        Transport transport;

        try
        {
            propfile = new FileInputStream( "/etc/happyhotel/sendmail.conf" );
            config = new Properties();
            config.load( propfile );

            hostkey = config.getProperty( "mail.key" );
            hostname = config.getProperty( "mail.host" );
            mailfrom = config.getProperty( "mail.from" );
            erroraddr = config.getProperty( "mail.add" );
            propfile.close();

            Logging.info( "[SendMail.send()] property:" + hostkey + ", " + hostname + ", " + mailfrom + ", " + erroraddr );

            // 送信メールサーバのプロパティをセットする
            props.put( hostkey, hostname );
            props.put( mailfrom, erroraddr );
            props.put( "mail.smtp.localhost", "mail.hotenavi.com" );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send() propError]:" + e.toString() );
        }
        finally
        {
            if ( propfile != null )
            {
                try
                {
                    propfile.close();
                }
                catch ( Exception e )
                {
                }
            }
        }

        // セッションを取得する
        Session session = Session.getDefaultInstance( props, null );
        // session.setDebug( true );

        try
        {
            // メールを作成する
            MimeMessage msg = new MimeMessage( session );
            msg.setHeader( "Content-Transfer-Encoding", "7bit" );

            // 送信元メールアドレスのセット
            msg.setFrom( new InternetAddress( from ) );
            // 送信日時のセット
            msg.setSentDate( new Date() );

            // 送信先メールアドレスのセット
            InternetAddress[] toaddress = { new InternetAddress( to ) };
            msg.setRecipients( Message.RecipientType.TO, toaddress );

            // 題名のセット
            msg.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            // 本文のセット
            msg.setText( body, "iso-2022-jp" );

            transport = session.getTransport( "smtp" );
            // メールを送信する
            transport.send( msg );
            // 送信ログを残す
            Logging.info( "[SendMail.send()] from:" + from + ", to:" + to + ", subject" + subject + ", body:" + body );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send()] Exception=" + e.toString() + ", from:" + from + ", to:" + to );
        }
    }

    public static void sends(String from, String to, String subject, String body)
    {
        String hostkey;
        String hostname;
        String mailfrom = "";
        String erroraddr = "";
        Properties config = new Properties();
        Properties props = new Properties();
        FileInputStream propfile = null;
        Transport transport;

        try
        {
            propfile = new FileInputStream( "/etc/happyhotel/sendmail.conf" );
            config = new Properties();
            config.load( propfile );

            hostkey = config.getProperty( "mail.key" );
            hostname = config.getProperty( "mail.host" );
            mailfrom = config.getProperty( "mail.from" );
            erroraddr = config.getProperty( "mail.add" );
            propfile.close();

            Logging.info( "[SendMail.send()] property:" + hostkey + ", " + hostname + ", " + mailfrom + ", " + erroraddr );

            // 送信メールサーバのプロパティをセットする
            props.put( hostkey, hostname );
            props.put( mailfrom, erroraddr );
            props.put( "mail.smtp.localhost", "mail.hotenavi.com" );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send() propError]:" + e.toString() );
        }
        finally
        {
            if ( propfile != null )
            {
                try
                {
                    propfile.close();
                }
                catch ( Exception e )
                {
                }
            }
        }

        // セッションを取得する
        Session session = Session.getDefaultInstance( props, null );
        // session.setDebug( true );

        try
        {
            // メールを作成する
            MimeMessage msg = new MimeMessage( session );
            msg.setHeader( "Content-Transfer-Encoding", "7bit" );

            // 送信元メールアドレスのセット
            msg.setFrom( new InternetAddress( from ) );
            // 送信日時のセット
            msg.setSentDate( new Date() );

            // 送信先メールアドレスのセット
            msg.setRecipients( Message.RecipientType.TO, InternetAddress.parse( to ) );

            // 題名のセット
            msg.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            // 本文のセット
            msg.setText( body, "iso-2022-jp" );

            transport = session.getTransport( "smtp" );
            // メールを送信する
            transport.send( msg );
            // 送信ログを残す
            Logging.info( "[SendMail.send()] from:" + from + ", to:" + to + ", subject" + subject + ", body:" + body );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send()] Exception=" + e.toString() + ", from:" + from + ", to:" + to );
        }
    }

    /**
     * メールを送信する(添付ファイル付)
     * 
     * @param from 送信元メールアドレス
     * @param to 送信先メールアドレス
     * @param subject 題名
     * @param body 本文
     * @param Filename 添付ファイル名
     * 
     */
    public static void sendMailAttach(String from, String to, String subject, String body, String Filename)
    {
        String hostkey;
        String hostname;
        Properties config = new Properties();
        Properties props = new Properties();
        FileInputStream propfile = null;

        try
        {
            propfile = new FileInputStream( "/etc/happyhotel/sendmail.conf" );
            config = new Properties();
            config.load( propfile );

            hostkey = config.getProperty( "mail.key" );
            hostname = config.getProperty( "mail.host" );
            props.put( "mail.smtp.localhost", "mail.hotenavi.com" );

            Logging.info( "[SendMail.send()] property:" + hostkey + ", " + hostname );

            propfile.close();

            // 送信メールサーバのプロパティをセットする
            props.put( hostkey, hostname );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.sendMailAttach() propError]:" + e.toString() );
        }
        finally
        {
            if ( propfile != null )
            {
                try
                {
                    propfile.close();
                }
                catch ( Exception e )
                {
                }
            }
        }

        // セッションを取得
        Session session = Session.getDefaultInstance( props, null );

        try
        {
            // メールを作成する
            MimeMessage mimeMessage = new MimeMessage( session );
            mimeMessage.setHeader( "Content-Transfer-Encoding", "7bit" );

            // 送信元メールアドレスと送信者名を指定
            mimeMessage.setFrom( new InternetAddress( from ) );
            // 送信先メールアドレスを指定
            InternetAddress[] toaddress = { new InternetAddress( to ) };
            mimeMessage.setRecipients( Message.RecipientType.TO, toaddress );
            // メールのタイトルを指定
            mimeMessage.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            /** １つ目のボディパートを作成 **/
            MimeBodyPart mbp1 = new MimeBodyPart();
            // メールの内容を指定
            mbp1.setText( body, "iso-2022-jp" );

            /** ２つ目のボディパートを作成 **/
            MimeBodyPart mbp2 = new MimeBodyPart();
            // 添付するファイル名を指定
            FileDataSource fds = new FileDataSource( Filename );
            mbp2.setDataHandler( new DataHandler( fds ) );
            mbp2.setFileName( MimeUtility.encodeWord( fds.getName() ) );

            // 複数のボディを格納するマルチパートオブジェクトを生成
            Multipart mp = new MimeMultipart();
            // １つ目のボディパートを追加
            mp.addBodyPart( mbp1 );
            // ２つ目のボディパートを追加
            mp.addBodyPart( mbp2 );

            // マルチパートオブジェクトをメッセージに設定
            mimeMessage.setContent( mp );

            // 送信日付を指定
            mimeMessage.setSentDate( new Date() );
            // 送信します
            Transport.send( mimeMessage );
            // 送信ログを残す
            Logging.info( "[SendMail.send()] from:" + from + ", to:" + to + ", subject" + subject + ", body:" + body + ", filename:" + Filename );

        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.sendMailAttach()] Exception=" + e.toString() + ", from:" + from + ", to:" + to );
            e.printStackTrace();
        }
    }

    /**
     * メールを送信する
     * 
     * @param from 送信元メールアドレス
     * @param to 送信先メールアドレス
     * @param cc 送信先メールアドレス(cc)
     * @param bcc 送信先メールアドレス(bcc)
     * @param subject 題名
     * @param body 本文
     * 
     */
    public static void send(String from, String to, String cc, String bcc, String subject, String body)
    {
        String hostkey;
        String hostname;
        String mailfrom = "";
        String erroraddr = "";
        Properties config = new Properties();
        Properties props = new Properties();
        FileInputStream propfile = null;
        Transport transport;

        try
        {
            propfile = new FileInputStream( "/etc/happyhotel/sendmail.conf" );
            config = new Properties();
            config.load( propfile );

            hostkey = config.getProperty( "mail.key" );
            hostname = config.getProperty( "mail.host" );
            mailfrom = config.getProperty( "mail.from" );
            erroraddr = config.getProperty( "mail.add" );
            propfile.close();

            Logging.info( "[SendMail.send()] property:" + hostkey + ", " + hostname + ", " + mailfrom + ", " + erroraddr );

            // 送信メールサーバのプロパティをセットする
            props.put( hostkey, hostname );
            props.put( mailfrom, erroraddr );
            props.put( "mail.smtp.localhost", "mail.hotenavi.com" );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send() propError]:" + e.toString() );
        }
        finally
        {
            if ( propfile != null )
            {
                try
                {
                    propfile.close();
                }
                catch ( Exception e )
                {
                }
            }
        }

        // セッションを取得する
        Session session = Session.getDefaultInstance( props, null );
        // session.setDebug( true );

        try
        {
            // メールを作成する
            MimeMessage msg = new MimeMessage( session );
            msg.setHeader( "Content-Transfer-Encoding", "7bit" );

            // 送信元メールアドレスのセット
            msg.setFrom( new InternetAddress( from ) );
            // 送信日時のセット
            msg.setSentDate( new Date() );

            // 送信先メールアドレスのセット
            InternetAddress[] toaddress = { new InternetAddress( to ) };
            msg.setRecipients( Message.RecipientType.TO, toaddress );

            if ( (cc != null) && (cc.compareTo( "" ) != 0) )
            {
                // 送信先メールアドレスのセット(cc)
                InternetAddress[] ccaddress = { new InternetAddress( cc ) };
                msg.setRecipients( Message.RecipientType.CC, ccaddress );
            }
            if ( (bcc != null) && (bcc.compareTo( "" ) != 0) )
            {
                // 送信先メールアドレスのセット(bcc)
                InternetAddress[] bccaddress = { new InternetAddress( bcc ) };
                msg.setRecipients( Message.RecipientType.BCC, bccaddress );
            }
            // 題名のセット
            msg.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            // 本文のセット
            msg.setText( body, "iso-2022-jp" );

            transport = session.getTransport( "smtp" );
            // メールを送信する
            transport.send( msg );
            Logging.info( "[SendMail.send()] from:" + from + ", to:" + to + ", cc:" + cc + ", bcc:" + bcc + ", subject" + subject + ", body:" + body );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send()] Exception=" + e.toString() + ", from:" + from + ", to:" + to + ", cc:" + cc + ". bcc:" + bcc );
        }
    }

}
