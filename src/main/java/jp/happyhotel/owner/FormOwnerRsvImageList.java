package jp.happyhotel.owner;

import java.util.ArrayList;
import java.net.*;

/**
 * �摜�ꗗ��� Form�N���X
 * 
 */
public class FormOwnerRsvImageList
{

    public String getHotelID()
    {
        return hotelID;
    }

    public void setHotelID(String hotelID)
    {
        this.hotelID = hotelID;
    }

    public static final int                      PAGE_MAX    = 5;                                         // �P�y�[�W�̍ő�\����

    private String                               hotelID     = "";
    private int                                  selHotelID  = 0;
    private String                               errMsg      = "";
    private int                                  statusKind  = 0;
    private int                                  recordCount = 0;
    private String                               baseDir     = "";                                        // �摜�̊i�[�f�B���N�g��
    private String                               releaseDir  = "";                                        // �����[�X�摜�̃f�B���N�g��
    private int                                  start       = 1;                                         // �\���X�^�[�g�ʒu

    private int                                  imedia_user = 0;                                         // �A�����b�N�X�Ј��i�P�j

    // �摜��񃊃X�g
    private ArrayList<FormOwnerRsvImageListData> imgList     = new ArrayList<FormOwnerRsvImageListData>();

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getStatusKind()
    {
        return statusKind;
    }

    public void setStatusKind(int statusKind)
    {
        this.statusKind = statusKind;
    }

    public int getRecordCount()
    {
        return recordCount;
    }

    public void setRecordCount(int recordCount)
    {
        this.recordCount = recordCount;
    }

    public String getBaseDir()
    {
        return baseDir;
    }

    public void setBaseDir(String baseDir)
    {
        this.baseDir = baseDir;
    }

    public String getReleaseDir()
    {
        return releaseDir;
    }

    public void setReleaseDir(String releaseDir)
    {
        this.releaseDir = releaseDir;
    }

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getImedia_user()
    {
        return imedia_user;
    }

    public void setImedia_user(int imedia_user)
    {
        this.imedia_user = imedia_user;
    }

    public ArrayList<FormOwnerRsvImageListData> getImgList()
    {
        return imgList;
    }

    public void setImgList(FormOwnerRsvImageListData imgData)
    {
        this.imgList.add( imgData );
    }

    /*
     * �摜���
     */
    public class FormOwnerRsvImageListData
    {
        private int    hotelId;    //
        private int    imageId;    //
        private String hotelName;  // �z�e����
        private String userName;   //
        private int    uploadDate; // �A�b�v���[�h��
        private int    uploadTime; // �A�b�v���[�h����
        private String fileName;   // �I���W�i���t�@�C����
        private String upFileName; // �A�b�v���[�h�t�@�C����
        private String releaseFile; // �����[�X�t�@�C����
        private String message;    // ���b�Z�[�W
        private String dispDate;   // �\���p�A�b�v���[�h��
        private String dispTime;   // �\���p����
        private int    status;     // �X�e�[�^�X

        /*
         * �摜�t�@�C���̃p�X�t���t�@�C������Ԃ�
         */
        public String getImagePath(int enocde)
        {
            String imagePath = baseDir + hotelId + "/";
            if ( enocde != 0 )
            {
                try
                {
                    imagePath += URLEncoder.encode( upFileName, "UTF-8" );
                }
                catch ( Exception e )
                {
                }
            }
            else
            {
                imagePath += upFileName;
            }
            return imagePath;
        }

        public String getReleaseImagePath()
        {
            String imagePath = releaseDir + hotelId + "/";
            imagePath += releaseFile;
            return imagePath;
        }

        /*
         * �ȉ��́Agetter, setter
         */

        public int getHotelId()
        {
            return hotelId;
        }

        public void setHotelId(int hotelId)
        {
            this.hotelId = hotelId;
        }

        public int getImageId()
        {
            return imageId;
        }

        public void setImageId(int imageId)
        {
            this.imageId = imageId;
        }

        public String getHotelName()
        {
            return hotelName;
        }

        public void setHotelName(String hotelName)
        {
            this.hotelName = hotelName;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public int getUploadDate()
        {
            return uploadDate;
        }

        public void setUploadDate(int uploadDate)
        {
            this.uploadDate = uploadDate;
        }

        public int getUploadTime()
        {
            return uploadTime;
        }

        public void setUploadTime(int uploadTime)
        {
            this.uploadTime = uploadTime;
        }

        public String getFileName()
        {
            return fileName;
        }

        public void setFileName(String fileName)
        {
            this.fileName = fileName;
        }

        public String getUpFileName()
        {
            return upFileName;
        }

        public void setUpFileName(String upFileName)
        {
            this.upFileName = upFileName;
        }

        public String getReleaseFile()
        {
            return releaseFile;
        }

        public void setReleaseFile(String releaseFile)
        {
            this.releaseFile = releaseFile;
        }

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }

        public String getDispDate()
        {
            return dispDate;
        }

        public void setDispDate(String dispDate)
        {
            this.dispDate = dispDate;
        }

        public String getDispTime()
        {
            return dispTime;
        }

        public void setDispTime(String dispTime)
        {
            this.dispTime = dispTime;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

    }

}
