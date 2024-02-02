/*
 * @(#)SearchInfo.java  2.00 2004/03/18
 *
 * Copyright (C) ALMEX Inc. 2004
 *
 * ラブホサーチ共通クラス
 */

package com.hotenavi2.search;

import java.text.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import com.hotenavi2.common.*;

/**
 * ラブホサーチ関連のクラスです
 *
 * @author  S.Shiiya
 * @version 2.00 2004/12/27
 */
public class SearchInfo implements Serializable
{
    private LogLib    log;

    /**
     * SearchInfo。
     */
    public SearchInfo()
    {
        log = new LogLib();
    }

    /**
     *  エリア検索処理
     *
     *  @param localid 地方ID
     *  @param prefid 都道府県ID
     *  @param areaid エリアID
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForArea(int localid, int prefid, int areaid)
    {
        int    count = 0;
        String query = "";
        String querylocal = "";
        String querypref = "";
        String queryarea = "";
        String idlist = "";
        DbAccess db = null;
        ResultSet result = null;

        // エリアDB検索
        query = "SELECT * FROM search_hotel_location WHERE id <> 0";

        if( localid != 0 )
        {
            querylocal = " AND local_id=" + localid;
        }
        if( prefid != 0 )
        {
            querypref = " AND pref_id=" + prefid;
        }
        if( areaid != 0 )
        {
            queryarea = " AND (area_id1=" + areaid + " OR area_id2=" + areaid + " OR area_id3=" + areaid + ")";
        }

        query = query + querylocal + querypref + queryarea;

        try
        {
            db = new DbAccess();
            result = db.execQuery(query);
            if( result != null )
            {
                while( result.next() != false )
                {
                    if( count == 0 )
                    {
                        idlist = "" + result.getInt("id");
                    }
                    else
                    {
                        idlist = idlist + "," + result.getInt("id");
                    }

                    count++;
                }
            }
            db.close();
        }
        catch( Exception e )
        {
        }
        finally
        {
            try
            {
                if( result != null )
                {
                    result.close();
                }
                if( db != null )
                {
                    db.close();
                }
            }
            catch( Exception e )
            {
            }
        }

        return( idlist );
    }

    /**
     *  設備検索処理
     *
     *  @param equipcount 設備ID件数
     *  @param equipidlist 設備IDリスト
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForEquip(int equipcount, String equipidlist)
    {
        int count = 0;
        String query;
        String idlist = "";
        DbAccess db = null;
        ResultSet result = null;

        if( equipidlist.compareTo("") != 0 )
        {
            query = "SELECT * FROM search_hotel_equip WHERE equip_id IN(" + equipidlist + ")";
            query = query + " GROUP BY id";
            query = query + " HAVING count(id) >= " + equipcount;

            try
            {
                count = 0;
                db = new DbAccess();
                result = db.execQuery(query);
                if( result != null )
                {
                    while( result.next() != false )
                    {
                        if( count == 0 )
                        {
                            idlist = "" + result.getInt("id");
                        }
                        else
                        {
                            idlist = idlist + "," + result.getInt("id");
                        }

                        count++;
                    }
                }
                db.close();
            }
            catch( Exception e )
            {
            }
            finally
            {
                try
                {
                    if( result != null )
                    {
                        result.close();
                    }
                    if( db != null )
                    {
                        db.close();
                    }
                }
                catch( Exception e )
                {
                }
            }
        }
        return( idlist );
    }

    /**
     *  ワード検索処理（ホテル検索情報）
     *
     *  @param searchword 検索ワード
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForWordHotelFind(String searchword)
    {
        int i;
        int count = 0;
        String cutword[];
        String query;
        String idlist = "";
        DbAccess db = null;
        ResultSet result = null;

        // 検索ワードを分割する
        cutword = cutSearchWord(searchword);

        // ホテル検索情報で検索
        query = " SELECT * FROM search_hotel_find WHERE id<>0";
        for( i = 0 ; i < cutword.length ; i++ )
        {
            if( i == 0 )    query = query + " AND ";
            else            query = query + " OR ";

            query = query + " (findstr1 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr2 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr3 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr4 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr5 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr6 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr7 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr8 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr9 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr10 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%')";
        }

        log.info("getIdlistForWordHotelFind="+ query);

        try
        {
            count = 0;
            db = new DbAccess();
            result = db.execQuery(query);
            if( result != null )
            {
                while( result.next() != false )
                {
                    if( idlist.compareTo("") == 0 )
                    {
                        idlist = "" + result.getInt("id");
                    }
                    else
                    {
                        idlist = idlist + "," + result.getInt("id");
                    }

                    count++;
                }
            }
            db.close();
        }
        catch( Exception e )
        {
        }
        finally
        {
            try
            {
                if( result != null )
                {
                    result.close();
                }
                if( db != null )
                {
                    db.close();
                }
            }
            catch( Exception e )
            {
            }
        }

        return( idlist );
    }

    /**
     *  ワード検索処理（ホテル基本）
     *
     *  @param searchword 検索ワード
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForWordHotelBasic(String searchword)
    {
        int i;
        int count = 0;
        String cutword[];
        String query;
        String idlist = "";
        DbAccess db = null;
        ResultSet result = null;

        // 検索ワードを分割する
        cutword = cutSearchWord(searchword);

        // ホテル情報で検索
        query = "SELECT * FROM search_hotel WHERE id<>0";
        for( i = 0 ; i < cutword.length ; i++ )
        {
            if( i == 0 )    query = query + " AND ";
            else            query = query + " OR ";

            query = query + " (name LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR name_kana LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR address1 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR address2 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR url LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR pr LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%')";
        }

        log.info("getIdlistForWordHotelBasic="+ query);

        try
        {
            count = 0;
            db = new DbAccess();
            result = db.execQuery(query);
            if( result != null )
            {
                while( result.next() != false )
                {
                    if( idlist.compareTo("") == 0 )
                    {
                        idlist = "" + result.getInt("id");
                    }
                    else
                    {
                        idlist = idlist + "," + result.getInt("id");
                    }

                    count++;
                }
            }
            db.close();
        }
        catch( Exception e )
        {
        }
        finally
        {
            try
            {
                if( result != null )
                {
                    result.close();
                }
                if( db != null )
                {
                    db.close();
                }
            }
            catch( Exception e )
            {
            }
        }

        return( idlist );
    }

    /**
     *  ワード検索処理（ホテル複合）
     *
     *  @param searchword 検索ワード
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForWordHotel(String searchword)
    {
        int i;
        int count = 0;
        String cutword[];
        String query;
        String idlist = "";
        String preflist = "";
        String locallist = "";
        DbAccess db = null;
        ResultSet result = null;

        // 検索ワードを分割する
        cutword = cutSearchWord(searchword);

        // ホテル情報で検索
        query = "SELECT search_hotel.id FROM search_hotel,search_hotel_find2 WHERE search_hotel.id<>0";
        for( i = 0 ; i < cutword.length ; i++ )
        {
            // 地域・都道府県チェック
            preflist = getIdlistForWordPref(cutword[i]);
            locallist = getIdlistForWordLocal(cutword[i]);
            if( preflist.compareTo("") != 0 || locallist.compareTo("") != 0 )
            {
                if( i == 0 )    query = query + " AND ";
                else            query = query + " OR ";
            }
            else
            {
                if( i == 0 )    query = query + " AND ";
                else            query = query + " AND ";
            }

            query = query + " (search_hotel.name LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.name_kana LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.name_mobile LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.pref LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.pref_kana LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.address1 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.address2 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.url LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR search_hotel.pr LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";

            query = query + " OR search_hotel_find2.findstr LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%')";
            query = query + " AND search_hotel.id=search_hotel_find2.id";
        }
        query = query + " GROUP BY search_hotel_find2.id";

        log.info("getIdlistForWordHotel="+ query);

        try
        {
            count = 0;
            db = new DbAccess();
            result = db.execQuery(query);
            if( result != null )
            {
                while( result.next() != false )
                {
                    if( idlist.compareTo("") == 0 )
                    {
                        idlist = "" + result.getInt("id");
                    }
                    else
                    {
                        idlist = idlist + "," + result.getInt("id");
                    }

                    count++;
                }
            }
            db.close();
        }
        catch( Exception e )
        {
        }
        finally
        {
            try
            {
                if( result != null )
                {
                    result.close();
                }
                if( db != null )
                {
                    db.close();
                }
            }
            catch( Exception e )
            {
            }
        }

        return( idlist );
    }


    /**
     *  ワード検索処理（設備情報）
     *
     *  @param searchword 検索ワード
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForWordEquip(String searchword)
    {
        int i;
        int count = 0;
        String cutword[];
        String query;
        String equiplist = "";
        String idlist = "";
        DbAccess db = null;
        ResultSet result = null;

        // 検索ワードを分割する
        cutword = cutSearchWord(searchword);

        // 設備で検索
        query = "SELECT * FROM search_equip_master WHERE id <> 0";
        for( i = 0 ; i < cutword.length ; i++ )
        {
            if( i == 0 )    query = query + " AND ";
            else            query = query + " OR ";

            query = query + " (name LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR name_kana LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR find_name1 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR find_name2 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR find_name3 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR find_name4 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR find_name5 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%')";
        }

        log.info("getIdlistForWordEquip="+ query);

        try
        {
            count = 0;
            db = new DbAccess();
            result = db.execQuery(query);
            if( result != null )
            {
                while( result.next() != false )
                {
                    if( count == 0 )
                    {
                        equiplist = "" + result.getInt("id");
                    }
                    else
                    {
                        equiplist = equiplist + "," + result.getInt("id");
                    }

                    count++;
                }
            }
            db.close();
        }
        catch( Exception e )
        {
        }
        finally
        {
            try
            {
                if( result != null )
                {
                    result.close();
                }
                if( db != null )
                {
                    db.close();
                }
            }
            catch( Exception e )
            {
            }
        }

        if( equiplist.compareTo("") != 0 )
        {
            query = "SELECT * FROM search_hotel_equip WHERE id IN (";
            query = query + equiplist;
            query = query + ")";

            try
            {
                count = 0;
                db = new DbAccess();
                result = db.execQuery(query);
                if( result != null )
                {
                    while( result.next() != false )
                    {
                        if( idlist.compareTo("") == 0 )
                        {
                            idlist = "" + result.getInt("id");
                        }
                        else
                        {
                            idlist = idlist + "," + result.getInt("id");
                        }

                        count++;
                    }
                }
                db.close();
            }
            catch( Exception e )
            {
            }
            finally
            {
                try
                {
                    if( result != null )
                    {
                        result.close();
                    }
                    if( db != null )
                    {
                        db.close();
                    }
                }
                catch( Exception e )
                {
                }
            }
        }

        return( idlist );
    }

    /**
     *  ワード検索処理（エリア）
     *
     *  @param searchword 検索ワード
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForWordLocal(String searchword)
    {
        int i;
        int count = 0;
        String cutword[];
        String query;
        String arealist = "";
        String idlist = "";
        DbAccess db = null;
        ResultSet result = null;

        // 検索ワードを分割する
        cutword = cutSearchWord(searchword);

        // エリアで検索
        query = "SELECT * FROM search_local_master WHERE id <> 0";
        for( i = 0 ; i < cutword.length ; i++ )
        {
            if( i == 0 )    query = query + " AND ";
            else            query = query + " OR ";

            query = query + " (name LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR name_kana LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr1 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr2 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr3 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr4 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr5 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%')";
        }

        log.info("getIdlistForWordLocal="+ query);

        try
        {
            count = 0;
            db = new DbAccess();
            result = db.execQuery(query);
            if( result != null )
            {
                while( result.next() != false )
                {
                    if( count == 0 )
                    {
                        arealist = "" + result.getInt("id");
                    }
                    else
                    {
                        arealist = arealist + "," + result.getInt("id");
                    }

                    count++;
                }
            }
            db.close();
        }
        catch( Exception e )
        {
        }
        finally
        {
            try
            {
                if( result != null )
                {
                    result.close();
                }
                if( db != null )
                {
                    db.close();
                }
            }
            catch( Exception e )
            {
            }
        }

        if( arealist.compareTo("") != 0 )
        {
            query = "SELECT * FROM search_hotel_location WHERE local_id IN (";
            query = query + arealist;
            query = query + ")";

            try
            {
                count = 0;
                db = new DbAccess();
                result = db.execQuery(query);
                if( result != null )
                {
                    while( result.next() != false )
                    {
                        if( idlist.compareTo("") == 0 )
                        {
                            idlist = "" + result.getInt("id");
                        }
                        else
                        {
                            idlist = idlist + "," + result.getInt("id");
                        }

                        count++;
                    }
                }
                db.close();
            }
            catch( Exception e )
            {
            }
            finally
            {
                try
                {
                    if( result != null )
                    {
                        result.close();
                    }
                    if( db != null )
                    {
                        db.close();
                    }
                }
                catch( Exception e )
                {
                }
            }
        }
        return( idlist );
    }

    /**
     *  ワード検索処理（都道府県）
     *
     *  @param searchword 検索ワード
     *  @return ホテルIDリスト(失敗:null)
     */
    public String getIdlistForWordPref(String searchword)
    {
        int i;
        int count = 0;
        String cutword[];
        String query;
        String preflist = "";
        String idlist = "";
        DbAccess db = null;
        ResultSet result = null;

        // 検索ワードを分割する
        cutword = cutSearchWord(searchword);

        // エリアで検索
        query = "SELECT * FROM search_pref_master WHERE id <> 0";
        for( i = 0 ; i < cutword.length ; i++ )
        {
            if( i == 0 )    query = query + " AND ";
            else            query = query + " OR ";

            query = query + " (name LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR name_kana LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr1 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr2 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr3 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr4 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%'";
            query = query + " OR findstr5 LIKE '%" + ReplaceString.SQLEscape(cutword[i]) + "%')";
        }

        log.info("getIdlistForWordPref="+ query);

        try
        {
            count = 0;
            db = new DbAccess();
            result = db.execQuery(query);
            if( result != null )
            {
                while( result.next() != false )
                {
                    if( count == 0 )
                    {
                        preflist = "" + result.getInt("id");
                    }
                    else
                    {
                        preflist = preflist + "," + result.getInt("id");
                    }

                    count++;
                }
            }
            db.close();
        }
        catch( Exception e )
        {
        }
        finally
        {
            try
            {
                if( result != null )
                {
                    result.close();
                }
                if( db != null )
                {
                    db.close();
                }
            }
            catch( Exception e )
            {
            }
        }

        if( preflist.compareTo("") != 0 )
        {
            query = "SELECT * FROM search_hotel_location WHERE pref_id IN (";
            query = query + preflist;
            query = query + ")";

            try
            {
                count = 0;
                db = new DbAccess();
                result = db.execQuery(query);
                if( result != null )
                {
                    while( result.next() != false )
                    {
                        if( idlist.compareTo("") == 0 )
                        {
                            idlist = "" + result.getInt("id");
                        }
                        else
                        {
                            idlist = idlist + "," + result.getInt("id");
                        }

                        count++;
                    }
                }
                db.close();
            }
            catch( Exception e )
            {
            }
            finally
            {
                try
                {
                    if( result != null )
                    {
                        result.close();
                    }
                    if( db != null )
                    {
                        db.close();
                    }
                }
                catch( Exception e )
                {
                }
            }
        }
        return( idlist );
    }

