package ltd.boku.movieapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ltd.boku.movieapp.R;
import ltd.boku.movieapp.database.Review;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {

    Context mContext;
    List<Review> reviewList;

    public ReviewRecyclerViewAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public ReviewRecyclerViewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        int layoutIdForListItem = R.layout.review_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        ReviewViewHolder reviewViewHolder=new ReviewViewHolder(view);

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerViewAdapter.ReviewViewHolder holder, int position) {
        Review review=reviewList.get(position);
        holder.contentTextView.setText(review.getContent().toString());
        holder.authorTextView.setText(review.getAuthor().toString());
    }

    @Override
    public int getItemCount() {
        if (reviewList==null) return 0;
        return reviewList.size();
    }

    public void setReviews(List<Review> reviews){
        reviewList=reviews;
        notifyDataSetChanged();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView authorTextView;
        TextView contentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorTextView=itemView.findViewById(R.id.text_author);
            contentTextView=itemView.findViewById(R.id.text_content);
        }
    }
}
