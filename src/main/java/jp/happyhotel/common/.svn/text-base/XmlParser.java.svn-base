/*
 * @(#)XmlParser.java 1.00 2007/08/25 Copyright (C) ALMEX Inc. 2007 XMLパーサクラス
 */

package jp.happyhotel.common;

import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XMLの解釈を行うクラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/25
 */
public class XmlParser implements Serializable
{
    /**
     *
     */
    private static final long      serialVersionUID = -7180629366256594211L;

    private DocumentBuilderFactory dbfactory;
    private DocumentBuilder        builder;
    private Document               doc;

    /**
     * データを初期化します。
     */
    public XmlParser(String url)
    {
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
        }
    }

    /**
     * データを初期化します。
     */
    public XmlParser(InputStream input)
    {
        try
        {
            // ドキュメントビルダーファクトリを生成
            dbfactory = DocumentBuilderFactory.newInstance();
            // ドキュメントビルダーを生成
            builder = dbfactory.newDocumentBuilder();
            // パースを実行してDocumentオブジェクトを取得
            doc = builder.parse( input );
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * XMLエレメントの値取得
     * 
     * @param rootElement 親要素
     * @param name 取得要素
     */
    public String getElementValue(String rootElement, String name)
    {
        int i;
        String data;
        Element root;
        Element element;
        Node nd;
        NodeList list;

        data = null;
        root = doc.getDocumentElement();

        list = root.getElementsByTagName( name );

        for( i = 0 ; i < list.getLength() ; i++ )
        {
            element = (Element)list.item( i );
            nd = element.getFirstChild();
            data = nd.getNodeValue();
        }

        return(data);
    }
}
