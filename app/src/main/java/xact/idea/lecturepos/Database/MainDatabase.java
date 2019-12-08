package xact.idea.lecturepos.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import xact.idea.lecturepos.Database.Datasources.Converters;
import xact.idea.lecturepos.Database.Local.BookDao;
import xact.idea.lecturepos.Database.Local.ChallanDao;
import xact.idea.lecturepos.Database.Local.CustomerDao;
import xact.idea.lecturepos.Database.Local.LoginDao;
import xact.idea.lecturepos.Database.Local.SaleDetailsDao;
import xact.idea.lecturepos.Database.Local.SaleMastersDao;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Challan;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.Login;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;

@Database(entities = {Customer.class, Book.class, SalesDetails.class, SalesMaster.class, Login.class, Challan.class}, version =3,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MainDatabase extends RoomDatabase {

    public abstract CustomerDao customerDao();

    public abstract BookDao bookDao();

    public abstract SaleDetailsDao saleDetailsDao();

    public abstract SaleMastersDao saleMastersDao();
    public abstract LoginDao loginDao();
    public abstract ChallanDao challanDao();

    //
    private static MainDatabase instance;

    public static MainDatabase getInstance(Context context) {
        instance = Room.databaseBuilder(context, MainDatabase.class, "LecturePos").addMigrations(MainDatabase.MIGRATION_1_2).allowMainThreadQueries().build();

        return instance;

    }
    static final Migration MIGRATION_1_2 = new Migration(1, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {


        }
    };
    public static MainDatabase getAppDatabase(Context context) {
        if (instance == null)
        {
            instance =Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class, "LecturePos")
                    .addMigrations(MainDatabase.MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}