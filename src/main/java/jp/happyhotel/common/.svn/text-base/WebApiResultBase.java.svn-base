package jp.happyhotel.common;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

// import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public abstract class WebApiResultBase
{
    protected XmlTag root;

    public void setRootNode(String rootNodeName)
    {
        setRootNode( XmlTag.createXmlTag( rootNodeName ) );
        return;
    }

    public void setRootNode(XmlTag rootTag)
    {
        root = rootTag;
        return;
    }

    protected abstract void initXmlNodeInfo();

    // XML生成処理
    public String createXml()
    {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document document;
        String ret = null;

        try
        {
            initXmlNodeInfo();
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder(); // DocumentBuilderインスタンス
            document = builder.newDocument(); // Documentの生成

            if ( root != null )
            {
                Element rootNode = _constructElement( root, document );
                if ( rootNode != null )
                {
                    document.appendChild( rootNode );
                }

                DOMSource domSource = new DOMSource( document );
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult( writer );
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
                transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
                transformer.setOutputProperty( OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4" );
                transformer.transform( domSource, result );
                ret = writer.toString();
                // 改行コードなどを削除
                ret = ReplaceString.replaceApiBr( ret );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[WebApiResultBase.createXml()]Exception:" + e.toString() );
        }

        return(ret);
    }

    public String createXml(String encode)
    {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document document;
        String ret = null;

        try
        {
            initXmlNodeInfo();
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder(); // DocumentBuilderインスタンス
            document = builder.newDocument(); // Documentの生成

            if ( root != null )
            {
                Element rootNode = _constructElement( root, document );
                if ( rootNode != null )
                {
                    document.appendChild( rootNode );
                }

                DOMSource domSource = new DOMSource( document );
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult( writer );
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
                transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
                transformer.setOutputProperty( OutputKeys.ENCODING, encode );
                transformer.setOutputProperty( OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4" );
                transformer.transform( domSource, result );
                ret = writer.toString();
                // 改行コードなどを削除
                ret = ReplaceString.replaceApiBr( ret );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[WebApiResultBase.createXml()]Exception:" + e.toString() );
        }

        return(ret);
    }

    // ルート直下のタグ作成
    protected final XmlTag createRootChild(String tagName)
    {
        XmlTag ret = createRootChild( tagName, "" );
        return(ret);
    }

    protected final XmlTag createRootChild(String tagName, String tagValue)
    {
        XmlTag ret = XmlTag.createXmlTag( tagName, tagValue );
        XmlTag.setParent( root, ret );
        // ret.setParent( root );
        return(ret);
    }

    // 要素構築
    static private Element _constructElement(XmlTag xmlTag, Document document)
    {
        Element ret = null;
        while( true )
        {
            if ( document == null )
            {
                break;
            }
            if ( xmlTag == null )
            {
                break;
            }
            if ( !xmlTag.isValidTag() )
            {
                break;
            }
            ret = document.createElement( xmlTag.getName() );

            // 属性セット
            _setAttributes( xmlTag, ret );

            // 値セット
            if ( xmlTag.getHasValue() )
            {
                ret.setTextContent( xmlTag.getValue() );
            }

            if ( !xmlTag.hasChild() )
            {
                break;
            }

            // 子要素セット
            ArrayList<XmlTag> children = xmlTag.getChildren();
            for( int i = 0 ; i < children.size() ; i++ )
            {
                Element addElement = _constructElement( children.get( i ), document );
                if ( addElement != null )
                {
                    ret.appendChild( addElement );
                }
            }

            break;
        }

        return(ret);
    }

    // 属性セット処理
    static private void _setAttributes(XmlTag xmlTag, Element element)
    {
        while( true )
        {
            if ( !xmlTag.hasAttribte() )
            {
                break;
            }
            // 属性セット
            ArrayList<XmlAttribute> attrs = xmlTag.getAttributes();
            for( int i = 0 ; i < attrs.size() ; i++ )
            {
                element.setAttribute( attrs.get( i ).getName(), attrs.get( i ).getValue() );
            }
            break;
        }
        return;
    }
}
