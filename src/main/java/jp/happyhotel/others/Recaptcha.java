package jp.happyhotel.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jp.happyhotel.common.Logging;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * reCAPTCHA���ؗp�N���X<br>
 * <br>
 * Google���񋟂���ureCAPTCHA�v�̗��p���Ɏg�p����N���X�ł��B<br>
 * reCAPTCHA�̔F�؂ɂ�萶�����ꂽ�R�[�h���A���������̂��ǂ��������؂��܂��B<br>
 * 
 * @author koshiba-y1
 * @version 1.00 2017/10/02
 * @see <a href="https://developers.google.com/recaptcha/intro">reCAPTCHA Developer's Guide<a>
 */
public class Recaptcha
{
    /** POST���N�G�X�g���M�p�C���i�[�N���X */
    private static class PostRequest
    {
        /**
         * �R���X�g���N�^
         */
        private PostRequest()
        {
        }

        /**
         * POST���N�G�X�g���M<br>
         * <br>
         * POST���N�G�X�g�𑗐M���܂��B<br>
         * 
         * @param url ���M���URL
         * @param contentType �R���e�L�X�g�^�C�v�Ɏw�肷�镶����
         * @param params �p�����[�^���ƒl���i�[�����}�b�v
         * @return POST���N�G�X�g��������
         * @throws IOException
         */
        public static String sendRequest(final String url, final String contentType, final String requestParam) throws IOException
        {
            HttpURLConnection conn = (HttpURLConnection)(new URL( url ).openConnection());
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", contentType );
            conn.setDoOutput( true );

            OutputStreamWriter outputStream = new OutputStreamWriter( conn.getOutputStream() );
            outputStream.write( requestParam );
            outputStream.close();

            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );
            StringBuilder strBuilder = new StringBuilder();

            String line;
            while( (line = reader.readLine()) != null )
            {
                strBuilder.append( line );
            }
            reader.close();
            inputStream.close();
            conn.disconnect();

            return strBuilder.toString();
        }

        /**
         * ���N�G�X�g�p�����[�^����<br>
         * <br>
         * ���N�G�X�g�ɕt�����邽�߂̃p�����[�^�𐶐����܂��B<br>
         * ���N�G�X�g�p�����[�^�́A�p�����[�^���ƒl��"="�Ō������A������"&"�ŘA���������̂ƂȂ�܂��B<br>
         * 
         * @param params �p�����[�^���ƒl���i�[�����}�b�v
         * @return ���N�G�X�g�p�����[�^
         */
        public static String generateRequestParam(final Map<String, String> params)
        {
            StringBuilder strBuilder = new StringBuilder();

            for( Map.Entry<String, String> param : params.entrySet() )
            {
                if ( strBuilder.length() > 0 )
                {
                    strBuilder.append( "&" );
                }
                strBuilder.append( param.getKey() + "=" + param.getValue() );
            }

            return strBuilder.toString();
        }
    }

    /** ���J�� */
    private static final String siteKey  = "6LcWPDIUAAAAADMf019p1fuEwwL-sY3pOiLaHFMi";

    /** �G���[���b�Z�[�W */
    private String              errorMsg = null;

    /**
     * �R���X�g���N�^
     */
    public Recaptcha()
    {
        this.errorMsg = "";
    }

    /**
     * siteKey�̃Q�b�^�[
     * 
     * @return siteKey�̒l
     */
    public static String getSiteKey()
    {
        return Recaptcha.siteKey;
    }

    /**
     * errorMsg�̃Q�b�^�[
     * 
     * @return errorMsg�̒l
     */
    public String getErrorMsg()
    {
        return this.errorMsg;
    }

    /**
     * ���؏���<br>
     * <br>
     * Google����POST���\�b�h�𓊂��A�L���v�`�����������s��ꂽ���ǂ��������؂��Ă��炢�܂��B<br>
     * 
     * @param gRecaptchaResponse g-recaptcha-response�p�����[�^����擾����������
     * @return ���،��ʂ�����JSON�X�g�����O
     * @throws IOException
     */
    private static String verify(final String gRecaptchaResponse) throws IOException
    {
        final String secretKey = "6LcWPDIUAAAAAOkGzGSlf5HY2PrJoQ-L2IQmSiGt";

        Map<String, String> params = new HashMap<String, String>();
        params.put( "secret", secretKey );
        params.put( "response", gRecaptchaResponse );

        return PostRequest.sendRequest(
                "https://www.google.com/recaptcha/api/siteverify",
                "application/x-www-form-urlencoded",
                PostRequest.generateRequestParam( params ) );
    }

    /**
     * ���b�Z�[�W���o<br>
     * <br>
     * JSONArray�Ɋi�[���ꂽ��������A�J���}�{�X�y�[�X��؂�̕�����ɕϊ����܂��B<br>
     * 
     * @param msgs ��������i�[����JSONArray
     * @return �z��Ɋi�[���ꂽ��������A�J���}�{�X�y�[�X��؂�Ō�������������
     */
    private static String extractMsgs(final JSONArray msgs)
    {
        StringBuilder strBuilder = new StringBuilder();
        for( int i = 0 ; i < msgs.length() ; i++ )
        {
            if ( strBuilder.length() > 0 )
            {
                strBuilder.append( ", " );
            }
            strBuilder.append( msgs.getString( i ) );
        }

        return strBuilder.toString();
    }

    /**
     * �L���v�`���̌���<br>
     * <br>
     * �L���v�`�����������s��ꂽ���ǂ��������؂��܂��B<br>
     * ��������g-recaptcha-response�p�����[�^����擾�����������^���Ă��������B<br>
     * ���؂̌��ʁA�������s��ꂽ�ꍇ��true���A�������s���Ȃ������ꍇ��false��Ԃ��܂��B<br>
     * ���،��ʂ�false�̏ꍇ�AerrorMsg�ɃG���[�̓��e���L�^����܂��B<br>
     * 
     * @param gRecaptchaResponse g-recaptcha-response�p�����[�^����擾����������
     * @return ���،��ʂ������u�[���l
     */
    public boolean verifyCapture(final String gRecaptchaResponse)
    {
        // �G���[���b�Z�[�W�̏�����
        this.errorMsg = "";

        // �󕶎�����
        if ( StringUtils.isEmpty( gRecaptchaResponse ) )
        {
            this.errorMsg = "input is empty";
            Logging.warn( "[Recaptcha.verifyCapture] " + getErrorMsg() );

            return false;
        }

        // �L���v�`�����������s��ꂽ������
        String jsonStr;
        try
        {
            jsonStr = Recaptcha.verify( gRecaptchaResponse );
        }
        catch ( IOException e )
        {
            this.errorMsg = "post request error";
            Logging.error( "[Recaptcha.verify] Exception=" + e.toString() );

            return false;
        }

        // ���X�|���XJSON���
        JSONObject jsonObj = JSONObject.fromObject( jsonStr );
        boolean success = (Boolean)(jsonObj.get( "success" ));
        if ( !success )
        {
            JSONArray errorMsgs = (JSONArray)(jsonObj.get( "error-codes" ));
            this.errorMsg = Recaptcha.extractMsgs( errorMsgs );
            Logging.warn( "[Recaptcha.verifyCapture] error-codes: " + getErrorMsg() );
        }

        return success;
    }
}
