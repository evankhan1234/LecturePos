package xact.idea.lecturepos.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "login")
public class Login {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "USER_ID")
    public String USER_ID;
    @ColumnInfo(name = "PASSWORD")
    public String PASSWORD;
    @ColumnInfo(name = "ACTIVE")
    public String ACTIVE;
    @ColumnInfo(name = "DEVICE")
    public String DEVICE;
    @ColumnInfo(name = "CUSTOMER_NAME")
    public String CUSTOMER_NAME;
    @ColumnInfo(name = "CUSTOMER_ADDRESS")
    public String CUSTOMER_ADDRESS;
}
