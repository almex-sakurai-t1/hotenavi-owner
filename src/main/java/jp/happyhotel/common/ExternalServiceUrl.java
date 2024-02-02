package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.data.DataHotelUrl;

/**
 * �O���T�[�r�XURL�Ǘ��p�N���X<br>
 * <br>
 * �O���T�[�r�X�iFacebook�AGoogleMap�ALINE@�Aetc...�j��URL���Ǘ�����N���X�ł��B<br>
 * DB�Ƀf�[�^��o�^������ADB����f�[�^�����o�����肷��@�\��񋟂��܂��B<br>
 * 
 * @author koshiba-y1
 */
public class ExternalServiceUrl extends DataHotelUrl implements Serializable
{
    /**
     * �R���X�g���N�^
     */
    public ExternalServiceUrl()
    {
        // ���ɂȂ�
    }

    /**
     * �R���X�g���N�^
     * 
     * @param id �z�e��ID
     */
    public ExternalServiceUrl(int id)
    {
        this.id = id;
    }

    /**
     * �C���X�^���X�ϐ��̏�����
     */
    private void initInstanceVariable()
    {
        this.id = 0;
        this.seq = 0;
        this.url = "";
        this.dataType = 0;
        this.delFlag = 0;
        this.startDate = 0;
        this.endDate = 0;
    }

    /**
     * �N�G���̎��s<br>
     * <br>
     * hh_hotel_url�Ɋւ���SELECT�������s���A���ʂ��C���X�^���X�ϐ��ɏ������݂܂��B<br>
     * ���ʂ��擾�ł����ꍇ��true���A�擾�ł��Ȃ������ꍇ��false��Ԃ��܂��B<br>
     * 
     * @param query ���s����N�G��
     * @return ���s����
     * @throws Exception
     */
    private boolean executeQuery(String query) throws Exception
    {
        // DB�A�N�Z�X�֘A
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result == null )
            {
                initInstanceVariable();
                return false;
            }

            if ( result.next() == false )
            {
                initInstanceVariable();
                return false;
            }

