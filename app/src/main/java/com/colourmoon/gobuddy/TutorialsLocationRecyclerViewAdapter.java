package com.colourmoon.gobuddy;
//
//public class TutorialsLocationRecyclerViewAdapter {
// }


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.model.TutorialModel;

import java.util.List;

public class  TutorialsLocationRecyclerViewAdapter extends RecyclerView.Adapter<TutorialsLocationRecyclerViewAdapter.TutorialsRecyclerViewHolder> {
    private static final int RECOVERY_REQUEST = 101;
    private Context context;
    private List<TutorialLocationModel> tutorialLocationModellList;

    public  TutorialsLocationRecyclerViewAdapter(Context context, List<TutorialLocationModel> tutorialLocationModelList) {
        this.context = context;
        this.tutorialLocationModellList = tutorialLocationModelList;
    }

    @NonNull
    @Override
    public  TutorialsLocationRecyclerViewAdapter.TutorialsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_location_tutorial_item, viewGroup, false);
        return new  TutorialsLocationRecyclerViewAdapter.TutorialsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialsRecyclerViewHolder holder, int position) {
        TutorialLocationModel tutorialLocationModel = tutorialLocationModellList.get(position);
        holder.youTubePlayerView.getSettings().setJavaScriptEnabled(true);
        String videoUrl = "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + tutorialLocationModel.getVideo_link() +"?rel=0&fs=1"+
                "\" frameborder=\"0\" allowfullscreen=\"allowfullscreen\" mozallowfullscreen=\"mozallowfullscreen\" msallowfullscreen=\"msallowfullscreen\" oallowfullscreen=\"oallowfullscreen\" webkitallowfullscreen=\"webkitallowfullscreen\"\"></iframe></body></html>";
        holder.youTubePlayerView.loadData(videoUrl, "text/html", "utf-8");
        holder.tutorialTitle.setText(tutorialLocationModel.getTitle());
        holder.tutorialDesc.setText(tutorialLocationModel.getDescription());
        /*tutorialsRecyclerViewHolder.youTubePlayerView.initialize(GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    youTubePlayer.cueVideo(tutorialModel.getTutorialYoutubeId());
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    //  youTubeInitializationResult.getErrorDialog(context, RECOVERY_REQUEST).show();
                } else {
                    String error = String.format(context.getString(R.string.player_error), youTubeInitializationResult.toString());
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }




    @Override
    public int getItemCount() {
        return tutorialLocationModellList.size();
    }

    public class TutorialsRecyclerViewHolder extends RecyclerView.ViewHolder {

        private WebView youTubePlayerView;
        private TextView tutorialTitle, tutorialDesc;

        public TutorialsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.tutorial_location_YoutubeView);
            tutorialTitle = itemView.findViewById(R.id.tutorial_location_TitleView);
            tutorialDesc = itemView.findViewById(R.id.tutorial_location_DescriptionView);
        }
    }
}

