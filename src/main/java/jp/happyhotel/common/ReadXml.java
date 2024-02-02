/*
 * @(#)OutputSwf.java 1.00 2009/06/15 Copyright (C) ALMEX Inc. 2009 flfast用xmlファイル生成クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML解釈クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/25
 */

public class ReadXml implements Serializable
{
    /**
     *
     */
    private static final long      serialVersionUID = -3222668244585585249L;
    private DocumentBuilderFactory dbfactory;
    private DocumentBuilder        builder;
    private Document               doc;
    // SmartRouting APIで使用
    private int                    resultCode;
    private String                 routeList;
    private String                 pointList;
    private int                    distance;
    private int                    time;
    private String                 nodeList;
    private String                 partList;
    private String                 partDistance;
    // Sokodoko sAPIで使用
    private String                 address;
    private String                 addressKana;
    private String                 code;
    private String                 zipCode;
    private String                 coordinate;
    private String                 boundingBox;
    private String                 ul;
    private String                 ur;
    private String                 dl;
    private String                 dr;
    private String                 scale;

    /* ----------------------▼getter▼---------------------- */

    public String getBoundingBox()
    {
        return boundingBox;
    }

    public String getScale()
    {
        return scale;
    }

    public String getUl()
    {
        return ul;
    }

    public String getUr()
    {
        return ur;
    }

    public String getDl()
    {
        return dl;
    }

    public String getDr()
    {
        return dr;
    }

    public String getCoordinate()
    {
        return coordinate;
    }

    /**
     * 住所取得
     * 
     * @return 住所を取得
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * 住所(カナ)を取得
     * 
     * @return 住所(カナ)を取得
     */
    public String getAddressKana()
    {
        return addressKana;
    }

    /**
     * コードを取得
     * 
     * @return コードを取得
     */
    public String getCode()
    {
        return code;
    }

    /**
     * 距離を取得
     * 
     * @return 距離を取得する
     */
    public int getDistanceList()
    {
        return distance;
    }

    /**
     * @return 終始点以外のノード一覧を取得する
     * @see "detailInfoの値が1の場合のみ取得可能"
     */
    public String getNodeList()
    {
        return nodeList;
    }

    /**
     * @return パートごとの距離を取得する
     * @see "detailInfoの値が1の場合のみ取得可能"
     */
    public String getPartDistance()
    {
        return partDistance;
    }

    /**
     * @return パートの一覧を取得する
     * @see "detailInfoの値が1の場合のみ取得可能"
     */
    public String getPartList()
    {
        return partList;
    }

    /**
     * @return 指定点の最寄点一覧を取得
     */
    public String getPointList()
    {
        return pointList;
    }

    /**
     * @return 検索結果コードを取得(0:ルート検索終了,-1～-6:失敗)
     * 
     */
    public int getResultCode()
    {
        return resultCode;
    }

    /**
     * @return ルート検索結果の一覧を取得
     */
    public String getRouteList()
    {
        return routeList;
    }

    /**
     * @return 所要時間の一覧を取得する
     */
    public int getTimeRequiredList()
    {
        return time;
    }

    /**
     * @return 郵便番号を取得
     */
    public String getZipCode()
    {
        return zipCode;
    }

    /* ----------------------▲getter▲---------------------- */

    /* ----------------------▼setter▼---------------------- */

    /**
     * @param address 住所をセット
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @param addressKana 住所(カナ)をセット
     */
    public void setAddressKana(String addressKana)
    {
        this.addressKana = addressKana;
    }

    /**
     * @param code コードをセット
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @param distance 距離をセットする
     */
    public void setDistanceList(int distance)
    {
        this.distance = distance;
    }

    /**
     * @param nodeList 終始点以外のノードの一覧をセットする
     */
    public void setNodeList(String nodeList)
    {
        this.nodeList = nodeList;
    }

    /**
     * @param partDistance パートごとの距離をセットする
     */
    public void setPartDistance(String partDistance)
    {
        this.partDistance = partDistance;
    }

    /**
     * @param partList パートの一覧をセットする
     */
    public void setPartList(String partList)
    {
        this.partList = partList;
    }

    /**
     * @param pointList 指定点の最寄点一覧をセット
     */
    public void setPointList(String pointList)
    {
        this.pointList = pointList;
    }

