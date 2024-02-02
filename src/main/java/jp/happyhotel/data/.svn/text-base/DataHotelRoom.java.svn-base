/*
 * @(#)DataHotelRoom.java  1.00 2007/07/17
 *
 * Copyright (C) ALMEX Inc. 2007
 *
 * ホテル部屋情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;
import jp.happyhotel.common.*;

/**
 * ホテル部屋情報データ取得クラス
 *
* @author  S.Shiiya
* @version 1.00 2007/07/17
* @version 1.1 2007/11/26
*/
public class DataHotelRoom implements Serializable {
	private static final long serialVersionUID = 4407150924836879105L;

	private int    id;
	private int    seq;
	private String roomName;
	private String roomText;
	private byte[] roomPicturePc;
	private byte[] roomPictureGif;
	private byte[] roomPicturePng;
	private int    dispFlag;
	private String referName;

	/**
	 * データを初期化します。
	 */
	public DataHotelRoom( ) {
		id             = 0;
		seq            = 0;
		roomName       = "";
		roomText       = "";
		roomPicturePc  = new byte[0];
		roomPictureGif = new byte[0];
		roomPicturePng = new byte[0];
		dispFlag       = 0;
		referName      = "";
	}

	public String getReferName() {
		return referName;
	}

	public void setReferName(String referName) {
		this.referName = referName;
	}

	public int getDispFlag( ) {
		return dispFlag;
	}

	public int getId( ) {
		return id;
	}

	public String getRoomName( ) {
		return roomName;
	}

	public byte[] getRoomPictureGif( ) {
		return roomPictureGif;
	}

	public byte[] getRoomPicturePc( ) {
		return roomPicturePc;
	}

	public byte[] getRoomPicturePng( ) {
		return roomPicturePng;
	}

	public String getRoomText( ) {
		return roomText;
	}

	public int getSeq( ) {
		return seq;
	}

	public void setDispFlag( int dispFlag ) {
		this.dispFlag = dispFlag;
	}

	public void setId( int id ) {
		this.id = id;
	}

	public void setRoomName( String roomName ) {
		this.roomName = roomName;
	}

	public void setRoomPictureGif( byte[] roomPictureGif ) {
		this.roomPictureGif = roomPictureGif;
	}

	public void setRoomPicturePc( byte[] roomPicturePc ) {
		this.roomPicturePc = roomPicturePc;
	}

	public void setRoomPicturePng( byte[] roomPicturePng ) {
		this.roomPicturePng = roomPicturePng;
	}

	public void setRoomText( String roomText ) {
		this.roomText = roomText;
	}

	public void setSeq( int seq ) {
		this.seq = seq;
	}

