package jp.happyhotel.data;

import jp.happyhotel.sponsor.SponsorData_M2;

public class DataSearchArea_M2
{

    private DataHotelCity_M2[] dataHotelCity;
    private DataHotelArea_M2[] dataHotelArea;
    private int                cityCount;
    private int                hotelAreaCount;
    // �X�|���T�[�L��
    private SponsorData_M2     sponsorData;
    // ���[�e�[�V�����o�i�[�p
    private SponsorData_M2     randomSponsorData;
    private boolean            sponserDisplayResult;
    private boolean            randomSponserDisplayResult;
    private String             prefName;
    private String             parameter1;

    public String getParameter1()
    {
        return parameter1;
    }

    public void setParameter1(String parameter1)
    {
        this.parameter1 = parameter1;
    }

    /**
     * @return �X�|���T�[�L����\�����邩�ǂ�����Ԃ�
     */
    public boolean isSponserDisplayResult()
    {
        return sponserDisplayResult;
    }

    /**
     * @param �X�|���T�[�L���̃f�[�^�̂���Ȃ��𔻒f����
     */
    public void setSponserDisplayResult(boolean sponserDisplayResult)
    {
        this.sponserDisplayResult = sponserDisplayResult;
    }

    /**
     * @return �X�|���T�[�L���f�[�^�̎擾
     */
    public SponsorData_M2 getSponsorData()
    {
        return sponsorData;
    }

    /**
     * @param �X�|���T�[�L���f�[�^�̃Z�b�g
     */
    public void setSponsorData(SponsorData_M2 sponsorData)
    {
        this.sponsorData = sponsorData;
    }

    /**
     * @return ���[�e�[�V�����o�i�[��\�����邩�ǂ�����Ԃ�
     */
    public boolean isRandomSponserDisplayResult()
    {
        return randomSponserDisplayResult;
    }

    /**
     * @param ���[�e�[�V�����o�i�[�̃f�[�^�̂���Ȃ��𔻒f����
     */
    public void setRandomSponsorDisplayResult(boolean randomSponsorDisplayResult)
    {
        this.randomSponserDisplayResult = randomSponsorDisplayResult;
    }

    /**
     * @return ���[�e�[�V�����o�i�[�f�[�^�̎擾
     */
    public SponsorData_M2 getRandomSponsorData()
    {
        return randomSponsorData;
    }

    /**
     * @param ���[�e�[�V�����o�i�[�f�[�^�̃Z�b�g
     */
    public void setRandomSponsorData(SponsorData_M2 randomSponsorData)
    {
        this.randomSponsorData = randomSponsorData;
    }

    public DataHotelArea_M2[] getDataHotelArea()
    {
        return dataHotelArea;
    }

    public void setDataHotelArea(DataHotelArea_M2[] dataHotelArea)
    {
        this.dataHotelArea = dataHotelArea;
    }

    public DataHotelCity_M2[] getDataHotelCity()
    {
        return dataHotelCity;
    }

    public void setDataHotelCity(DataHotelCity_M2[] dataHotelCity)
    {
        this.dataHotelCity = dataHotelCity;
    }

    public int getCityCount()
    {
        return cityCount;
    }

    public void setCityCount(int cityCount)
    {
        this.cityCount = cityCount;
    }

    public int getHotelAreaCount()
    {
        return hotelAreaCount;
    }

    public void setHotelAreaCount(int hotelAreaCount)
    {
        this.hotelAreaCount = hotelAreaCount;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

}
