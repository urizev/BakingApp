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
    private final BehaviorSubject<VS> viewStateSubject;
    private final RecipeRepository repository;
    private final CompositeDisposable disposables;

    public Presenter(RecipeRepository repository) {
        this.repository = repository;
        this.viewStateSubject = BehaviorSubject.create();
        disposables = new CompositeDisposable();
    }

    Observable<VS> observeViewState() {
        return viewStateSubject;
    }

    protected VS currentViewState() {
        return viewStateSubject.getValue();
    }

    protected void publishViewState(VS vs) {
        viewStateSubject.onNext(vs);
    }

    protected RecipeRepository getRepository() {
        return repository;
    }

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    protected void dispose() {
        disposables.dispose();
    }
}
