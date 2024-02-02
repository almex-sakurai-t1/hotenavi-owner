package jp.happyhotel.others;

import java.io.OutputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jp.happyhotel.common.Logging;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public class GenerateXmlTouch implements Serializable
{
    String                 encode        = "";
    DocumentBuilderFactory factory;
    DocumentBuilder        builder;
    Document               document;
    Element                rootNode      = null;
    Element                resultNode    = null;
    Element                pointNode     = null;
    Element                messageNode   = null;
    Element                registUrlNode = null;
    Element                fileUrlNode1  = null;
    Element                fileUrlNode2  = null;

    public GenerateXmlTouch()
    {
        try
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder(); // DocumentBuilderインスタンス
            document = builder.newDocument(); // Documentの生成
            encode = "";
        }
        catch ( Exception e )
        {
            Logging.error( "[GenerateXmlTouch] Exception=" + e.toString() );
        }
    }

    public String getEncode()
    {
        return this.encode;
    }

    public void setEncode(String encode)
    {
        this.encode = encode;
    }

    /** ルートノード作成 **/
    public void setRootName(String methodName)
    {
        this.rootNode = document.createElement( methodName );
    }

    /** ルートノードの値をセット **/
    public void setRootValue(String methodValue)
    {
        this.rootNode.appendChild( document.createTextNode( methodValue ) );
    }

    /** 結果ノード作成 **/
    public void setResultName(String resultName)
    {
        this.resultNode = document.createElement( resultName );
    }

    /** 結果ノードの値をセット **/
    public void setResultValue(String resultValue)
    {
        this.resultNode.appendChild( document.createTextNode( resultValue ) );
    }

    /** ポイントノード作成 **/
    public void setPointName(String pointName)
    {
        this.pointNode = document.createElement( pointName );
    }

    /** メッセージポイントノードの値をセット **/
    public void setPointValue(String pointValue)
    {
        this.pointNode.appendChild( document.createTextNode( pointValue ) );
    }

    /** メッセージノード作成 **/
    public void setMessageName(String messageName)
    {
        this.messageNode = document.createElement( messageName );
    }

    /** メッセージポイントノードの値をセット **/
    public void setMessageValue(String messageValue)
    {
        this.messageNode.appendChild( document.createTextNode( messageValue ) );
    }

    /** 登録URLノード作成 **/
    public void setRegistUrlName(String registUrlName)
    {
        this.registUrlNode = document.createElement( registUrlName );
    }

    /** 登録URLノードの値をセット **/
    public void setRegistUrlValue(String registUrlValue)
    {
        this.registUrlNode.appendChild( document.createTextNode( registUrlValue ) );
    }

    /** ファイルURLノード作成 **/
    public void setFileUrl1Name(String fileUrlName1)
    {
        this.fileUrlNode1 = document.createElement( fileUrlName1 );
    }

    /** ファイルURLノードの値をセット **/
    public void setFileUrl1Value(String fileUrlValue1)
    {
        this.fileUrlNode1.appendChild( document.createTextNode( fileUrlValue1 ) );
    }

    /** ファイルURLノード作成 **/
    public void setFileUrl2Name(String fileUrlName2)
    {
        this.fileUrlNode2 = document.createElement( fileUrlName2 );
    }

    /** ファイルURLノードの値をセット **/
    public void setFileUrl2Value(String fileUrlValue2)
    {
        this.fileUrlNode2.appendChild( document.createTextNode( fileUrlValue2 ) );
    }

    /**
     * XML出力メソッド
     * 
     * @param response HttpServletResponse
     * @see 必ずrootNodeをセットしておくこと
     * */
    public void xmlOutput(OutputStream response)
    {
        if ( this.rootNode != null )
        {
            document.appendChild( rootNode );
            if ( this.resultNode != null )
            {
                this.rootNode.appendChild( this.resultNode );
            }
            if ( this.pointNode != null )
            {
                this.rootNode.appendChild( this.pointNode );
            }
            if ( this.messageNode != null )
            {
                this.rootNode.appendChild( this.messageNode );
            }
            if ( this.registUrlNode != null )
            {
                this.rootNode.appendChild( this.registUrlNode );
            }

            if ( this.fileUrlNode1 != null )
            {
                this.rootNode.appendChild( this.fileUrlNode1 );
            }
            if ( this.fileUrlNode2 != null )
            {
                this.rootNode.appendChild( this.fileUrlNode2 );
            }

            try
            {
                TransformerFactory tff = TransformerFactory.newInstance();
                Transformer tf = tff.newTransformer();
                tf.setOutputProperty( "encoding", this.encode );

                // インデントを行う
                tf.setOutputProperty( OutputKeys.INDENT, "yes" );
                tf.setOutputProperty( OutputKeys.METHOD, "xml" );

                // インデントの文字数
                tf.setOutputProperty( OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4" );
                // xmlファイルを出力
                tf.transform( new DOMSource( document ), new StreamResult( response ) );

            }
            catch ( Exception e )
            {
                Logging.error( "[GenerateXmlTouch.xmlOutput()] Exception=" + e.toString() );
            }
        }
        else
        {
        }

    }
}