    /**
     * @param resultCode 検索結果コードをセット(0:ルート検索終了,-1～-6:失敗)
     */
    public void setResultCode(int resultCode)
    {
        this.resultCode = resultCode;
    }

    /**
     * @param routeList ルート検索結果をセットする
     */
    public void setRouteList(String routeList)
    {
        this.routeList = routeList;
    }

    /**
     * @param time 所要時間の一覧をセットする
     */
    public void setTimeRequiredList(int time)
    {
        this.time = time;
    }

    /**
     * @param zipCode 郵便番号をセット
     */
    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setScale(String scale)
    {
        this.scale = scale;
    }

    public void setUl(String ul)
    {
        this.ul = ul;
    }

    public void setUr(String ur)
    {
        this.ur = ur;
    }

    public void setDl(String dl)
    {
        this.dl = dl;
    }

    public void setDr(String dr)
    {
        this.dr = dr;
    }

    public void setBoundingBox(String boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    public void setCoordinate(String coordinate)
    {
        this.coordinate = coordinate;
    }

    /* ----------------------▲setter▲---------------------- */

    /**
     * Yahooプレミアム会員チェック処理
     * 
     * @param obj チェック対象XML
     * @return true→yahooプレミアム会員,false→yahooプレミアム非会員
     */
    public boolean checkYahooPremiumXml(Object obj)
    {
        boolean ret = false;
        Document yahooResult = null;
        Element root = null;
        Element element;
        NodeList ndListParent;
        Node ndParent;
        Node ndChild;
        Node ndGrandChild;
        NodeList ndListGrandChild;
        NodeList ndListChild;

        try
        {
            if ( obj != null )
            {
                yahooResult = (Document)obj;
                if ( yahooResult != null )
                {
                    root = doc.getDocumentElement();
                    ndListParent = root.getElementsByTagName( "ResultSet" );
                    // 先頭のノードをセットする
                    ndParent = ndListParent.item( 0 );
                    // 子ノードがあるかどうかを確認する
                    if ( ndParent.hasChildNodes() != false )
                    {
                        // 子ノードリストを取得する
                        ndListChild = ndParent.getChildNodes();
                        for( int i = 0 ; i < ndListChild.getLength() ; i++ )
                        {
                            ndChild = ndListChild.item( i );
                            // 孫ノードがあるかどうかを確認する
                            if ( ndChild.hasChildNodes() != false )
                            {
                                // 孫ノードリストにセット
                                ndListGrandChild = ndChild.getChildNodes();
                                // 孫ノードをそれぞれ取得(LLPoint)
                                for( int j = 0 ; j < ndListGrandChild.getLength() ; j++ )
                                {
                                    ndGrandChild = ndListGrandChild.item( j );
                                    // エレメントノードのみ要素を取得する
                                    if ( Node.ELEMENT_NODE == ndGrandChild.getNodeType() )
                                    {
                                        element = (Element)ndListGrandChild.item( j );
                                        if ( element.getAttributeNode( "Yp" ).getNodeValue().equals( "True" ) )
                                        {
                                            ret = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReadXml.checkYahooPremiumXml()] Exception:" + e.toString() );
        }
        finally
        {
        }

        return(ret);
    }

    /**
     * データを初期化します（コンストラクタ）
     * 
     * @param url URL
     * @see "URLからXMLをパース"
     */
    public ReadXml(String url)
    {
        // プライベート変数の初期化
        resultCode = 0;
        routeList = "";
        pointList = "";
        distance = 0;
        time = 0;
        nodeList = "";
        partList = "";
        address = "";
        addressKana = "";
        code = "";
        zipCode = "";

        try
        {
            // ドキュメントビルダーファクトリを生成
            dbfactory = DocumentBuilderFactory.newInstance();
            // ドキュメントビルダーを生成
            builder = dbfactory.newDocumentBuilder();
            // パースを実行してDocumentオブジェクトを取得
            doc = builder.parse( url );
        }
        catch ( Exception e )
        {
            Logging.info( "[ ReadXml( String url ) ] Exception:" + e.toString() );
        }
    }

    /**
     * XMLエレメントの値取得(ルート検索の結果)
     * 
     */
    public boolean getElementValue()
    {
        boolean ret;

        ret = false;
        try
        {
            // resultCodeの取得
            this.resultCode = getSamrtRoutingInfo();
            Logging.info( "resultCode:" + this.resultCode );
            // ルートリストの取得
            this.routeList = getElementValueStr( "RouteList" );
            // ポイントリストの取得
            this.pointList = getElementValueStr( "PointList" );
            // ノードリストの取得
            this.nodeList = getElementValueStr( "NodeList" );
            // 距離の取得
            this.distance = getElementValueInt( "DistanceList", "distance" );
            // 所要時間の取得
            this.time = getElementValueInt( "TimeRequiredList", "time" );
            // パートリストの取得
            getPartListElementValue();
        }
        catch ( Exception e )
        {
            Logging.error( "[ReadXml.getElementValue()] Exception:" + e.toString() );
        }
        if ( routeList.compareTo( "" ) != 0 )
        {
            ret = true;
        }
        return(ret);

    }

    /**
     * XMLエレメントの値取得(緯度経度から住所検索)
     * 
     */
    public boolean getElementAddr()
    {
        boolean ret;
        int i;
        int j;
        int count;
        Element root;
        Element element;
        Node ndParent;
        Node ndChild;
        Node ndGrandChild;
        NodeList ndListParent;
        NodeList ndListChild;
        NodeList ndListGrandChild;

        ret = false;
        count = 0;
        root = doc.getDocumentElement();

        try
        {
            Logging.debug( "=============================[readXML] getElementAddr debugging START" );
            // タグからRouteListを取得する
            // ndListParent = root.getElementsByTagName( "ResultList" );
            ndListParent = root.getElementsByTagName( "Feature" );
            // 先頭のノードをセットする
            ndParent = ndListParent.item( 0 );
            // 子ノードがあるかどうかを確認する
            if ( ndParent.hasChildNodes() != false )
            {
                // 子ノードリストを取得する
                ndListChild = ndParent.getChildNodes();

                // 子のノードをそれぞれ取得(RoutingPoint)
                for( i = 0 ; i < ndListChild.getLength() ; i++ )
                {
                    // それぞれの子ノードをセット
                    ndChild = ndListChild.item( i );

                    if ( i == 2 )
                    {
                        this.address = ndChild.getTextContent();
                    }

                    if ( i == 3 )
                    {
                        ndGrandChild = ndListChild.item( i );
                        this.coordinate = ndGrandChild.getChildNodes().item( 1 ).getTextContent();
                        this.boundingBox = ndGrandChild.getChildNodes().item( 2 ).getTextContent();
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReadXml.getElementAddr()] Exception:" + e.toString() );

        }
        if ( this.address.compareTo( "" ) != 0 )
        {
            ret = true;
        }
        return(ret);

    }

    /**
     * XMLエレメントの値取得(共通項目の取得)
     * 
     * @param name 取得要素（親のタグネーム）
     * @return ノードから取得した文字列
     */
    public String getElementValueStr(String name)
    {
        int i;
        int j;
        int count;
        String data;
        Element root;
        Element element;
        Node ndParent;
        Node ndChild;
        Node ndGrandChild;
        NodeList ndListParent;
        NodeList ndListChild;
        NodeList ndListGrandChild;

        data = "";
        count = 0;
        root = doc.getDocumentElement();
        try
        {
            // タグからRouteListを取得する
            ndListParent = root.getElementsByTagName( name );
            // 先頭のノードをセットする
            ndParent = ndListParent.item( 0 );
            // 子ノードがあるかどうかを確認する
            if ( ndParent.hasChildNodes() != false )
            {
                // 子ノードリストを取得する
                ndListChild = ndParent.getChildNodes();

                // 子のノードをそれぞれ取得(RoutingPoint)
                for( i = 0 ; i < ndListChild.getLength() ; i++ )
                {
                    // それぞれの子ノードをセット
                    ndChild = ndListChild.item( i );
                    // 孫ノードがあるかどうかを確認する
                    if ( ndChild.hasChildNodes() != false )
                    {
                        // 孫ノードリストにセット
                        ndListGrandChild = ndChild.getChildNodes();

                        // 孫ノードをそれぞれ取得(LLPoint)
                        for( j = 0 ; j < ndListGrandChild.getLength() ; j++ )
                        {
                            ndGrandChild = ndListGrandChild.item( j );
                            // エレメントノードのみ要素を取得する
                            if ( Node.ELEMENT_NODE == ndGrandChild.getNodeType() )
                            {
                                element = (Element)ndListGrandChild.item( j );
                                if ( count > 0 )
                                {
                                    data = data + ",";
                                }
                                data += element.getAttributeNode( "lat" ).getNodeValue() + "," +
                                        element.getAttributeNode( "lon" ).getNodeValue();
                                count++;
                            }
                        }
                    }
                }
            }
            else
            {
                data = "";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReadXml.getElementValueStr()] " + name + " Exception:" + e.toString() );
        }
        return(data);
    }

    /**
     * XMLエレメントの値取得(共通項目の取得)
     * 
     * @param parentName 取得要素（親のタグネーム）
     * @param name 取得要素（）
     * @return ノードから取得した文字列
     */
    public int getElementValueInt(String parentName, String name)
    {
        int i;
        int count;
        int data;
        Element root;
        Element element;
        Node ndParent;
        Node ndChild;
        NodeList ndListParent;
        NodeList ndListChild;

        data = 0;
        count = 0;
        root = doc.getDocumentElement();
        try
        {
            // タグからRouteListを取得する
            ndListParent = root.getElementsByTagName( parentName );
            // 先頭のノードをセットする
            ndParent = ndListParent.item( 0 );
            // 子ノードがあるかどうかを確認する
            if ( ndParent.hasChildNodes() != false )
            {
                // 子ノードリストを取得する
                ndListChild = ndParent.getChildNodes();

                // 子のノードをそれぞれ取得(RoutingPoint)
                for( i = 0 ; i < ndListChild.getLength() ; i++ )
                {
                    // それぞれの子ノードをセット
                    ndChild = ndListChild.item( i );
                    // エレメントノードのみ要素を取得する
                    if ( Node.ELEMENT_NODE == ndChild.getNodeType() )
                    {
                        element = (Element)ndListChild.item( i );
                        data += Integer.parseInt( element.getAttributeNode( name ).getNodeValue() );
                        count++;
                    }
                }
            }
            else
            {
                data = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReadXml.getElementValueInt()] " + parentName + " Exception:" + e.toString() );
        }
        return(data);
    }

    /**
     * XMLエレメントの値取得(共通項目の取得)
     * 
     * @return ノードから取得した文字列
     */
    public int getSamrtRoutingInfo()
    {
        int data;
        Element root;
        Node node;
        NamedNodeMap nodeMap;

        data = 0;
        root = doc.getDocumentElement();
        try
        {
            // 最初の子ノードを取得
            if ( root.hasChildNodes() != false )
            {
                node = root.getFirstChild();
                nodeMap = node.getAttributes();
                if ( nodeMap != null )
                {
                    node = nodeMap.getNamedItem( "resultCode" );
                    Logging.info( "nodeValue:" + node.getNodeValue() );
                }
            }
        }
        catch ( Exception e )
        {
            // data = -1;
            Logging.error( "[ReadXml.getSamrtRoutingInfo()] Exception:" + e.toString() );
        }
        return(data);
    }

    /**
     * XMLエレメントの値取得(共通項目の取得)
     * 
     * @return ノードから取得した文字列
     */
    public boolean getPartListElementValue()
    {
        boolean ret;
        int i;
        int j;
        int k;
        int m;
        int count;
        int nCount;
        String data;
        String strDistance;
        Element root;
        Element element;
        Node ndParent;
        Node ndChild;
        Node ndGrandChild;
        Node ndGreatGrandChild;
        Node ndTerminal;
        NodeList ndListParent;
        NodeList ndListChild;
        NodeList ndListGrandChild;
        NodeList ndListGreatGrandChild;
        NodeList ndListTerminal;

        ret = false;
        data = "";
        strDistance = "";
        count = 0;
        nCount = 0;
        root = doc.getDocumentElement();
        try
        {
            // タグからRouteListを取得する
            ndListParent = root.getElementsByTagName( "PartList" );
            // 先頭のノードをセットする
            ndParent = ndListParent.item( 0 );
            // 子ノードがあるかどうかを確認する
            if ( ndParent.hasChildNodes() != false )
            {
                // 子ノードリストを取得する
                ndListChild = ndParent.getChildNodes();

                // 子のノードをそれぞれ取得(RoutingPoint)
                for( i = 0 ; i < ndListChild.getLength() ; i++ )
                {
                    // それぞれの子ノードをセット
                    ndChild = ndListChild.item( i );
                    // 孫ノードがあるかどうかを確認する
                    if ( ndChild.hasChildNodes() != false )
                    {
                        // 孫ノードリストにセット
                        ndListGrandChild = ndChild.getChildNodes();

                        count = 0;
                        // 孫ノードをそれぞれ取得(LLPoint)
                        for( j = 0 ; j < ndListGrandChild.getLength() ; j++ )
                        {
                            ndGrandChild = ndListGrandChild.item( j );
                            // エレメントノードのみ要素を取得する
                            if ( Node.ELEMENT_NODE == ndGrandChild.getNodeType() )
                            {
                                element = (Element)ndListGrandChild.item( j );
                                if ( count > 0 )
                                {
                                    strDistance = strDistance + ",";
                                }
                                strDistance += element.getAttributeNode( "distance" ).getNodeValue();
                                count++;

                                // さらに子のノードがあるかどうかを確認する
                                if ( ndGrandChild.hasChildNodes() != false )
                                {
                                    ndListGreatGrandChild = ndGrandChild.getChildNodes();

                                    // 子ノード数分繰り返す
                                    for( k = 0 ; k < ndListGreatGrandChild.getLength() ; k++ )
                                    {
                                        // ひ孫ノードにセット
                                        ndGreatGrandChild = ndListGreatGrandChild.item( k );
                                        // ひ孫ノードの子ノードが存在するか
                                        if ( ndGreatGrandChild.hasChildNodes() != false )
                                        {
                                            ndListTerminal = ndGreatGrandChild.getChildNodes();

                                            for( m = 0 ; m < ndListTerminal.getLength() ; m++ )
                                            {
                                                ndTerminal = ndListTerminal.item( m );
                                                // エレメントノードのみ要素を取得する
                                                if ( Node.ELEMENT_NODE == ndTerminal.getNodeType() )
                                                {
                                                    element = (Element)ndListTerminal.item( m );

                                                    if ( nCount > 0 )
                                                    {
                                                        data += ",";
                                                    }
                                                    data += element.getAttributeNode( "lat" ).getNodeValue() + "," +
                                                            element.getAttributeNode( "lon" ).getNodeValue();
                                                    nCount++;

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        this.partDistance = strDistance;
                        this.partList = data;
                        ret = true;
                    }
                }
            }
            else
            {
                this.partDistance = "";
                this.partList = "";
                ret = false;
            }
        }
        catch ( Exception e )
        {
            this.partDistance = "";
            this.partList = "";
            Logging.error( "[ReadXml.getPartListElementValue()] Exception:" + e.toString() );
            return(false);
        }
        return(ret);
    }

    /**
     * XMLエレメントの値取得(YahooXmlから座標取得)
     * 
     */
    public boolean getElementCoordinate()
    {
        boolean ret;
        int i;
        int j;
        int count;
        Element root;
        Element element;
        Node ndParent;
        Node ndChild;
        Node ndGrandChild;
        NodeList ndListParent;
        NodeList ndListChild;
        NodeList ndListGrandChild;

        ret = false;
        count = 0;
        root = doc.getDocumentElement();

        try
        {
            Logging.debug( "=============================[readXML] getElementCoordinate debugging START" );
            // タグからRouteListを取得する
            // ndListParent = root.getElementsByTagName( "ResultList" );
            ndListParent = root.getElementsByTagName( "Result" );
            // 先頭のノードをセットする
            ndParent = ndListParent.item( 0 );
            // 子ノードがあるかどうかを確認する
            if ( ndParent.hasChildNodes() != false )
            {
                // 子ノードリストを取得する
                ndListChild = ndParent.getChildNodes();
                this.coordinate = ndListChild.item( 0 ).getTextContent();
                this.ul = ndListChild.item( 1 ).getTextContent();
                this.ur = ndListChild.item( 2 ).getTextContent();
                this.dl = ndListChild.item( 3 ).getTextContent();
                this.dr = ndListChild.item( 4 ).getTextContent();
                this.scale = ndListChild.item( 5 ).getTextContent();

                Logging.info( "=============================[readXML] getElementCoordinate :" + this.coordinate );
                Logging.info( "=============================[readXML] getElementCoordinate ur: " + this.ur );
                Logging.info( "=============================[readXML] getElementCoordinate dl: " + this.dl );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReadXml.getElementAddr()] Exception:" + e.toString() );

        }
        if ( this.coordinate.compareTo( "" ) != 0 )
        {
            ret = true;
        }
        return(ret);

    }
}
