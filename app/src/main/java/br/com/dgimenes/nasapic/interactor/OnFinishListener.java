package br.com.dgimenes.nasapic.interactor;

public interface OnFinishListener<T> {
    void onSuccess(T result);

    void onError(Throwable throwable);
}
