package org.xiaoyu.micro.io;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sub.AnnoScan;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceResolverTest {

    @Test
    public void scanClass() {
        var pkg = "org.xiaoyu.scan";
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
            }
            return null;
        });
        Collections.sort(classes);
        System.out.println(classes);
        String[] listClasses = new String[] {
                // list of some scan classes:
                "org.xiaoyu.scan.convert.ValueConverterBean", //
                "org.xiaoyu.scan.destroy.AnnotationDestroyBean", //
                "org.xiaoyu.scan.init.SpecifyInitConfiguration", //
                "org.xiaoyu.scan.proxy.OriginBean", //
                "org.xiaoyu.scan.proxy.FirstProxyBeanPostProcessor", //
                "org.xiaoyu.scan.proxy.SecondProxyBeanPostProcessor", //
                "org.xiaoyu.scan.nested.OuterBean", //
                "org.xiaoyu.scan.nested.OuterBean$NestedBean", //
                "org.xiaoyu.scan.sub1.Sub1Bean", //
                "org.xiaoyu.scan.sub1.sub2.Sub2Bean", //
                "org.xiaoyu.scan.sub1.sub2.sub3.Sub3Bean", //
        };
        for (String clazz : listClasses) {
            assertTrue(classes.contains(clazz));
        }
    }

    @Test
    public void scanJar() {
        var pkg = PostConstruct.class.getPackageName();
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");
            }
            return null;
        });
        // classes in jar:
        assertTrue(classes.contains(PostConstruct.class.getName()));
        assertTrue(classes.contains(PreDestroy.class.getName()));
        assertTrue(classes.contains(PermitAll.class.getName()));
        assertTrue(classes.contains(DataSourceDefinition.class.getName()));
        // jakarta.annotation.sub.AnnoScan is defined in classes:
        assertTrue(classes.contains(AnnoScan.class.getName()));
    }

    @Test
    public void scanTxt() {
        var pkg = "org.xiaoyu.scan";
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".txt")) {
                return name.replace("\\", "/");
            }
            return null;
        });
        Collections.sort(classes);
        assertArrayEquals(new String[] {
                // txt files:
                "org/xiaoyu/scan/sub1/sub1.txt", //
                "org/xiaoyu/scan/sub1/sub2/sub2.txt", //
                "org/xiaoyu/scan/sub1/sub2/sub3/sub3.txt", //
        }, classes.toArray(String[]::new));
    }
}
