package com.urizev.bakingapp.view.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urizev.bakingapp.App;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public abstract class PresenterFragment<VS extends ViewState, P extends Presenter<VS>> extends Fragment {
    private P mPresenter;
    private Disposable mDisposable;

    protected abstract @LayoutRes int getLayoutRes();
    protected abstract P createPresenter();
    protected abstract void renderViewState(VS vs);
    protected abstract void bindView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter == null) {
            mPresenter = createPresenter();
            getIdlingResource().increment();
        }
    }

    @VisibleForTesting
    protected IdlingResourceActivity.SemaphoreIdlingResource getIdlingResource() {
        return ((IdlingResourceActivity) getActivity()).getmIdlingResource();
    }

    protected void setIdlingResourceIdle() {
        IdlingResourceActivity.SemaphoreIdlingResource ir = getIdlingResource();
        if (ir != null) {
            ir.decrement();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        mDisposable = mPresenter.observeViewState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::renderViewState);
    }

    @Override
    public void onDestroyView() {
        mDisposable.dispose();
        super.onDestroyView();
    }

    protected P getmPresenter() {
        return mPresenter;
    }

    public App getApp() {
        return (App) getActivity().getApplication();
    }
}
