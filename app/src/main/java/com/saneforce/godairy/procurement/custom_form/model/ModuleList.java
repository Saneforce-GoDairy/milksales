package com.saneforce.godairy.procurement.custom_form.model;

public class ModuleList {
    private String ModuleName;
    private String ModuleId;

    public void setModuleId(String moduleId) {
        ModuleId = moduleId;
    }

    public String getModuleId() {
        return ModuleId;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }
}