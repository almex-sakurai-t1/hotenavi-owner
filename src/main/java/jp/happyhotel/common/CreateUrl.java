/*
 * @(#)CreateUrl.java 1.00 2009/06/17 Copyright (C) ALMEX Inc. 2009 yahoo map �pURL�쐬�N���X
 */
package jp.happyhotel.common;

import java.io.Serializable;
import java.net.URLEncoder;

/**
 * yahoo map�pURL�쐬�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/17
 * @see "yahooMapAPI�ɂ��ẮA�n�}�����n�����܂��́AAPICourseDevelopersGuide���Q�Ƃ��Ă��������B"
 */
public class CreateUrl implements Serializable
{

    private static final long serialVersionUID = -3280429617771026903L;
    private final String      YOLP_APP_ID      = "?appid=dj0zaiZpPWEybUpEVXg2NFNiNiZzPWNvbnN1bWVyc2VjcmV0Jng9M2U-";            // �A�v���P�[�V����ID
    private final String      YOLP_URL         = "http://map.olp.yahooapis.jp/OpenLocalPlatform/V1/static";                    // API�Ăяo��URL
    private final String      YOLP_REVERSE_URL = "http://reverse.search.olp.yahooapis.jp/OpenLocalPlatform/V1/reverseGeoCoder"; // API�Ăяo��URL
    private final String      YOLP_ROUTE_ID    = "?appid=dj0zaiZpPXF2cWVaNzVTdlRjbiZzPWNvbnN1bWVyc2VjcmV0Jng9NjU-";
    private final String      YOLP_ROUTE_URL   = "http://routemap.olp.yahooapis.jp/OpenLocalPlatform/V1/routeMap";             // RouteAPI�Ăяo�� URL
    private final String      APP_ID           = "appid=o9_096x27";                                                            // �A�v���P�[�V����ID
    private final String      URL              = "http://api.pmx.proatlas.net/";                                               // API�Ăяo��URL
    private final String      PES_API          = "PESWebService/v1/";                                                          // PES API
    private final String      SOKODOKO_API     = "SokodokoWebService/v1/";                                                     // SOKODOKO API
    private final String      SMARTROUTING_API = "SmartRoutingWebService/v1/routing?";                                         // SmartRouting API�irouting���\�b�h���݁j
    private final String      DRAWMAP          = "drawMap?";                                                                   // drawMap���\�b�h
    private final String      GETMAPINFO       = "getMapInfo?";                                                                // getMapInfo���\�b�h
    private final String      GEODECODE        = "geoDecode?";                                                                 // getDecode���\�b�h
    private final String      GEOCODER_URL     = "http://geo.search.olp.yahooapis.jp/OpenLocalPlatform/V1/geoCoder";

    // ����l
    private final int         WALKINGSPEED     = 5;
    private final int         HIGHWAYSPEED     = 100;
    private final int         NATIONROUTESPEED = 60;
    private final int         PREFROADSPEED    = 50;
    private final int         OTHERSROADSPEED  = 20;
    private final int         TOLLROADSPEED    = 80;
    private final int         ONE              = 1;
    private final int         TWO              = 2;
    private final int         THREE            = 3;

    // ���ʂŎg�p����ϐ�
    private String            point;
    private String            output;
    // PES API��SmartRouting API�ŋ���
    private String            outdatum;
    // PES API�Ŏg�p����ϐ�
    private int               width;
    private int               height;
    private int               scale;
    private int               dscaleX;
    private int               dscaleY;
    private int               dscale;
    private boolean           widthMobile;
    private boolean           heightMobile;
    private String            upperLeft;
    private String            upperRight;
    private String            downLeft;
    private String            downRight;
    private String            center;
    private String            pin;
    private String            pindefault;
    private String            select;
    private String            color;
    private String            scalebar;
    private String            info;
    private String            map;
    private String            pos;
    private String            blink;
    private String            circle;
    private String            line;
    private String            area;
    private String            strWord;
    private String            pointlist;
    private String            radius;
    private String            imgtype;
    private String            dataname;
    private String            address;
    private String            zipcode;
    private String            lat;
    private String            lon;
    // SmartRouting API�Ŏg�p����ϐ�
    private int               tollPriority;
    private int               costPriority;
    private int               transport;
    private int               travelingFlag;
    private int               useAroundTollRoad;
    private int               speedWalk;
    private int               speedHighway;
    private int               speedCityHighway;
    private int               speedNationRoute;
    private int               speedMainRoad;
    private int               speedPrefRoad;
    private int               speedOtherRoad;
    private int               speedTollRoad;
    private int               detailInfo;
    // SokodokoAPI�Ŏg�p����ϐ�
    private int               mlv;

    /**
     * �f�[�^�����������܂��B
     */
    public CreateUrl()
    {
        // ���ʂŎg�p����ϐ�
        point = "";
        output = "";
        // PES API��SmartRouting API�Ŏg�p����ϐ�
        outdatum = "";
        // PES API�Ŏg�p����ϐ�
        width = 0;
        height = 0;
        scale = 0;
        dscaleX = 0;
        dscaleY = 0;
        dscale = 0;
        widthMobile = false;
        heightMobile = false;
        select = "";
        upperLeft = "";
        upperRight = "";
        downLeft = "";
        downRight = "";
        center = "";
        pin = "";
        pindefault = "";
        color = "";
        scalebar = "";
        info = "";
        map = "";
        pos = "";
        blink = "";
        circle = "";
        line = "";
        area = "";
        strWord = "";
        pointlist = "";
        radius = "";
        imgtype = "";
        dataname = "";
        address = "";
        zipcode = "";
        lat = "";
        lon = "";
        // SmartRouting API�Ŏg�p����ϐ�
        tollPriority = 0;
        costPriority = 0;
        transport = 0;
        travelingFlag = 0;
        useAroundTollRoad = 0;
        speedWalk = 0;
        speedHighway = 0;
        speedCityHighway = 0;
        speedNationRoute = 0;
        speedMainRoad = 0;
        speedPrefRoad = 0;
        speedOtherRoad = 0;
        speedTollRoad = 0;
        detailInfo = 0;
        // Sokodoko API�Ŏg�p����ϐ�
        mlv = 0;
    }

