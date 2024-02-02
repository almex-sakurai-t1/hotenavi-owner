/*
 * @(#)SearchSystemInfo.java 1.00 2009/01/07 Copyright (C) ALMEX Inc. 2009 �������[�h���f�[�^�擾�N���X
 */
package jp.happyhotel.search;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * �������[�h���f�[�^�擾�N���X
 * 
 * @author N.Ide
 * @version 1.00 2009/01/07
 */
public class SearchSystemFreeword implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = -4608809242232034748L;

    private int                  dataCount;
    private int                  maxSearchCount;
    private int                  minSearchCount;
    private DataSystemFreeword[] systemFreeword;

    /**
     * �f�[�^�����������܂��B
     */
    public SearchSystemFreeword()
    {
        dataCount = 0;
        maxSearchCount = 0;
        minSearchCount = 0;
        systemFreeword = new DataSystemFreeword[0];
    }

    public int getDataCount()
    {
        return dataCount;
    }

    public int getMaxSearchCount()
    {
        return maxSearchCount;
    }

    public int getMinSearchCount()
    {
        return minSearchCount;
    }

    public DataSystemFreeword[] getSystemFreeword()
    {
        return systemFreeword;
    }

    /**
     * �t���[���[�h�����Ǘ��f�[�^�ꗗ�擾(���t�w��A�����w��)
     * 
     * @param date ���t
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @param randomFlag �����_���t���O(TRUE:�����_����,FALSE:�����񐔏�)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getDataListByDate(int date, int countNum, int pageNum, boolean randomFlag)
    {
        int i;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String subquery;
        String[] searchWordList;
        int ngwordCount;
        String[] ngwordList;

        ret = false;

        // NG���[�h�ꗗ�̎擾
        query = "SELECT ng_word FROM hh_master_ngword";
        subquery = "";
        try
        {
            count = 0;
            ngwordCount = 0;
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���R�[�h�����擾
                    if ( result.last() != false )
                    {
                        ngwordCount = result.getRow();
                    }

                    // String�̔z���p�ӂ��A����������B
                    ngwordList = new String[ngwordCount];
                    for( i = 0 ; i < ngwordCount ; i++ )
                    {
                        ngwordList[i] = new String();
                    }
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // NG���[�h���̎擾
                        ngwordList[count] = result.getString( "ng_word" );
                        subquery = subquery + " AND freeword <> '" + ngwordList[count] + "'";
                        count++;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemFreeword.getDataListByDate(NGword)] Exception=" + e.toString() );
            return(ret);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // NG���[�h���͂����ꂽ�������[�h�ꗗ���擾
        query = "SELECT * FROM hh_system_freeword";
        query = query + " WHERE date = ?";
        query = query + " AND freeword != ''";

        if ( subquery.compareTo( "" ) != 0 )
        {
            query = query + subquery;
        }
        query = query + " ORDER BY count DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        try
        {
            count = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���R�[�h�����擾
                    if ( result.last() != false )
                    {
                        dataCount = result.getRow();
                    }

                    // String�̔z���p�ӂ��A����������B
                    searchWordList = new String[dataCount];
                    for( i = 0 ; i < dataCount ; i++ )
                    {
                        searchWordList[i] = new String();
                    }
                    result.beforeFirst();
                    subquery = " AND freeword IN (";
                    while( result.next() != false )
                    {
                        // NG���[�h���͂����ꂽ�������[�h���̎擾
                        searchWordList[count] = result.getString( "freeword" );
                        if ( count < dataCount - 1 )
                        {
                            subquery = subquery + "'" + searchWordList[count] + "',";
                            // �����񐔂̍ő�l�̎擾
                            if ( count == 0 )
                            {
                                maxSearchCount = result.getInt( "count" );
                            }
                        }
                        else
                        {
                            subquery = subquery + "'" + searchWordList[count] + "')";
                            minSearchCount = result.getInt( "count" );
                        }
                        count++;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemFreeword.getDataListByDate(Keyword)] Exception=" + e.toString() );
            return(ret);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �������[�h�ꗗ�̃f�[�^�������_���Ŏ擾
        query = "SELECT * FROM hh_system_freeword";
        query = query + " WHERE date = ?";
        if ( subquery.compareTo( "" ) != 0 )
        {
            query = query + subquery;
        }

        if ( randomFlag != false )
        {
            query = query + " ORDER BY RAND()";
        }
        else
        {
            query = query + " ORDER BY count DESC";
        }

        try
        {
            count = 0;
            dataCount = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���R�[�h�����擾
                    if ( result.last() != false )
                    {
                        dataCount = result.getRow();
                    }

                    // �N���X�̔z���p�ӂ��A����������B
                    this.systemFreeword = new DataSystemFreeword[this.dataCount];
                    for( i = 0 ; i < dataCount ; i++ )
                    {
                        systemFreeword[i] = new DataSystemFreeword();
                    }
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // �z�e�����̎擾
                        this.systemFreeword[count].setData( result );
                        count++;
                    }

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemFreeword.getDataListByDate] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
