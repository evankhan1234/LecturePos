package xact.idea.lecturepos.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import xact.idea.lecturepos.Database.Datasources.Converters;
import xact.idea.lecturepos.Database.Local.BookDao;
import xact.idea.lecturepos.Database.Local.CustomerDao;
import xact.idea.lecturepos.Database.Local.SaleDetailsDao;
import xact.idea.lecturepos.Database.Local.SaleMastersDao;
import xact.idea.lecturepos.Database.Model.Book;
import xact.idea.lecturepos.Database.Model.Customer;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Database.Model.SalesMaster;

@Database(entities = {Customer.class, Book.class, SalesDetails.class, SalesMaster.class},version = 1)
@TypeConverters({Converters.class})
public abstract class MainDatabase extends RoomDatabase {

   public abstract CustomerDao customerDao();
   public abstract BookDao bookDao();
    public abstract SaleDetailsDao saleDetailsDao();
     public abstract SaleMastersDao saleMastersDao();
//
    private static MainDatabase instance;

    public static MainDatabase getInstance(Context context){
        instance= Room.databaseBuilder(context,MainDatabase.class,"LecturePos").allowMainThreadQueries().build();

        return instance;

    }


}