    /*---------------��getter��---------------*/

    // ���ʂŎg�p���郁�\�b�h
    /**
     * �`�悷��R���e���c�̈ܓx�A�o�x���擾
     */
    public String getPoint()
    {
        return(point);
    }

    /**
     * �o�̓p�����[�^�̑��n�n���擾
     */
    public String getOutdatum()
    {
        return(outdatum);
    }

    /**
     * �o�̓p�����[�^�̑��n�n���擾
     */
    public String getOutput()
    {
        return(output);
    }

    // PES API�Ŏg�p���郁�\�b�h
    /**
     * �n�}�̕��i�摜�T�C�Y�j���擾
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * �n�}�̍����i�摜�T�C�Y�j���擾
     */
    public int getHeight()
    {
        return(height);
    }

    /**
     * �n�}�̏k�ڂ��擾
     */
    public int getScale()
    {
        return scale;
    }

    /**
     * �n�}�̉������̈ړ��ʂ��擾
     */
    public int getDx()
    {
        return(dscaleX);
    }

    /**
     * �n�}�̏c�����̈ړ��ʂ��擾
     */
    public int getDy()
    {
        return(dscaleY);
    }

    /**
     * �ړ��ʂ̊�ƂȂ�k�ڂ��擾
     */
    public int getDscale()
    {
        return(dscale);
    }

    /**
     * �g�ь����̒n�}���E�摜�t�H�[�}�b�g���������@�\�̗L���̎擾
     */
    public boolean getWm()
    {
        return widthMobile;
    }

    /**
     * �g�ь����̒n�}�����E�摜�t�H�[�}�b�g���������@�\�̗L���̎擾
     */
    public boolean getHm()
    {
        return heightMobile;
    }

    /**
     * �n�}�̍���̈ܓx�o�x�̎擾
     */
    public String getUl()
    {
        return(upperLeft);
    }

    /**
     * �n�}�̉E��̈ܓx�o�x�̎擾
     */
    public String getUr()
    {
        return(upperRight);
    }

    /**
     * �n�}�̍����̈ܓx�o�x�̎擾
     */
    public String getDl()
    {
        return(downLeft);
    }

    /**
     * �n�}�̉E���̈ܓx�o�x�̎擾
     */
    public String getDr()
    {
        return(downRight);
    }

    /**
     * �n�}�̒��S�̈ܓx�o�x�̎擾
     */
    public String getC()
    {
        return(center);
    }

    /**
     * �z�e����PIN�̂̈ܓx�o�x�̎擾
     */
    public String getPin()
    {
        return(pin);
    }

    /**
     * �z�e����PIN�̂̈ܓx�o�x�̎擾
     */
    public String getPindefault()
    {
        return(pindefault);
    }

    /**
     * �k�ڊK�w�̑I����@���擾
     */
    public String getSelect()
    {
        return select;
    }

    /**
     * �g���p���b�g���擾
     */
    public String getColor()
    {
        return(color);
    }

    /**
     * �X�P�[���o�[�̗L�����擾
     */
    public String getScaleBar()
    {
        return(scalebar);
    }

    /**
     * �o�̓p�����[�^�̗L�����擾
     */
    public String getInfo()
    {
        return(info);
    }

    /**
     * �n�}�摜�o�̗͂L�����擾
     */
    public String getMap()
    {
        return(map);
    }

    /**
     * �`�悷��R���e���c���擾
     */
    public String getPos()
    {
        return(pos);
    }

    /**
     * ��������A�C�R����pos�p�����[�^�ɂ�����index���擾
     */
    public String getBlink()
    {
        return(blink);
    }

    /**
     * �`�悷��~���擾
     */
    public String getCircle()
    {
        return(circle);
    }

    /**
     * �`�悷����̎擾
     */
    public String getLine()
    {
        return(line);
    }

    /**
     * �`�悷��ʂ̎擾
     */
    public String getArea()
    {
        return(area);
    }

    /**
     * �`�悷�镶���̎擾
     */
    public String getStr()
    {
        return(strWord);
    }

    /**
     * �`�悷����̈ܓx�o�x�̎擾
     */
    public String getPointList()
    {
        return(pointlist);
    }

    /**
     * �`�悷��~�̔��a���擾
     */
    public String getRadius()
    {
        return(radius);
    }

    /**
     * �摜�̏o�͌`�����擾
     */
    public String getImgType()
    {
        return(imgtype);
    }

    /**
     * �n�}�̎�ނ��擾
     */
    public String getDataName()
    {
        return(dataname);
    }

    /**
     * ��������Z����������擾
     */
    public String getAddress()
    {
        return(address);
    }

    /**
     * ��������X�֔ԍ����擾
     */
    public String getZipCode()
    {
        return(zipcode);
    }

    // SmartRouting API�Ŏg�p���郁�\�b�h
    /**
     * �D�悷�铹�H���擾
     */
    public int getTollPriority()
    {
        return(tollPriority);
    }

