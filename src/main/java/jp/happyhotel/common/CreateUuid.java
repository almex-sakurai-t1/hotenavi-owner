/*
 * @(#)RandomString.java 1.00
 * 2009/07/15 Copyright (C) ALMEX Inc. 2009
 * �����_��������擾�N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.util.UUID;

import jp.happyhotel.data.DataApUuid;

/**
 * UUID�쐬�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2014/12/14
 */
public class CreateUuid implements Serializable
{

    /**
     * UUID���쐬
     * 
     * @param digit �����_��������̌���
     * @return ��������("":���s)
     */
    static public String create()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * �d�����Ă��Ȃ�UUID��Ԃ��B
     * 
     * @return ��������("":���s)
     */
    public static String getUuid()
    {
        String uuid = "";
        DataApUuid dau = new DataApUuid();

        dau = new DataApUuid();

        // ���̂Ȃ�UUID���������ꂽ��break
        while( true )
        {
            uuid = create();
            // ����UUID���o�^����Ă��Ȃ����OK
            if ( dau.getData( uuid ) == false )
            {
                break;
            }
        }
        return(uuid);
    }
}
