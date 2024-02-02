/*
 * @(#)UserTermInfo.java 1.00
 * 2010/12/27 Copyright (C) ALMEX Inc. 2010
 * ���[�U18�֏��擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.data.DataUserR18Check;

/**
 * ���[�U��18�Έȏ�(18��)�̊m�F���s���N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2010/12/27
 */
public class UserR18Check implements Serializable
{

    private static final long serialVersionUID = 7180630700601158446L;
    private final int         KIND             = 3;                   // DateEdit.isValidDate�Ŏg�p������(0�F�N�A1�F���A2�F���A3�F���ԁA4�F���A5�F�b)
    private final int         ELAPSED_TIME     = 24;                  // DateEdit.isValidDate�Ŏg�p����o�ߎ��ԁB�L�����Ԃ�24���ԁB

    private DataUserR18Check  userR18Check;
    private Boolean           validFlag;                              // true:�L���������Afalse:�L�������O
    private Boolean           getFlag;                                // true:�f�[�^�擾�Afalse�F�f�[�^�Ȃ�

    /**
     * �f�[�^�����������܂��B
     */
    public UserR18Check()
    {
        getFlag = false;
        validFlag = false;
    }

    /** ���[�U18�֏��擾 **/
    public DataUserR18Check getR18()
    {
        return(this.userR18Check);
    }

    /**
     * ���[�U18�֏��擾
     * 
     * @return �������� (true:�f�[�^�擾�Afalse�F�f�[�^�Ȃ�)
     */
    public boolean isGetFlag()
    {
        return(this.getFlag);
    }

    /**
     * ���[�U18�֏��L�������`�F�b�N
     * 
     * @return �������ʁitrue:�L���Afalse:�����j
     **/
    public boolean isValidDate()
    {
        return(this.validFlag);
    }

    /**
     * 18�֏����擾����
     * 
     * @param termNo �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserR18(String termNo)
    {
        boolean ret = false;

        this.userR18Check = new DataUserR18Check();
        ret = this.userR18Check.getData( termNo );

        // �f�[�^�擾�̌��ʂ��X�V�t���O�ɃZ�b�g
        this.getFlag = ret;

        // �f�[�^���������ꍇ�͗L���������`�F�b�N����
        if ( this.getFlag != false )
        {
            this.validFlag = DateEdit.isValidDate( this.userR18Check.getRegistDate(), this.userR18Check.getRegistTime(),
                    KIND, ELAPSED_TIME );
        }
        return(ret);
    }

    /**
     * 18�֏����X�V����
     * 
     * @param termNo �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateUserR18(String termNo)
    {
        boolean ret = false;

        this.userR18Check.setMobileTermno( termNo );
        this.userR18Check.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        this.userR18Check.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        if ( this.getFlag != false )
        {
            ret = this.userR18Check.updateData( termNo );
        }
        else
        {
            ret = this.userR18Check.insertData();
        }
        if ( ret != false )
        {
            this.validFlag = true;
            this.getFlag = true;
        }

        return(ret);
    }
}
