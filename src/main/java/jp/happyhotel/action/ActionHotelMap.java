package jp.happyhotel.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.CreateUrl;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.search.SearchHotelMapPoint;

/**
 *
 * 地図表示クラス
 *
 * @author S.Tashiro
 * @version 1.0 2009/07/07
 */

public class ActionHotelMap extends BaseAction
{

	private RequestDispatcher requestDispatcher = null;
	private static final String HOTEL = "II6G";

	/**
	 * ホテル詳細ページの地図の移動縮尺を変更する
	 *
	 * @param request クライアントからサーバへのリクエスト
	 * @param response サーバからクライアントへのレスポンス
	 * @see "/キャリアのフォルダ/search/hotelmap_M2.jsp うまくいった場合に遷移する"
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		boolean memberFlag;
		boolean paymemberFlag;
		boolean paymemberTempFlag;
		boolean ret;
		int registStatus;
		int delFlag;
		int scale;
		int dispScale;
		int carrierFlag;
		String userId;
		String paramId;
		String paramDx;
		String paramDy;
		String paramScale;
		String paramDscale;
		String url;
		String paramUidLink = null;
		String paramAcRead;
		String termNo;
		DataLoginInfo_M2 dataLoginInfo_M2;
		DataHotelBasic dhb;
		CreateUrl cu;
		AuAuthCheck auCheck;

		// 近くの駅・IC検索用変数
		int icCount;
		int stCount;
		String[] icDistance;
		String[] stDistance;
		DataMapPoint[] dmpIc;
		DataMapPoint[] dmpStation;
		SearchHotelMapPoint shm;

		termNo = "";
		url = "";
		ret = false;
		memberFlag = false;
		paymemberFlag = false;
		paymemberTempFlag = false;
		dhb = new DataHotelBasic();
		cu = new CreateUrl();
		dataLoginInfo_M2 = (DataLoginInfo_M2) request.getAttribute("LOGIN_INFO");

		icCount = 0;
		stCount = 0;
		icDistance = null;
		stDistance = null;
		dmpIc = null;
		dmpStation = null;
		shm = new SearchHotelMapPoint();
		carrierFlag = UserAgent.getUserAgentType(request);
		paramAcRead = request.getParameter("acread");
		paramUidLink = (String) request.getAttribute("UID-LINK");

		try
		{
			// ユーザー情報の取得
			if (dataLoginInfo_M2 != null)
			{
				memberFlag = dataLoginInfo_M2.isMemberFlag();
				paymemberFlag = dataLoginInfo_M2.isPaymemberFlag();
				paymemberTempFlag = dataLoginInfo_M2.isPaymemberTempFlag();
				registStatus = dataLoginInfo_M2.getRegistStatus();
				delFlag = dataLoginInfo_M2.getDelFlag();
				userId = dataLoginInfo_M2.getUserId();

			}
		} catch (Exception e)
		{
			Logging.error("[ActionHotelMap dataLoginInfo] Exception:" + e.toString());
		}

		paramId = request.getParameter("hotel_id");
		paramDx = request.getParameter("dx");
		paramDy = request.getParameter("dy");
		paramScale = request.getParameter("scale");
		paramDscale = request.getParameter("dscale");

		// 各種パラメータのチェック
		if ((paramId == null) || (paramId.compareTo("") == 0) || (CheckString.numCheck(paramId) == false))
		{
			paramId = "0";
		}
		if ((paramDx == null) || (paramDx.compareTo("") == 0) || (CheckString.numCheck(paramDx) == false))
		{
			paramDx = "0";
		}
		if ((paramDy == null) || (paramDy.compareTo("") == 0) || (CheckString.numCheck(paramDy) == false))
		{
			paramDy = "0";
		}
		if ((paramScale == null) || (paramScale.compareTo("") == 0) || (CheckString.numCheck(paramScale) == false))
		{
			paramScale = "0";
		}
		if ((paramDscale == null) || (paramDscale.compareTo("") == 0) || (CheckString.numCheck(paramDscale) == false))
		{
			paramDscale = "0";
		}

		// 基準となる縮尺
		scale = this.getScale(Integer.parseInt(paramScale));
		// 移動または、拡大縮小後の縮尺
		dispScale = this.getScale(Integer.parseInt(paramDscale));

		ret = dhb.getData(Integer.parseInt(paramId));
		if (ret != false)
		{

			// ホテル周辺の駅を探す
			ret = shm.getSearchHotelNearStation(paramId);
			if (ret != false)
			{
				stCount = shm.getStationCount();
				dmpStation = shm.getStation();
				stDistance = shm.getStationDistance();
			}
			// ホテル周辺のICを探す
			ret = shm.getSearchHotelNearIc(paramId);
			if (ret != false)
			{
				icCount = shm.getIcCount();
				dmpIc = shm.getIc();
				icDistance = shm.getIcDistance();
			}

			// 移動量が0の場合、scaleパラメータに表示縮尺(dispScale)をセットする。それ以外だと、dscaleパラメータに表示縮尺をセットする
			if ((Integer.parseInt(paramDx) == 0) && (Integer.parseInt(paramDy) == 0))
			{
				cu.setScale(dispScale);
			}
			else
			{
				// scaleパラメータに基準スケール(scale)をセット、それぞれの移動後のパラメータもセット
				// cu.setScale( scale );
				cu.setScale(dispScale);
				cu.setDx(Integer.parseInt(paramDx) * 100);
				cu.setDy(Integer.parseInt(paramDy) * 100);
				cu.setDscale(dispScale);
			}
			cu.setC("WGS84," + dhb.getHotelLat() + "," + dhb.getHotelLon());
			cu.setPin(dhb.getHotelLat() + "," + dhb.getHotelLon());
			cu.setPos(HOTEL + ":PWGS84," + dhb.getHotelLat() + "," + dhb.getHotelLon());
			// 携帯向けに画像の自動調整を有効にする
			cu.setWm(true);
			cu.setHm(true);

			// キャリアによって出す画像タイプを変更
			if ((carrierFlag == DataMasterUseragent.CARRIER_AU)
					|| (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK))
			{
				cu.setImgType("PNG");
				Logging.info("[ActionHotelMap]:PNGセット");
			}
			else if (carrierFlag == DataMasterUseragent.CARRIER_DOCOMO)
			{
				cu.setImgType("GIF");
				Logging.info("[ActionHotelMap]:GIFセット");
			}

			// 地図表示用URLの作成
			url = cu.getYolpDrawMap();
			Logging.info("ActionHotelMap getYolpDrawMap url : " + url);

			// urlを一つのパラメータとするため、&を!に置き換える
			// url = url.replaceAll( "&", "!" );
			try
			{
				request.setAttribute("hotel_id", paramId);
				request.setAttribute("scale", paramScale);
				request.setAttribute("dx", paramDx);
				request.setAttribute("dy", paramDy);
				request.setAttribute("dscale", paramDscale);
				request.setAttribute("url", url);
				request.setAttribute("dhb", dhb);

				request.setAttribute("countIc", icCount);
				request.setAttribute("countSt", stCount);
				request.setAttribute("ic", dmpIc);
				request.setAttribute("st", dmpStation);
				request.setAttribute("distanceIc", icDistance);
				request.setAttribute("distanceSt", stDistance);

				requestDispatcher = request.getRequestDispatcher("hotelmap_M2.jsp");
				requestDispatcher.forward(request, response);
			} catch (Exception e)
			{
				Logging.error("[ActionDispMap requestDispatcher] Exception:" + e.toString());
			}

		}
		else
		{
			try
			{
				response.sendRedirect("../index.jsp?" + paramUidLink);
			} catch (IOException e)
			{
				Logging.error("[ActionHotelMap sendRedirect] Exception:" + e.toString());
			}
			return;
		}
	}

	/**
	 * 縮尺を取得する
	 *
	 * @param scale スケール。縮尺
	 * @return 処理結果(5000～3000000までの縮尺)
	 *
	 */
	private int getScale(int scale)
	{
		int dispScale;

		switch (scale)
		{
		case -4:
			dispScale = 5000;
			break;
		case -3:
			dispScale = 5000;
			break;
		case -2:
			dispScale = 10000;
			break;
		case -1:
			dispScale = 25000;
			break;
		case 0:
			dispScale = 70000;
			break;
		case 1:
			dispScale = 250000;
			break;
		case 2:
			dispScale = 500000;
			break;
		case 3:
			dispScale = 1000000;
			break;
		case 4:
			dispScale = 3000000;
			break;
		default:
			dispScale = 70000;
		}
		return (dispScale);
	}
}
