package jp.happyhotel.common;

import java.io.Serializable;

public class TimeParseYahooWallet implements Serializable
{

    public String cutDate = "";
    public String cutTime = "";

    public String getDate()
    {
        return cutDate;
    }

    public String getTime()
    {
        return cutTime;
    }

    public boolean parseTime(String time)
    {
        boolean ret = false;
        int nIndex = -1;
        int nIndex2 = -1;

        Logging.info( "[TimeParseYahooWallet] time:" + time );

        // timeÇì˙ïtÅAéûçèÇ…ï™âÇ∑ÇÈ
        time = time.replaceAll( ":", "" );
        Logging.info( "[TimeParseYahooWallet] time:" + time );

        nIndex = time.indexOf( "T" );
        Logging.info( "[TimeParseYahooWallet] nIndex:" + nIndex );

        nIndex2 = time.indexOf( "+" );
        if ( nIndex2 == -1 )
        {
            nIndex2 = time.indexOf( " " );
        }
        Logging.info( "[TimeParseYahooWallet] nIndex2:" + nIndex2 );
        if ( nIndex != -1 && nIndex2 != -1 )
        {
            cutDate = time.substring( 0, nIndex );
            Logging.info( "[TimeParseYahooWallet] cutDate:" + cutDate );
            cutTime = time.substring( nIndex + 1, nIndex2 );
            Logging.info( "[TimeParseYahooWallet] cutTime:" + cutTime );

            cutDate = cutDate.replace( "-", "" );
            Logging.info( "[TimeParseYahooWallet] cutDate:" + cutDate );

            if ( CheckString.numCheck( cutTime ) == false )
            {
                ret = false;
            }
            else if ( DateEdit.checkDate( cutDate ) == false ||
                    DateEdit.checkTime( Integer.parseInt( cutTime ) ) == false )
            {
                ret = false;
            }
            else
            {
                ret = true;
            }
        }
        else
        {
            ret = false;
        }
        return ret;
    }
}
