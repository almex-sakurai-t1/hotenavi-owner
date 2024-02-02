/*
 * @(#)SendMail.java 1.00 2007/09/07 Copyright (C) ALMEX Inc. 2007 ���[�����M�N���X
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
 * ���[�����M�N���X
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
     * ���[���𑗐M����
     * 
     * @param from ���M�����[���A�h���X
     * @param to ���M�惁�[���A�h���X
     * @param subject �薼
     * @param body �{��
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

            // ���M���[���T�[�o�̃v���p�e�B���Z�b�g����
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

        // �Z�b�V�������擾����
        Session session = Session.getDefaultInstance( props, null );
        // session.setDebug( true );

        try
        {
            // ���[�����쐬����
            MimeMessage msg = new MimeMessage( session );
            msg.setHeader( "Content-Transfer-Encoding", "7bit" );

            // ���M�����[���A�h���X�̃Z�b�g
            msg.setFrom( new InternetAddress( from ) );
            // ���M�����̃Z�b�g
            msg.setSentDate( new Date() );

            // ���M�惁�[���A�h���X�̃Z�b�g
            InternetAddress[] toaddress = { new InternetAddress( to ) };
            msg.setRecipients( Message.RecipientType.TO, toaddress );

            // �薼�̃Z�b�g
            msg.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            // �{���̃Z�b�g
            msg.setText( body, "iso-2022-jp" );

            transport = session.getTransport( "smtp" );
            // ���[���𑗐M����
            transport.send( msg );
            // ���M���O���c��
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

            // ���M���[���T�[�o�̃v���p�e�B���Z�b�g����
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

        // �Z�b�V�������擾����
        Session session = Session.getDefaultInstance( props, null );
        // session.setDebug( true );

        try
        {
            // ���[�����쐬����
            MimeMessage msg = new MimeMessage( session );
            msg.setHeader( "Content-Transfer-Encoding", "7bit" );

            // ���M�����[���A�h���X�̃Z�b�g
            msg.setFrom( new InternetAddress( from ) );
            // ���M�����̃Z�b�g
            msg.setSentDate( new Date() );

            // ���M�惁�[���A�h���X�̃Z�b�g
            msg.setRecipients( Message.RecipientType.TO, InternetAddress.parse( to ) );

            // �薼�̃Z�b�g
            msg.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            // �{���̃Z�b�g
            msg.setText( body, "iso-2022-jp" );

            transport = session.getTransport( "smtp" );
            // ���[���𑗐M����
            transport.send( msg );
            // ���M���O���c��
            Logging.info( "[SendMail.send()] from:" + from + ", to:" + to + ", subject" + subject + ", body:" + body );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send()] Exception=" + e.toString() + ", from:" + from + ", to:" + to );
        }
    }

    /**
     * ���[���𑗐M����(�Y�t�t�@�C���t)
     * 
     * @param from ���M�����[���A�h���X
     * @param to ���M�惁�[���A�h���X
     * @param subject �薼
     * @param body �{��
     * @param Filename �Y�t�t�@�C����
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

            // ���M���[���T�[�o�̃v���p�e�B���Z�b�g����
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

        // �Z�b�V�������擾
        Session session = Session.getDefaultInstance( props, null );

        try
        {
            // ���[�����쐬����
            MimeMessage mimeMessage = new MimeMessage( session );
            mimeMessage.setHeader( "Content-Transfer-Encoding", "7bit" );

            // ���M�����[���A�h���X�Ƒ��M�Җ����w��
            mimeMessage.setFrom( new InternetAddress( from ) );
            // ���M�惁�[���A�h���X���w��
            InternetAddress[] toaddress = { new InternetAddress( to ) };
            mimeMessage.setRecipients( Message.RecipientType.TO, toaddress );
            // ���[���̃^�C�g�����w��
            mimeMessage.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            /** �P�ڂ̃{�f�B�p�[�g���쐬 **/
            MimeBodyPart mbp1 = new MimeBodyPart();
            // ���[���̓��e���w��
            mbp1.setText( body, "iso-2022-jp" );

            /** �Q�ڂ̃{�f�B�p�[�g���쐬 **/
            MimeBodyPart mbp2 = new MimeBodyPart();
            // �Y�t����t�@�C�������w��
            FileDataSource fds = new FileDataSource( Filename );
            mbp2.setDataHandler( new DataHandler( fds ) );
            mbp2.setFileName( MimeUtility.encodeWord( fds.getName() ) );

            // �����̃{�f�B���i�[����}���`�p�[�g�I�u�W�F�N�g�𐶐�
            Multipart mp = new MimeMultipart();
            // �P�ڂ̃{�f�B�p�[�g��ǉ�
            mp.addBodyPart( mbp1 );
            // �Q�ڂ̃{�f�B�p�[�g��ǉ�
            mp.addBodyPart( mbp2 );

            // �}���`�p�[�g�I�u�W�F�N�g�����b�Z�[�W�ɐݒ�
            mimeMessage.setContent( mp );

            // ���M���t���w��
            mimeMessage.setSentDate( new Date() );
            // ���M���܂�
            Transport.send( mimeMessage );
            // ���M���O���c��
            Logging.info( "[SendMail.send()] from:" + from + ", to:" + to + ", subject" + subject + ", body:" + body + ", filename:" + Filename );

        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.sendMailAttach()] Exception=" + e.toString() + ", from:" + from + ", to:" + to );
            e.printStackTrace();
        }
    }

    /**
     * ���[���𑗐M����
     * 
     * @param from ���M�����[���A�h���X
     * @param to ���M�惁�[���A�h���X
     * @param cc ���M�惁�[���A�h���X(cc)
     * @param bcc ���M�惁�[���A�h���X(bcc)
     * @param subject �薼
     * @param body �{��
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

            // ���M���[���T�[�o�̃v���p�e�B���Z�b�g����
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

        // �Z�b�V�������擾����
        Session session = Session.getDefaultInstance( props, null );
        // session.setDebug( true );

        try
        {
            // ���[�����쐬����
            MimeMessage msg = new MimeMessage( session );
            msg.setHeader( "Content-Transfer-Encoding", "7bit" );

            // ���M�����[���A�h���X�̃Z�b�g
            msg.setFrom( new InternetAddress( from ) );
            // ���M�����̃Z�b�g
            msg.setSentDate( new Date() );

            // ���M�惁�[���A�h���X�̃Z�b�g
            InternetAddress[] toaddress = { new InternetAddress( to ) };
            msg.setRecipients( Message.RecipientType.TO, toaddress );

            if ( (cc != null) && (cc.compareTo( "" ) != 0) )
            {
                // ���M�惁�[���A�h���X�̃Z�b�g(cc)
                InternetAddress[] ccaddress = { new InternetAddress( cc ) };
                msg.setRecipients( Message.RecipientType.CC, ccaddress );
            }
            if ( (bcc != null) && (bcc.compareTo( "" ) != 0) )
            {
                // ���M�惁�[���A�h���X�̃Z�b�g(bcc)
                InternetAddress[] bccaddress = { new InternetAddress( bcc ) };
                msg.setRecipients( Message.RecipientType.BCC, bccaddress );
            }
            // �薼�̃Z�b�g
            msg.setSubject( MimeUtility.encodeText( subject, "iso-2022-jp", "B" ) );

            // �{���̃Z�b�g
            msg.setText( body, "iso-2022-jp" );

            transport = session.getTransport( "smtp" );
            // ���[���𑗐M����
            transport.send( msg );
            Logging.info( "[SendMail.send()] from:" + from + ", to:" + to + ", cc:" + cc + ", bcc:" + bcc + ", subject" + subject + ", body:" + body );
        }
        catch ( Exception e )
        {
            Logging.error( "[SendMail.send()] Exception=" + e.toString() + ", from:" + from + ", to:" + to + ", cc:" + cc + ". bcc:" + bcc );
        }
    }

}
