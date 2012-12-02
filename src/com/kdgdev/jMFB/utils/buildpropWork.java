package com.kdgdev.jMFB.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kirill
 * Date: 02.12.12
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public class buildpropWork {

    private List<String[]> values = new ArrayList<String[]>();
    File workFile;

    public buildpropWork(File file) throws IOException {
        workFile = file;
        List<String> contents = FileUtils.readLines(workFile);
        for (String line : contents) {
            if (!line.contains("#")) values.add(line.split("=", 1));
        }
    }

    public void listSectionNames() {
        for (String[] val : values) {
            System.out.println(val[0]);
        }
    }

    public String readProp(String section) {
        for (String[] val : values) {
            if (val[0].equals(section)) return val[1];
        }
        return null;
    }

    public void writeProp(String section, String value) {
        for (int i = 0; values.size() < i; i++) {
            if (values.get(i)[0].equals(section)) {
                values.set(i, new String[]{section, value});
            } else {
                values.add(new String[]{section, value});
            }
        }
    }

    public void write() throws IOException {
        List<String> contents = new ArrayList<String>();
        for (String[] val : values) {
            contents.add(val[0] + "=" + val[1]);
        }
        FileUtils.writeLines(workFile, contents);
    }
}
