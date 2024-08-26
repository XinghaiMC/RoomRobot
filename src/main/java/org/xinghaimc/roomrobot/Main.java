package org.xinghaimc.roomrobot;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    private FileConfiguration Config = null;
    private File configFile = null;

    @Override
    public void onEnable() {
        getLogger().info("正在加载 扫地机器人插件...");

        // 加载或创建配置文件
        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.Config = YamlConfiguration.loadConfiguration(this.configFile);

        // 初始化配置文件
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
                // 设置默认值
                Config.addDefault("SleepTime", 5);
                Config.options().copyDefaults(true);
                Config.save(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 从配置文件中读取SleepTime
        int SleepTime = Config.getInt("SleepTime")*20;

        new Clean().runTaskTimer(this, SleepTime, SleepTime);
        getLogger().info("扫地机器人插件加载完成！");
    }

    @Override
    public void onDisable() {
        getLogger().info("正在卸载 扫地机器人插件...");
        try {
            Config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("卸载成功！感谢使用！");
    }

    private class Clean extends BukkitRunnable {
        @Override
        public void run() {
            CommandSender console = Bukkit.getConsoleSender();
            Bukkit.getServer().dispatchCommand(console, "kill @e[type=minecraft:item]");
            Bukkit.broadcastMessage("扫地机器人正在全力清扫...");
        }
    }
}