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
     * �I�[�i�[�֘A�f�[�^�폜����
     * 
     * @param id �z�e��ID
     * @return �������ʁitrue:�����Afalse:���s�j
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
            // �ғ������ǂ������m�F����
            ret = dhn.getData( id );
            if ( ret != false )
            {
                ret = false;
                if ( dhn.getDateStart() > 0 )
                {
                    this.errMsg += "���ɖ{�ғ����Ă��܂��B<br>�f�[�^���폜�ł��܂���<br>";
                }
                else if ( dhn.getDateStart() == 0 )
                {
                    // �f�[�^�̍폜�Ɏ��|����
                    this.boolCi = hc.deleteCiData( id );
                    if ( this.boolCi == false )
                    {
                        this.errMsgCi += "�`�F�b�N�C���f�[�^���폜�ł��܂���ł���";
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
                    this.errMsg += "�z�e�������X�f�[�^���擾�ł��܂���ł���<br>";
                }
            }
            else
            {
                this.errMsg += "�z�e�������X�f�[�^���擾�ł��܂���ł���<br>";
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
