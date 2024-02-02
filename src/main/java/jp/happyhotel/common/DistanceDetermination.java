/*
 * @(#)DistanceDetermination.java 1.00 2009/07/03 Copyright (C) ALMEX Inc. 2009 �����v���N���X �pURL�쐬�N���X
 */
package jp.happyhotel.common;

import java.io.Serializable;

/**
 * �����v���N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/17
 * @see "yahooMapAPI�ɂ��ẮA�n�}�����n�����܂��́AAPICourseDevelopersGuide���Q�Ƃ��Ă��������B"
 */
public class DistanceDetermination implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -4000275997311216660L;

    /**
     * �����v�����\�b�h�i�}�b�v�|�C���g�̈ܓx�o�x�ƃz�e���̈ܓx�o�x�j
     * 
     * @param startLat �X�^�[�g�n�_�̈ܓx(���{���n�n:10�i�\�L)
     * @param startLon �X�^�[�g�n�_�̌o�x(���{���n�n:10�i�\�L)
     * @param goalLat �S�[���n�_�̈ܓx(���{���n�n:10�i�\�L)
     * @param goalLon �S�[���n�_�̌o�x(���{���n�n:10�i�\�L)
     * @return 2�_�̋���(�P�ʂ̓��[�g��)
     */
    public int getDistance(int startLat, int startLon, int goalLat, int goalLon)
    {
        double startLatW;
        double startLonW;
        double goalLatW;
        double goalLonW;
        double startX;
        double startY;
        double goalX;
        double goalY;
        double degree;
        double dist;
        long distLong;
        int distNum;

        try
        {
            startLatW = (double)startLat;
            startLonW = (double)startLon;
            goalLatW = (double)goalLat;
            goalLonW = (double)goalLon;
            startX = (goalLonW / 3600000) * Math.PI / 180;
            startY = (goalLatW / 3600000) * Math.PI / 180;
            goalX = (startLonW / 3600000) * Math.PI / 180;
            goalY = (startLatW / 3600000) * Math.PI / 180;
            degree = Math.sin( startY ) * Math.sin( goalY ) + Math.cos( startY ) * Math.cos( goalY ) * Math.cos( goalX - startX );
            dist = 6378140 * (Math.atan( -degree / Math.sqrt( -degree * degree + 1 ) ) + Math.PI / 2);
            distLong = Math.round( dist );
            distNum = (int)distLong;
        }
        catch ( Exception e )
        {
            Logging.error( "[DistanceDetermination.getDistance] Exception:" + e.toString() );
            distNum = 0;
        }
        return(distNum);
    }
}
