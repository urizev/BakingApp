package com.urizev.bakingapp.view.detail;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.urizev.bakingapp.R;
import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.model.Step;
import com.urizev.bakingapp.view.common.PresenterFragment;
import com.urizev.bakingapp.widget.ErrorView;
import com.urizev.bakingapp.widget.LoadingView;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import timber.log.Timber;

public class RecipeStepDetailFragment extends PresenterFragment<RecipeStepDetailViewState,RecipeStepDetailPresenter> {
    @BindView(R.id.content) View contentView;
    @BindView(R.id.loading) LoadingView loadingView;
    @BindView(R.id.error) ErrorView errorView;
    @BindView(R.id.player) SimpleExoPlayerView playerView;
    @Nullable @BindView(R.id.description) TextView descriptionView;
    @Nullable @BindView(R.id.previous) View prevView;
    @Nullable @BindView(R.id.next) View nextView;
    @Nullable @BindView(R.id.navigation) View navigation;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private SimpleExoPlayer player;
    private Uri mediaUri;
    private String userAgent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.userAgent = Util.getUserAgent(getContext(), "MediaSession");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recipe_step_detail;
    }

    @Override
    protected RecipeStepDetailPresenter createPresenter() {
        RecipeRepository repository = getApp().getRecipeRepository();
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        int recipeId = delegate.getRecipeId();
        int stepId = delegate.getRecipeStepId();
        return new RecipeStepDetailPresenter(recipeId, stepId, repository);
    }

    @Override
    protected void renderViewState(RecipeStepDetailViewState viewState) {
        contentView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        Step step = viewState.step();
        if (viewState.loading()) {
            loadingView.setVisibility(View.VISIBLE);
        }
        else if (error != null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setMessage(error.getMessage());
        }
        else if (step != null) {
            Timber.d("Updating video");
            RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
            delegate.setTitle(step.shortDescription());
            contentView.setVisibility(View.VISIBLE);
            if (descriptionView != null) {
                descriptionView.setText(step.description());
            }
            Uri mediaUri = null;
            if (!TextUtils.isEmpty(step.videoURL())) {
                mediaUri = Uri.parse(step.videoURL());
            }
            else if (!TextUtils.isEmpty(step.videoURL())) {
                mediaUri = Uri.parse(step.thumbnailURL());
            }

            if (mediaUri != null) {
                playerView.setVisibility(View.VISIBLE);
                initializeMediaSession();
                initializePlayer(mediaUri, viewState.playerPosition());
            }
            else {
                playerView.setVisibility(View.GONE);
            }
        }
        else {
            throw new IllegalStateException();
        }
    }

    private void initializePlayer(Uri mediaUri, final long playerPosition) {
        if (player == null) {
            this.mediaUri = null;
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            playerView.setPlayer(player);

            player.addListener(new Player.EventListener(){
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    getPresenter().setPlayerPosition(player.getCurrentPosition());
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if((playbackState == Player.STATE_READY) && playWhenReady){
                        stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, player.getCurrentPosition(), 1f);
                    } else if((playbackState == Player.STATE_READY)){
                        stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, player.getCurrentPosition(), 1f);
                    } else {
                        return;
                    }
                    mediaSession.setPlaybackState(stateBuilder.build());
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {
                    getPresenter().setPlayerPosition(player.getCurrentPosition());
                }
            });
        }

        if (!mediaUri.equals(this.mediaUri)) {
            if (this.mediaUri != null) {
                player.stop();
            }

            this.mediaUri = mediaUri;
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.seekTo(playerPosition);
            player.prepare(mediaSource, true,false);

            player.setPlayWhenReady(true);
        }
    }

    private void initializeMediaSession() {
        if (mediaSession == null) {
            mediaSession = new MediaSessionCompat(getContext(), "MediaSession");
            mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mediaSession.setMediaButtonReceiver(null);
            stateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mediaSession.setPlaybackState(stateBuilder.build());
            mediaSession.setActive(true);
        }
    }

    @Override
    public void onPause() {
        if (player != null) {
            getPresenter().setPlayerPosition(player.getCurrentPosition());
            player.stop();
            player.release();
            player = null;
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        super.onDestroyView();
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Optional
    @OnClick(R.id.previous)
    public void onPreviousClicked() {
        getPresenter().onPreviousClicked();
    }

    @Optional
    @OnClick(R.id.next)
    public void onNextClicked() {
        getPresenter().onNextClicked();
    }

    public void showStepId(int stepId) {
        getPresenter().showStepId(stepId);
    }
}
