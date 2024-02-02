package jp.happyhotel.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GenMap
{

    private static String       strMYSQLDriver           = "org.gjt.mm.mysql.Driver";
    private static String       strDatabaseURL           = null;
    private static String       strUserName              = null;
    private static String       strPassword              = null;
    private static Connection   conn                     = null;                                              // connection object

    private static StringBuffer strSiteMap               = new StringBuffer();
    private static StringBuffer strSiteMapDocomo         = new StringBuffer();
    private static StringBuffer strSiteMapAu             = new StringBuffer();
    private static StringBuffer strSiteMapSoftbank       = new StringBuffer();
    private static StringBuffer strRor                   = new StringBuffer();
    private static StringBuffer strRorDocomo             = new StringBuffer();
    private static StringBuffer strRorAu                 = new StringBuffer();
    private static StringBuffer strRorSoftbank           = new StringBuffer();

    // This constant is for defining the sitemap config file path for Linux system

    // private static final String strConfigSiteMapFilePath = "sitemap.config";

    // This constant is for defining the ror config file path for Linux system
    // private static final String strConfigRoRFilePath = "ror.config";

    // This constant is for defining the sitemap config file path for Windows system
    private static final String strConfigSiteMapFilePath = File.separator + "etc//happyhotel//sitemap.config";

    // This constant is for defining the ror config file path for Windows system
    private static final String strConfigRoRFilePath     = File.separator + "etc//happyhotel//ror.config";

    static
    {

        try
        {
            // load the MYSQL JDBC Driver
            Class.forName( strMYSQLDriver );

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            System.out.println( "MySQL Driver not found ->" + strMYSQLDriver );
            System.exit( 1 );

        }

    }

    /**
     * This method appends string into String Buffer for SiteMap
     * 
     * @param sbr
     * @param strLoc
     * @param strLastmode
     * @param strChangeFrq
     * @param strPriority
     * @return StringBuffer
     */
    private static StringBuffer generateSiteMapXML(StringBuffer sbr, String strLoc, String strLastmode, String strChangeFrq, String strPriority)
    {
        sbr.append( "<url>\n" );
        sbr.append( "<loc>" );
        sbr.append( strLoc );
        sbr.append( "</loc>\n" );
        sbr.append( "<lastmod>" );
        sbr.append( strLastmode );
        sbr.append( "</lastmod>\n" );
        sbr.append( "<changefreq>" );
        sbr.append( strChangeFrq );
        sbr.append( "</changefreq>\n" );
        sbr.append( "<priority>" );
        sbr.append( strPriority );
        sbr.append( "</priority>\n" );
        sbr.append( "</url>\n" );

        return sbr;
    }

    /**
     * This method appends static string into String Buffer for ROR
     * 
     * @param sbr
     * @param strTitle
     * @param strLink
     * @param strTitle1
     * @param strLink1
     * @param strAbout
     * @param strType
     * @return StringBuffer
     */
    private static StringBuffer generateRORXML_Common(StringBuffer sbr, String strTitle, String strLink,
            String strTitle1, String strLink1, String strAbout, String strType)
    {
        sbr.append( "<channel>\n" );
        sbr.append( "<title>" );
        sbr.append( strTitle );
        sbr.append( "</title>\n" );
        sbr.append( "<link>" );
        sbr.append( strLink );
        sbr.append( "</link>\n" );
        sbr.append( "<item>\n" );
        sbr.append( "<title>" );
        sbr.append( strTitle1 );
        sbr.append( "</title>\n" );
        sbr.append( "<link>" );
        sbr.append( strLink1 );
        sbr.append( "</link>\n" );
        sbr.append( "<ror:about>" );
        sbr.append( strAbout );
        sbr.append( "</ror:about>\n" );
        sbr.append( "<ror:type>" );
        sbr.append( strType );
        sbr.append( "</ror:type>\n" );
        sbr.append( "</item>\n" );
        return sbr;
    }

    /**
     * This method appends dynamic string into String Buffer for ROR
     * 
     * @param sbr
     * @param strLink
     * @param strTitle
     * @param strUpdatePrd
     * @param strSortOrd
     * @param strResourceOf
     * @return StringBuffer
     */
    private static StringBuffer generateRORXML_Dynamic(StringBuffer sbr, String strLink, String strTitle,
            String strUpdatePrd, String strSortOrd, String strResourceOf)
    {
        sbr.append( "<item>\n" );
        sbr.append( "<link>" );
        sbr.append( strLink );
        sbr.append( "</link>\n" );
        sbr.append( "<title>" );
        sbr.append( strTitle );
        sbr.append( "</title>\n" );
        sbr.append( "<ror:updatePeriod>" );
        sbr.append( strUpdatePrd );
        sbr.append( "</ror:updatePeriod>\n" );
        sbr.append( "<ror:sortOrder>" );
        sbr.append( strSortOrd );
        sbr.append( "</ror:sortOrder>\n" );
        sbr.append( "<ror:resourceOf>" );
        sbr.append( strResourceOf );
        sbr.append( "</ror:resourceOf>\n" );
        sbr.append( "</item>\n" );
        return sbr;
    }

    /**
     * This method generates the XML file containing site map or Ror data
     * 
     * @param strFileName
     * @param sbr
     * @throws Exception
     */
    private static void generateXMLFile(String strFileName, StringBuffer sbr) throws Exception
    {
        try
        {

            OutputStream fout = new FileOutputStream( strFileName );
            OutputStream bout = new BufferedOutputStream( fout );
            OutputStreamWriter out = new OutputStreamWriter( bout, "UTF-8" );
            out.write( sbr.toString() );
            out.flush();
            out.close();
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in generateXMLFile()=" + e.getMessage() );
            throw e;
        }
    }

    /**
     * This method returns the current date in YYYY-MM-DD format
     * 
     * @return String
     * @throws Exception
     */
    private static String getDate() throws Exception
    {
        String todayStr = null;
        try
        {
            Calendar cal = Calendar.getInstance();
            java.util.Date today = cal.getTime();
            SimpleDateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd" );
            todayStr = fmt.format( today );
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in getDate()=" + e.getMessage() );
            throw e;
        }
        return todayStr;
    }

    /**
     * This method finds a string from the given string and in case it finds the string it replaces it with the given string
     * 
     * @param strOriginal
     * @param strFind
     * @param strReplacement
     * @return String
     */
    private static String replaceString(String strOriginal, String strFind, String strReplacement)
    {
        int i = strOriginal.indexOf( strFind );
        if ( i < 0 )
        {
            return strOriginal; // return original if 'strFind' is not in it.
        }

        String partBefore = strOriginal.substring( 0, i );
        String partAfter = strOriginal.substring( i + strFind.length() );

        return partBefore + strReplacement + partAfter;
    }

    /**
     * This method fetches the ID and Name of all hotels from the database
     * 
     * @param strFileName
     * @param strCheckValue
     * @return ArrayList<Object>
     * @throws Exception
     */
    private static ArrayList<Object> getRecord(String strFileName, String strCheckValue) throws Exception
    {
        ArrayList<Object> arrRecord = new ArrayList<Object>();

        Statement stmt = null; // statement object
        ResultSet rs = null; // result set object
        String record = null;
        BufferedReader bis = null;
        String[] test = new String[2];
        String strName = null;
        HappyHotelBasicInfo objHappyHotelBasicInfo = null;
        try
        {

            // make database connection
            if ( conn == null )
            {
                conn = DriverManager.getConnection( strDatabaseURL, strUserName, strPassword );
            }

            stmt = conn.createStatement();
            if ( strCheckValue.equals( "$i" ) )
            {
                // get all hotel id and hotel name whose rank >=1 and Kind <= 7 from the hh_hotel_basic table
                rs = stmt.executeQuery( "SELECT id,name FROM hh_hotel_basic where rank >= 1 and kind <=7 order by id asc" );
            }
            else
            {
                // get all hotel id and hotel name whose rank =2 and Kind <= 7 from the hh_hotel_basic table
                rs = stmt.executeQuery( "SELECT id,name FROM hh_hotel_basic where rank = 2 and kind <=7 order by id asc" );
            }
            // iterate the result set and get one row at a time
            while( rs.next() )
            {
                objHappyHotelBasicInfo = new HappyHotelBasicInfo(); // create instance of HappyHotelBasicInfo
                objHappyHotelBasicInfo.setStrHotelId( rs.getString( "ID" ) );
                strName = rs.getString( "NAME" );
                // System.out.println("records are ="+strName+"Kind is "+strKind+"Rank is "+strRank+"Count is "+count);
                if ( strName.contains( "&" ) )
                {
                    strName = replaceString( strName, "&", "&amp;" );
                }
                objHappyHotelBasicInfo.setStrName( strName );
                arrRecord.add( objHappyHotelBasicInfo ); // adding object of HappyHotelBasicInfoarray Array list
            }
        }
        catch ( SQLException se )
        {
            System.out.println( "SQLException Occured in getrecord()=" + se.getMessage() );
            throw se;
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in getrecord()=" + e.getMessage() );
            throw e;
        }
        finally
        {
            try
            {
                if ( bis != null )
                {
                    bis.close();
                }

                if ( rs != null )
                {
                    rs.close();
                }
                if ( stmt != null )
                {
                    stmt.close();
                }

            }
            catch ( Exception e )
            {
            }
        }
        return arrRecord;
    }

    /**
     * This method reads the configuration file
     * 
     * @param strFileName
     * @return BufferedReader
     * @throws Exception
     */
    private static BufferedReader readConfigFile(String strFileName) throws Exception
    {
        BufferedReader bis = null;

        try
        {
            File f = new File( strFileName );
            FileInputStream fis = new FileInputStream( f );
            bis = new BufferedReader( new InputStreamReader( fis, "UTF-8" ) );
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in readConfigFile()=" + e.getMessage() );
            throw e;
        }
        return bis;
    }

    /**
     * This method splits the given string as key value pair
     * 
     * @param strRecord
     * @return String[]
     */
    private static String[] getKeyValue(String strRecord)
    {
        String[] strKeyValue = new String[2];
        strKeyValue[0] = strRecord.substring( 0, strRecord.indexOf( '=' ) );
        strKeyValue[1] = strRecord.substring( strRecord.indexOf( '=' ) + 1 );
        return strKeyValue;
    }

    /**
     * This function generates the output for SiteMap as XML
     * 
     * @throws Exception
     */
    private static void genSiteMap() throws Exception
    {
        StringBuffer sbr1 = null;
        StringBuffer sbrDocomo = null;
        StringBuffer sbrAu = null;
        StringBuffer sbrSoftbank = null;
        String strXML = "<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8" + "\"?>";
        String strURLSetXML = "<urlset xmlns=" + "\"http://www.sitemaps.org/schemas/sitemap/0.9\"" + ">";

        String record = null;
        String strOutputFileName = null;
        String strOutputFileNameForDocomo = null;
        String strOutputFileNameForAu = null;
        String strOutputFileNameForSoftbank = null;
        BufferedReader bis = null;
        String[] test;
        String strUrlCommon = null;

        try
        {
            bis = readConfigFile( strConfigSiteMapFilePath );
            while( (record = bis.readLine()) != null )
            {

                test = getKeyValue( record );
                if ( test.length > 0 )
                {
                    // System.out.println("test[0].toString()="+test[0].toString());
                    if ( test[0].toString().startsWith( "database.url" ) )
                    {
                        // System.out.println("test[1] value="+test[1].toString());
                        strDatabaseURL = test[1].toString();
                    }
                    else if ( test[0].toString().startsWith( "database.username" ) )
                    {
                        strUserName = test[1].toString();
                    }
                    else if ( test[0].toString().startsWith( "database.password" ) )
                    {
                        strPassword = test[1].toString();
                    }
                    else if ( test[0].toString().startsWith( "output.filename" ) )
                    {
                        strOutputFileName = test[1].toString();
                    }
                    // output file for docomo
                    else if ( test[0].toString().startsWith( "output.docomo" ) )
                    {
                        strOutputFileNameForDocomo = test[1].toString();
                    }
                    // output file for au
                    else if ( test[0].toString().startsWith( "output.au" ) )
                    {
                        strOutputFileNameForAu = test[1].toString();
                    }
                    // output file for softbank
                    else if ( test[0].toString().startsWith( "output.softbank" ) )
                    {
                        strOutputFileNameForSoftbank = test[1].toString();
                    }
                    else if ( test[0].toString().startsWith( "url.common" ) )
                    {
                        strUrlCommon = test[1].toString();
                        strSiteMap.append( strXML + "\n" );
                        strSiteMap.append( strURLSetXML + "\n" );
                        // for docomo
                        strSiteMapDocomo.append( strXML + "\n" );
                        strSiteMapDocomo.append( strURLSetXML + "\n" );
                        // for au
                        strSiteMapAu.append( strXML + "\n" );
                        strSiteMapAu.append( strURLSetXML + "\n" );
                        // for softbank
                        strSiteMapSoftbank.append( strXML + "\n" );
                        strSiteMapSoftbank.append( strURLSetXML + "\n" );

                        sbr1 = generateSiteMapXML( strSiteMap, test[1].toString() + "/", getDate(), "daily", "1.0" );
                        sbrDocomo = generateSiteMapXML( strSiteMapDocomo, test[1].toString() + "/i/", getDate(), "daily", "1.0" );
                        sbrAu = generateSiteMapXML( strSiteMapAu, test[1].toString() + "/au/", getDate(), "daily", "1.0" );
                        sbrSoftbank = generateSiteMapXML( strSiteMapSoftbank, test[1].toString() + "/y/", getDate(), "daily", "1.0" );
                    }
                    else if ( test[0].toString().startsWith( "contents.html" ) )
                    {
                        sbr1 = generateSiteMapXML( strSiteMap, strUrlCommon + test[1].toString(), getDate(), "Weekly", "0.5" );
                    }
                    // for mobile
                    else if ( test[0].toString().startsWith( "contents_mobile.html" ) )
                    {
                        // String strdocomoCheck=test[1].replaceAll("$m", "i");

                        if ( test[1].indexOf( "?" ) == -1 )
                        {
                            sbrDocomo = generateSiteMapXML( strSiteMapDocomo, strUrlCommon + replaceString( test[1], "$m", "i" ) + "?uid=NULLGWDOCOMO", getDate(), "Weekly", "0.5" );
                        }
                        else
                        {
                            sbrDocomo = generateSiteMapXML( strSiteMapDocomo, strUrlCommon + replaceString( test[1], "$m", "i" ) + "&amp;uid=NULLGWDOCOMO", getDate(), "Weekly", "0.5" );
                        }
                        sbrAu = generateSiteMapXML( strSiteMapAu, strUrlCommon + replaceString( test[1], "$m", "au" ), getDate(), "Weekly", "0.5" );
                        sbrSoftbank = generateSiteMapXML( strSiteMapSoftbank, strUrlCommon + replaceString( test[1], "$m", "y" ), getDate(), "Weekly", "0.5" );
                    }
                    else if ( test[0].toString().startsWith( "contents.jsp" ) )
                    {
                        String strValueAfterEqualSign = test[1].toString().substring( test[1].toString().indexOf( '$' ) );
                        String strValueTillEqualSign = test[1].toString().substring( 0, test[1].toString().indexOf( '$' ) );
                        // System.out.println(" strCheckValue value is="+strCheckValue+"URL is "+strUrlCommon+ba);
                        ArrayList<Object> arrHotelObj = getRecord( strConfigSiteMapFilePath, strValueAfterEqualSign );
                        for( int arrCount = 0 ; arrCount < arrHotelObj.size() ; arrCount++ )
                        {
                            HappyHotelBasicInfo objHappyHotelBasicInfo = (HappyHotelBasicInfo)arrHotelObj.get( arrCount );
                            sbr1 = generateSiteMapXML( strSiteMap, strUrlCommon + strValueTillEqualSign + objHappyHotelBasicInfo.getStrHotelId(), getDate(), "Weekly", "0.5" );
                        }
                    }
                    else if ( test[0].toString().startsWith( "contents_mobile.jsp" ) )
                    {
                        // String strValueAfterEqualSign = test[1].toString().substring(test[1].toString().indexOf('$'));

                        String strValueAfterEqualSign = test[1].toString().substring( test[1].toString().lastIndexOf( '$' ) );

                        String strValueTillEqualSign = test[1].toString().substring( 0, test[1].toString().lastIndexOf( '$' ) );
                        // System.out.println(" strCheckValue value is="+strCheckValue+"URL is "+strUrlCommon+ba);
                        ArrayList<Object> arrHotelObj = getRecord( strConfigSiteMapFilePath, strValueAfterEqualSign );
                        for( int arrCount = 0 ; arrCount < arrHotelObj.size() ; arrCount++ )
                        {
                            HappyHotelBasicInfo objHappyHotelBasicInfo = (HappyHotelBasicInfo)arrHotelObj.get( arrCount );

                            if ( test[1].indexOf( "?" ) == -1 )
                            {
                                sbrDocomo = generateSiteMapXML( strSiteMapDocomo, strUrlCommon + replaceString( strValueTillEqualSign, "$m", "i" ) + objHappyHotelBasicInfo.getStrHotelId() + "?uid=NULLGWDOCOMO", getDate(), "Weekly", "0.5" );
                            }
                            else
                            {
                                sbrDocomo = generateSiteMapXML( strSiteMapDocomo, strUrlCommon + replaceString( strValueTillEqualSign, "$m", "i" ) + objHappyHotelBasicInfo.getStrHotelId() + "&amp;uid=NULLGWDOCOMO", getDate(), "Weekly", "0.5" );
                            }
                            sbrAu = generateSiteMapXML( strSiteMapAu, strUrlCommon + replaceString( strValueTillEqualSign, "$m", "au" ) + objHappyHotelBasicInfo.getStrHotelId(), getDate(), "Weekly", "0.5" );
                            sbrSoftbank = generateSiteMapXML( strSiteMapSoftbank, strUrlCommon + replaceString( strValueTillEqualSign, "$m", "y" ) + objHappyHotelBasicInfo.getStrHotelId(), getDate(), "Weekly", "0.5" );

                        }
                    }
                }
            }
            sbr1.append( "</urlset>\n" );
            sbrDocomo.append( "</urlset>\n" );
            sbrAu.append( "</urlset>\n" );
            sbrSoftbank.append( "</urlset>\n" );

            System.out.println( "Creating sitemap for PC ->" + strOutputFileName );
            generateXMLFile( strOutputFileName, sbr1 );

            System.out.println( "Creating sitemap for Docomo ->" + strOutputFileNameForDocomo );
            generateXMLFile( strOutputFileNameForDocomo, sbrDocomo );

            System.out.println( "Creating sitemap for AU ->" + strOutputFileNameForAu );
            generateXMLFile( strOutputFileNameForAu, sbrAu );

            System.out.println( "Creating sitemap for Softbank ->" + strOutputFileNameForSoftbank );
            generateXMLFile( strOutputFileNameForSoftbank, sbrSoftbank );

            // System.out.println("XML file for SiteMap is generated Successfully");
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in genSiteMap()!" + e.getMessage() );
            throw e;
        }
        finally
        {
            // if the file is opened , make sure we close it
            if ( bis != null )
            {
                try
                {
                    bis.close();
                }
                catch ( IOException ioe )
                {
                }
            }
        }

    }

    /**
     * This function generates the output for ROR as XML
     * 
     * @throws Exception
     */
    private static void genRORXML() throws Exception
    {
        StringBuffer sbr1 = null;
        StringBuffer sbrDocomo = null;
        StringBuffer sbrAu = null;
        StringBuffer sbrSoftbank = null;
        String strXML = "<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8" + "\"?>";
        String strRssSetXML = "<rss version=" + "\"2.0\" " + "xmlns:ror=" + "\"http://rorweb.com/0.1/\"" + ">";
        String strCommonString = "ROR Sitemap for ";

        String record = null;
        String strOutputFileName = null;
        String strOutputFileNameForDocomo = null;
        String strOutputFileNameForAu = null;
        String strOutputFileNameForSoftbank = null;
        BufferedReader bis = null;

        String strUrlCommon = null;
        String strHtmlContents = null;
        String strHtmlContentsDocomo = null;
        String strHtmlContentsAu = null;
        String strHtmlContentsSoftbank = null;

        String strHotelId = null;
        String strTitle = null;
        String strValueTillEqualSign = null;
        String strTitleConst = null;
        String strTitleConstForMobile = null;
        String strJSPTitle = null;
        ArrayList<Object> arrHotelObj = null;
        String strCommonContent = null;
        String strCommonContentForDocomo = null;
        String strCommonContentForAu = null;
        String strCommonContentForSoftbank = null;

        try
        {
            // ArrayList<Object> arrHotelObj = getRecord(strConfigRoRFilePath,"$i");
            bis = readConfigFile( strConfigRoRFilePath );
            while( (record = bis.readLine()) != null )
            {
                String[] test = getKeyValue( record );

                if ( test[0].toString().startsWith( "database.url" ) )
                {
                    // System.out.println("test[1] value="+test[1].toString());
                    strDatabaseURL = test[1].toString();
                }
                else if ( test[0].toString().startsWith( "database.username" ) )
                {
                    strUserName = test[1].toString();
                }
                else if ( test[0].toString().startsWith( "database.password" ) )
                {
                    strPassword = test[1].toString();
                }
                else if ( test[0].toString().startsWith( "output.filename" ) )
                {
                    strOutputFileName = test[1].toString();

                }
                // output file for docomo
                else if ( test[0].toString().startsWith( "output.docomo" ) )
                {
                    strOutputFileNameForDocomo = test[1].toString();

                }
                // output file for au
                else if ( test[0].toString().startsWith( "output.au" ) )
                {
                    strOutputFileNameForAu = test[1].toString();

                }
                // output file for softbank
                else if ( test[0].toString().startsWith( "output.softbank" ) )
                {
                    strOutputFileNameForSoftbank = test[1].toString();

                }

                else if ( test[0].toString().startsWith( "url.common" ) )
                {
                    strUrlCommon = test[1].toString();
                    strRor.append( strXML + "\n" );
                    strRor.append( strRssSetXML + "\n" );

                    // for docomo
                    strRorDocomo.append( strXML + "\n" );
                    strRorDocomo.append( strRssSetXML + "\n" );
                    // for au
                    strRorAu.append( strXML + "\n" );
                    strRorAu.append( strRssSetXML + "\n" );
                    // for softbank
                    strRorSoftbank.append( strXML + "\n" );
                    strRorSoftbank.append( strRssSetXML + "\n" );

                    sbr1 = generateRORXML_Common( strRor, strCommonString + test[1].toString() + "/", test[1].toString() + "/",
                            strCommonString + test[1].toString() + "/", test[1].toString() + "/", "sitemap", "SiteMap" );

                    sbrDocomo = generateRORXML_Common( strRorDocomo, strCommonString + test[1].toString() + "/i/", test[1].toString() + "/i/",
                            strCommonString + test[1].toString() + "/i/", test[1].toString() + "/i/", "sitemap", "SiteMap" );
                    sbrAu = generateRORXML_Common( strRorAu, strCommonString + test[1].toString() + "/au/", test[1].toString() + "/au/",
                            strCommonString + test[1].toString() + "/au/", test[1].toString() + "/au/", "sitemap", "SiteMap" );
                    sbrSoftbank = generateRORXML_Common( strRorSoftbank, strCommonString + test[1].toString() + "/y/", test[1].toString() + "/y/",
                            strCommonString + test[1].toString() + "/y/", test[1].toString() + "/y/", "sitemap", "SiteMap" );

                }
                else if ( test[0].toString().startsWith( "contents.html" ) )
                {
                    strHtmlContents = test[1].toString();

                }
                // for mobile
                else if ( test[0].toString().startsWith( "contents_mobile.html" ) )
                {
                    strHtmlContents = test[1].toString();

                    strHtmlContentsDocomo = replaceString( test[1], "$m", "i" );
                    strHtmlContentsAu = replaceString( test[1], "$m", "au" );
                    strHtmlContentsSoftbank = replaceString( test[1], "$m", "y" );

                }
                else if ( test[0].toString().startsWith( "contents.title" ) )
                {

                    // String test1=new String(test[1].getBytes("SHIFT_jis"), "UTF-8");
                    // System.out.println("title:"+test1);
                    sbr1 = generateRORXML_Dynamic( strRor, strUrlCommon + strHtmlContents, test[1].toString(), "Weekly", "0", "sitemap" );

                }

                // for mobile content title
                else if ( test[0].toString().startsWith( "contents_mobile.title" ) )
                {
                    sbrDocomo = generateRORXML_Dynamic( strRorDocomo, strUrlCommon + strHtmlContentsDocomo, test[1].toString(), "Weekly", "0", "sitemap" );
                    sbrAu = generateRORXML_Dynamic( strRorAu, strUrlCommon + strHtmlContentsAu, test[1].toString(), "Weekly", "0", "sitemap" );
                    sbrSoftbank = generateRORXML_Dynamic( strRorSoftbank, strUrlCommon + strHtmlContentsSoftbank, test[1].toString(), "Weekly", "0", "sitemap" );

                }
                else if ( test[0].toString().equalsIgnoreCase( "contents.jsp" ) )
                {
                    String strValueAfterEqualSign = test[1].toString().substring( test[1].toString().indexOf( '$' ) );
                    strValueTillEqualSign = test[1].toString().substring( 0, test[1].toString().indexOf( '$' ) );
                    arrHotelObj = getRecord( strConfigSiteMapFilePath, strValueAfterEqualSign );
                }
                // for mobile content.jsp
                else if ( test[0].toString().equalsIgnoreCase( "contents_mobile.jsp" ) )
                {
                    String strValueAfterEqualSign = test[1].toString().substring( test[1].toString().lastIndexOf( '$' ) );
                    strValueTillEqualSign = test[1].toString().substring( 0, test[1].toString().lastIndexOf( '$' ) );
                    arrHotelObj = getRecord( strConfigSiteMapFilePath, strValueAfterEqualSign );
                }

                else if ( test[0].toString().equalsIgnoreCase( "contents.jsp.title" ) )
                {
                    // strTitleConst = test[1].toString().substring(0,test[1].toString().indexOf('$'));
                    strTitleConst = test[1].toString();
                    for( int arrCount = 0 ; arrCount < arrHotelObj.size() ; arrCount++ )
                    {
                        HappyHotelBasicInfo objHappyHotelBasicInfo = (HappyHotelBasicInfo)arrHotelObj.get( arrCount );
                        strHotelId = objHappyHotelBasicInfo.getStrHotelId();
                        strTitle = objHappyHotelBasicInfo.getStrName();
                        strCommonContent = strUrlCommon + strValueTillEqualSign + strHotelId;
                        // strJSPTitle=strTitleConst+"("+strTitle+")";
                        strJSPTitle = strTitleConst.replaceAll( "\\$n", strTitle );
                        sbr1 = generateRORXML_Dynamic( strRor, strCommonContent, strJSPTitle, "Weekly", "0", "sitemap" );
                    }
                }
                // for mobile
                else if ( test[0].toString().equalsIgnoreCase( "contents_mobile.jsp.title" ) )
                {
                    // strTitleConst = test[1].toString().substring(0,test[1].toString().indexOf('$'));
                    strTitleConstForMobile = test[1].toString();
                    for( int arrCount = 0 ; arrCount < arrHotelObj.size() ; arrCount++ )
                    {
                        HappyHotelBasicInfo objHappyHotelBasicInfo = (HappyHotelBasicInfo)arrHotelObj.get( arrCount );
                        strHotelId = objHappyHotelBasicInfo.getStrHotelId();
                        strTitle = objHappyHotelBasicInfo.getStrName();
                        strCommonContentForDocomo = strUrlCommon + replaceString( strValueTillEqualSign, "$m", "i" ) + strHotelId;
                        strCommonContentForAu = strUrlCommon + replaceString( strValueTillEqualSign, "$m", "au" ) + strHotelId;
                        strCommonContentForSoftbank = strUrlCommon + replaceString( strValueTillEqualSign, "$m", "y" ) + strHotelId;

                        strJSPTitle = strTitleConstForMobile.replaceAll( "\\$n", strTitle );
                        sbrDocomo = generateRORXML_Dynamic( strRorDocomo, strCommonContentForDocomo, strJSPTitle, "Weekly", "0", "sitemap" );
                        sbrAu = generateRORXML_Dynamic( strRorAu, strCommonContentForAu, strJSPTitle, "Weekly", "0", "sitemap" );
                        sbrSoftbank = generateRORXML_Dynamic( strRorSoftbank, strCommonContentForSoftbank, strJSPTitle, "Weekly", "0", "sitemap" );

                    }
                }

            }
            sbr1.append( "</channel>\n" );
            sbr1.append( "</rss>\n" );

            sbrDocomo.append( "</channel>\n" );
            sbrDocomo.append( "</rss>\n" );

            sbrAu.append( "</channel>\n" );
            sbrAu.append( "</rss>\n" );

            sbrSoftbank.append( "</channel>\n" );
            sbrSoftbank.append( "</rss>\n" );

            System.out.println( "Creating ROR for PC ->" + strOutputFileName );
            generateXMLFile( strOutputFileName, sbr1 );

            System.out.println( "Creating ROR for Docomo ->" + strOutputFileNameForDocomo );
            generateXMLFile( strOutputFileNameForDocomo, sbrDocomo );

            System.out.println( "Creating ROR for AU ->" + strOutputFileNameForAu );
            generateXMLFile( strOutputFileNameForAu, sbrAu );

            System.out.println( "Creating ROR for Softbank ->" + strOutputFileNameForSoftbank );
            generateXMLFile( strOutputFileNameForSoftbank, sbrSoftbank );

            // System.out.println("XML file for ROR is generated Successfully");
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in genRORXML()!" + e.getMessage() );
            throw e;
        }
        finally
        {
            // if the file is opened , make sure we close it
            if ( bis != null )
            {
                try
                {
                    bis.close();
                }
                catch ( IOException ioe )
                {
                }
            }
        }

    }

    /**
     * This is the main method called by the application
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {

            /* generate sitemap xml files for both PC and Mobile */
            System.out.println( "Generating SiteMap ..." );
            genSiteMap();

            /* generate ROR xml files for both PC and Mobile */
            System.out.println( "Generating ROR ..." );
            genRORXML();

            System.out.println( "** Finished creating sitemap and ror for pc and mobile **" );

            /*
             * if(args.length==0){ System.out.print("Please enter SM to generate sitemap or ROR to generate ror:"); BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); String strEnterValue = br.readLine();
             * if(strEnterValue.equalsIgnoreCase("SM")){ genSiteMap(); } else if(strEnterValue.equalsIgnoreCase("ROR")){ genRORXML(); } else{ System.out.print("Please enter correct value"); } } else{ if(args[0].equalsIgnoreCase("SM")){ genSiteMap(); } else
             * if(args[0].equalsIgnoreCase("ROR")){ genRORXML(); } else{ System.out.print("Please enter SM or ROR at command prompt to run the program "); } }
             */
        }
        catch ( Exception e )
        {
            System.out.println( "Exception catched in main()!" + e.getMessage() );
        }
    }

}
