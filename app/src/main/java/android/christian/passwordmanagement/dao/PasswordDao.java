package android.christian.passwordmanagement.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.christian.passwordmanagement.entity.Password;

import java.util.List;
import java.util.function.ToDoubleBiFunction;


/**
 * Created by Christian on 31/03/2018.
 */

@Dao
public interface PasswordDao {

    @Insert
    void insertPassword(Password password);

    @Update
    void updatePassword(Password... passwords);

    @Delete
    void deletePassword(Password... passwords);

    @Query("SELECT * FROM password ORDER BY nomeSito")
    Password[] loadAllPassword();
/*
    @Query("SELECT * FROM password WHERE active LIKE true")
    public  Password[] loadAllActivePassword();*/

    @Query("SELECT * FROM password WHERE nomeSito LIKE :sito ORDER BY nomeSito")
    List<Password> getPasswordForSite(String sito);

    @Query("SELECT * FROM password WHERE nomeSito LIKE :sito ORDER BY nomeSito")
    Password[] getGeneralResult(String sito);

    @Query("SELECT * FROM password WHERE nomeSito LIKE :sito AND username LIKE :user")
    List<Password> getPasswordForSiteUser(String sito, String user);

    //TODO 2018-06-09. Query complessa per effettuare una UNION di risultati per una search generica
}
