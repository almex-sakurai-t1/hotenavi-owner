package jp.happyhotel.others;

import java.sql.ResultSet;

import jp.happyhotel.common.AppleReceiptCheck;
import jp.happyhotel.common.Logging;

public class PremiumMemberCheck {

	public static void main(String[] args) throws Exception {

		int status_code = 0;
		String query = "";
		ResultSet result;
		AppleReceiptCheck arc = new AppleReceiptCheck();

		try
		{
			System.out.println("処理開始");
			Logging.info("Check.main start");

			//アプリ内課金ユーザデータ取得
			DbAccess da = new DbAccess(false);
			query = "select * from ap_uuid where regist_status_pay=2";
			da.setQuery(query);
			result = da.execQuery();

			while (result.next() != false)
			{
				try {
					//レシート検証結果取得
					status_code = arc.execute(result.getString("uuid"), result.getString("receipt"));

					//対象ユーザデータ更新 (レシート有効期限切れ)
					if (status_code == 121006 | status_code == 221006) {
						query = "update ap_uuid set regist_status_pay = 1,update_date = DATE_FORMAT(now(),'%Y%m%d'),update_time = DATE_FORMAT(now(),'%k%i%s')  where uuid = '"
								+ result.getString("uuid") + "'  ";
						da.setQuery(query);
						da.execUpdate();
					}
				} catch (Exception e) {
					System.out.println("異常終了");
					e.printStackTrace();
				}
			}
			System.out.println("処理終了");
			Logging.info("Check.main end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
