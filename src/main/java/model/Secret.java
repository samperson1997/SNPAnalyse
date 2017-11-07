package main.java.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "secret")
public class Secret implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String character;
    private String chs_name;
    private String sim_name;

    /**
     * default constructor
     */
    public Secret() {

    }

    /**
     * full constructor
     */
    public Secret(String character, String chs_name, String sim_name) {
        this.character = character;
        this.chs_name = chs_name;
        this.sim_name = sim_name;
    }

    @Column(name = "s_character", nullable = false, length = 10)
    public void setS_Characters(String character) {
        this.character = character;
    }

    public String getS_Characters() {
        return character;
    }

    @Column(name = "Chs_name", nullable = false, length = 20)
    public void setChs_name(String chs_name) {
        this.chs_name = chs_name;
    }

    public String getChs_name() {
        return chs_name;
    }

    @Column(name = "sim_name", nullable = false, length = 20)
    public void setSim_name(String sim_name) {
        this.sim_name = sim_name;
    }

    public String getSim_name() {
        return sim_name;
    }
}
