package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DBSync;
import jp.happyhotel.common.Logging;

public class DataHotelSearch implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 6675605603241371219L;
    private int               id;
    private String            word;
    private int               kind;

    public DataHotelSearch()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public int getKind()
    {
        return kind;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    /**
     * �z�e���������擾
     * 
     * @param id �z�e��ID
     * @param word ���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int id, String word)
    {
        boolean ret;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        final String query = "SELECT * FROM hh_hotel_search WHERE id = ? and word = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, word );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.word = result.getString( "word" );
                    this.kind = result.getInt( "kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �z�e���������ݒ�
     * 
     * @param result �z�e���������f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.word = result.getString( "word" );
                this.kind = result.getInt( "kind" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * �z�e���������f�[�^�ǉ�
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        final String query = "INSERT  hotenavi.hh_hotel_search SET "
                + " id = ?,"
                + " word = ?,"
                + " kind = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, id );
            prestate.setString( 2, word );
            prestate.setInt( 3, kind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        if ( ret != false )
        {
        }
        return(ret);
    }

    /**
     * �z�e���������f�[�^�ύX
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(int id, String word)
    {
        word = word.length() > 255 ? word.substring( 0, 255 ) : word; // 255���𒴂����ꍇ�ɐ擪����255�����݂̂ɂ���

        int result;
        boolean ret;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        final String query = "UPDATE  hotenavi.hh_hotel_search SET "
                + " word = ?"
                + " WHERE id = ? AND kind = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, word );
            prestate.setInt( 2, id );
            prestate.setInt( 3, this.kind );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.updateData] Exception=" + e.toString() );
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
     * �z�e���������f�[�^�ύX
     * 
     * @param id �z�e��ID
     * @param word �L�[���[�h
     * @param kind �L�[���[�h���
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(int id, String word, int kind)
    {
        word = word.length() > 255 ? word.substring( 0, 255 ) : word; // 255���𒴂����ꍇ�ɐ擪����255�����݂̂ɂ���
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            if ( isData( connection, id, kind ) )
            {
                if ( word.equals( "" ) )
                {
                    ret = deleteData( connection, id, kind );
                }
                else
                {
                    ret = updateData( connection, id, word, kind );
                }
            }
            else if ( !word.equals( "" ) )
            {
                ret = insertData( connection, id, word, kind );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �z�e�������L�[���[�h�L���擾
     * 
     * @param id �z�e��ID
     * @param kind ���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean isData(Connection connection, int id, int kind)
    {
        boolean ret = false;
        ResultSet result = null;
        PreparedStatement prestate = null;

        final String query = "SELECT 1 FROM hh_hotel_search WHERE id = ? AND kind = ?";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, kind );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.isData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �z�e���������f�[�^�ǉ�
     * 
     * @param id �z�e��ID
     * @param word �L�[���[�h
     * @param kind �L�[���[�h���
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData(Connection connection, int id, String word, int kind)
    {
        word = word.length() > 255 ? word.substring( 0, 255 ) : word; // 255���𒴂����ꍇ�ɐ擪����255�����݂̂ɂ���
        int result;
        boolean ret;
        PreparedStatement prestate = null;

        ret = false;

        final String query = "INSERT  hotenavi.hh_hotel_search SET "
                + " id = ?,"
                + " word = ?,"
                + " kind = ?";

        try
        {
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, id );
            prestate.setString( 2, word );
            prestate.setInt( 3, kind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �z�e���������f�[�^�ύX
     * 
     * @param id �z�e��ID
     * @param word �L�[���[�h
     * @param kind �L�[���[�h���
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(Connection connection, int id, String word, int kind)
    {
        word = word.length() > 255 ? word.substring( 0, 255 ) : word; // 255���𒴂����ꍇ�ɐ擪����255�����݂̂ɂ���
        int result;
        boolean ret = false;
        PreparedStatement prestate = null;

        final String query = "UPDATE hotenavi.hh_hotel_search SET "
                + " word = ?"
                + " WHERE id = ? AND kind = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, word );
            prestate.setInt( 2, id );
            prestate.setInt( 3, kind );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �z�e���������f�[�^�폜
     * 
     * @param id �z�e��ID
     * @param kind �L�[���[�h���
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean deleteData(Connection connection, int id, int kind)
    {
        int result;
        boolean ret = false;
        PreparedStatement prestate = null;

        final String query = "DELETE FROM hotenavi.hh_hotel_search"
                + " WHERE id = ? AND kind = ?";

        try
        {
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, id );
            prestate.setInt( 2, kind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSearch.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

}
