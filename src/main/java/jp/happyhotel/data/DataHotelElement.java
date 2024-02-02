package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �z�e���ʐݒ�t�@�C���擾�N���X
 * 
 * @author sakurai-t1
 * @version 1.00 2017/5/9
 */
public class DataHotelElement implements Serializable
{
    /**
     * 
     */
    private static final long  serialVersionUID = -4312153612808848009L;
    public static final String TABLE            = "hotel_element";
    private String             hotelId;                                 //
    private int                chkAgeFlg;                               // �N��m�F�t���O �i1:�N�b�V�����y�[�W����j
    private String             htmlHead;                                // HTML�w�b�_���
    private String             htmlLoginForm;                           // ���O�C���t�H�[��
    private String             offlineflg;                              // ���O�C���L���t���O�i0:�����o�[�y�[�W�Ȃ� 1:�����o�[�y�[�W����j
    private String             mailmagazineflg;                         // �����}�K�L���t���O�i0�F���� 1:�L�j
    private String             mailtoflg;                               // �z�e���ֈꌾ�L���t���O�i0�F���� 1:�L�j
    private String             mailnameflg;                             // �z�e���ֈꌾ�z�e�������L���i0�F�����Ƀz�e��������(default) 1:�����Ƀz�e�����L�j
    private String             mailname;                                // �z�e���ֈꌾ�z�e����
    private String             viewflg;                                 // 0:�ʏ�@1:�Q�ƃo�[�W���� 2:�Q�ƃo�[�W�����i�����o�[���j 9:�x�~��
    private String             bbsgroupflg;                             // ���X�܌f���̎g�p(0:�ʏ�@1:���X�܃o�[�W����)
    private String             prizeHotelid;                            // ���i�����p�z�e��ID�i�����͂̏ꍇ��hotel_id���g�p�j
    private int                couponMapFlg;                            // 0:Yahoo!MAP���g�p,1:�摜�t�@�C�����g�p
    private String             couponMapImg1;                           // �N�[�|���摜1
    private String             couponMapImg2;                           // �N�[�|���摜2
    private int                bbsTempFlg;                              // 0:�ʏ�f����,1:�����e�f���i�f���ǉ�����del_flag��1���Z�b�g�j
    private int                rankingHiddenFlg;                        // 0:�ʏ�,1:�����L���O�����o�͂��Ȃ�
    private int                ownercount;                              // �I�[�i�[�Y���[���B����
    private int                trialDate;                               // ���j���[�A���J�n���t(YYYYMMDD)�@
    private int                startDate;                               // �ғ����t(YYYYMMDD)�@

