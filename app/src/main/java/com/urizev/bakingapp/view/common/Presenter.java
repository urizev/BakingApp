package com.urizev.bakingapp.view.common;

import com.urizev.bakingapp.model.RecipeRepository;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public class Presenter<VS extends ViewState> {
    private final BehaviorSubject<VS> mViewStateSubject;
    private final RecipeRepository mRepository;
    private final CompositeDisposable mDisposables;

    public Presenter(RecipeRepository repository) {
        this.mRepository = repository;
        this.mViewStateSubject = BehaviorSubject.create();
        mDisposables = new CompositeDisposable();
    }

    Observable<VS> observeViewState() {
        return mViewStateSubject;
    }

    protected VS currentViewState() {
        return mViewStateSubject.getValue();
    }

    protected void publishViewState(VS vs) {
        mViewStateSubject.onNext(vs);
    }

    protected RecipeRepository getRepository() {
        return mRepository;
    }

    protected void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    protected void dispose() {
        mDisposables.dispose();
    }
}
