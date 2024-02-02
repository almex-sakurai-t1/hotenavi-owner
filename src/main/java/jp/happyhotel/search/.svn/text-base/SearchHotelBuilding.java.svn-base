/*
 * @(#)SearchHotelMessage.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル最新情報検索クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;

/**
 * ホテル建物形式別ホテル情報検索クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/03/04
 */
public class SearchHotelBuilding implements Serializable
{
	private static final long serialVersionUID = -4973018296302612156L;
	private int m_hotelCount;
	private int m_hotelAllCount;
	private int[] m_hotelIdList;
	private DataHotelBasic[] m_hotelInfo;

	/**
	 * データを初期化します。
	 */
	public SearchHotelBuilding()
	{
		m_hotelCount = 0;
		m_hotelAllCount = 0;
		m_hotelIdList = new int[0];
		m_hotelInfo = new DataHotelBasic[0];
	}

	public int getCount()
	{
		return (m_hotelCount);
	}

	public int getAllCount()
	{
		return (m_hotelAllCount);
	}

	public int[] getHotelIdList()
	{
		return (m_hotelIdList);
	}

	public DataHotelBasic[] getHotelInfo()
	{
		return (m_hotelInfo);
	}

	/**
	 * ホテル最新情報検索結果取得(ホテルランク順)
	 * 
	 * @param hotelIdList ホテルIDリスト(null:全件検索)
	 * @param type 建物形式(0:なし1:ビル形式2:戸建形式3:連棟形式, 4:ビルor戸建形式5:ビルor連棟形式6:戸建or連棟形式7:ビルor戸建or連棟)
	 * @param countNum 取得件数（0：全件 ※pageNum無視）
	 * @param pageNum ページ番号（0～）
	 * @return 処理結果(TRUE:正常,FALSE:異常)
	 */
	public boolean getHotelList(int[] hotelIdList, int type, int countNum, int pageNum)
	{
		int i;
		int count;
		boolean ret;
		String query;
		String subQuery;

		Connection connection = null;
		ResultSet result = null;
		PreparedStatement prestate = null;

		if (hotelIdList != null)
		{
			if (hotelIdList.length == 0)
			{
				return (true);
			}
		}

		subQuery = " id, rank, kind, hotenavi_id, name, name_kana, name_mobile, zip_code, jis_code, pref_id," +
				" pref_name, pref_kana, address1, address2, address3, address_all, tel1, tel2, fax," +
				" charge_last, charge_first, charge_kana_last, charge_kana_first, charge_tel, charge_mail," +
				" open_date, renewal_date, url, url_official1, url_official2, url_official_mobile, pr," +
				" pr_detail, pr_event, pr_member, access, access_station, access_ic, room_count, parking," +
				" parking_count, type_building, type_kodate, type_rentou, type_etc, location_station, " +
				" location_ic, location_kougai, benefit, roomservice, pay_front, pay_auto, credit, " +
				" credit_visa, credit_master, credit_jcb, credit_dc, credit_nicos, credit_amex, credit_etc," +
				" halfway, coupon, possible_one, possible_three, reserve, reserve_tel, reserve_mail," +
				" reserve_web, empty_method, hotel_lat, hotel_lon, hotel_lat_num, hotel_lon_num, disp_lat," +
				" disp_lon, zoom, over18_flag, company_type, empty_kind, empty_status, group_code," +
				" last_update, last_uptime, pr_room, pr_etc, renewal_flag, url_special, hotel_lat_jp," +
				" hotel_lon_jp, map_code, high_roof, high_roof_count, empty_hotenavi_id, attention_flag," +
				" ad_pref_id, renewal_date_text, new_open_search_flag";

		query = "SELECT " + getNewColumnString(subQuery) + " FROM hh_hotel_basic";
		query = query + " LEFT JOIN hh_hotel_status";
		query = query + " ON hh_hotel_basic.id = hh_hotel_status.id";
		if (type == 1)
		{
			query = query + " WHERE type_building=1 AND";
		}
		else if (type == 2)
		{
			query = query + " WHERE type_kodate=1 AND";
		}
		else if (type == 3)
		{
			query = query + " WHERE type_rentou=1 AND";
		}
		else if (type == 4)
		{
			query = query + " WHERE ( type_building=1 OR type_kodate=1 ) AND";
		}
		else if (type == 5)
		{
			query = query + " WHERE ( type_building=1 OR type_rentou=1 ) AND";
		}
		else if (type == 6)
		{
			query = query + " WHERE ( type_kodate=1 OR type_rentou=1 ) AND";
		}
		else if (type == 7)
		{
			query = query + " WHERE ( type_building=1 OR type_kodate=1 OR type_rentou=1 ) AND";
		}
		else
		{
			query = query + " WHERE ";
		}
		query = query + " hh_hotel_basic.id > 0";
		if (hotelIdList != null)
		{
			if (hotelIdList.length > 0)
			{
				query = query + " AND hh_hotel_basic.id IN(";
				for (i = 0; i < hotelIdList.length; i++)
				{
					query = query + hotelIdList[i];
					if (i < hotelIdList.length - 1)
					{
						query = query + ",";
					}
				}
				query = query + ")";
			}
		}

		query = query + " GROUP BY hh_hotel_basic.id";
		query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

		if (countNum > 0)
		{
			query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
		}

		count = 0;
		try
		{
			connection = DBConnection.getConnectionRO();
			prestate = connection.prepareStatement(query);
			result = prestate.executeQuery();

			if (result != null)
			{
				// レコード件数取得
				if (result.last() != false)
				{
					m_hotelCount = result.getRow();
				}

				// クラスの配列を用意し、初期化する。
				this.m_hotelInfo = new DataHotelBasic[this.m_hotelCount];
				for (i = 0; i < m_hotelCount; i++)
				{
					m_hotelInfo[i] = new DataHotelBasic();
				}

				result.beforeFirst();
				while (result.next() != false)
				{
					// ホテル情報の取得
					this.m_hotelInfo[count++].setData(result);
				}
			}

			ret = true;
		} catch (Exception e)
		{
			Logging.info("[getHotelList] Exception=" + e.toString());
			ret = false;
		} finally
		{
			result = null;
			prestate = null;
		}

		// ホテル総件数の取得
		query = "SELECT id from hh_hotel_basic";
		if (type == 1)
		{
			query = query + " WHERE type_building=1 AND";
		}
		else if (type == 2)
		{
			query = query + " WHERE type_kodate=1 AND";
		}
		else if (type == 3)
		{
			query = query + " WHERE type_rentou=1 AND";
		}
		else if (type == 4)
		{
			query = query + " WHERE ( type_building=1 OR type_kodate=1 ) AND";
		}
		else if (type == 5)
		{
			query = query + " WHERE ( type_building=1 OR type_rentou=1 ) AND";
		}
		else if (type == 6)
		{
			query = query + " WHERE ( type_kodate=1 OR type_rentou=1 ) AND";
		}
		else if (type == 7)
		{
			query = query + " WHERE ( type_building=1 OR type_kodate=1 OR type_rentou=1 ) AND";
		}
		else
		{
			query = query + " WHERE ";
		}

		query = query + " id > 0";
		if (hotelIdList != null)
		{
			if (hotelIdList.length > 0)
			{
				query = query + " AND id IN(";
				for (i = 0; i < hotelIdList.length; i++)
				{
					query = query + hotelIdList[i];
					if (i < hotelIdList.length - 1)
					{
						query = query + ",";
					}
				}
				query = query + ")";
			}
		}
		query = query + " GROUP BY id";
		query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

		count = 0;

		try
		{
			prestate = connection.prepareStatement(query);
			result = prestate.executeQuery();

			if (result != null)
			{
				// レコード件数取得
				if (result.last() != false)
				{
					// 総件数の取得
					this.m_hotelAllCount = result.getRow();
				}

				m_hotelIdList = new int[this.m_hotelAllCount];

				result.beforeFirst();
				while (result.next() != false)
				{
					m_hotelIdList[count++] = result.getInt("id");
				}
			}
		} catch (Exception e)
		{
			Logging.error("[getHotelList] Exception=" + e.toString());
			ret = false;
		} finally
		{
			DBConnection.releaseResources(result, prestate, connection);
		}
		return (ret);
	}

	private String getNewColumnString(String colString) {
		StringBuilder newColumnSb = new StringBuilder();
		String[] colArr = colString.split(",");
		String col;
		for (int i = 0; i < colArr.length; i++) {
			newColumnSb.append(" ");
			if (i > 0) {
				newColumnSb.append(", ");
			}
			col = colArr[i].trim().toLowerCase();
			if ("empty_kind".equals(col)) {
				newColumnSb.append("IFNULL(hh_hotel_status.`mode`, 0)");
			} else if ("empty_status".equals(col)) {
				newColumnSb.append("IFNULL(hh_hotel_status.").append(col).append(", 0)");
			} else {
				newColumnSb.append("hh_hotel_basic.").append(col);
			}
			newColumnSb.append(" ").append(col);
		}
		return newColumnSb.toString();
	}

}