    /**
     * ���ԁE�����̗D��̎擾
     */
    public int getCostPriority()
    {
        return(costPriority);
    }

    /**
     * �ړ���i�̎擾
     */
    public int getTransport()
    {
        return(transport);
    }

    /**
     * ����o�H�������s���t���O�̎擾
     */
    public int getTravelingFlag()
    {
        return(travelingFlag);
    }

    /**
     * �L�����H��̎n�I�_�̑Ώۃt���O�̎擾
     */
    public int getUseAroundTollRoad()
    {
        return(useAroundTollRoad);
    }

    /**
     * �k���̑��x�̎擾
     */
    public int getSpeedWalk()
    {
        return(speedWalk);
    }

    /**
     * �������H�̑��x�̎擾
     */
    public int getSpeedHighway()
    {
        return(speedHighway);
    }

    /**
     * �s�s�����̑��x�̎擾
     */
    public int getSpeedCityHighway()
    {
        return(speedCityHighway);
    }

    /**
     * �����̑��x�̎擾
     */
    public int getSpeedNationRoute()
    {
        return(speedNationRoute);
    }

    /**
     * ��v���̑��x�̎擾
     */
    public int getSpeedMainRoad()
    {
        return(speedMainRoad);
    }

    /**
     * �����̑��x�̎擾
     */
    public int getSpeedPrefRoad()
    {
        return(speedPrefRoad);
    }

    /**
     * ���̑��̓��̑��x�̎擾
     */
    public int getSpeedOtherRoad()
    {
        return(speedOtherRoad);
    }

    /**
     * �L�����̑��x�̎擾
     */
    public int getSpeedTollRoad()
    {
        return(speedTollRoad);
    }

    /**
     * NordList,PartList�̏o�̓t���O���擾
     */
    public int getDetailInfo()
    {
        return(detailInfo);
    }

    /**
     * @return ��͐[�x���擾
     */
    public int getMlv()
    {
        return mlv;
    }

    /**
     * @return �ܓx���擾
     */
    public String getLat()
    {
        return lat;
    }

    /**
     * @return �o�x���擾
     */
    public String getLon()
    {
        return lon;
    }

    /*---------------��getter��---------------*/

    /*---------------��setter��---------------*/
    // ���ʂŎg�p���郁�\�b�h
    /**
     * �`�悷��R���e���c�̈ܓx�A�o�x���Z�b�g
     * 
     * @see "��:Tokyo97,35,135���Z�b�g����B"
     */
    public void setPoint(String point)
    {
        this.point = point;
    }

    /**
     * �o�̓p�����[�^�̑��n�n���Z�b�g
     * 
     * @see "Tokyo97(����l),JGD2000,WGS84���Z�b�g����"
     */
    public void setOutdatum(String outdatum)
    {
        this.outdatum = outdatum;
    }

    /**
     * �o�̓p�����[�^�̑��n�n���Z�b�g
     * 
     * @see "xml(����l),json���Z�b�g����"
     */
    public void setOutput(String output)
    {
        this.output = output;
    }

    /**
     * �n�}�̕��i�摜�T�C�Y�j���Z�b�g
     * 
     * @see "����l: 320 (�P��=�s�N�Z��),�ő�l: 1280"
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * �n�}�̍����i�摜�T�C�Y�j���Z�b�g
     * 
     * @see "����l: 240 (�P��=�s�N�Z��),�ő�l: 1280"
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * �n�}�̏k�ڂ��Z�b�g
     * 
     * @see "�w�肳�ꂽ�ܓx�o�x�ŕ\���ł���ő�k�ڂ�����l�ƂȂ�B"
     */
    public void setScale(int scale)
    {
        this.scale = scale;
    }

    /**
     * �n�}�̉������̈ړ��ʂ��Z�b�g
     * 
     * @see "�P�ʂ̓h�b�g"
     */
    public void setDx(int dx)
    {
        this.dscaleX = dx;
    }

    /**
     * �n�}�̏c�����̈ړ��ʂ��Z�b�g
     * 
     * @see "�P�ʂ̓h�b�g"
     */
    public void setDy(int dy)
    {
        this.dscaleY = dy;
    }

    /**
     * �ړ��ʂ̊�ƂȂ�k�ڂ��Z�b�g
     */
    public void setDscale(int dscale)
    {
        this.dscale = dscale;
    }

    /**
     * �g�ь����̒n�}���E�摜�t�H�[�}�b�g���������@�\�̗L���̃Z�b�g
     * 
     * @see "true:�g�p,false:�g�p���Ȃ�(����l)"
     */
    public void setWm(boolean wm)
    {
        this.widthMobile = wm;
    }

    /**
     * �g�ь����̒n�}�����E�摜�t�H�[�}�b�g���������@�\�̗L���̃Z�b�g
     * 
     * @see "true:�g�p,false:�g�p���Ȃ�(����l)"
     */
    public void setHm(boolean hm)
    {
        this.heightMobile = hm;
    }

    /**
     * �n�}�̍���̈ܓx�o�x�̃Z�b�g
     */
    public void setUl(String ul)
    {
        this.upperLeft = ul;
    }

    /**
     * �n�}�̉E��̈ܓx�o�x�̂��Z�b�g
     */
    public void setUr(String ur)
    {
        this.upperRight = ur;
    }

    /**
     * �n�}�̍����̈ܓx�o�x�̃Z�b�g
     */
    public void setDl(String dl)
    {
        this.downLeft = dl;
    }

    /**
     * �n�}�̉E���̈ܓx�o�x�̃Z�b�g
     */
    public void setDr(String dr)
    {
        this.downRight = dr;
    }

