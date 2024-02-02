package jp.happyhotel.others;

import java.awt.Color;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.UserAgent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartServlet extends HttpServlet implements javax.servlet.Servlet
{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String QuestionId = null;
        int Id = 0;
        boolean ret = true;
        QuestionAnswer quesAns = null;
        String BranchTitle = null;
        String TitlePie = null;
        String TitleBar = null;
        int value = 0;
        int graphStyle = 0;
        int graphDispType = 0;
        int totalAns = 0;

        quesAns = new QuestionAnswer();
        DefaultPieDataset pieDataset = null;
        DefaultCategoryDataset barDataset = null;
        JFreeChart chart = null;

        QuestionId = request.getParameter( "id" );

        if ( QuestionId != null )
        {
            if ( CheckString.numCheck( QuestionId ) != false )
            {
                Id = Integer.parseInt( QuestionId );
                ServletOutputStream out = response.getOutputStream();

                try
                {
                    ret = quesAns.getRealTimeQuestionAnswer( Id );

                    if ( ret != false )
                    {

                        graphStyle = quesAns.getMasterQuestionData()[0].getGraphStyle();
                        graphDispType = quesAns.getMasterQuestionData()[0].getGraphDispKind();

                        if ( graphStyle == 0 )
                        {

                            pieDataset = new DefaultPieDataset();
                            for( int i = 0 ; i < quesAns.getCount() ; i++ )
                            {
                                TitlePie = quesAns.getMasterQuestionData()[i].getQuestionTitle();
                                BranchTitle = quesAns.getMasterQuestionBranch()[i].getBranchTitle();
                                value = quesAns.getCollectCount()[i];
                                totalAns = totalAns + value;
                                pieDataset.setValue( BranchTitle, value );

                            }
                            if ( graphDispType == 0 )
                            {
                                // 数字と％表記
                                chart = ChartFactory.createPieChart( "", pieDataset, true, false, false );

                                // グラフの背景色を設定する
                                chart.setBackgroundPaint( Color.white );

                                final PiePlot plot = (PiePlot)chart.getPlot();

                                // ラベルの表示形式　{0}：タイトル、{1}：応募数、{2}：応募数(%表記)
                                plot.setLabelGenerator( new StandardPieSectionLabelGenerator( "{0}\r\n{1}票({2})" ) );
                                // グラフの描画領域の背景色を設定する
                                plot.setBackgroundPaint( Color.LIGHT_GRAY );

                            }
                        }

                    }

                    try
                    {
                        if ( UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_DOCOMO )
                        {
                            response.setContentType( "image/jpeg" );

                            ChartUtilities.writeChartAsJPEG( out, chart, 238, 280 );

                        }
                        else if ( UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_AU )

                        {
                            response.setContentType( "image/png" );
                            ChartUtilities.writeChartAsPNG( out, chart, 238, 280 );

                        }
                        else if ( UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SOFTBANK )
                        {

                            response.setContentType( "image/png" );
                            ChartUtilities.writeChartAsPNG( out, chart, 238, 280 );

                        }
                        else if ( UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_PC )
                        {

                            response.setContentType( "image/jpeg" );
                            ChartUtilities.writeChartAsJPEG( out, chart, 600, 360 );

                        }
                        else
                        {

                            response.setContentType( "image/jpeg" );
                            ChartUtilities.writeChartAsJPEG( out, chart, 600, 360 );

                        }

                    }
                    catch ( Exception exc )
                    {
                        System.err.println( "Error writing image to file" );
                        System.out.println( "error" + exc.getMessage() );
                    }

                }
                catch ( Exception e )
                {
                    System.out.println( e.toString() );
                }

            } // End Method

        }
    }

    public float Round(float Rval, int Rpl)
    {
        float p = (float)Math.pow( 10, Rpl );
        Rval = Rval * p;
        float tmp = Math.round( Rval );
        return (float)tmp / p;
    }

}// End Class

