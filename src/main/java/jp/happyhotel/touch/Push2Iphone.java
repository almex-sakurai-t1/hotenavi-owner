package jp.happyhotel.touch;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jp.happyhotel.common.Logging;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsDelegateAdapter;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.DeliveryError;
import com.notnoop.apns.PayloadBuilder;

/**
 * �n�s�z�e�^�b�`�`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 * @see ���N�G�X�g�F1000�d��<br>
 *      ���X�|���X�F1001�d��
 */
public class Push2Iphone implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = 8544511852177631618L;
    private static final int    OS_TYPE_IPHONE   = 1;                             // iPhone
    private static final String configFilePath   = "//etc//happyhotel//push.conf";
    FileInputStream             propfile         = null;
    Properties                  config;
    private int                 apliKind;                                         // 1:�n�s�z�e�A�v��,10:�\��A�v��
    int                         ErrorIdentifier;                                  // �s���g�[�N���̔z�񎯕�
    int                         SentIdentifier;                                   // ���M�ς݃g�[�N���̔z�񎯕�
    ArrayList<String>           inactiveToken    = new ArrayList<String>();       // ���M�G���[�g�[�N���z��
    ArrayList<String>           invalidToken     = new ArrayList<String>();       // �s���g�[�N���z��

    ApnsService                 apnsService      = null;

    /**
     *
     */
    public Push2Iphone()
    {
        this.apliKind = 1;
        ErrorIdentifier = -1;
        SentIdentifier = -1;
    }

    public int getApliKind()
    {
        return apliKind;
    }

    public ArrayList<String> getInactiveToken()
    {
        return inactiveToken;
    }

    public ArrayList<String> getInvalidToken()
    {
        return invalidToken;
    }

    public void setApliKind(int apliKind)
    {
        this.apliKind = apliKind;
    }

    public void setInactiveToken(ArrayList<String> inactiveToken)
    {
        this.inactiveToken = inactiveToken;
    }

    public void setInvalidToken(ArrayList<String> invalidToken)
    {
        this.invalidToken = invalidToken;
    }

    /**
     * �v�b�V�����M
     * 
     * @param paramMessage
     * @param paramUrl
     * @see �Ώے[����ap_user_push_config��push_flag=1�̃��[�U
     */
    public void getTokenList(String paramMessage, String paramUrl)
    {
        boolean ret = false;
        TokenUser tk = new TokenUser();
        GooglePushMapper gpm = null;
        ArrayList<String> tokenList = new ArrayList<String>();

        try
        {
            gpm = new GooglePushMapper();
            // Android���[�U��push_flag=1���擾
            ret = tk.getTokenUserList( OS_TYPE_IPHONE, 0 );

            if ( paramMessage == null || paramMessage.equals( "" ) != false )
            {
                paramMessage = "";
            }
            if ( paramUrl == null )
            {
                paramUrl = "";
            }

            if ( ret != false )
            {
                for( int i = 0 ; i < tk.getTokenList().length ; i++ )
                {
                    Logging.info( "user_id:" + tk.getTokenList()[i].getUserId() );
                    this.push( tk.getTokenList()[i].getToken(), paramMessage );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[Push2IPhone Exception]:" + e.toString() );
        }
    }

    public boolean push(String deviceToken, String msg)
    {
        String url = "";
        boolean ret;

        ret = this.push( deviceToken, msg, url );
        return ret;

    }

    public boolean push(String deviceToken, String msg, String url)
    {
        boolean ret = false;

        // Logging.info( "[Push2IPhone]push()" ,"Push2Iphone");

        // TODO certFilePath��certPassword��DC�Ђ̏ؖ����̂��߁A�����[�X�܂łɕK�����ЃA�J�E���g��p�ӂ��邱��

        String certFilePath = ""; // Apple�̃T�[�o�[�ƒʐM���邽�߂̏ؖ����t�@�C����Path���擾����
        String certPassword = ""; // �ؖ����̃p�X���[�h���擾����

        // �ؖ��������擾
        try
        {
            propfile = new FileInputStream( configFilePath );
            config = new Properties();
            config.load( propfile );

            if ( apliKind == 1 || apliKind == 2 )
            {
                certFilePath = String.valueOf( config.getProperty( "cert.filepath" ) );
                certPassword = String.valueOf( config.getProperty( "cert.password" ) );
            }
            else
            {
                certFilePath = String.valueOf( config.getProperty( "cert.filepath.rsv" ) );
                certPassword = String.valueOf( config.getProperty( "cert.password.rsv" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[Push2Iphone]Exception:" + e.toString() );
            return false;
        }

        ApnsServiceBuilder serviceBuilder =
                APNS.newService().withCert( certFilePath, certPassword );
        // ��:�����w�肵�Ȃ��Ɠ�����Thread�𐶐����悤�Ƃ��ăR�P��
        // ���w�肷���connectionClosed��ʂ炸�z�M�G���[���擾�ł��Ȃ�
        // .withNoErrorDetection();

        // �ڑ���Ƃ���Sandbox(�J���p��)���w�肷��ꍇ
        // serviceBuilder.withSandboxDestination();
        // �ڑ���Ƃ��Ė{�ԗp�����w�肷��ꍇ
        serviceBuilder.withProductionDestination();

        // PUSH�z�M�̃X�e�[�^�X�ʒm���擾���邽�ߒǉ�
        serviceBuilder.withDelegate( new MyApnsDelegate() );

        // ApnsService apnsService = null;
        try
        {
            apnsService = serviceBuilder.build();
            PayloadBuilder payloadBuilder = APNS.newPayload();

            // alert������
            payloadBuilder.alertBody( msg );

            // badge
            payloadBuilder.badge( 1 );

            payloadBuilder.customField( "url", url );

            // Push�ʒm�̑��M(�����̃f�o�C�X�g�[�N�����܂Ƃ߂đ��M���\)
            String[] token = deviceToken.split( ",", 0 );

            ArrayList<String> ids = new ArrayList<String>();
            for( int i = 0 ; i < token.length ; i++ )
            {
                ids.add( token[i] );
            }

            // �����g�[�N�����܂Ƃ߂�PUSH ��Identifier�́A1�`
            Collection<? extends ApnsNotification> result = null;
            try
            {
                result = apnsService.push( ids, payloadBuilder.build() );
            }
            catch ( Exception e )
            {
                Logging.error( "[Push2IPhone] push() Exception:" + e.toString() );
                if ( SentIdentifier > -1 )
                {
                    ErrorIdentifier = SentIdentifier + 1;
                }
            }

            // Collection<? extends EnhancedApnsNotification> result = null;
            // Date d = new Date();
            // result = apnsService.push( ids, payloadBuilder.build(), d );

            /*
             * //�ꌏ����PUSH������@ ��Identifier�́A0�`
             * try {
             * String payload = payloadBuilder.build();
             * for (int i = 0 ; i < token.length ; i++){
             * int now = (int)(new Date().getTime()/1000);
             * EnhancedApnsNotification notification = new EnhancedApnsNotification(i,
             * now + 60 * 60 ,
             * token[i] ,
             * payload);
             * apnsService.push(notification);
             * }
             * } catch (Exception e1) {
             * Logging.info( "IosPush :" + e1.getMessage());
             * }
             */

            // ���ꂪ�Ȃ��ƁAconnectionClosed���������m�ł��Ȃ�
            Thread.sleep( 1000 );

            // �G���[���������Ƃ��͍ċA����
            // Logging.info( "ErrorIdentifier=" + ErrorIdentifier );
            if ( ErrorIdentifier >= 0 )
            {
                // �s���g�[�N���z��ɒǉ�
                invalidToken.add( ids.get( ErrorIdentifier - 1 ) );
                // invalidToken.add(ids.get(ErrorIdentifier));

                // �g�[�N���z��č쐬
                String newDeviceToken = "";
                if ( ids.size() > ErrorIdentifier )
                // if( ids.size() > ErrorIdentifier + 1)
                {
                    for( int i = ErrorIdentifier ; i < ids.size() ; i++ )
                    {
                        // for (int i = ErrorIdentifier + 1 ; i < ids.size() ; i++){
                        newDeviceToken += "," + ids.get( i );
                    }
                    newDeviceToken = newDeviceToken.substring( 1, newDeviceToken.length() );
                    // push�֐��ċA����
                    Logging.info( "pushSAISOU ErrorIdentifier=" + ErrorIdentifier + " newDeviceToken=" + newDeviceToken );
                    ErrorIdentifier = -1;
                    push( newDeviceToken, msg, url );
                }
            }

            // �t�B�[�h�o�b�N�T�[�r�X�Ɍ�z�M���ꂽ�ƕ񍐂��ꂽ�f�o�C�X���X�g���擾������@
            // �EapnsService.getInactiveDevices()���g�p���Ď擾����
            // �EAPN�����A�g�[�N���ƃA�v���P�[�V�������f�o�C�X�ɂ��͂⑶�݂��Ȃ��Ɣ��f�����^�C���X�^���v���ʒu�Â���Map
            // �E�f�o�C�X���X�g��1�x�擾����ƃN���A�����
            // �E�f�o�C�X���X�g�ɓo�^����Ă��Ă��A�A�v�����ăC���X�g�[����push()����������ƃ��X�g���������
            // �EAPN�����ێ����Ă��Ȃ��f�o�C�X�̓��X�g�ɓo�^����Ȃ��i��Faaaaaa�j

            Map<String, Date> InactiveDevices = new HashMap<String, Date>();
            InactiveDevices = apnsService.getInactiveDevices();
            // Logging.info( "map.size()  " + InactiveDevices.size() );
            for( String str : InactiveDevices.keySet() )
            {
                // Logging.info( "Map " + str + ":" + InactiveDevices.get( str ), "Push2Iphone" );
                inactiveToken.add( str );
            }

            // Logging.info( "[Push2IPhone]:end to send ids.size() =" + ids.size() + " push.token=" + deviceToken );
            // Logging.info( "ErrorIdentifier! =" + ErrorIdentifier );

            ret = true;

        }
        catch ( Exception e )
        {
            Logging.error( "[Push2IPhone] Exception:" + e.toString() );
            ret = false;
        }
        finally
        {
            // Connection�����
            if ( apnsService != null )
            {
                apnsService.stop();
            }
        }

        return ret;
    }

    public class MyApnsDelegate extends ApnsDelegateAdapter
    {

        // Logger log = LoggerFactory.getLogger(MyApnsDelegate.class);

        @Override
        public void messageSent(ApnsNotification apnsNotification, boolean b)
        {

            SentIdentifier = apnsNotification.getIdentifier();
            /*
             * byte[] bytes1 = apnsNotification.getDeviceToken();
             * String tmp1 = "";
             * tmp1 = new String( Hex.encodeHex( bytes1 ) );
             * for( byte b1 : bytes1 )
             * {
             * tmp1 += b1;
             * }
             * Logging.info( "[messageSent] messageSent called! [" + apnsNotification.getIdentifier() + "] DeviceToken=" + tmp1 );
             */
            // Logging.info( "[messageSent] apnsNotification.getDeviceToken()=" + tmp1 );
            // Logging.info( "[messageSent] apnsNotification..marshall() =" + apnsNotification.marshall() );
            // Logging.info( "[messageSent] apnsNotification..getIdentifier() =" + apnsNotification.getIdentifier() );
            // Logging.info( "[messageSent] apnsNotification..getExpiry() =" + apnsNotification.getExpiry() );

        }

        @Override
        public void messageSendFailed(ApnsNotification message, Throwable e)
        {

            /*
             * byte[] bytes1 = message.getDeviceToken();
             * String tmp1 = "";
             * tmp1 = new String( Hex.encodeHex( bytes1 ) );
             * for( byte b1 : bytes1 )
             * {
             * tmp1 += b1;
             * }
             * Logging.info( "messageSendFailed called! [" + message.getIdentifier() + "] DeviceToken=" + tmp1 );
             */
            // Logging.info( "[messageSendFailed] message.getDeviceToken()=" + tmp1 );
            // Logging.info( "[messageSendFailed] message.marshall() =" + message.marshall() );
            // Logging.info( "[messageSendFailed] message.getIdentifier() =" + message.getIdentifier() );
            // Logging.info( "[messageSendFailed] message.getExpiry() =" + message.getExpiry() );
            // Logging.info( "[messageSendFailed] e=" + e );

        }

        @Override
        public void connectionClosed(DeliveryError e, int messageIdentifier)
        {

            // .withNoErrorDetection()���R�����g�A�E�g����ƒʂ�悤�ɂȂ�
            // Logging.info("MyConnectionClosed! messageIdentifier="+messageIdentifier,"Push2Iphone");
            // push�����㏉�߂ăR�l�N�V�������؂��Ƃ���messageIdentifier�ɕs���g�[�N���̎��ʎq������B�i0�ȏ�j
            // messageIdentifier �� -1�ȉ��̂Ƃ��́A���ɃR�l�N�V�������؂�Ă���Ƃ��Ȃ̂ŁA��������B
            //
            if ( messageIdentifier > -1 )
            {
                ErrorIdentifier = messageIdentifier;

                // Connection�����
                if ( apnsService != null )
                {
                    apnsService.stop();
                }
            }

            // Logging.info( "[connectionClosed] code()=" + e.code() );
            // Logging.info( "[connectionClosed] ofCode(int code)=" + e.ofCode( e.code() ) );
            // Logging.info( "[connectionClosed] messageIdentifier()=" + messageIdentifier );

        }
    }

}