    /**
     * �n�}�̒��S�̈ܓx�o�x�̃Z�b�g
     */
    public void setC(String c)
    {
        this.center = c;
    }

    /**
     * �z�e���̈ܓx�o�x�̃Z�b�g
     */
    public void setPin(String pin)
    {
        this.pin = pin;
    }

    /**
	 *
	 */
    public void setPindefault(String pindefault)
    {
        this.pindefault = pindefault;
    }

    /**
     * �k�ڊK�w�̑I����@���Z�b�g
     * 
     * @see "point,area,comp,scroll�̂��Âꂩ���Z�b�g"
     */
    public void setSelect(String select)
    {
        this.select = select;
    }

    /**
     * �g���p���b�g���Z�b�g
     * 
     * @see "point,area,comp,scroll�̂��Âꂩ���Z�b�g"
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * �X�P�[���o�[�̗L�����Z�b�g
     * 
     * @see "yes:����(����l),no:�Ȃ�"
     */
    public void setScaleBar(String scalebar)
    {
        this.scalebar = scalebar;
    }

    /**
     * �o�̓p�����[�^�̗L�����Z�b�g
     * 
     * @see "yes:(����l),no;�Ȃ�"
     */
    public void setInfo(String info)
    {
        this.info = info;
    }

    /**
     * �n�}�摜�o�̗͂L�����Z�b�g
     * 
     * @see "yes:����(����l),no:�Ȃ�"
     */
    public void setMap(String map)
    {
        this.map = map;
    }

    /**
     * �`�悷��R���e���c���Z�b�g
     * 
     * @see "I:�����w��(no,null,star,pin�ȂǕ\������A�C�R�����w�肷��),p:���S�ܓx�o�x"
     */
    public void setPos(String pos)
    {
        this.pos = pos;
    }

    /**
     * ��������A�C�R����pos�p�����[�^�ɂ�����index���Z�b�g
     * 
     * @see "index�l:(0�`49)"
     */
    public void setBlink(String blink)
    {
        this.blink = blink;
    }

    /**
     * �`�悷��~���Z�b�g(API Course Developers Guide���Q��)
     * 
     * @see "A:�~���̐F,B:�~�����̐F,R:���a(���[�g��),S:�~�̓h��Ԃ����@, W:�~�����̕����w�肷��(����l:2),P:�~�̒��S�ܓx�o�x"
     */
    public void setCircle(String circle)
    {
        this.circle = circle;
    }

    /**
     * �`�悷����̃Z�b�g(API Course Developers Guide���Q��)
     * 
     * @see "c:�F,W:��(����l2),P:�����������߂̎n�I�_"
     */
    public void setLine(String line)
    {
        this.line = line;
    }

    /**
     * �`�悷��ʂ̃Z�b�g
     * 
     * @see "A:�ʂ̐F,B:���E���̐F,S:�ʂ̓h��Ԃ����@,W:���E���̕�,P:�ʂ�`�����߂̈ܓx�o�x"
     */
    public void setArea(String area)
    {
        this.area = area;
    }

    /**
     * �`�悷�镶���̃Z�b�g
     * 
     * @see "X:�������̔z�u���W(�h�b�g),Y:�c�����̔z�u���W,B:�w�i�F,F:�����F,T:�t�H���g�^�C�v,P:�ܓx�o�x, W:�������̍��W�C��,H:�c�����̍��W�C��,S:�\�����镶����"
     */
    public void setStr(String str)
    {
        this.strWord = str;
    }

    /**
     * �`�悷����̈ܓx�o�x�̃Z�b�g
     */
    public void setPointList(String pointlist)
    {
        this.pointlist = pointlist;
    }

    /**
     * �`�悷��~�̔��a���Z�b�g
     * 
     * @see "���[�g���P�ʁi����l:1000�j"
     */
    public void setRadius(String radius)
    {
        this.radius = radius;
    }

    /**
     * �摜�̏o�͌`�����Z�b�g
     * 
     * @see "PNG(����l),JPG,GIF���Z�b�g"
     */
    public void setImgType(String imgtype)
    {
        this.imgtype = imgtype;
    }

    /**
     * �n�}�̎�ނ��Z�b�g
     * 
     * @see "ProAlas:�v���A�g���X�n�},ProAtlasWeb:Web�n�}(����l),ProAtlasWebCity:�s�X�n�}, ProAtlasCrear:�N���A�n�},ProAtlasCrearCity:�N���A�s�X�n�}"
     */
    public void setDataName(String dataname)
    {
        this.dataname = dataname;
    }

    /**
     * ��������Z����������Z�b�g
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * ��������X�֔ԍ����Z�b�g
     */
    public void setZipCode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    // SmartRouting API�Ŏg�p���郁�\�b�h
    /**
     * �D�悷�铹�H���Z�b�g
     * 
     * @see "1:�L�����H�D��(����l),2:��ʓ��D��,3:��ʓ��̂�"
     */
    public void setTollPriority(int tolPriority)
    {
        this.tollPriority = tolPriority;
    }

    /**
     * ���ԁE�����̗D��̎擾
     * 
     * @see "1:���ԗD��(����l),2:�����D��"
     */
    public void setCostPriority(int costPriority)
    {
        this.costPriority = costPriority;
    }

    /**
     * �ړ���i�̃Z�b�g
     * 
     * @see "1:��(����l),2:�k��"
     */
    public void setTransport(int transport)
    {
        this.transport = transport;
    }

    /**
     * ����o�H�������s���t���O�̃Z�b�g
     * 
     * @see "0:����o�H�������s��Ȃ�(����l),1:����o�H�������s��"
     */
    public void setTravelingFlag(int travelingFlag)
    {
        this.travelingFlag = travelingFlag;
    }

