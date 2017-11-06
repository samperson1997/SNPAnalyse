package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "secret")
public class Secret implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String character;
    private String chs_name;
    private String sim_name;

    public Secret(String character, String chs_name, String sim_name) {
        this.character = character;
        this.chs_name = chs_name;
        this.sim_name = sim_name;
    }

    @Id

    @Column(name = "character")
    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCharacter() {

        return character;
    }

    @Column(name = "Chs_name")
    public void setChs_name(String chs_name) {
        this.chs_name = chs_name;
    }

    public String getChs_name() {
        return chs_name;
    }

    @Column(name = "sim_name")
    public void setSim_name(String sim_name) {
        this.sim_name = sim_name;
    }

    public String getSim_name() {
        return sim_name;
    }
}