    /**
     *  ホテル件数取得処理
     *
     *  @param idlist IDリスト(カンマ編集)
     *  @return 件数
     */
    public int getHotelCount(String idlist)
    {
        int count = 0;
        String query;
        DbAccess dbcount = null;
        ResultSet retcount = null;

        if( idlist.compareTo("") != 0 )
        {
            query = "SELECT count(*) FROM search_hotel WHERE id IN(";
            query = query + idlist;
            query = query + ")";

            try
            {
                dbcount = new DbAccess();
                retcount = dbcount.execQuery(query);
                if( retcount != null )
                {
                    if( retcount.next() != false )
                    {
                        count = retcount.getInt(1);
                    }
                }
                dbcount.close();
            }
            catch( Exception e )
            {
            }
            finally
            {
                try
                {
                    if( retcount != null )
                    {
                        retcount.close();
                    }
                    if( dbcount != null )
                    {
                        dbcount.close();
                    }
                }
                catch( Exception e )
                {
                }
            }
        }
        return( count );
    }

    /**
     *  キーワードランキング更新処理
     *
     *  @param word キーワード
     *  @return なし
     */
    public void updateKeyword(String word) throws Exception
    {
        int i;
        int retupdate;
        String cutword[];
        String query;
        DbAccess dbword;
        DbAccess dbwordupdate;
        ResultSet retword;

        // 検索ワードを分割する
        cutword = cutSearchWord(word);

        for( i = 0 ; i < cutword.length ; i++ )
        {
            query = "SELECT * FROM search_word WHERE";
            query = query + " word='";
            query = query + ReplaceString.SQLEscape(cutword[i]);
            query = query + "'";
            dbword = new DbAccess();
            retword = dbword.execQuery(query);
            if( retword != null )
            {
                if( retword.next() != false )
                {
                    // 利用回数加算
                    query = "UPDATE search_word SET";
                    query = query + " count=count+1";
                    query = query + " WHERE";
                    query = query + " id=" + retword.getInt("id");
                }
                else
                {
                    // 利用回数新規追加
                    query = "INSERT INTO search_word SET";
                    query = query + " count=1";
                    query = query + ",word='";
                    query = query + ReplaceString.SQLEscape(cutword[i]);
                    query = query + "'";
                }

                dbwordupdate = new DbAccess();
                retupdate = dbwordupdate.execUpdate(query);
                if( retupdate != -1 )
                {
                }
                dbwordupdate.close();

                retword.close();
            }
            dbword.close();
        }
    }

