package com.colourmoon.gobuddy.view.adapters;

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

public class TutorialsRecyclerViewAdapter extends RecyclerView.Adapter<TutorialsRecyclerViewAdapter.TutorialsRecyclerViewHolder> {
    private static final int RECOVERY_REQUEST = 101;
    private Context context;
    private List<TutorialModel> tutorialModelList;

    public TutorialsRecyclerViewAdapter(Context context, List<TutorialModel> tutorialModelList) {
        this.context = context;
        this.tutorialModelList = tutorialModelList;
    }

    @NonNull
    @Override
    public TutorialsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tutorial_item, viewGroup, false);
        return new TutorialsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialsRecyclerViewHolder holder, int i) {
        TutorialModel tutorialModel = tutorialModelList.get(i);
        holder.youTubePlayerView.getSettings().setJavaScriptEnabled(true);
        String videoUrl = "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + tutorialModel.getTutorialYoutubeId() +"?rel=0&fs=1"+
                "\" frameborder=\"0\" allowfullscreen=\"allowfullscreen\" mozallowfullscreen=\"mozallowfullscreen\" msallowfullscreen=\"msallowfullscreen\" oallowfullscreen=\"oallowfullscreen\" webkitallowfullscreen=\"webkitallowfullscreen\"\"></iframe></body></html>";
        holder.youTubePlayerView.loadData(videoUrl, "text/html", "utf-8");
        holder.tutorialTitle.setText(tutorialModel.getTutorialTitle());
        holder.tutorialDesc.setText(tutorialModel.getTutorialDesc());
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
        return tutorialModelList.size();
    }

    public class TutorialsRecyclerViewHolder extends RecyclerView.ViewHolder {

        private WebView youTubePlayerView;
        private TextView tutorialTitle, tutorialDesc;

        public TutorialsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.tutorialYoutubeView);
            tutorialTitle = itemView.findViewById(R.id.tutorialTitleView);
            tutorialDesc = itemView.findViewById(R.id.tutorialDescriptionView);
        }
    }
}
