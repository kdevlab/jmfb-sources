package com.kdgdev.apkengine;

import brut.androlib.Androlib;
import brut.androlib.AndrolibException;
import brut.androlib.res.util.ExtFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class patcher {

    //private final static Logger LOGGER = Logger.getLogger(Androlib.class.getName());

    public static void rebuildFiles(String Apk, String ApkName, String otaUpdateURL) throws IOException, AndrolibException {
        //http://update.miui.com/updates/mi-updateV4.php

        if (ApkName.equals("miuihome.apk")) {
            File res = new File(Apk + File.separatorChar + "smali" + File.separatorChar + "com" + File.separatorChar + "miui" + File.separatorChar + "home" + File.separatorChar + "launcher" + File.separatorChar + "gadget" + File.separatorChar + "WeatherBase.smali");
            if (res.exists()) {
                    String patchFile = FileUtils.readFileToString(res);
                    patchFile = StringUtils.replace(patchFile, "\"market://details?id=com.miui.weather2\"", "\"https://github.com/KDGDev/jmfb-firmware-precompiled/raw/master/data/preinstall_apps/Weather.apk\"");
                    patchFile = StringUtils.replace(patchFile, "\"com.xiaomi.market\"", "\"com.android.browser\"");
                    FileUtils.writeStringToFile(res, patchFile);
            }
        }

        if (ApkName.equals("updater.apk")) {
            File res = new File(Apk + File.separatorChar + "smali" + File.separatorChar + "com" + File.separatorChar + "android" + File.separatorChar + "updater" + File.separatorChar + "utils" + File.separatorChar + "SysUtils.smali");
            if (res.exists()) {
                    String patchFile = FileUtils.readFileToString(res);
                    patchFile = StringUtils.replace(patchFile, "\"http://update.miui.com/updates/mi-updateV4.php\"", "\"" + otaUpdateURL + "\"");
                    patchFile = StringUtils.replace(patchFile, "\"http://update.miui.com/updates/mi-updateV5.php\"", "\"" + otaUpdateURL + "\"");
                    patchFile = StringUtils.replace(patchFile, "\"http://update.miui.com/updates/mi-updateV6.php\"", "\"" + otaUpdateURL + "\"");
                    patchFile = StringUtils.replace(patchFile, "\"http://update.miui.com/updates/mi-updateV7.php\"", "\"" + otaUpdateURL + "\"");
                    patchFile = StringUtils.replace(patchFile, "\"http://update.miui.com/updates/mi-updateV8.php\"", "\"" + otaUpdateURL + "\"");
                    patchFile = StringUtils.replace(patchFile, "\"http://update.miui.com/updates/mi-updateV9.php\"", "\"" + otaUpdateURL + "\"");
                    patchFile = StringUtils.replace(patchFile, "\"http://www.miui.com/api.php?mod=wm", "\"http://ota.romz.bz/api.php?mod=wm");
                    FileUtils.writeStringToFile(res, patchFile);
            }
        }

        if (ApkName.equals("mms.apk")) {
            File res = new File(Apk + File.separatorChar + "smali" + File.separatorChar + "com" + File.separatorChar + "android" + File.separatorChar + "mms" + File.separatorChar + "data" + File.separatorChar + "FestivalSmsUpdater.smali");
            if (res.exists()) {
                    String patchFile = FileUtils.readFileToString(res);
                    patchFile = StringUtils.replace(patchFile, "\"http://www.miui.com\"", "\"http://localhost\"");
                    FileUtils.writeStringToFile(res, patchFile);
            }
            res = new File(Apk + File.separatorChar + "smali" + File.separatorChar + "com" + File.separatorChar + "android" + File.separatorChar + "mms" + File.separatorChar + "ui" + File.separatorChar + "BirthdayActivity.smali");
            if (res.exists()) {
                    String patchFile = FileUtils.readFileToString(res);
                    patchFile = StringUtils.replace(patchFile, "\"\\u751f\\u65e5\\u5feb\\u4e50\"", "\"\\u0441 \\u0434\\u043d\\u0435\\u043c \\u0440\\u043e\\u0436\\u0434\\u0435\\u043d\\u0438\\u044f\"");
                    FileUtils.writeStringToFile(res, patchFile);
            }
        }

        if (ApkName.equals("contactsprovider.apk")) {
            File res = new File(Apk + File.separatorChar + "smali" + File.separatorChar + "com" + File.separatorChar + "android" + File.separatorChar + "providers" + File.separatorChar + "contacts" + File.separatorChar + "t9" + File.separatorChar + "T9Builder.smali");
            if (res.exists()) {
                    String patchFile = FileUtils.readFileToString(res);
                    patchFile = StringUtils.replace(patchFile, "invoke-static {v4}, Lcom/android/providers/contacts/t9/T9Utils;->d(C)C", "invoke-static {v4}, Lcom/android/providers/contacts/t9/T9Kdg;->formatCharToT9(C)C");
                    patchFile = StringUtils.replace(patchFile, "Lcom/android/providers/contacts/t9/T9Utils;->formatCharToT9(C)C", "Lcom/android/providers/contacts/t9/T9Kdg;->formatCharToT9(C)C");
                    FileUtils.writeStringToFile(res, patchFile);

            }
            res = new File(Apk + File.separatorChar + "smali" + File.separatorChar + "com" + File.separatorChar + "android" + File.separatorChar + "providers" + File.separatorChar + "contacts" + File.separatorChar + "t9" + File.separatorChar + "k.smali");
            if (res.exists()) {
                    String patchFile = FileUtils.readFileToString(res);
                    patchFile = StringUtils.replace(patchFile, "invoke-static {v4}, Lcom/android/providers/contacts/t9/h;->b(C)C", "invoke-static {v4}, Lcom/android/providers/contacts/t9/T9Kdg;->formatCharToT9(C)C");
                    FileUtils.writeStringToFile(res, patchFile);
            }
        }

        if (ApkName.equals("miuicompass.apk")) {
                Map<String, Object> meta = new Androlib().readMetaFile(new ExtFile(Apk));
                Map<String, Object> uses = new LinkedHashMap<String, Object>();
                Integer[] ids = {1, 6};
                uses.put("ids", ids);
                meta.put("usesFramework", uses);
                new Androlib().writeMetaFile(new File(Apk), meta);
        }
        if (ApkName.equals("framework-miui-res.apk")) {
                Map<String, Object> meta = new Androlib().readMetaFile(new ExtFile(Apk));
                Map<String, Object> uses = new LinkedHashMap<String, Object>();
                Integer[] ids = {1, 2, 3, 4, 5};
                uses.put("ids", ids);
                meta.put("usesFramework", uses);
                new Androlib().writeMetaFile(new File(Apk), meta);
        }
    }

}
