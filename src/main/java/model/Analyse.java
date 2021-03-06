package main.java.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Analyse entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "analyse")

public class Analyse implements java.io.Serializable {

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String fileName;
    private String data9;
    private String data10;
    private String data11;
    private String data12;
    private String location;
    //正常DNA序列
    private String dna;
    //不正常DNA序列
    private String UDna;
    //确认双峰
    private String yc;
    //疑似双峰
    private String ys;

    // Constructors

    /**
     * default constructor
     */
    public Analyse() {
    }

    /**
     * minimal constructor
     */
    public Analyse(String fileName, String data9, String data10, String data11, String data12, String location,
                   String dna, String UDna) {
        this.fileName = fileName;
        this.data9 = data9;
        this.data10 = data10;
        this.data11 = data11;
        this.data12 = data12;
        this.location = location;
        this.dna = dna;
        this.UDna = UDna;
    }

    /**
     * full constructor
     */
    public Analyse(String fileName, String data9, String data10, String data11, String data12, String location,
                   String dna, String UDna, String yc, String ys) {
        this.fileName = fileName;
        this.data9 = data9;
        this.data10 = data10;
        this.data11 = data11;
        this.data12 = data12;
        this.location = location;
        this.dna = dna;
        this.UDna = UDna;
        this.yc = yc;
        this.ys = ys;
    }

    // Property accessors
    @Id
    @Column(name = "file_name", unique = true, nullable = false, length = 20)
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getData9() {
        return this.data9;
    }

    public void setData9(String data9) {
        this.data9 = data9;
    }

    public String getData10() {
        return this.data10;
    }

    public void setData10(String data10) {
        this.data10 = data10;
    }

    public String getData11() {
        return this.data11;
    }

    public void setData11(String data11) {
        this.data11 = data11;
    }

    public String getData12() {
        return this.data12;
    }

    public void setData12(String data12) {
        this.data12 = data12;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDna() {
        return this.dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public String getUDna() {
        return this.UDna;
    }

    public void setUDna(String UDna) {
        this.UDna = UDna;
    }

    @Column(name = "yc", length = 100)

    public String getYc() {
        return this.yc;
    }

    public void setYc(String yc) {
        this.yc = yc;
    }

    @Column(name = "ys", length = 100)

    public String getYs() {
        return this.ys;
    }

    public void setYs(String ys) {
        this.ys = ys;
    }

}