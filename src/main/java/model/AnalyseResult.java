package main.java.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Analyse entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "analyse")

public class AnalyseResult implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * �쳣������DNAƬ���ϵ���ʵλ��
     */
    private int realPosition;

    /**
     * ������CDSƬ���ϵ���ʵλ��
     */
    private int CDSPosition;

    /**
     * �쳣����DNAƬ������
     */
    private String area;

    /**
     * �쳣�仯��Ϣ
     */
    private String changedInfo;

    /**
     * ������仯��Ϣ
     */
    private String changedSecret;


    public AnalyseResult() {


    }

    public AnalyseResult(int realPosition, int CDSPosition, String area, String changedInfo, String changedSecret) {
        this.realPosition = realPosition;
        this.CDSPosition = CDSPosition;
        this.area = area;
        this.changedInfo = changedInfo;
        this.changedSecret = changedSecret;
    }

    @Override
    public String toString() {
        return "AnalyseResult{" +
                "realPosition: " + realPosition +
                ", CDSPosition: " + CDSPosition +
                ", area: '" + area + '\'' +
                ", changedInfo: '" + changedInfo + '\'' +
                ", changedSecret: '" + changedSecret + '\'' +
                '}';
    }

    public int getRealPosition() {
        return realPosition;
    }

    public void setRealPosition(int realPosition) {
        this.realPosition = realPosition;
    }

    public int getCDSPosition() {
        return CDSPosition;
    }

    public void setCDSPosition(int CDSPosition) {
        this.CDSPosition = CDSPosition;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getChangedInfo() {
        return changedInfo;
    }

    public void setChangedInfo(String changedInfo) {
        this.changedInfo = changedInfo;
    }

    public String getChangedSecret() {
        return changedSecret;
    }

    public void setChangedSecret(String changedSecret) {
        this.changedSecret = changedSecret;
    }
}