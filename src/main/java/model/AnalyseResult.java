package main.java.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "analyse_result")

public class AnalyseResult implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;

    /**
     * �쳣������DNAƬ���ϵ���ʵλ��
     */
    private int realPosition;

    /**
     * ������CDSƬ���ϵ���ʵλ��
     */
    private int CDSPosition;

    /**
     * ���찱�����λ��
     */
    private int secretPosition;

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

    public AnalyseResult(int realPosition, int CDSPosition,int secretPosition, String area, String changedInfo, String changedSecret) {
        this.realPosition = realPosition;
        this.CDSPosition = CDSPosition;
        this.secretPosition=secretPosition;
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
                ", SecretPosition: '" + secretPosition + '\'' +
                '}';
    }
    @Id
    @Column(name = "file_name", nullable = false)
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "real_position", nullable = false)
    public int getRealPosition() {
        return realPosition;
    }

    public void setRealPosition(int realPosition) {
        this.realPosition = realPosition;
    }

    @Column(name = "secret_position", nullable = false)
    public int getSecretPosition() {
        return secretPosition;
    }

    public void setSecretPosition(int secretPosition) {
        this.secretPosition = secretPosition;
    }

    @Column(name = "CDS_position", nullable = false)
    public int getCDSPosition() {
        return CDSPosition;
    }

    public void setCDSPosition(int CDSPosition) {
        this.CDSPosition = CDSPosition;
    }

    @Column(name = "area", nullable = false)
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Column(name = "changed_info", nullable = false)
    public String getChangedInfo() {
        return changedInfo;
    }

    public void setChangedInfo(String changedInfo) {
        this.changedInfo = changedInfo;
    }

    @Column(name = "changed_secret", nullable = false)
    public String getChangedSecret() {
        return changedSecret;
    }

    public void setChangedSecret(String changedSecret) {
        this.changedSecret = changedSecret;
    }
}