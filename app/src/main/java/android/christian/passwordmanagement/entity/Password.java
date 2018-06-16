package android.christian.passwordmanagement.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.christian.passwordmanagement.utility.RandomString;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by Christian on 31/03/2018.
 */

@Entity ( indices = { @Index("uuid"), @Index("nomeSito") } )
public class Password implements Serializable {

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @PrimaryKey
    @NonNull
    private String uuid;
    private String nomeSito;
    private String urlSito;
    private String username;
    private String password;
    private String email;
    private String note;
    private Boolean active = true;

    public Password(String nomeSito, String urlSito, String username, String password, String email, String note) {
        this.uuid = String.valueOf(UUID.randomUUID());
        this.nomeSito = nomeSito;
        this.urlSito = urlSito;
        this.username = username;
        this.password = password;
        this.email = email;
        this.note = note;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNomeSito() {
        return nomeSito;
    }

    public void setNomeSito(String nomeSito) {
        this.nomeSito = nomeSito;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUrlSito() {
        return urlSito;
    }

    public void setUrlSito(String urlSito) {
        this.urlSito = urlSito;
    }

    public static String generateRandomPassword(int maxLength, String level) {

        RandomString randomString = new RandomString(maxLength, new SecureRandom(), level);
        return randomString.nextString();
    }

    @Override
    public String toString() {
        return "Password{" +
                "uuid='" + uuid + '\'' +
                ", nomeSito='" + nomeSito + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", note='" + note + '\'' +
                ", active=" + active +
                '}';
    }

    public String[] toCSVString() {
        return (nomeSito + "|" + username + "|" + password + "|" + email + "|" + note + "|").split("\\|", -1);
    }
}