    /**
     * �L�����H��̎n�I�_�̑Ώۃt���O�̃Z�b�g
     * 
     * @see "0:�L�����H��̎n�I�_�̑ΏۂƂ��Ȃ�(����l),1:�L�����H��̎n�I�_�̑ΏۂƂ���"
     */
    public void setUseAroundTollRoad(int useAroundTollRoad)
    {
        this.useAroundTollRoad = useAroundTollRoad;
    }

    /**
     * �k���̑��x�̃Z�b�g
     * 
     * @see "����l:5"
     */
    public void setSpeedWalk(int speedWalk)
    {
        this.speedWalk = speedWalk;
    }

    /**
     * �������H�̑��x�̃Z�b�g
     * 
     * @see "����l:100"
     */
    public void setSpeedHighWay(int speedHighway)
    {
        this.speedHighway = speedHighway;
    }

    /**
     * �s�s�����̑��x�̃Z�b�g
     * 
     * @see "����l:100"
     */
    public void setSpeedCityHighWay(int speedCityHighway)
    {
        this.speedCityHighway = speedHighway;
    }

    /**
     * �����̑��x�̃Z�b�g
     * 
     * @see "����l:60"
     */
    public void setSpeedNationRoute(int speedNationRoute)
    {
        this.speedNationRoute = speedNationRoute;
    }

    /**
     * ��v���̑��x�̃Z�b�g
     * 
     * @see "����l:60"
     */
    public void setSpeedMainRoad(int speedMainRoad)
    {
        this.speedMainRoad = speedMainRoad;
    }

    /**
     * �����̑��x�̃Z�b�g
     * 
     * @see "����l:50"
     */
    public void setSpeedPrefRoad(int speedPrefRoad)
    {
        this.speedPrefRoad = speedPrefRoad;
    }

    /**
     * ���̑��̓��̑��x�̃Z�b�g
     * 
     * @see "����l:20"
     */
    public void setSpeedOtherRoad(int speedOtherRoad)
    {
        this.speedOtherRoad = speedOtherRoad;
    }

    /**
     * �L�����̑��x�̃Z�b�g
     * 
     * @see "����l:80"
     */
    public void setSpeedTollRoad(int speedTollRoad)
    {
        this.speedTollRoad = speedTollRoad;
    }

    /**
     * NordList,PartList�̏o�̓t���O���Z�b�g
     * 
     * @see "0:�o�͂��Ȃ�(����l),1:�o�͂���"
     */
    public void setDetailInfo(int detailInfo)
    {
        this.detailInfo = detailInfo;
    }

    /**
     * @param mlv �Z�b�g�����͐[�x
     * @see "1:�s���{��,2:�s�撬��,3:���厚,4:���ڂ܂Ń}�b�`���O"
     */
    public void setMlv(int mlv)
    {
        this.mlv = mlv;
    }

    /**
     * /**
     * �ܓx���Z�b�g
     * */
    public void setLat(String lat)
    {
        this.lat = lat;
    }

    /**
     * �o�x���Z�b�g
     * */
    public void setLon(String lon)
    {
        this.lon = lon;
    }

    /*---------------��setter��---------------*/

    /**
     * PES API�̒n�}�摜�\�����\�b�h���Ăяo��URL�̍쐬
     * 
     * @return �쐬����URL
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    public String getDrawMap()
    {
        String returnUrl;
        returnUrl = URL + PES_API + DRAWMAP + APP_ID;

        // JGD2000��WGS84�̂Ƃ��̂݃p�����[�^��ǉ�(���ʍ���)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        /*---------------��PES API���ʍ��ځ�---------------*/
        returnUrl += stringCheck( "ul", upperLeft );
        returnUrl += stringCheck( "ur", upperRight );
        returnUrl += stringCheck( "dl", downLeft );
        returnUrl += stringCheck( "dr", downRight );
        returnUrl += stringCheck( "c", center );
        returnUrl += intCheck( "scale", scale );

        // �C�ӂ̕����񂪓����Ă��Ȃ�������ǉ����Ȃ�
        if ( select.indexOf( "point" ) != -1 || (select.indexOf( "area" ) != -1) ||
                select.indexOf( "comp" ) != -1 || select.indexOf( "scroll" ) != -1 )
        {
            returnUrl += "&select=" + encodeUtf8( select );
        }

        // dx,dy,dscale��3������ĂȂ��Ɩ����ƂȂ邽�߁Adscale�̒l��0�ȊO��������Z�b�g����
        if ( dscale != 0 )
        {
            returnUrl += intCheck( "dscale", dscale );
            returnUrl += "&dx=" + encodeUtf8( Integer.toString( dscaleX ) );
            returnUrl += "&dy=" + encodeUtf8( Integer.toString( dscaleY ) );
        }
        returnUrl += intCheck( "width", width );
        returnUrl += intCheck( "height", height );
        /*---------------��PES API���ʍ��ځ�---------------*/

        /*---------------��drawMap���\�b�h���ځ�---------------*/
        returnUrl += boolCheck( "wm", widthMobile );
        returnUrl += boolCheck( "hm", heightMobile );

        // gray��halftone�̂Ƃ������p�����[�^������
        if ( color.compareTo( "gray" ) == 0 || color.compareTo( "halftone" ) == 0 )
        {
            returnUrl += "&color=" + encodeUtf8( color );
        }

        // no�ƈ�v����Ƃ������p�����[�^��ǉ�����
        returnUrl += stringCheckEqual( "scalebar", scalebar, "no" );
        returnUrl += stringCheckEqual( "info", info, "no" );
        returnUrl += stringCheckEqual( "map", map, "no" );