    /**
     *  検索ワード分割処理
     *
     *  @param word キーワード
     *  @return 分割後ワード
     */
    public String[] cutSearchWord(String word)
    {
        int i;
        int count = 0;
        int spccount = 0;
        char charbuff;
        String strbuff;
        String cutword[];
        StringBuffer wordbuff = new StringBuffer(word.replace('　', ' ').trim());

        for( i = 0 ; i < wordbuff.length() ; i++ )
        {
            charbuff = wordbuff.charAt(i);
            if( charbuff == ' ' )
            {
                if( spccount > 0 )
                {
                    count++;
                    spccount = 0;
                }
            }
            else if( charbuff == '　' )
            {
                if( spccount > 0 )
                {
                    count++;
                    spccount = 0;
                }
            }
            else
            {
                spccount++;
            }
        }

        if( count > 0 )
        {
            cutword = new String[count+1];

            count = 0;
            spccount = 0;
            strbuff = "";

            for( i = 0 ; i < wordbuff.length() ; i++ )
            {
                charbuff = wordbuff.charAt(i);
                if( charbuff == ' ' )
                {
                    if( spccount > 0 )
                    {
                        cutword[count] = strbuff;

                        strbuff = "";
                        count++;
                        spccount = 0;
                    }
                }
                else if( charbuff == '　' )
                {
                    if( spccount > 0 )
                    {
                        cutword[count] = strbuff;

                        strbuff = "";
                        count++;
                        spccount = 0;
                    }
                }
                else
                {
                    strbuff = strbuff + Character.toString(charbuff);
                    spccount++;
                }
            }

            if( strbuff.compareTo("") != 0 )
            {
                cutword[count] = strbuff;
            }
        }
        else
        {
            cutword = new String[1];
            cutword[0] = wordbuff.toString();
        }

        return( cutword );
    }
}

