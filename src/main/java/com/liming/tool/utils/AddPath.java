package com.liming.tool.utils;

import com.liming.tool.service.MainService;
import com.liming.tool.service.VersionService;

import java.util.function.BiConsumer;

public enum AddPath {
    EXCEL((name, value) -> MainService.getInstance().saveExcelPath(name, value)
    ), GIT((name, value) -> VersionService.getInstance().saveGITPath(name, value)
    ), SVN((name, value) -> VersionService.getInstance().saveSVNPath(name, value)
    );

    private final BiConsumer<String, String> execute;

    AddPath(BiConsumer<String, String> execute) {
        this.execute = execute;
    }

    public BiConsumer<String, String> getExecute() {
        return execute;
    }
}