	/**
	 *  ホテル部屋情報データ取得
	 *
	 *  @param hotelId ホテルコード
	 *  @param seq 管理番号
	 *  @return 処理結果(TRUE:正常,FALSE:異常)
	 */
	public boolean getData( int hotelId, int seq ) {
		String               query;
		Connection connection = null;
		ResultSet            result = null;
		PreparedStatement    prestate = null;

		query = "SELECT * FROM hh_hotel_room WHERE id = ? AND seq = ?";
		try {
			connection = DBConnection.getConnection();
			prestate = connection.prepareStatement(query);
			prestate.setInt( 1, hotelId );
			prestate.setInt( 2, seq );
			result = prestate.executeQuery();

			if ( result != null ) {
				if ( result.next() != false ) {
					this.id             = result.getInt("id");
					this.seq            = result.getInt("seq");
					this.roomName       = result.getString("room_name");
					this.roomText       = result.getString("room_text");
					this.roomPicturePc  = result.getBytes("room_picture_pc");
					this.roomPictureGif = result.getBytes("room_picture_gif");
					this.roomPicturePng = result.getBytes("room_picture_png");
					this.dispFlag       = result.getInt("disp_flag");
					this.referName      = result.getString("refer_name");
				}
			}
		} catch ( Exception e ) {
			Logging.error( "[DataHotelRoom.getData] Exception=" + e.toString() );
			return( false );
		} finally {
			DBConnection.releaseResources(result, prestate, connection);
		}
		return( true );
	}
	/**
	 *  ホテル部屋情報データ取得
	 *
	 *  @param hotelId ホテルコード
	 *  @param roomName 部屋番号
	 *  @return 処理結果(TRUE:正常,FALSE:異常)
	 */
	public boolean getData( int hotelId, String roomName ) {
		String               query;
		Connection connection = null;
		ResultSet            result = null;
		PreparedStatement    prestate = null;

		query = "SELECT * FROM hh_hotel_room WHERE id = ? AND room_name = ?";

		try {
			connection = DBConnection.getConnection();
			prestate = connection.prepareStatement(query);
			prestate.setInt( 1, hotelId );
			prestate.setString( 2, roomName );
			result = prestate.executeQuery();

			if ( result != null ) {
				if ( result.next() != false ) {
					this.id             = result.getInt("id");
					this.seq            = result.getInt("seq");
					this.roomName       = result.getString("room_name");
					this.roomText       = result.getString("room_text");
					this.roomPicturePc  = result.getBytes("room_picture_pc");
					this.roomPictureGif = result.getBytes("room_picture_gif");
					this.roomPicturePng = result.getBytes("room_picture_png");
					this.dispFlag       = result.getInt("disp_flag");
					this.referName      = result.getString("refer_name");
				}
			}
		} catch ( Exception e ) {
			Logging.error( "[DataHotelRoom.getData] Exception=" + e.toString() );
			return( false );
		} finally {
			DBConnection.releaseResources(result, prestate, connection);
		}
		return( true );
	}
	/**
	 *  ホテル部屋情報データ設定
	 *
	 *  @param result ホテル部屋情報データレコード
	 *  @return 処理結果(TRUE:正常,FALSE:異常)
	 */
	public boolean setData( ResultSet result ) {
		try {
			if ( result != null ) {
				this.id             = result.getInt("id");
				this.seq            = result.getInt("seq");
				this.roomName       = result.getString("room_name");
				this.roomText       = result.getString("room_text");
				this.roomPicturePc  = result.getBytes("room_picture_pc");
				this.roomPictureGif = result.getBytes("room_picture_gif");
				this.roomPicturePng = result.getBytes("room_picture_png");
				this.dispFlag       = result.getInt("disp_flag");
				this.referName      = result.getString("refer_name");
			}
		} catch ( Exception e ) {
			Logging.error( "[DataHotelRoom.setData] Exception=" + e.toString() );
		}

		return( true );
	}
	/**
	 *  ホテル部屋情報データ追加
	 *
	 *  @see "値のセット後(setXXX)に行うこと"
	 *  @return 処理結果(TRUE:正常,FALSE:異常)
	 */
	public boolean insertData( ) {
		int                  result;
		boolean              ret;
		String               query;
		Connection connection = null;
		PreparedStatement    prestate = null;

		ret = false;

		query = "INSERT hh_hotel_room SET ";
		query = query + " id = ?,";
		query = query + " seq = ?,";
		query = query + " room_name = ?,";
		query = query + " room_text = ?,";
		query = query + " room_picture_pc = ?,";
		query = query + " room_picture_gif = ?,";
		query = query + " room_picture_png = ?,";
		query = query + " disp_flag = ?,";
		query = query + " refer_name = ?";
		try {
			connection = DBConnection.getConnection();
			prestate = connection.prepareStatement(query);
			// 更新対象の値をセットする
			prestate.setInt( 1, this.id );
			prestate.setInt( 2, this.seq );
			prestate.setString( 3, this.roomName );
			prestate.setString( 4, this.roomText );
			prestate.setBytes( 5, this.roomPicturePc );
			prestate.setBytes( 6, this.roomPictureGif );
			prestate.setBytes( 7, this.roomPicturePng );
			prestate.setInt( 8, this.dispFlag );
			prestate.setString( 9, this.referName );

			result = prestate.executeUpdate();
			if ( result > 0 ) {
				ret = true;
			}
		} catch ( Exception e ) {
			Logging.error( "[DataHotelRoom.insertData] Exception=" + e.toString() );
			ret = false;
		} finally {
			DBConnection.releaseResources(prestate);
			DBConnection.releaseResources(connection);
		}
		return( ret );
	}
	/**
	 *  ホテル部屋情報データ変更
	 *
	 *  @see "値のセット後(setXXX)に行うこと"
	 *  @param id ホテルID
	 *  @param seq 管理番号
	 *  @return 処理結果(TRUE:正常,FALSE:異常)
	 */
	public boolean updateData( int id, int seq ) {
		int                  result;
		boolean              ret;
		String               query;
		Connection connection = null;
		PreparedStatement    prestate = null;

		ret = false;

		query = "UPDATE hh_hotel_room SET ";
		query = query + " room_name = ?,";
		query = query + " room_text = ?,";
		query = query + " room_picture_pc = ?,";
		query = query + " room_picture_gif = ?,";
		query = query + " room_picture_png = ?,";
		query = query + " disp_flag = ?,";
		query = query + " refer_name = ?";
		query = query + " WHERE id = ? AND seq = ?";

		try {
			connection = DBConnection.getConnection();
			prestate = connection.prepareStatement(query);
			// 更新対象の値をセットする
			prestate.setString( 1, this.roomName );
			prestate.setString( 2, this.roomText );
			prestate.setBytes( 3, this.roomPicturePc );
			prestate.setBytes( 4, this.roomPictureGif );
			prestate.setBytes( 5, this.roomPicturePng );
			prestate.setInt( 6, this.dispFlag );
			prestate.setString( 7, this.referName );
			prestate.setInt( 8, id );
			prestate.setInt( 9, seq );

			result = prestate.executeUpdate();
			if ( result > 0 ) {
				ret = true;
			}
		} catch ( Exception e ) {
			Logging.error( "[DataHotelRoom.updateData] Exception=" + e.toString() );
			ret = false;
		} finally {
			DBConnection.releaseResources(prestate);
			DBConnection.releaseResources(connection);
		}
		return( ret );
	}
}