        // �C�ӂ̃R�}���h�����������Ă���ꍇ�̂݃p�����[�^��ǉ�����
        if ( pos.indexOf( "I" ) != -1 || pos.indexOf( "P" ) != -1 )
        {
            returnUrl += "&pos=" + encodeUtf8( pos );
            // �u�����N��pos�p�����[�^��index���w�肷��̂ŁApos�̃p�����[�^��ǉ�����Ƃ��̂�
            returnUrl += stringCheck( "blink", blink );
        }
        if ( circle.indexOf( "A" ) != -1 || circle.indexOf( "B" ) != -1 ||
                circle.indexOf( "R" ) != -1 || circle.indexOf( "S" ) != -1 ||
                circle.indexOf( "W" ) != -1 )
        {
            returnUrl += "&circle=" + encodeUtf8( circle );
        }
        if ( line.indexOf( "C" ) != -1 || line.indexOf( "W" ) != -1 || line.indexOf( "P" ) != -1 )
        {
            returnUrl += "&line=" + encodeUtf8( line );
        }
        if ( area.indexOf( "A" ) != -1 || area.indexOf( "B" ) != -1 || area.indexOf( "S" ) != -1 ||
                area.indexOf( "W" ) != -1 || area.indexOf( "P" ) != -1 )
        {
            returnUrl += "&area=" + encodeUtf8( area );
        }
        if ( strWord.indexOf( "X" ) != -1 || strWord.indexOf( "Y" ) != -1 || strWord.indexOf( "B" ) != -1 ||
                strWord.indexOf( "F" ) != -1 || strWord.indexOf( "T" ) != -1 || strWord.indexOf( "P" ) != -1 ||
                strWord.indexOf( "W" ) != -1 || strWord.indexOf( "H" ) != -1 )
        {
            returnUrl += "&str=" + encodeUtf8( strWord );
        }

        // �f�[�^������΁A�p�����[�^�Ƃ��Ēǉ�
        returnUrl += stringCheck( "point", point );
        returnUrl += stringCheck( "pointlist", pointlist );

        // JPG��GIF�ƈ�v�����Ƃ������A�p�����[�^��ǉ�����
        if ( imgtype.compareTo( "JPG" ) == 0 || imgtype.compareTo( "GIF" ) == 0 )
        {
            returnUrl += "&imgtype=" + encodeUtf8( imgtype );
        }

        // ����l�ȊO�̎�ނ������Ă����Ƃ��̂݃p�����[�^��ǉ�����
        if ( dataname.compareTo( "ProAtlas" ) == 0 || dataname.compareTo( "ProAtlasWebCity" ) == 0 ||
                dataname.compareTo( "ProAtlasClear" ) == 0 || dataname.compareTo( "ProAtlasClearCity" ) == 0 )
        {
            returnUrl += "&dataname=" + encodeUtf8( dataname );
        }
        else
        {
            returnUrl += "&dataname=ProAtlasClearCity";
        }

        // �f�[�^������΁A�p�����[�^�Ƃ��Ēǉ�
        returnUrl += stringCheck( "zipcode", zipcode );
        returnUrl += stringCheck( "address", address );
        // returnUrl += "&address=" + address;
        /*---------------��drawMap���\�b�h���ځ�---------------*/

