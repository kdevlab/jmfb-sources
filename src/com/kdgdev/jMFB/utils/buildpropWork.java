package com.kdgdev.jMFB.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public buildpropWork(String file) throws IOException {
        workFile = new File(file);
        List<String> contents = FileUtils.readLines(workFile);
        String sPattern = "(.*)=(.*)";
        Pattern p = Pattern.compile(sPattern);
        for (String line : contents) {
            Matcher m = p.matcher(line);
            if (m.matches() && !line.contains("#")) values.add(line.split("=", 2));
        }
    }

    public void listNames() {
        for (String[] val : values) {
            System.out.println(val[0].trim() + "|" + val[1].trim());
        }
    }

    public String readProp(String section) {
        for (String[] val : values) {
            if (val[0].equals(section)) return val[1];
        }
        return null;
    }

    public void writeProp(String section, String value) {
        Boolean exists = false;
        int val = -1;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i)[0].equals(section)) {
                exists = true;
                val = i;
                break;
            }
        }
        if (exists)
            values.set(val, (section + "=" + value).split("=", 2));
        else
            values.add((section + "=" + value).split("=", 2));
    }

    public void write() throws IOException {
        List<String> contents = new ArrayList<String>();
        for (String[] val : values) {
            contents.add(val[0] + "=" + val[1]);
        }
        FileUtils.writeLines(workFile, contents);
    }
}
