package support.plugin.onyx.commands.handler;

import lombok.Getter;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/*
Copyright (c) 2017 PluginManager LTD. All rights reserved.
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge and/or publish copies of the Software,
and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
Any copies of the Software shall stay private and cannot be resold.
Credit to PluginManager LTD shall be expressed in all forms of advertisement and/or endorsement.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
public abstract class SubCommand {

    @Getter
    private final AtomicReference<String> subCommand;
    @Getter
    private final List<String> aliases;
    @Getter
    private final String description;
    @Getter
    private final AtomicBoolean playerOnly;
    @Getter
    private final Onyx instance;

    public SubCommand(Onyx instance, String subCommand, List<String> aliases, String description, Boolean playerOnly) {
        this.instance = instance;
        this.subCommand = new AtomicReference<>(subCommand);
        this.aliases = aliases;
        this.description = description;
        this.playerOnly = new AtomicBoolean(playerOnly);
    }

    public abstract void execute(Player player, String[] args);
}