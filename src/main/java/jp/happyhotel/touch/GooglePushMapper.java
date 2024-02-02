package jp.happyhotel.touch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GooglePushMapper implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -2931541288552745139L;

    /**
     * 
     * @see このクラスで定義する変数は、JSONへそのまま変換されるため注意してください。
     *      Google Play Servicesで指定しているフィールドと異なる名前を定義すると、400エラーになります。
     *      ※registration_idsとdataは変数名を変更しない。
     */

    public List<String>        registration_ids;
    public Map<String, String> notification;
    public Map<String, String> data;

    public GooglePushMapper()
    {
        registration_ids = null;
        notification = null;
        data = null;
    }

    // ArrayListへの追加
    public void addRegId(String regId)
    {
        if ( registration_ids == null )
        {
            registration_ids = new LinkedList<String>();
        }
        // regIdがカンマ区切りの場合、分解してpush
        String[] token = regId.split( ",", 0 );
        for( int i = 0 ; i < token.length ; i++ )
        {
            registration_ids.add( token[i] );
        }
    }

    //
    public void setRegId(LinkedList<String> regIdList)
    {
        registration_ids = regIdList;
    }

    public void createData(String message, String url)
    {
        if ( notification == null )
        {
            notification = new HashMap<String, String>();
        }
        // メッセージをセット
        notification.put( "body", message );

        // URLの指定がある場合はURLをセット
        if ( url.equals( "" ) == false )
        {
            data = new HashMap<String, String>();
            data.put( "url", url );
        }
    }
}
