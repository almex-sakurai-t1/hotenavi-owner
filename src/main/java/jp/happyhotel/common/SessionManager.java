package jp.happyhotel.common;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * �N�b�L�[���g�����ȈՃZ�b�V�����Ǘ��N���X
 *
 */
public class SessionManager
{
    /** �ȈՃZ�b�V�����ϐ��i�[�� */
    private static LinkedHashMap<String, Object> SES_MAP = new LinkedHashMap<String, Object>();
    /** �ȈՃZ�b�V�����ő�ێ��� */
    private static final int MAX_COUNT = 100;

    /**
     * �ȈՃZ�b�V�����ǉ�����
     * @param sessionId �Z�b�V����ID
     * @param obj �I�u�W�F�N�g
     */
    public static void addSession(String sessionId, Object obj)
    {
        try
        {
            boolean isExistKey = SES_MAP.containsKey(sessionId);

            if(isExistKey == false && SES_MAP.size() >= MAX_COUNT)
            {
                // �V�K�ɒǉ����镪1�ŌẪZ�b�V�������폜����
                for (Entry<String, Object> entry : SES_MAP.entrySet()) {
                    SES_MAP.remove(entry);
                    break;
                }
            }
            SES_MAP.put(sessionId, obj);
        }
        catch(Exception ex)
        {
            Logging.error( "[SessionManager addSession]Exception:" + ex.toString() );
        }
        finally
        {

        }

        return;
    }
    /**
     * �ȈՃZ�b�V�����擾����
     * @param sessionId �Z�b�V����ID
     * @return obj �I�u�W�F�N�g
     */
    public static Object getAttribute(String key)
    {
        Object ret = null;

        try
        {
            if(SES_MAP.containsKey(key) == true)
            {
                ret = SES_MAP.get(key);
            }
        }
        catch(Exception ex)
        {
            Logging.error( "[SessionManager getAttribute]Exception:" + ex.toString() );
        }
        finally
        {

        }

        return(ret);
    }


}
