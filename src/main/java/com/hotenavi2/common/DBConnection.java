package com.hotenavi2.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.sql.DataSource;

public class DBConnection implements Serializable
{
	private static final long serialVersionUID = 239312761897519395L;
	private static String jdbcds = null;
	/**
	 * DataSource Object
	 */
	private static DataSource  objDS = null;
	/**
	 * JNDI root context
	 */
	private static Context initCtx = null;
    private static Properties prop;
    private static String     driver;
    private static String     url;
    private static String     user;
    private static String     password;

	static
	{
	      try
	        {
	            File file = new File( "/etc/hotenavi/dbconnect.conf" );
	            if (!file.isFile()) {
	                file = new File( "c:\\ALMEX\\etc\\hotenavi\\dbconnect.conf" );
	            }
	            FileInputStream propfile = new FileInputStream( file );
	            prop = new Properties();
	            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
	            prop.load( propfile );
	            // "jdbc.driver"に設定されている値を取得します
	            driver = prop.getProperty( "jdbc.driver" );
	            // "jdbc.url"に設定されている値を取得します
	            url = prop.getProperty( "jdbc.url" );
	            // "jdbc.user"に設定されている値を取得します
	            user = prop.getProperty( "jdbc.user" );
	            // "jdbc.url"に設定されている値を取得します
	            password = prop.getProperty( "jdbc.password" );
	            // "jdbc.datasource"に設定されている値を取得します
	            jdbcds = prop.getProperty( "jdbc.datasource" );
	            propfile.close();
	        }
	        catch ( Exception e )
	        {
	            System.out.println( e.toString() );
	        }
	}	
	/**
	 * This method returns the Database connection object.
	 * @return Connection
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
	    Logging.debug("getConnection called");
		Connection conn = null;
		try {
            Class.forName( driver );
            conn = DriverManager.getConnection( url, user, password );
            if ( conn == null )
            {
                Logging.info( " DBConnection - getConnection() ---> Connection:null" );
            }
            else
            {
                conn.setReadOnly( false );
                conn.setAutoCommit( true );
            }
	      }
        catch ( Exception ex )
        {
            Logging.error( " DBConnection - getConnection() ---> connection failed : " + ex.toString() );
            throw ex;
        }
  		return conn;

	}

	/**
	 * This method closes database resources and returns the connection object
	 * back to the pooler
	 * 
	 * @param resultset
	 * @param statement
	 * @param connection
	 */
	public static void releaseResources(ResultSet resultset,
		Statement statement, Connection connection)  {
		try {
			if (resultset != null) {
				resultset.close();
				resultset = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}

		} catch (SQLException se) {
			Logging.error("Error while closing the connection resources"+se.toString());
		} catch (Exception ex) {
			Logging.error("Error while closing the connection resources"+ex.toString());
		}
	}
	
	/**
	 * This method closes database connection and returns the connection object
	 * back to the pooler
	 * @param connection
	 */
	public static void releaseResources(Connection connection)  {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}

		} catch (SQLException se) {
			Logging.error("Error while closing the connection resources"+se.toString());
		} catch (Exception ex) {
			Logging.error("Error while closing the connection resources"+ex.toString());
		}
	}
	
	/**
	 * This method closes resultset.
	 * @param resultset
	 */
	public static void releaseResources(ResultSet resultset)  {
		try {
			if (resultset != null) {
				resultset.close();
				resultset = null;
			}
			
		} catch (SQLException se) {
			Logging.error("Error while closing the resultset "+se.toString());
		} catch (Exception ex) {
			Logging.error("Error while closing the resultset "+ex.toString());
		}
	}
	
	/**
	 * This method closes statement.
	 * @param statement
	 */
	public static void releaseResources(Statement statement)  {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}

		} catch (SQLException se) {
			Logging.error("Error while closing the statement "+se.toString());
		} catch (Exception ex) {
			Logging.error("Error while closing the statement "+ex.toString());
		}
	}

} // end class DBConnection


