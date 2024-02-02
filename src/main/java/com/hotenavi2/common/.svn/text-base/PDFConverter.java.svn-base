package com.hotenavi2.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/*
 * singleton class for converting excel doc to pdf
 */
public class PDFConverter
{

    private final String        confFilePass     = "/etc/hotenavi/pdfconverter.conf";
    private static Counter      counter;
    private static PDFConverter myself;
    // PDF Creator server installation default pdf output dir
    private String              tempFolder       = "C:\\ALMEX\\PDFWORK\\PDFCreater\\Temp\\";
    private String              pdfFileOutputLoc = "C:\\ALMEX\\PDFWORK\\PDFCreater\\PDFs\\";
    private String              convert2PDF      = "wscript C:\\ALMEX\\WWW\\Convert2PDF.vbs ";

    private long                pdfLength;
    private static long         pdfCounter;
    private FileInputStream     propfile         = null;
    private Properties          config           = null;

    private PDFConverter()
    {
        String configStr;
        String execString = "net use \\\\hnwbow01\\pdfwork /user:admin AGrh8u2b";

        try
        {
            propfile = new FileInputStream( confFilePass );
            config = new Properties();
            config.load( propfile );
            propfile.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        try
        {
            configStr = config.getProperty( "tempFolder" );
            System.out.println( "tempFolder=" + configStr );
            if ( configStr != null )
            {
                if ( configStr.compareTo( "" ) != 0 )
                {
                    tempFolder = configStr;
                }
            }
            configStr = config.getProperty( "pdfFileOutputLoc" );
            System.out.println( "pdfFileOutputLoc=" + configStr );
            if ( configStr != null )
            {
                if ( configStr.compareTo( "" ) != 0 )
                {
                    tempFolder = configStr;
                }
            }
            configStr = config.getProperty( "convert2PDF" );
            System.out.println( "convert2PDF=" + configStr );
            if ( configStr != null )
            {
                if ( configStr.compareTo( "" ) != 0 )
                {
                    convert2PDF = configStr;
                }
            }
            configStr = config.getProperty( "serverDrive" );
            System.out.println( "serverDrive=" + configStr );
            if ( configStr != null )
            {
                if ( configStr.compareTo( "" ) != 0 )
                {
                    execString = configStr;
                }
            }

            // "net use \\\\mars\\pdfwork /user:shiiya blackbass "
            // ÉhÉâÉCÉuäÑÇËìñÇƒêÊÇÃéÊìæ
            System.out.println( execString );
            Runtime rt = Runtime.getRuntime();
            Process ps = rt.exec( execString );
            ps.destroy();
        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
        }

        deleteTempFiles( tempFolder );
        deleteTempFiles( pdfFileOutputLoc );
        counter = new Counter();

    }

    private void deleteTempFiles(String folderPath)
    {
        File file = null;
        File folder = new File( folderPath );

        // create temp dir if it does not exist
        if ( !folder.exists() )
        {
            folder.mkdirs();
        }
        // delete all files from the temp dir if exist
        else
        {
            String[] str = folder.list();
            for( int i = 0 ; i < str.length ; i++ )
            {
                file = new File( tempFolder + str[i] );
                System.out.println( file.delete() );
            }
        }

    }

    public static synchronized PDFConverter getInstance()
    {
        if ( myself == null )
        {
            myself = new PDFConverter();
        }
        pdfCounter = counter.getCounter();

        return myself;
    }

    /**
     * function to convert the sheet present in excel workbook into pdf
     * 
     * @param xlsFileFullPathName absolute path of the excel file
     * @param sheetNum sheet number of the workbook to be converted into pdf (starting from 1)
     * @return location of the converted pdf file
     */
    public String convert(String xlsFileFullPathName, int sheetNum, HttpServletResponse response) throws Exception
    {
        String pdfFileName = null;
        File pdfFile;
        String tempXLSFileName;
        File tempXLSFile;

        // step 1: copy the xls file to temp folder

        System.out.println( "filename : " + xlsFileFullPathName );
        System.out.println( "Counter ->" + pdfCounter );
        String outputFileName = xlsFileFullPathName.substring( (xlsFileFullPathName.lastIndexOf( "\\" )) + 1 );
        tempXLSFileName = outputFileName + "." + pdfCounter;
        System.out.println( "tempXLSFileName ->" + tempXLSFileName );

        copyToTemp( xlsFileFullPathName, tempFolder, tempXLSFileName );

        tempXLSFile = new File( tempFolder + tempXLSFileName );
        System.out.println( tempXLSFile.getName() );
        System.out.println( "Temporary xls file ->" + tempXLSFile.getAbsolutePath() );

        pdfFileName = tempXLSFileName.substring( 0, tempXLSFileName.indexOf( "." ) );
        pdfFileName = pdfFileName + "." + pdfCounter;

        // step 2: convert the xls to pdf
        String execString = convert2PDF + tempXLSFile.getAbsolutePath() + " " + pdfFileName + " " + sheetNum;
        // String execString = "C:\\ALMEX\\WWW\\CreatePDF.bat "+tempXLSFile.getAbsolutePath()+ " "+sheetNum;
        System.out.println( execString );
        Runtime rt = Runtime.getRuntime();
        Process ps = rt.exec( execString );

        pdfFile = new File( pdfFileOutputLoc + pdfFileName + ".pdf" );
        System.out.println( pdfFile.getAbsolutePath() );

        // step 3: wait for the pdf file to be created
        int retryCount = 0;
        long prev_lastmodifiedtime = 0;
        long current_lastmodifiedtime = 0;
        while( true )
        {
            // ps.waitFor();
            // Thread.sleep(2000);
            if ( pdfFile.exists() )
            {
                /**
                 * if ( prev_lastmodifiedtime !=0 ) {
                 * current_lastmodifiedtime = pdfFile.lastModified();
                 * 
                 * if ( current_lastmodifiedtime == prev_lastmodifiedtime) {
                 * break;
                 * }
                 * }
                 **/
                prev_lastmodifiedtime = pdfFile.lastModified();
                System.out.println( prev_lastmodifiedtime );

                pdfLength = pdfFile.length();
                break;
            }

            try
            {
                Thread.sleep( 500 );
            }
            catch ( Exception ex )
            {
                // System.out.println(" time out ");
            }
            retryCount++;
            if ( retryCount >= 500 )
            {
                System.out.println( "going to sleep for " + retryCount + " times ->" + pdfFile.getName() );
                throw new Exception();
            }

        }

        // step 4: delete the xls from the temp folder

        sendPDF( pdfFileOutputLoc + pdfFileName + ".pdf", response );
        deleteFile( tempXLSFile.getAbsolutePath() );
        return pdfFile.getAbsolutePath();
    }

    /**
     * Copies files from one source folder to another target folder (Temp)
     * 
     * @param inputFile the source file with absolute path
     * @param outputfile the destination file with absolute path
     */
    private void copyToTemp(String inputFile, String outputFileLoc, String outputFileName)
            throws Exception
    {

        String outputFile = outputFileLoc + outputFileName;

        FileInputStream fis = null;
        FileOutputStream fos = null;

        FileChannel source = null;
        FileChannel target = null;

        System.out.println( "inputFile ->" + inputFile );
        System.out.println( "outputFile ->" + outputFile );

        try
        {
            File in = new File( inputFile );
            File out = new File( outputFile );

            fis = new FileInputStream( in );
            fos = new FileOutputStream( out );

            source = fis.getChannel();
            target = fos.getChannel();

            source.transferTo( 0, source.size(), target );

        }
        finally
        {
            if ( fis != null )
                fis.close();
            if ( source != null )
                source.close();
            if ( fos != null )
                fos.close();
            if ( target != null )
                target.close();
        }

    }

    /**
     * writing files on output response stream
     * 
     * @param pdf file location
     * @param response stream
     * 
     */

    public void sendPDF(String PDFOutputFileLoc, HttpServletResponse response) throws Exception
    {

        int count = 0;
        File pdf1 = new File( PDFOutputFileLoc );
        String outputFileName = PDFOutputFileLoc.substring( (PDFOutputFileLoc.lastIndexOf( "\\" )) + 1 );
        ServletOutputStream stream = null;
        BufferedInputStream buf = null;
        FileInputStream input = null;
        try
        {
            stream = response.getOutputStream();
            response.setContentType( "application/pdf" );
            // response.setHeader("Cache-Control", "private");
            System.out.println( "(int)pdf1.length()=" + (int)pdf1.length() );
            // response.setContentLength((int)pdf1.length());
            response.setHeader( "Content-disposition", "inline; filename=\"" + outputFileName + "\"" );
            input = new FileInputStream( pdf1 );
            buf = new BufferedInputStream( input );
            int readBytes = 0;

            while( (readBytes = buf.read()) != -1 )
            {
                stream.write( readBytes );
            }
            System.out.println( "PDFOutputFileLoc(count=" + count + ")" );

        }
        catch ( IOException ioe )
        {
            // System.out.println("PDFConverter............"+PDFOutputFileLoc);
            // System.out.println("Closing file ->"+PDFOutputFileLoc);

            if ( input != null )
            {
                input.close();
            }
            deleteFile( PDFOutputFileLoc );
            throw new ServletException( ioe.getMessage() );
        }
        finally
        {
            if ( stream != null )
                stream.close();
            if ( buf != null )
                buf.close();
            if ( input != null )
            {
                // System.out.println("Closing file ->"+PDFOutputFileLoc);
                input.close();
            }

        }

    }

    /**
     * delete files from specified location
     * 
     * @param file path
     */
    public void deleteFile(String tempFilePath)
    {

        File file = null;
        boolean isDeleted = false;
        file = new File( tempFilePath );
        System.out.println( "deleting files : " );
        if ( file.exists() )
        {

            isDeleted = file.delete();
            System.out.println( "file " + tempFilePath + " deleted : " + isDeleted );

        }

    }
}

class Counter
{
    long count = 0;

    public synchronized long getCounter()
    {
        ++count;
        return count;
    }
}
