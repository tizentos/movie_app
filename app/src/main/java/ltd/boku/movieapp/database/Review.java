package ltd.boku.movieapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "review", foreignKeys = @ForeignKey(
        entity = Movie.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete=CASCADE,
        onUpdate = CASCADE
))
public class Review {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "movie_id")
    public long movieId;

    public String author;
    public String content;

    @Ignore
    public Review(String id, long movieId, String author,String content) {
        this.id = id;
        this.movieId = movieId;
        this.author=author;
        this.content = content;
    }

    public Review(String id, String author,String content) {
        this.id = id;
       // this.movieId = movieId;
        this.author=author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
