package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.Logging;
import jp.happyhotel.util.AccessToken;

import org.apache.commons.lang.StringUtils;

/**
 * �^�b�`�p���[�U�F��
 * 
 * �A�v������̃A�N�Z�XToken�Ɋ�Â����[�U�F�؂��s��
 */
public class UserAuth implements Serializable
{
    public static String getUserId(String accessToken)
    {
        String userId = "";
        accessToken = StringUtils.defaultIfEmpty( accessToken, "" );

        // �g�[�N�����󂯎�����ꍇ�A�g�[�N���ɐݒ肳�ꂽ���[�UID�Ƃ��̃��[�U�̃p�X���[�h��ϐ��ɐݒ�
        if ( accessToken.equals( "" ) == false )
        {
            // �g�[�N���̌��؂Ɏ��s
            if ( AccessToken.verify( accessToken ) == false )
            {
                Logging.warn( "token verification failed. (token: " + accessToken + ")" );
            }

            // �g�[�N���̗L�������؂�
            if ( AccessToken.isWithinExpirationTime( accessToken ) == false )
            {
                Logging.warn( "token has expired. (token: " + accessToken + ")" );
            }

            userId = AccessToken.getUserId( accessToken );
        }
        return userId;
    }

}
