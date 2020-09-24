package com.ericlam.mc.bungee.hnmc.config.yaml;

import java.io.IOException;

/**
 * 用於創建 Config class 時繼承
 */
public abstract class BungeeConfiguration {

    private FileController controller;

    /**
     * 重載源文件
     */
    public void reload() {
        controller.reload(this);
    }

    /**
     * 保存源文件
     *
     * @throws IOException 文件出錯
     */
    public void save() throws IOException {
        controller.save(this);
    }

}
