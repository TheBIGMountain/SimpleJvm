package com.dqpi.simple_jvm.jvm;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TheBIGMountain
 * @date 2020/10/30
 */
@Component
@Getter
public class Cmd {
    private final List<String> argList = new ArrayList<>();
    private boolean help = false;
    private boolean version = false;
    private boolean verboseClassFlag = false;
    private boolean verboseInstFlag = false;
    private String className = null;
    private String jreOption = null;
    private String cp = null;

    public void parse(String... args) {
        if (args.length == 0) {
            return;
        }
        boolean flag = false;
        for (int i = 0; i < args.length; i++) {
            if ("-help".equals(args[i])) {
                help = true;
                return;
            }
            if ("-version".equals(args[i])) {
                version = true;
                return;
            }
            if ("-cp".equals(args[i])) {
                cp = args[++i];
                continue;
            }
            if ("-Xjre".equals(args[i])) {
                jreOption = args[++i];
                continue;
            }
            if ("-verbose:class".equals(args[i])) {
                verboseClassFlag = true;
                continue;
            }
            if ("-verbose:inst".equals(args[i])) {
                verboseInstFlag = true;
                continue;
            }
            if (flag) {
                argList.add(args[i]);
                continue;
            }
            className = args[i];
            flag = true;
        }
    }
}
