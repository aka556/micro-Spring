package org.xiaoyu.micro.aop.metric;

import org.xiaoyu.micro.annotation.Component;

@Component
public class HashWorker extends BaseWorker {

    @Metric("SHA-1")
    public final String sha1(String input) {
        return hash("SHA-1", input);
    }

    @Metric("SHA-256")
    protected String sha256(String input) {
        return hash("SHA-256", input);
    }
}
