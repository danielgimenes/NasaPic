package br.com.dgimenes.nasapic.service.interactor;

public interface OnFinishListener<T> {
    void onSuccess(T result);

    void onError(Throwable throwable);
}