    /**
     * �f�[�^�����������܂��B
     */
    public DataHotelElement()
    {
        this.hotelId = "";
        this.chkAgeFlg = 0;
        this.htmlHead = "";
        this.htmlLoginForm = "";
        this.offlineflg = "";
        this.mailmagazineflg = "";
        this.mailtoflg = "";
        this.mailnameflg = "";
        this.mailname = "";
        this.viewflg = "";
        this.bbsgroupflg = "";
        this.prizeHotelid = "";
        this.couponMapFlg = 0;
        this.couponMapImg1 = "";
        this.couponMapImg2 = "";
        this.bbsTempFlg = 0;
        this.rankingHiddenFlg = 0;
        this.ownercount = 0;
        this.trialDate = 0;
        this.startDate = 0;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getChkAgeFlg()
    {
        return chkAgeFlg;
    }

    public String getHtmlHead()
    {
        return htmlHead;
    }

    public String getHtmlLoginForm()
    {
        return htmlLoginForm;
    }

    public String getOfflineflg()
    {
        return offlineflg;
    }

    public String getMailmagazineflg()
    {
        return mailmagazineflg;
    }

    public String getMailtoflg()
    {
        return mailtoflg;
    }

    public String getMailnameflg()
    {
        return mailnameflg;
    }

    public String getMailname()
    {
        return mailname;
    }

    public String getViewflg()
    {
        return viewflg;
    }

    public String getBbsgroupflg()
    {
        return bbsgroupflg;
    }

    public String getPrizeHotelid()
    {
        return prizeHotelid;
    }

    public int getCouponMapFlg()
    {
        return couponMapFlg;
    }

    public String getCouponMapImg1()
    {
        return couponMapImg1;
    }

    public String getCouponMapImg2()
    {
        return couponMapImg2;
    }

    public int getBbsTempFlg()
    {
        return bbsTempFlg;
    }

    public int getRankingHiddenFlg()
    {
        return rankingHiddenFlg;
    }

    public int getOwnercount()
    {
        return ownercount;
    }

    public int getTrialDate()
    {
        return trialDate;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setChkAgeFlg(int chkAgeFlg)
    {
        this.chkAgeFlg = chkAgeFlg;
    }

    public void setHtmlHead(String htmlHead)
    {
        this.htmlHead = htmlHead;
    }

    public void setHtmlLoginForm(String htmlLoginForm)
    {
        this.htmlLoginForm = htmlLoginForm;
    }

    public void setOfflineflg(String offlineflg)
    {
        this.offlineflg = offlineflg;
    }

    public void setMailmagazineflg(String mailmagazineflg)
    {
        this.mailmagazineflg = mailmagazineflg;
    }

    public void setMailtoflg(String mailtoflg)
    {
        this.mailtoflg = mailtoflg;
    }

    public void setMailnameflg(String mailnameflg)
    {
        this.mailnameflg = mailnameflg;
    }

    public void setMailname(String mailname)
    {
        this.mailname = mailname;
    }

    public void setViewflg(String viewflg)
    {
        this.viewflg = viewflg;
    }

    public void setBbsgroupflg(String bbsgroupflg)
    {
        this.bbsgroupflg = bbsgroupflg;
    }

    public void setPrizeHotelid(String prizeHotelid)
    {
        this.prizeHotelid = prizeHotelid;
    }

    public void setCouponMapFlg(int couponMapFlg)
    {
        this.couponMapFlg = couponMapFlg;
    }

    public void setCouponMapImg1(String couponMapImg1)
    {
        this.couponMapImg1 = couponMapImg1;
    }

    public void setCouponMapImg2(String couponMapImg2)
    {
        this.couponMapImg2 = couponMapImg2;
    }

    public void setBbsTempFlg(int bbsTempFlg)
    {
        this.bbsTempFlg = bbsTempFlg;
    }

    public void setRankingHiddenFlg(int rankingHiddenFlg)
    {
        this.rankingHiddenFlg = rankingHiddenFlg;
    }

    public void setOwnercount(int ownercount)
    {
        this.ownercount = ownercount;
    }

    public void setTrialDate(int trialDate)
    {
        this.trialDate = trialDate;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    /****
     * �z�e���ʐݒ�t�@�C���擾
     * 
     * @param hotelId
     * @return
     */
    public boolean getData(String hotelId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM hotenavi.hotel_element WHERE hotel_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �z�e���ʐݒ�t�@�C���ݒ�
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.hotelId = result.getString( "hotel_id" );
                this.chkAgeFlg = result.getInt( "chk_age_flg" );
                this.htmlHead = result.getString( "html_head" );
                this.htmlLoginForm = result.getString( "html_login_form" );
                this.offlineflg = result.getString( "offlineflg" );
                this.mailmagazineflg = result.getString( "mailmagazineflg" );
                this.mailtoflg = result.getString( "mailtoflg" );
                this.mailnameflg = result.getString( "mailnameflg" );
                this.mailname = result.getString( "mailname" );
                this.viewflg = result.getString( "viewflg" );
                this.bbsgroupflg = result.getString( "bbsgroupflg" );
                this.prizeHotelid = result.getString( "prize_hotelid" );
                this.couponMapFlg = result.getInt( "coupon_map_flg" );
                this.couponMapImg1 = result.getString( "coupon_map_img1" );
                this.couponMapImg2 = result.getString( "coupon_map_img2" );
                this.bbsTempFlg = result.getInt( "bbs_temp_flg" );
                this.rankingHiddenFlg = result.getInt( "ranking_hidden_flg" );
                this.ownercount = result.getInt( "ownercount" );
                this.trialDate = result.getInt( "trial_date" );
                this.startDate = result.getInt( "start_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �z�e���ʐݒ�t�@�C���}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT hotenavi.hotel_element SET ";
        query += " hotel_id=?";
        query += ", chk_age_flg=?";
        query += ", html_head=?";
        query += ", html_login_form=?";
        query += ", offlineflg=?";
        query += ", mailmagazineflg=?";
        query += ", mailtoflg=?";
        query += ", mailnameflg=?";
        query += ", mailname=?";
        query += ", viewflg=?";
        query += ", bbsgroupflg=?";
        query += ", prize_hotelid=?";
        query += ", coupon_map_flg=?";
        query += ", coupon_map_img1=?";
        query += ", coupon_map_img2=?";
        query += ", bbs_temp_flg=?";
        query += ", ranking_hidden_flg=?";
        query += ", ownercount=?";
        query += ", trial_date=?";
        query += ", start_date=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.chkAgeFlg );
            prestate.setString( i++, this.htmlHead );
            prestate.setString( i++, this.htmlLoginForm );
            prestate.setString( i++, this.offlineflg );
            prestate.setString( i++, this.mailmagazineflg );
            prestate.setString( i++, this.mailtoflg );
            prestate.setString( i++, this.mailnameflg );
            prestate.setString( i++, this.mailname );
            prestate.setString( i++, this.viewflg );
            prestate.setString( i++, this.bbsgroupflg );
            prestate.setString( i++, this.prizeHotelid );
            prestate.setInt( i++, this.couponMapFlg );
            prestate.setString( i++, this.couponMapImg1 );
            prestate.setString( i++, this.couponMapImg2 );
            prestate.setInt( i++, this.bbsTempFlg );
            prestate.setInt( i++, this.rankingHiddenFlg );
            prestate.setInt( i++, this.ownercount );
            prestate.setInt( i++, this.trialDate );
            prestate.setInt( i++, this.startDate );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �z�e���ʐݒ�t�@�C���X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param hotelId
     * @return
     */
    public boolean updateData(String hotelId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE hotenavi.hotel_element SET ";
        query += " chk_age_flg=?";
        query += ", html_head=?";
        query += ", html_login_form=?";
        query += ", offlineflg=?";
        query += ", mailmagazineflg=?";
        query += ", mailtoflg=?";
        query += ", mailnameflg=?";
        query += ", mailname=?";
        query += ", viewflg=?";
        query += ", bbsgroupflg=?";
        query += ", prize_hotelid=?";
        query += ", coupon_map_flg=?";
        query += ", coupon_map_img1=?";
        query += ", coupon_map_img2=?";
        query += ", bbs_temp_flg=?";
        query += ", ranking_hidden_flg=?";
        query += ", ownercount=?";
        query += ", trial_date=?";
        query += ", start_date=?";
        query += " WHERE hotel_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.chkAgeFlg );
            prestate.setString( i++, this.htmlHead );
            prestate.setString( i++, this.htmlLoginForm );
            prestate.setString( i++, this.offlineflg );
            prestate.setString( i++, this.mailmagazineflg );
            prestate.setString( i++, this.mailtoflg );
            prestate.setString( i++, this.mailnameflg );
            prestate.setString( i++, this.mailname );
            prestate.setString( i++, this.viewflg );
            prestate.setString( i++, this.bbsgroupflg );
            prestate.setString( i++, this.prizeHotelid );
            prestate.setInt( i++, this.couponMapFlg );
            prestate.setString( i++, this.couponMapImg1 );
            prestate.setString( i++, this.couponMapImg2 );
            prestate.setInt( i++, this.bbsTempFlg );
            prestate.setInt( i++, this.rankingHiddenFlg );
            prestate.setInt( i++, this.ownercount );
            prestate.setInt( i++, this.trialDate );
            prestate.setInt( i++, this.startDate );
            prestate.setString( i++, this.hotelId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
