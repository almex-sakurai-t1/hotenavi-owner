package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CheckMailAddr implements Serializable
{
    /**
     * メールアドレスが既に登録されているかチェックする
     * 
     * @param userId ユーザID
     * @param mailAddr メールアドレス
     * @return true:登録済み false:未登録
     */
    private static final long serialVersionUID = -3258253319220844948L;

    public static boolean IsRegistedMailAddr(String userId, String mailAddr) throws Exception
    {
        if ( mailAddr.indexOf( "@" ) != -1 ) // メールアドレス箇所に""がセットされるとスロークエリになってしまう
        {
            Connection connection = null;
            ResultSet result = null;
            PreparedStatement prestate = null;

            // メールアドレスの登録状況をチェックする。
            StringBuilder query = new StringBuilder();
            query.append( "SELECT * FROM hh_user_basic WHERE mail_addr = ?" );
            query.append( " AND user_id <> ?" );
            query.append( " AND del_flag=0" );
            query.append( " UNION" );
            query.append( " SELECT * FROM hh_user_basic WHERE mail_addr_mobile = ?" );
            query.append( " AND user_id <> ?" );
            query.append( " AND del_flag=0" );

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query.toString() );
                int i = 1;
                prestate.setString( i++, mailAddr );
                prestate.setString( i++, userId );
                prestate.setString( i++, mailAddr );
                prestate.setString( i++, userId );
                result = prestate.executeQuery();

                if ( result.next() != false )
                {
                    return true;
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[CheckMailAddr.IsRegistMailAddr] Exception=" + e.toString() );
                throw e;
            }
            finally
            {
                result.close();
                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( connection );
            }
        }
        return false;
    }

    /**
     * PCメールか携帯メールかチェックする
     * 
     * @param メールアドレス
     * @return チェック結果 ( -1:異常　1:PCメアド　2:携帯メアド )
     */
    public static int checkMailKind(String mailAddr)
    {

        int result = -1;

        // メールアドレスとして有効な文字列かどうかを事前に確認
        if ( CheckString.mailaddrCheck( mailAddr ) == false )
        {
            return result;
        }

        if ( mailAddr.indexOf( "@docomo.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@disney.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@ezweb.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@au.com" ) == -1 &&
                mailAddr.indexOf( "@softbank.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@t.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@d.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@h.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@c.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@k.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@r.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@n.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@s.vodafone.ne.jp" ) == -1 &&
                mailAddr.indexOf( "@q.vodafone.ne.jp" ) == -1 )
        {
            result = 1;
        }
        else
        {
            result = 2;
        }
        return result;
    }
}
