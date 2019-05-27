package com.rcircle.service.stream.utils.core;

public interface CommandCallback<T> {
    public void preProcess(T flag);
    public void processing(T flag, String ret);
    public void afterProcess(T flag);
}
