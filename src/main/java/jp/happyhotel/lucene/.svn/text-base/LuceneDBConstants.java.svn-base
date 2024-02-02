package jp.happyhotel.lucene;

public class LuceneDBConstants
{
    public static String       LUCENE_DB_ROOT_DIR                 = "/lucene/lucene_indexes";
    public static final String FREEWORD_SEARCH_INDEX_PRIMARY      = "word";
    public static final String FREEWORD_SEARCH_INDEX_DIR          = "FREEWORD_SEARCH";
    public static final String COMB_FREEWORD_SEARCH_INDEX_PRIMARY = "id";
    public static final String COMB_FREEWORD_SEARCH_INDEX_DIR     = "COMB_FREEWORD_SEARCH";
    public static final String HOTEL_BASIC_INDEX_PRIMARY          = "id";
    public static final String HOTEL_BASIC_INDEX_DIR              = "HOTEL_BASIC_INFO";

    public static final String QUERY_FREEWORD_SEARCH              =

                                                                  " SELECT hh_hotel_search.word, hh_hotel_search.id  FROM hh_hotel_search,hh_hotel_basic,hh_hotel_pv" +
                                                                          " WHERE hh_hotel_search.id <> 0" +
                                                                          " AND hh_hotel_search.id=hh_hotel_basic.id" +
                                                                          " AND hh_hotel_basic.kind <= 7" +
                                                                          " AND hh_hotel_basic.id = hh_hotel_pv.id" +
                                                                          " AND hh_hotel_pv.collect_date = 0" +
                                                                          " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_pv DESC, hh_hotel_basic.name_kana";

    public static final String COMB_QUERY_FREEWORD_SEARCH         =

                                                                  " SELECT hh_hotel_search.word, hh_hotel_search.id  FROM hh_hotel_search,hh_hotel_basic,hh_hotel_pv" +
                                                                          " WHERE hh_hotel_search.id <> 0" +
                                                                          " AND hh_hotel_search.id=hh_hotel_basic.id" +
                                                                          " AND hh_hotel_basic.kind <= 7" +
                                                                          " AND hh_hotel_basic.id = hh_hotel_pv.id" +
                                                                          " AND hh_hotel_pv.collect_date = 0" +
                                                                          " ORDER BY hh_hotel_search.id, hh_hotel_basic.rank DESC, hh_hotel_pv.total_pv DESC, hh_hotel_basic.name_kana";
    public static final String QUERY_HOTEL_BASIC                  =

                                                                  " SELECT HB.id, HB.rank, HB.name, HB.pref_name, " +
                                                                          " HB.address1, HB.address_all, HB.tel1, HB.url, HB.url_official1, " +
                                                                          " HB.url_official2, HB.pr, HB.reserve, HB.reserve_tel, HB.reserve_mail, " +
                                                                          " HB.reserve_web, IFNULL(HS.empty_status, 0) empty_status,HB.hotel_picture_pc" +
                                                                          " FROM hh_hotel_basic HB" +
                                                                          " LEFT JOIN hh_hotel_status HS" +
                                                                          " ON HB.id = HS.id" +
                                                                          " , hh_hotel_pv HP" +
                                                                          " WHERE HB.kind <= 7" +
                                                                          " AND HB.id = HP.id" +
                                                                          " AND HP.collect_date = 0";

    static
    {
        if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
        {
            LUCENE_DB_ROOT_DIR = "C:/lucene/lucene_indexes";
        }
        else
        {
            LUCENE_DB_ROOT_DIR = "/lucene/lucene_indexes";
        }
    }

}
