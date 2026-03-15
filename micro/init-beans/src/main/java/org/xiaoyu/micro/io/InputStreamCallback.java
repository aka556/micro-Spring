package org.xiaoyu.micro.io;

@FunctionalInterface
public interface InputStreamCallback<T> {
    T doWithInputStream(java.io.InputStream inputStream) throws java.io.IOException;
}
