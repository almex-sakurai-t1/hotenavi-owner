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
			System.out.println("�����J�n");
			Logging.info("Check.main start");

			//�A�v�����ۋ����[�U�f�[�^�擾
			DbAccess da = new DbAccess(false);
			query = "select * from ap_uuid where regist_status_pay=2";
			da.setQuery(query);
			result = da.execQuery();

			while (result.next() != false)
			{
				try {
					//���V�[�g���،��ʎ擾
					status_code = arc.execute(result.getString("uuid"), result.getString("receipt"));

					//�Ώۃ��[�U�f�[�^�X�V (���V�[�g�L�������؂�)
					if (status_code == 121006 | status_code == 221006) {
						query = "update ap_uuid set regist_status_pay = 1,update_date = DATE_FORMAT(now(),'%Y%m%d'),update_time = DATE_FORMAT(now(),'%k%i%s')  where uuid = '"
								+ result.getString("uuid") + "'  ";
						da.setQuery(query);
						da.execUpdate();
					}
				} catch (Exception e) {
					System.out.println("�ُ�I��");
					e.printStackTrace();
				}
			}
			System.out.println("�����I��");
			Logging.info("Check.main end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