            this.id = result.getInt( "id" );
            this.seq = result.getInt( "seq" );
            this.url = result.getString( "url" );
            this.dataType = result.getInt( "data_type" );
            this.delFlag = result.getInt( "del_flag" );
            this.startDate = result.getInt( "start_date" );
            this.endDate = result.getInt( "end_date" );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag�̒l�͍l�����܂���B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @param now_date �X�V���Ƃ��ċL�^������t�iYYYYMMDD�j
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectUrlRow(int id, int data_type, int now_date) throws Exception
    {
        // �N�G������
        String query = "";
        query += "SELECT * FROM hh_hotel_url WHERE id=" + id;
        query += " AND data_type = " + data_type;
        query += " AND start_date <= " + now_date;
        query += " AND end_date >= " + now_date;
        query += " ORDER BY seq DESC";

        // �N�G���̎��s
        try
        {
            if ( this.executeQuery( query ) == false )
            {
                return "";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.selectUrlRow] Exception=" + e.toString() );
            throw e;
        }

        return this.url;
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag�̒l�͍l�����܂���B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectUrlRow(int id, int data_type) throws Exception
    {
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        return this.selectUrlRow( id, data_type, now_date );
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag�̒l�͍l�����܂���B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param data_type �T�[�r�X��\�����ʎq
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectUrlRow(int data_type) throws Exception
    {
        return this.selectUrlRow( this.id, data_type );
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag��0�̂��̂Ɋւ��Ď擾���܂��B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @param now_date �X�V���Ƃ��ċL�^������t�iYYYYMMDD�j
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectNotDeletedUrlRow(int id, int data_type, int now_date) throws Exception
    {
        // �N�G������
        String query = "";
        query += "SELECT * FROM hh_hotel_url WHERE id=" + id;
        query += " AND data_type = " + data_type;
        query += " AND start_date <= " + now_date;
        query += " AND end_date >= " + now_date;
        query += " AND del_flag = 0";
        query += " ORDER BY seq DESC";

        // �N�G���̎��s
        try
        {
            if ( this.executeQuery( query ) == false )
            {
                return "";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.selectNotDeletedUrlRow] Exception=" + e.toString() );
            throw e;
        }

        return this.url;
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag��0�̂��̂Ɋւ��Ď擾���܂��B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectNotDeletedUrlRow(int id, int data_type) throws Exception
    {
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        return this.selectNotDeletedUrlRow( id, data_type, now_date );
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag��0�̂��̂Ɋւ��Ď擾���܂��B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param data_type �T�[�r�X��\�����ʎq
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectNotDeletedUrlRow(int data_type) throws Exception
    {
        return this.selectNotDeletedUrlRow( this.id, data_type );
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag��1�̂��̂Ɋւ��Ď擾���܂��B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @param now_date �X�V���Ƃ��ċL�^������t�iYYYYMMDD�j
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectDeletedUrlRow(int id, int data_type, int now_date) throws Exception
    {
        // �N�G������
        String query = "";
        query += "SELECT * FROM hh_hotel_url WHERE id=" + id;
        query += " AND data_type = " + data_type;
        query += " AND start_date <= " + now_date;
        query += " AND end_date >= " + now_date;
        query += " AND del_flag = 1";
        query += " ORDER BY seq DESC";

        // �N�G���̎��s
        try
        {
            if ( this.executeQuery( query ) == false )
            {
                return "";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.selectDeletedUrlRow] Exception=" + e.toString() );
            throw e;
        }

        return this.url;
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag��1�̂��̂Ɋւ��Ď擾���܂��B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectDeletedUrlRow(int id, int data_type) throws Exception
    {
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        return this.selectDeletedUrlRow( id, data_type, now_date );
    }

    /**
     * URL�i���܂ލs�f�[�^�j�̎擾����<br>
     * <br>
     * DB�ihh_hotel_url�j����Y������URL���܂ލs�f�[�^���擾���AURL��Ԃ��܂��B<br>
     * del_flag��1�̂��̂Ɋւ��Ď擾���܂��B<br>
     * �s�����݂��Ȃ������ꍇ�͋󕶎���Ԃ��܂��B<br>
     * �s�̃f�[�^�̓C���X�^���X�ϐ��Ɋi�[����܂��B<br>
     * 
     * @param data_type �T�[�r�X��\�����ʎq
     * @return �O���T�[�r�X��URL
     * @throws Exception
     */
    public String selectDeletedUrlRow(int data_type) throws Exception
    {
        return this.selectDeletedUrlRow( this.id, data_type );
    }

    /**
     * URL�̑}������<br>
     * <br>
     * DB�ihh_hotel_url�j��URL�̃f�[�^��}�����܂��B<br>
     * del_flag��1�ŏ������݂܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @param url �T�[�r�X��URL
     * @param now_date �X�V���Ƃ��ċL�^������t�iYYYYMMDD�j
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int id, int data_type, String url, int now_date) throws Exception
    {
        // �p�����[�^�̑��
        this.id = id;
        this.url = url;
        this.dataType = data_type;
        this.delFlag = 1;
        this.startDate = now_date;
        this.endDate = 29991231;

        // �X�V�������s
        if ( this.insertData() == false )
        {
            throw new Exception( "URL�̑}�������Ɏ��s���܂����B" );
        }
    }

    /**
     * URL�̑}������<br>
     * <br>
     * DB�ihh_hotel_url�j��URL�̃f�[�^��}�����܂��B<br>
     * del_flag��1�ŏ������݂܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @param url �T�[�r�X��URL
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int id, int data_type, String url) throws Exception
    {
        // �X�V���t�Ƃ��ċL�^���邽�߂̌��ݓ������擾
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        this.insertUrlWithDelFlag( id, data_type, url, now_date );
    }

    /**
     * URL�̑}������<br>
     * <br>
     * DB�ihh_hotel_url�j��URL�̃f�[�^��}�����܂��B<br>
     * del_flag��1�ŏ������݂܂��B<br>
     * 
     * @param data_type �T�[�r�X��\�����ʎq
     * @param url �T�[�r�X��URL
     * @param now_date �X�V���Ƃ��ċL�^������t�iYYYYMMDD�j
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int data_type, String url, int now_date) throws Exception
    {
        this.insertUrlWithDelFlag( this.id, data_type, url, now_date );
    }

    /**
     * URL�̑}������<br>
     * <br>
     * DB�ihh_hotel_url�j��URL�̃f�[�^��}�����܂��B<br>
     * del_flag��1�ŏ������݂܂��B<br>
     * 
     * @param data_type �T�[�r�X��\�����ʎq
     * @param url �T�[�r�X��URL
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int data_type, String url) throws Exception
    {
        this.insertUrlWithDelFlag( this.id, data_type, url );
    }

    /**
     * �Ή�����s��del_flag�̍X�V����<br>
     * <br>
     * DB�ihh_hotel_url�j�̃f�[�^���X�V���܂��B<br>
     * del_flag��0�ɏ����ς��܂��B<br>
     * 
     * @param id �z�e��ID
     * @param seq �V�[�P���X�ԍ�
     * @param now_date �X�V���Ƃ��ċL�^������t�iYYYYMMDD�j
     * @throws Exception
     */
    public void updateToNotDeletedUrl(int id, int seq, int now_date) throws Exception
    {
        // �N�G������
        String query = "";
        query += "UPDATE hh_hotel_url SET ";
        query += " del_flag = 0";
        query += ", start_date = " + now_date;
        query += " WHERE id = " + id;
        query += " AND seq = " + seq;

        // �N�G�����s
        Connection connection = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.updateToNotDeletedUrl] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * �Ή�����s��del_flag�̍X�V����<br>
     * <br>
     * DB�ihh_hotel_url�j�̃f�[�^���X�V���܂��B<br>
     * del_flag��0�ɏ����ς��܂��B<br>
     * 
     * @param id �z�e��ID
     * @param seq �V�[�P���X�ԍ�
     * @throws Exception
     */
    public void updateToNotDeletedUrl(int id, int seq) throws Exception
    {
        // �X�V���t�Ƃ��ċL�^���邽�߂̌��ݓ������擾
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        this.updateToNotDeletedUrl( id, seq, now_date );
    }

    /**
     * �Ή�����s��del_flag�̍X�V����<br>
     * <br>
     * DB�ihh_hotel_url�j�̃f�[�^���X�V���܂��B<br>
     * del_flag��0�ɏ����ς��܂��B<br>
     * 
     * @param seq �V�[�P���X�ԍ�
     * @throws Exception
     */
    public void updateToNotDeletedUrl(int seq) throws Exception
    {
        this.updateToNotDeletedUrl( this.id, seq );
    }

    /**
     * �Ή�����s��del_flag�̍X�V����<br>
     * <br>
     * DB�ihh_hotel_url�j�̃f�[�^���X�V���܂��B<br>
     * del_flag��1�ɏ����ς��܂��B<br>
     * 
     * @param id �z�e��ID
     * @param seq �V�[�P���X�ԍ�
     * @param now_date �X�V���Ƃ��ċL�^������t�iYYYYMMDD�j
     * @throws Exception
     */
    public void updateToDeletedUrl(int id, int seq, int now_date) throws Exception
    {
        // �N�G������
        String query = "";
        query += "UPDATE hh_hotel_url SET ";
        query += " del_flag = 1";
        query += ", end_date = " + now_date;
        query += " WHERE id = " + id;
        query += " AND seq = " + seq;

        // �N�G�����s
        Connection connection = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.updateToDeletedUrl] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * �Ή�����s��del_flag�̍X�V����<br>
     * <br>
     * DB�ihh_hotel_url�j�̃f�[�^���X�V���܂��B<br>
     * del_flag��1�ɏ����ς��܂��B<br>
     * 
     * @param id �z�e��ID
     * @param seq �V�[�P���X�ԍ�
     * @throws Exception
     */
    public void updateToDeletedUrl(int id, int seq) throws Exception
    {
        // �X�V���t�Ƃ��ċL�^���邽�߂̌��ݓ������擾
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        this.updateToDeletedUrl( id, seq, now_date );
    }

    /**
     * �Ή�����s��del_flag�̍X�V����<br>
     * <br>
     * DB�ihh_hotel_url�j�̃f�[�^���X�V���܂��B<br>
     * del_flag��1�ɏ����ς��܂��B<br>
     * 
     * @param seq �V�[�P���X�ԍ�
     * @throws Exception
     */
    public void updateToDeletedUrl(int seq) throws Exception
    {
        this.updateToDeletedUrl( this.id, seq );
    }

    /**
     * �f�[�^�̍폜<br>
     * <br>
     * �w�肵���O���T�[�r�X�ɂ����āA�w�肵���V�[�P���X�ԍ����傫���f�[�^���폜���܂��B<br>
     * 
     * @param id �z�e��ID
     * @param data_type �T�[�r�X��\�����ʎq
     * @param seq �V�[�P���X�ԍ�
     * @throws Exception
     */
    public void deleteOverSeqRows(int id, int data_type, int seq) throws Exception
    {
        // �N�G������
        String query = "";
        query += "DELETE FROM hh_hotel_url ";
        query += " WHERE id = " + id;
        query += " AND data_type = " + data_type;
        query += " AND seq > " + seq;

        // �N�G�����s
        Connection connection = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.deleteOverSeqRows] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * �f�[�^�̍폜<br>
     * <br>
     * �w�肵���O���T�[�r�X�ɂ����āA�w�肵���V�[�P���X�ԍ����傫���f�[�^���폜���܂��B<br>
     * 
     * @param data_type �T�[�r�X��\�����ʎq
     * @param seq �V�[�P���X�ԍ�
     * @throws Exception
     */
    public void deleteOverSeqRows(int data_type, int seq) throws Exception
    {
        this.deleteOverSeqRows( this.id, data_type, seq );
    }
}
