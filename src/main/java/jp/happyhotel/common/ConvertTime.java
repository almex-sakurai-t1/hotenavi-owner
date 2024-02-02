package jp.happyhotel.common;

import java.io.Serializable;

/**
 * ���ԕϊ��N���X
 *
 * @author
 * @version 1.00 2010/12/27
 */
public class ConvertTime implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1014150637847161186L;

    /**
     * ���ԕҏW�P
     * �����̒l����ɕ����^�ŕԂ��B
     *
     * @param convTime �ϊ��O����
     * @param mode �ҏW�^�C�v(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HHMMSS�`��
     */
    public static String convTimeStr(int convTime, int mode )
    {
        String retTime = "";

        //
        if ( convTime == 0 )
        {
            retTime = "000000";
        }
        else if ( convTime < 100 )
        {
            // �b�݂̂ł��O���O���ŕԂ�
            retTime = "000000";
        }
        else if ( convTime < 1000 )
        {
            retTime = "000" + String.valueOf( convTime );
        }
        else if ( convTime < 10000 )
        {
            retTime = "00" + String.valueOf( convTime );
        }
        else if ( convTime < 100000 )
        {
            retTime = "0" + String.valueOf( convTime );
        }
        else
        {
            retTime = String.valueOf( convTime );
        }
        switch (mode)
        {
            case 0:
                break;
            case 1:
                retTime = retTime.substring( 0, 2 ) + ":" + retTime.substring( 2, 4 ) + ":" + retTime.substring( 4, 6 );
                break;
            case 2:
                retTime = retTime.substring( 0, 4 );
                break;
            case 3:
                retTime = retTime.substring( 0, 2 ) + ":" + retTime.substring( 2, 4 );
               break;
            default:
                break;
        }

        return retTime;
    }

    /**
     * ���ԕҏW�Q
     * �����̒l����ɕ����^�ŕԂ��B
     *
     * @param convTime �ϊ��O����
     * @param mode �ҏW��^�C�v(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return
     */
    public static int convTimeInt(String convTime, int mode)
    {
        int retTime = 0;
        String strVal = "";

        // �ȗ����͒l�O�ŕԂ�
        if ((convTime.equals( null )) || (convTime.equals( "" )))
        {
            retTime = 0;
        }
        else
        {
            if ( (mode == 1) || (mode == 3) )
            {
                // ":"�t���ň����n���ꂽ�ꍇ":"���͂���
                strVal = convTime.replace( ":", "" );
            }
            else
            {
                strVal = convTime;
            }
            if ( (mode == 2) || (mode == 3) )
            {
                // �����̏ꍇ�͕b��t������
                strVal = strVal + "00";
            }
            retTime = Integer.parseInt( strVal );
        }
        return retTime;
    }

    /**
     * ���ԕҏW�R
     * �����̒l��100�{(�b��)���āAint�^�ŕԂ��B
     *
     * @param convTime �ϊ��O����
     * @param mode �ҏW��^�C�v(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HHMM00�`��(int�^)
     */
    public static int convTimeSS(String convTime, int mode){
        int retTime = 0;
        int convTimeHHMM = 0;

        if ((convTime.equals( null )) || (convTime.equals( "" )))
        {
            retTime = 0;
            return (retTime);
        }

        convTimeHHMM = convTimeInt(convTime, mode);

        retTime = convTimeHHMM * 100;

        return (retTime);
    }

    /**
     * ���ԕҏW�S
     * �����Ŏw�肳�ꂽ�l��HHMMSS�ɕϊ����ĕԂ�
     *
     * @param convTimeHH �ϊ��O���� ��
     * @param convTimeMM �ϊ��O���� ��
     * @param mode �ҏW��^�C�v(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HH:MM�`��
     */
    public static int convTimeSS(int timeHH, int timeMM, int mode){
        int retTime = 0;
        int convTime = 0;
        String strTime = "";

        if (timeMM < 10) {
            strTime = Integer.toString( timeHH ) +  "0" + Integer.toString( timeMM );
        } else {
            strTime = Integer.toString( timeHH ) +  Integer.toString( timeMM );
        }
        convTime = convTimeInt(strTime, mode);

        retTime = convTime;

        return (retTime);
    }

    /**
     * ���ԕҏW�T
     * �����Ŏw�肳�ꂽ�l��HH:MM�ɕϊ����ĕԂ�
     *
     * @param convTime �ϊ��O����
     * @param mode �ҏW��^�C�v(0:HHMMSS, 1:HH:MM:SS, 2:HHMM, 3:HH:MM)
     * @return HH:MM�`��
     */
    public static String convTimeHHMM(int convTime, int mode){
        String retTime = "";
        String convTimeHHMM = "";

        convTimeHHMM = convTimeStr(convTime, mode);

        retTime = convTimeHHMM.substring( 0, 2 ) + ":" + convTimeHHMM.substring( 2, 4 );

        return (retTime);
    }

    /**
     * ���ԕҏW�U
     * �����Ŏw�肳�ꂽ�l�̎���(HH)��Ԃ�
     *
     * @param convTime �ϊ��O����(HHMMSS��int�^�̒l)
     * @return HH
     */
    public static String convTimeHH(int convTime){
        String retTime = "";
        String convTimeHH = "";

        convTimeHH = convTimeStr(convTime, 0);

        retTime = convTimeHH.substring( 0, 2 );

        return (retTime);
    }

    /**
     * ���ԕҏW�V
     * �����Ŏw�肳�ꂽ�l�̕�(MM)��Ԃ�
     *
     * @param convTime �ϊ��O����(HHMMSS��int�^�̒l)
     * @return MM
     */
    public static String convTimeMM(int convTime){
        String retTime = "";
        String convTimeMM = "";

        convTimeMM = convTimeStr(convTime, 0);

        retTime = convTimeMM.substring( 2, 4 );

        return (retTime);
    }
}
