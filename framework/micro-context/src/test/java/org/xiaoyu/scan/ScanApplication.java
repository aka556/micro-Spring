package org.xiaoyu.scan;

import org.xiaoyu.imported.LocalDateConfiguration;
import org.xiaoyu.imported.ZonedDateConfiguration;
import org.xiaoyu.micro.annotation.ComponentScan;
import org.xiaoyu.micro.annotation.Import;

@ComponentScan
@Import({ LocalDateConfiguration.class, ZonedDateConfiguration.class })
public class ScanApplication {
}
