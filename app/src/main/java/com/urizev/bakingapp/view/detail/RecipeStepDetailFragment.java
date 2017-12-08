package com.urizev.bakingapp.view.detail;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;
import com.urizev.bakingapp.R;
import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.model.Step;
import com.urizev.bakingapp.ui.ErrorView;
import com.urizev.bakingapp.ui.LoadingView;
import com.urizev.bakingapp.view.common.PresenterFragment;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class RecipeStepDetailFragment extends PresenterFragment<RecipeStepDetailViewState,RecipeStepDetailPresenter> {
    @BindView(R.id.step_detail_include_content) View mContentView;
    @BindView(R.id.step_detail_loading) LoadingView mLoadingView;
    @BindView(R.id.step_detail_error) ErrorView mErrorView;
    @BindView(R.id.step_detail_player) SimpleExoPlayerView mPlayerView;
    @Nullable @BindView(R.id.step_detail_image) ImageView mImageView;
    @Nullable @BindView(R.id.step_detail_description) TextView mDescriptionView;
    @Nullable @BindView(R.id.step_detail_previous) View mPrevView;
    @Nullable @BindView(R.id.step_detail_next) View mNextView;
    @Nullable @BindView(R.id.step_detail_navigation) View mNavigation;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mPlayer;
    private Uri mMediaUri;
    private String mUserAgent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mUserAgent = Util.getUserAgent(getContext(), "MediaSession");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recipe_step_detail;
    }

    @Override
    protected RecipeStepDetailPresenter createPresenter() {
        RecipeRepository repository = getApp().getmRecipeRepository();
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        int recipeId = delegate.getRecipeId();
        int stepId = delegate.getRecipeStepId();
        return new RecipeStepDetailPresenter(recipeId, stepId, repository);
    }

    @Override
    protected void renderViewState(RecipeStepDetailViewState viewState) {
        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        Step step = viewState.step();
        if (viewState.loading()) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else if (error != null) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setMessage(error.getMessage());
        }
        else if (step != null) {
            RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
            delegate.setTitle(step.shortDescription());
            mContentView.setVisibility(View.VISIBLE);
            if (mDescriptionView != null) {
                mDescriptionView.setText(step.description());
            }

            mPlayerView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(step.getVideoUrl())) {
                Uri mediaUri = Uri.parse(step.getVideoUrl());
                if (mediaUri != null) {
                    mPlayerView.setVisibility(View.VISIBLE);
                    initializeMediaSession();
                    initializePlayer(mediaUri, viewState.playerPosition());
                }
            }

            if (mImageView != null) {
                mImageView.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(step.getImageUrl())) {
                    Uri mediaUri = Uri.parse(step.getImageUrl());
                    if (mediaUri != null) {
                        mImageView.setVisibility(View.VISIBLE);
                        Picasso.with(getContext())
                                .load(mediaUri)
                                .into(mImageView);
                    }
                }
            }

            setIdlingResourceIdle();
        }
        else {
            throw new IllegalStateException();
        }
    }

    private void initializePlayer(Uri mediaUri, final long playerPosition) {
        if (mPlayer == null) {
            this.mMediaUri = null;
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            mPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mPlayer);

            mPlayer.addListener(new Player.EventListener(){
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    getmPresenter().setPlayerPosition(mPlayer.getCurrentPosition());
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
                        mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mPlayer.getCurrentPosition(), 1f);
                    } else if((playbackState == Player.STATE_READY)){
                        mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mPlayer.getCurrentPosition(), 1f);
                    } else {
                        return;
                    }
                    mMediaSession.setPlaybackState(mStateBuilder.build());
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
                    getmPresenter().setPlayerPosition(mPlayer.getCurrentPosition());
                }
            });
        }

        if (!mediaUri.equals(this.mMediaUri)) {
            if (this.mMediaUri != null) {
                mPlayer.stop();
            }

            this.mMediaUri = mediaUri;
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), mUserAgent), new DefaultExtractorsFactory(), null, null);
            mPlayer.seekTo(playerPosition);
            mPlayer.prepare(mediaSource, true,false);

            mPlayer.setPlayWhenReady(true);
        }
    }

    private void initializeMediaSession() {
        if (mMediaSession == null) {
            mMediaSession = new MediaSessionCompat(getContext(), "MediaSession");
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setMediaButtonReceiver(null);
            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mMediaSession.setPlaybackState(mStateBuilder.build());
            mMediaSession.setActive(true);
        }
    }

    @Override
    public void onPause() {
        if (mPlayer != null) {
            getmPresenter().setPlayerPosition(mPlayer.getCurrentPosition());
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroyView();
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Optional
    @OnClick(R.id.step_detail_previous)
    public void onPreviousClicked() {
        getmPresenter().onPreviousClicked();
    }

    @Optional
    @OnClick(R.id.step_detail_next)
    public void onNextClicked() {
        getmPresenter().onNextClicked();
    }

    public void showStepId(int stepId) {
        getmPresenter().showStepId(stepId);
    }
}
