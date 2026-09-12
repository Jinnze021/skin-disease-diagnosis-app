package com.project.demo.easydl;

public class EasyDlDisabledException extends EasyDlUnavailableException {

    public EasyDlDisabledException() {
        super("EasyDL integration is disabled");
    }
}
