package com.evolveum.midpoint.schrodinger.util;

import java.util.ArrayList;
import java.util.List;

public class ImportOptions {

    public enum Option {
        RAW("raw"), OVERWRITE("overwrite");

        private String optionName;

        Option(String optionName) {
            this.optionName = optionName;
        }
    }

    private boolean raw;
    private boolean overwrite;

    public ImportOptions(boolean raw, boolean overwrite) {
        this.raw = raw;
        this.overwrite = overwrite;
    }

    public List<String> createOptionList() {
        List<String> optionList = new ArrayList<>();
        if (raw) {
            optionList.add(Option.RAW.optionName);
        }
        if (overwrite) {
            optionList.add(Option.OVERWRITE.optionName);
        }
        return optionList;
    }

}
