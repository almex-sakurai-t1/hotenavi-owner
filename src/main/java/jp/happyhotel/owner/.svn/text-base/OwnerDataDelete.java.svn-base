package jp.happyhotel.owner;

import java.io.Serializable;

import jp.happyhotel.data.DataHotelNewhappie;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.user.UserPointDelete;

public class OwnerDataDelete implements Serializable
{

    public boolean boolCi         = false;
    public boolean boolBko        = false;
    public boolean boolUserPoint  = false;
    public String  errMsg         = "";
    public String  errMsgCi       = "";
    public String  errMsgBko      = "";
    public String  errMsgPointPay = "";

    public boolean isDeleteCi()
    {
        return boolCi;
    }

    public boolean isDeleterBko()
    {
        return boolBko;
    }

    public boolean isDeletePointPay()
    {
        return boolUserPoint;
    }

    public String getErrorMessage()
    {
        return errMsg;
    }

    public String getErrorMessageCi()
    {
        return errMsgCi;
    }

    public String getErrorMessageBko()
    {
        return errMsgBko;
    }

    public String getErrorMessagePointPay()
    {
        return errMsgPointPay;
    }

    /***
     * オーナー関連データ削除処理
     * 
     * @param id ホテルID
     * @return 処理結果（true:成功、false:失敗）
     */
    public boolean deleteData(int id)
    {
        boolean ret = false;
        DataHotelNewhappie dhn;
        HotelCi hc;
        BkoDataDelete bko;
        UserPointDelete upd;

        dhn = new DataHotelNewhappie();
        hc = new HotelCi();
        bko = new BkoDataDelete();
        upd = new UserPointDelete();

        try
        {
            // 稼働日かどうかを確認する
            ret = dhn.getData( id );
            if ( ret != false )
            {
                ret = false;
                if ( dhn.getDateStart() > 0 )
                {
                    this.errMsg += "既に本稼動しています。<br>データを削除できません<br>";
                }
                else if ( dhn.getDateStart() == 0 )
                {
                    // データの削除に取り掛かる
                    this.boolCi = hc.deleteCiData( id );
                    if ( this.boolCi == false )
                    {
                        this.errMsgCi += "チェックインデータを削除できませんでした";
                    }
                    this.boolBko = bko.deleteData( id );
                    this.errMsgBko = bko.getErrorMessage();

                    // this.boolUserPoint = upd.deleteData( id );
                    // this.errMsgPointPay = upd.getErrorMessage();
                    this.boolUserPoint = true;

                    if ( (this.boolCi != false) && (this.boolBko != false) && (this.boolUserPoint != false) )
                    {
                        ret = true;
                    }
                }
                else
                {
                    this.errMsg += "ホテル加盟店データを取得できませんでした<br>";
                }
            }
            else
            {
                this.errMsg += "ホテル加盟店データを取得できませんでした<br>";
            }
        }
        catch ( Exception e )
        {
        }
        finally
        {
        }

        return ret;
    }
}