        return(returnUrl);
    }

    /**
     * PES API�̒n�}�摜�\�����\�b�h���Ăяo��URL�̍쐬
     * 
     * @return �쐬����URL
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    public String getYolpDrawMap()
    {
        String returnUrl;
        returnUrl = YOLP_URL + YOLP_APP_ID;

        // JGD2000��WGS84�̂Ƃ��̂݃p�����[�^��ǉ�(���ʍ���)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        /*---------------��PES API���ʍ��ځ�---------------*/
        returnUrl += stringCheck( "ul", upperLeft );
        returnUrl += stringCheck( "ur", upperRight );
        returnUrl += stringCheck( "dl", downLeft );
        returnUrl += stringCheck( "dr", downRight );
        returnUrl += stringCheck( "c", center );
        returnUrl += stringCheck( "pindefault", pin );
        returnUrl += intCheck( "scale", scale );

        // �C�ӂ̕����񂪓����Ă��Ȃ�������ǉ����Ȃ�
        if ( select.indexOf( "point" ) != -1 || (select.indexOf( "area" ) != -1) ||
                select.indexOf( "comp" ) != -1 || select.indexOf( "scroll" ) != -1 )
        {
            returnUrl += "&select=" + encodeUtf8( select );
        }

        // dx,dy,dscale��3������ĂȂ��Ɩ����ƂȂ邽�߁Adscale�̒l��0�ȊO��������Z�b�g����
        if ( dscale != 0 )
        {
            returnUrl += intCheck( "dscale", dscale );
            returnUrl += "&dx=" + encodeUtf8( Integer.toString( dscaleX ) );
            returnUrl += "&dy=" + encodeUtf8( Integer.toString( dscaleY ) );
        }

        /*---------------��PES API���ʍ��ځ�---------------*/

        /*---------------��drawMap���\�b�h���ځ�---------------*/
        /*---------------��drawMap���\�b�h���ځ�---------------*/

        return(returnUrl);
    }

    /**
     * PES API�̒n�}�摜�̏����擾����URL�̍쐬(XML�`���ŕԂ�)
     * 
     * @return �쐬����URL
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    public String getMapInfo()
    {
        String returnUrl;
        returnUrl = URL + PES_API + GETMAPINFO + APP_ID;

        // JGD2000��WGS84�̂Ƃ��̂݃p�����[�^��ǉ�(���ʍ���)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        /*---------------��PES API���ʍ��ځ�---------------*/
        returnUrl += stringCheck( "ul", upperLeft );
        returnUrl += stringCheck( "ur", upperRight );
        returnUrl += stringCheck( "dl", downLeft );
        returnUrl += stringCheck( "dr", downRight );
        returnUrl += stringCheck( "c", center );
        returnUrl += intCheck( "scale", scale );

        // �C�ӂ̕����񂪓����Ă��Ȃ�������ǉ����Ȃ�
        if ( select.indexOf( "point" ) != -1 || (select.indexOf( "area" ) != -1) ||
                select.indexOf( "comp" ) != -1 || select.indexOf( "scroll" ) != -1 )
        {
            returnUrl += "&select=" + encodeUtf8( select );
        }
        // dx,dy,dscale��3������ĂȂ��Ɩ����ƂȂ邽�߁Adscale�̒l��0�ȊO��������Z�b�g����
        if ( dscale != 0 )
        {
            returnUrl += intCheck( "dscale", dscale );
            returnUrl += "&dx=" + encodeUtf8( Integer.toString( dscaleX ) );
            returnUrl += "&dy=" + encodeUtf8( Integer.toString( dscaleY ) );
        }
        returnUrl += intCheck( "width", width );
        returnUrl += intCheck( "height", height );
        /*---------------��PES API���ʍ��ځ�---------------*/

        // json�ƈ�v����ꍇ�̂݃p�����[�^�𑝂₷
        returnUrl += stringCheckEqual( "output", output, "json" );

        return(returnUrl);
    }

    /**
     * SmartRouting API�̃��[�g�������s��URL�̍쐬
     * 
     * @return �쐬����URL
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    public String getRouting()
    {
        String returnUrl;
        returnUrl = URL + SMARTROUTING_API + APP_ID;

        // JGD2000��WGS84�̂Ƃ��̂݃p�����[�^��ǉ�(���ʍ���)
        if ( outdatum.compareTo( "JGD2000" ) == 0 || outdatum.compareTo( "WGS84" ) == 0 )
        {
            returnUrl += "&outdatum=" + encodeUtf8( outdatum );
        }

        returnUrl += stringCheck( "point", point );
        if ( tollPriority == TWO || tollPriority == THREE )
        {
            returnUrl += "&tollPriority=" + encodeUtf8( Integer.toString( tollPriority ) );
        }

        /*---------------��SmartRouting API��---------------*/
        // �l��2��������p�����[�^��ǉ�����
        returnUrl += intCheckEquals( "costPriority", costPriority, TWO );
        returnUrl += intCheckEquals( "transport", transport, TWO );
        // �l��1��������p�����[�^��ǉ�����
        returnUrl += intCheckEquals( "travelingFlag", travelingFlag, ONE );
        returnUrl += intCheckEquals( "useAroundTollRoad", useAroundTollRoad, ONE );

        // ����l�ȊO�̐����������Ă�����p�����[�^��ǉ�����
        returnUrl += intCheck( "speedWalk", speedWalk, WALKINGSPEED );
        returnUrl += intCheck( "speedHighway", speedHighway, HIGHWAYSPEED );
        returnUrl += intCheck( "speedCityHighway", speedCityHighway, HIGHWAYSPEED );
        returnUrl += intCheck( "speedNationRoute", speedNationRoute, NATIONROUTESPEED );
        returnUrl += intCheck( "speedMainRoad", speedMainRoad, NATIONROUTESPEED );
        returnUrl += intCheck( "speedPrefectualRoad", speedPrefRoad, PREFROADSPEED );
        returnUrl += intCheck( "speedOtherRoad", speedOtherRoad, OTHERSROADSPEED );
        returnUrl += intCheck( "speedTollRoad", speedTollRoad, TOLLROADSPEED );

        // �l��1��������p�����[�^��ǉ�����
        returnUrl += intCheckEquals( "detailInfo", detailInfo, 1 );
        // json�ƈ�v����ꍇ�̂݃p�����[�^�𑝂₷
        returnUrl += stringCheckEqual( "output", output, "json" );
        /*---------------��SmartRouting API��---------------*/

        return(returnUrl);
    }

    /**
     * Sokodoko API���g�p���Ĉܓx�o�x����Z������������
     * 
     * @return �쐬����URL
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    public String geoDecode()
    {
        String returnUrl;
        // returnUrl = URL + SOKODOKO_API + GEODECODE + APP_ID;
        // returnUrl = YOLP_REVERSE_URL + YOLP_ROUTE_ID;
        returnUrl = GEOCODER_URL + YOLP_ROUTE_ID;

        // �ܓx�̎w��
        returnUrl += stringCheck( "lat", lat );
        // �ܓx�̎w��
        returnUrl += stringCheck( "lon", lon );
        // �Z�������͂��ꂽ��Z������
        returnUrl += stringCheck( "query", address );

        Logging.info( "CreateUrl geoDecode returnUrl :" + returnUrl );

        return(returnUrl);
    }

    /**
     * Yahoo routeMap API
     * 
     * @return �쐬����URL
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    public String getYolpRouteMap()
    {
        String returnUrl;
        returnUrl = YOLP_ROUTE_URL + YOLP_ROUTE_ID;

        // �o�H�̎w��
        returnUrl += "&route=" + encodeUtf8( point );

        // �摜�T�C�Y�̕�
        returnUrl += intCheck( "width", width );

        // �摜�T�C�Y�̍���
        returnUrl += intCheck( "height", height );

        // �o�͌`��
        returnUrl += stringCheck( "output", getImgType() );

        return returnUrl;
    }

    /**
     * Yahoo API
     * 
     * @return �쐬����URL
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    public String getMapURL()
    {
        String returnUrl;
        returnUrl = YOLP_URL + YOLP_APP_ID;

        if ( center.compareTo( "" ) != 0 )
        {
            int index = 0;
            index = center.indexOf( "," );
            lat = center.substring( 0, index );
            lon = center.substring( index + 1 );
        }

        returnUrl += stringCheck( "lat", lat );
        returnUrl += stringCheck( "lon", lon );

        returnUrl += stringCheck( "pindefault", pindefault );

        if ( pin.compareTo( "" ) != 0 )
        {
            returnUrl += pin;
        }

        returnUrl += intCheck( "scale", scale );

        // �摜�T�C�Y�̕�
        returnUrl += intCheck( "width", width );
        // �摜�T�C�Y�̍���
        returnUrl += intCheck( "height", height );

        Logging.info( "CreateUrl getMapURL returnUrl :" + returnUrl );

        return returnUrl;
    }

    /**
     * ������f�[�^�󔒃`�F�b�N
     * 
     * @param addParam �ǉ�����p�����[�^�̕ϐ�
     * @param checkParam �`�F�b�N���镶����
     * @return �󔒂���Ȃ���΁AURL�ɒǉ�����p�����[�^��Ԃ�
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    private String stringCheck(String addParam, String checkParam)
    {
        String returnParam;
        returnParam = "";

        // �m�F���镶����Ƀf�[�^�����邩�ǂ������m�F���A�f�[�^������ꍇ�̓p�����[�^�𑝂₷
        if ( checkParam.compareTo( "" ) != 0 )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( checkParam );
        }
        return(returnParam);
    }

    /**
     * ������f�[�^��r
     * 
     * @param addParam �ǉ�����p�����[�^�̕ϐ�
     * @param checkParam �`�F�b�N���镶����
     * @param compareParam ��r�Ώۂ̕�����
     * @return ��v����΁AURL�ɒǉ�����p�����[�^��Ԃ�
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    private String stringCheckEqual(String addParam, String checkParam, String compareParam)
    {
        String returnParam;
        returnParam = "";

        // �m�F���镶����Ƀf�[�^�����邩�ǂ������m�F���A�f�[�^������ꍇ�̓p�����[�^�𑝂₷
        if ( checkParam.compareTo( compareParam ) == 0 )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( checkParam );
        }
        return(returnParam);
    }

    /**
     * �u�[���f�[�^�`�F�b�N
     * 
     * @param addParam �ǉ�����p�����[�^�̕ϐ�
     * @param checkParam �`�F�b�N���镶����
     * @return TRUE��������AURL�ɒǉ�����p�����[�^��Ԃ�
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    private String boolCheck(String addParam, boolean checkParam)
    {
        String returnParam;
        returnParam = "";

        // �m�F���镶����Ƀf�[�^�����邩�ǂ������m�F���A�f�[�^������ꍇ�̓p�����[�^�𑝂₷
        if ( checkParam != false )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Boolean.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * �����f�[�^���R���`�F�b�N
     * 
     * @param addParam �ǉ�����p�����[�^�̕ϐ�
     * @param checkParam �`�F�b�N���鐮���f�[�^
     * @return 0�ȊO��������AURL�ɒǉ�����p�����[�^��Ԃ�
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    private String intCheck(String addParam, int checkParam)
    {
        String returnParam;
        returnParam = "";

        // �m�F���鐮���f�[�^��0�ȊO���ǂ������m�F���A0�ȏ�̂���ꍇ�̓p�����[�^�𑝂₷
        if ( checkParam != 0 )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Integer.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * �����f�[�^����l�`�F�b�N
     * 
     * @param addParam �ǉ�����p�����[�^�̕ϐ�
     * @param checkParam �`�F�b�N���鐮���f�[�^
     * @param compareParam ��r�Ώۂ̐����f�[�^
     * @return ����l��������AURL�ɒǉ�����p�����[�^��Ԃ�
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    private String intCheckEquals(String addParam, int checkParam, int compareParam)
    {
        String returnParam;
        returnParam = "";

        // �m�F���鐮���f�[�^�����������ǂ����𔻒f
        if ( checkParam == compareParam )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Integer.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * �����f�[�^����l�O�`�F�b�N
     * 
     * @param addParam �ǉ�����p�����[�^�̕ϐ�
     * @param checkParam �`�F�b�N���鐮���f�[�^
     * @param defaultParam ����l
     * @return 0�ȏォ����l�ȊO��������AURL�ɒǉ�����p�����[�^
     * @see "�K�v�ȃp�����[�^���Z�b�g���邱��"
     */
    private String intCheck(String addParam, int checkParam, int defaultParam)
    {
        String returnParam;
        returnParam = "";

        // �m�F���鐮���f�[�^��0�ȏォ����l�ȊO��������p�����[�^���擾
        if ( checkParam > 0 && checkParam != defaultParam )
        {
            returnParam = "&" + addParam + "=" + encodeUtf8( Integer.toString( checkParam ) );
        }
        return(returnParam);
    }

    /**
     * UTF-8�G���R�[�f�B���O
     */
    private String encodeUtf8(String param)
    {
        try
        {
            param = URLEncoder.encode( param, "UTF-8" );
        }
        catch ( Exception e )
        {
            param = "";
            Logging.info( "[ CreateUrl.URLEncode() ] Exception:" + e.toString() );
        }
        return(param);
    }

}
