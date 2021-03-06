package ltd.boku.movieapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {Movie.class,Review.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";
    private static  final Object LOCK=new Object();
    private  static  final String DATABASE_NAME= "favorite_movies";
    private static  AppDatabase sInstance;

    public static  AppDatabase getsInstance(Context context){
        if (sInstance==null){
            synchronized (LOCK){
                Log.d(TAG, "getsInstance: Creating new database ");
                sInstance= Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "getsInstance:  getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
    public abstract ReviewDao reviewDao();
}
