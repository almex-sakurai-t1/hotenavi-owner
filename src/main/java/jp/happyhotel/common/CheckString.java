/*
 * @(#)CheckString.java 1.00 2007/07/19 Copyright (C) ALMEX Inc. 2007 ������`�F�b�N�ėp�N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * ������̃`�F�b�N���s�����\�b�h�Q�ł��B
 * </p>
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/19
 */
public class CheckString implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -4856108172406548350L;

    /**
     * ���p�����`�F�b�N����
     * 
     * @param orgNum �`�F�b�N�Ώە�����
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean numCheck(String orgNum)
    {

        char cutData;
        if ( orgNum != null && orgNum.trim().length() > 0 )
        {
            for( int i = 0 ; i < orgNum.trim().length() ; i++ )
            {
                cutData = orgNum.charAt( i );
                if ( (cutData < '0' || cutData > '9') && cutData != '-' )
                {
                    return(false);
                }
            }
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���p�p���`�F�b�N����
     * 
     * @param orgAlphabet �`�F�b�N�Ώە�����
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean alphabetCheck(String orgAlphabet)
    {
        int i;
        char cutData;

        for( i = 0 ; i < orgAlphabet.length() ; i++ )
        {
            cutData = orgAlphabet.charAt( i );
            if ( (cutData < 'A' || cutData > 'Z') && (cutData < 'a' || cutData > 'z') )
            {
                return(false);
            }
        }

        return(true);
    }

    /**
     * ���p�p�����`�F�b�N����
     * 
     * @param orgData �`�F�b�N�Ώە�����
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean numAlphaCheck(String orgData)
    {
        return(orgData.matches( "[0-9a-zA-Z._-]+" ));
    }

    /**
     * �S�p�Ђ炪�ȃ`�F�b�N����
     * 
     * @param orgHiragana �`�F�b�N�Ώە�����
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean hiraganaCheck(String orgHiragana)
    {
        int i;
        char cutData;

        for( i = 0 ; i < orgHiragana.length() ; i++ )
        {
            cutData = orgHiragana.charAt( i );
            if ( cutData < '��' || cutData > '��' )
            {
                return(false);
            }
        }

        return(true);
    }

    /**
     * �S�p�J�^�J�i�`�F�b�N����
     * 
     * @param orgKatakana �`�F�b�N�Ώە�����
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean katakanaCheck(String orgKatakana)
    {
        int i;
        char cutData;

        for( i = 0 ; i < orgKatakana.length() ; i++ )
        {
            cutData = orgKatakana.charAt( i );
            if ( (cutData < '�@' || cutData > '��') && cutData != '�[' )
            {
                return(false);
            }
        }

        return(true);
    }

    /**
     * �󔒃`�F�b�N����
     * �󔒁i���p�E�S�p�j�݂̂̏ꍇtrue
     * 
     * @param orgOnlySpace �`�F�b�N�Ώە�����
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean onlySpaceCheck(String orgOnlySpace)
    {
        int i;
        char cutData;
        boolean ret = true;

        for( i = 0 ; i < orgOnlySpace.length() ; i++ )
        {
            cutData = orgOnlySpace.charAt( i );
            if ( cutData != '�@' && cutData != ' ' )
            {
                ret = false;
                break;
            }
        }

        return(ret);
    }

    /**
     * null�`�F�b�N
     * 
     * @param objString ������
     * @return string(null�̏ꍇ�A""�ɒu��������)
     */
    public static String checkStringForNull(String objString)
    {
        if ( objString == null )
        {
            objString = "";
        }
        return objString.trim();
    }

    /**
     * �p�����[�^��null�A�󔒃`�F�b�N�inull�A�󔒂̏ꍇfalse�j
     * 
     * @param objString
     * @return ��������(true:����,false:�ُ�)
     */

    public static boolean isValidParameter(String objString)
    {
        if ( objString == null || objString.trim().length() <= 0 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * null�A�󔒃`�F�b�N�inull�A�󔒂̏ꍇfalse�j
     * 
     * @param objString
     * @return ��������(true:����,false:�ُ�)
     */

    public static boolean isvalidString(String objString)
    {
        if ( objString == null || objString.trim().length() <= 0 || objString.compareTo( "null" ) == 0 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * ���[���A�h���X�`�F�b�N���\�b�h
     * 
     * @param mailaddr
     * @return �������ʁitrue:����Afalse:�ُ�j
     */
    public static boolean mailaddrCheck(String mailaddr)
    {
        if ( mailaddr == null )
        {
            return false;
        }
        else
        {
            /*
             * PC�ł��A'..'��@�̑O��'.'��OK�Ƃ��Ĉ���
             * 2016/10/06�i#15020�Ή��j
             * �g�їp�̃��A�h�Ƃ��āivodafone.ne.jp�Łj'/ ? +'���܂܂ꂽ���̂��o�^����Ă����̂ŁA�����̋L�����܂ރ��A�h�ɂ��Ή�
             */
            if ( mailaddr.matches( "^[\\w\\d\\?\\+/._-]+\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$" ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * ����+�Ђ炪�ȃ`�F�b�N
     * 
     * @param str
     * @return �������ʁitrue:����Afalse:�ُ�j
     */
    public static boolean kanjiCheck(String str)
    {
        if ( str == null )
        {
            return false;
        }

        Pattern p = Pattern.compile( "^[��-Ꞃ�-��[]*$" );
        Matcher m = p.matcher( str );
        if ( m.find() )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * �z�e�i�r�I�[�i�[�T�C�g���O�C��ID�`�F�b�N����
     * 
     * @param loginId �z�e�i�r�I�[�i�[�T�C�g�l���[�U���i���O�C��ID�j
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean loginIdCheck(String loginId)
    {
        boolean check = true;

        check = loginId.matches( "[0-9a-zA-Z.@,_-]+" );

        if ( loginId.length() > 10 ) // 10���𒴂���ꍇ�ُ͈�l
        {
            check = false;
        }
        if ( loginId.length() < 1 ) // 1�������ُ͈�l
        {
            check = false;
        }

        return(check);
    }

    /**
     * �z�e�i�r�I�[�i�[�T�C�g���O�`�F�b�N����
     * 
     * @param name �z�e�i�r���O�`�F�b�N�j
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean nameCheck(String name)
    {
        boolean check = true;

        if ( name.length() > 10 ) // 10���𒴂���ꍇ�ُ͈�l
        {
            check = false;
        }
        if ( name.length() < 1 ) // 1�������ُ͈�l
        {
            check = false;
        }

        return(check);
    }

    /**
     * �z�e�i�r�I�[�i�[�T�C�g�g�їp�p�X���[�h�@�p�X���[�h�`�F�b�N����
     * 
     * @param password �z�e�i�r�I�[�i�[�T�C�g�g�їp�p�X���[�h
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean passwordCheck(String password)
    {
        boolean check = true;

        check = password.matches( "[0-9a-zA-Z!.@,_-]+" );
        if ( check )
        {
            if ( password.length() > 8 ) // 8���𒴂���ꍇ�ُ͈�l
            {
                check = false;
            }
        }
        if ( check )
        {
            if ( password.length() < 4 ) // 4�������ُ͈�l
            {
                check = false;
            }
        }

        return(check);
    }

    /**
     * �p�X���[�h�`�F�b�N�p���K�\�����擾
     * 
     * @return �p�X���[�h�`�F�b�N�p���K�\��
     */
    public static String getPasswordRegex()
    {
        String regex_kigou = "{}\\[\\],.<>;:'\"?/|\\\\\\`~!@#$%^&*()_=+-";
        String pattern_kigou = "(?=.*[" + regex_kigou + "])";
        String regex_password = "^("
                + "(?=.*[0-9])(?=.*[a-z])" // ���� �p������
                + "|"
                + "(?=.*[0-9])(?=.*[A-Z])" // ���� �p�啶��
                + "|"
                + "(?=.*[0-9])" + pattern_kigou // ���� �L��
                + "|"
                + "(?=.*[a-z])(?=.*[A-Z])" // �p������ �p�啶��
                + "|"
                + "(?=.*[a-z])" + pattern_kigou // �p������ �L��
                + "|"
                + "(?=.*[A-Z])" + pattern_kigou // �p�啶�� �L��
                + ")"
                + "[a-zA-Z0-9" + regex_kigou + "]{" + Constants.MINIMUM_PASSWORD_LENGTH + ","
                + Constants.MAXIMUM_PASSWORD_LENGTH + "}$"; // �p�啶���A�p�������A�����A�L���i����́j8�����ȏ�
        return regex_password;
    }

    /**
     * �p�X���[�h�`�F�b�N�p���K�\�����擾
     * 
     * @return �p�X���[�h�`�F�b�N�p���K�\��
     */
    public static String getPasswordRegex3()
    {
        String regex_kigou = "{}\\[\\],.<>;:'\"?/|\\\\\\`~!@#$%^&*()_=+-";
        String pattern_kigou = "(?=.*[" + regex_kigou + "])";
        String regex_password = "^("
                + "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" // ���� �p������ �p�啶��
                + "|"
                + "(?=.*[0-9])(?=.*[a-z])" + pattern_kigou // ���� �p������ �L��
                + "|"
                + "(?=.*[0-9])(?=.*[A-Z])" + pattern_kigou // ���� �p������ �L��
                + "|"
                + "(?=.*[a-z])(?=.*[A-Z])" + pattern_kigou // �p������ �p�啶��
                + ")"
                + "[a-zA-Z0-9" + regex_kigou + "]{" + Constants.MINIMUM_PASSWORD_LENGTH + ","
                + Constants.MAXIMUM_PASSWORD_LENGTH + "}$"; // �p�啶���A�p�������A�����A�L���i����́j8�����ȏ�
        return regex_password;
    }

    /**
     * id�p�����[�^�`�F�b�N
     * 
     * @param HttpServletRequest
     * @return �p�����[�^�l
     */
    public static String getParameterId(HttpServletRequest request)
    {

        String idstr = request.getParameter( "id" );
        int idLen = idstr.length();

        // ���l�E�����`�F�b�N
        if ( !numCheck( idstr ) || (idLen < 6 || idLen > 9) )
        {
            idstr = "0";
        }

        return(idstr);

    }

    /**
     * �z�e�i�rID�`�F�b�N����
     * 
     * @param hotenaviId �z�e�i�rID
     * @return ��������(true:����,false:�ُ�)
     */
    public static boolean hotenaviIdCheck(String hotenaviId)
    {
        boolean check = true;

        check = numAlphaCheck( hotenaviId ); // �p����_-�ȊO�ُ͈�l

        if ( check )
        {
            if ( hotenaviId.length() > 10 ) // 10���𒴂���ꍇ�ُ͈�l
            {
                check = false;
            }
        }

        return(check);
    }

    /*
     * @param ������
     * @return �������ʁitrue:����Afalse:�ُ�j
     */
    public static boolean isXSSRuleCheck(String val)
    {
        Pattern pattern = Pattern.compile( "(?i)[\\s\"'`;/0-9=\\x0B\\x09\\x0C\\x3B\\x2C\\x28\\x3B]+on[a-zA-Z]+[\\s\\x0B\\x09\\x0C\\x3B\\x2C\\x28\\x3B]*?=" );
        Matcher matcher = pattern.matcher( val );

        String checkStr = "";
        while( matcher.find() )
        {
            checkStr = matcher.group();
            break;
        }
        return !checkStr.equals( "" );
    }

    /*
     * @param ������
     * @return �������ʁitrue:����Afalse:�ُ�j
     */
    public static String isXSSRuleCheck(String val, String regex)
    {
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher( val );
        String checkStr = "";
        while( matcher.find() )
        {
            checkStr = matcher.group();
            break;
        }
        return checkStr;
    }

    /**
     * ���t�����񂪐��������t���`�F�b�N
     * 
     * @param str ymd
     * @return true:���������t�@false:�s���ȓ��t
     */
    public static boolean dateCheck(String ymd)
    {
        try
        {
            if ( ymd.equals( "99999999" ) )
                return true;

            // ���t�`�F�b�N
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
            sdf.setLenient( false );
            sdf.parse( ymd );

            return true;

        }
        catch ( Exception e )
        {
            return false;
        }
    }

}
