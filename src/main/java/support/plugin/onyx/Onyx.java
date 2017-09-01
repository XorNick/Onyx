package support.plugin.onyx;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import support.plugin.onyx.config.Configuration;
import support.plugin.onyx.factions.FactionManager;
import support.plugin.onyx.listeners.CombatListener;
import support.plugin.onyx.listeners.EnderpearlThrowListener;
import support.plugin.onyx.listeners.JoinListener;
import support.plugin.onyx.listeners.StuckListener;
import support.plugin.onyx.profiles.GameProfile;
import support.plugin.onyx.profiles.ProfileManager;
import support.plugin.onyx.timer.TimerManager;

/*

Copyright (c) 2017 PluginManager LTD

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */
public class Onyx extends JavaPlugin {

    @Getter
    public static Onyx instance;

    @Getter
    private Configuration settings,locale;

    @Getter
    private FactionManager factionManager;

    @Getter
    private TimerManager timerManager;

    @Getter
    private ProfileManager profileManager;

    public void onEnable(){

        instance = this;

        loadConfiguration();
        loadCommands();
        loadListeners();

        factionManager = new FactionManager(this);
        timerManager = new TimerManager(this);
        profileManager = new ProfileManager(this);

    }

    public void loadConfiguration(){

        this.settings = new Configuration(this,"settings.yml");
        this.locale = new Configuration(this,"locale.yml");

    }

    public void loadCommands(){

    }

    public void loadListeners(){

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new CombatListener(), this);
        pluginManager.registerEvents(new EnderpearlThrowListener(), this);
        pluginManager.registerEvents(new StuckListener(this), this);
        pluginManager.registerEvents(new JoinListener(this), this);

    }

}
