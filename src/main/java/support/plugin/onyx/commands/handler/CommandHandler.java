package support.plugin.onyx.commands.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class CommandHandler implements CommandExecutor {

    private final Map<String, SubCommand> commands;
    private SubCommand defaultCommand;

    public CommandHandler(String command, String description, String usage, List<String> aliases) {
        this.commands = new ConcurrentHashMap<>();
        this.defaultCommand = null;
    }

    public void setHelpPage(SubCommand defaultCommand) {
        this.defaultCommand = defaultCommand;
    }

    public void addSubCommand(String command, SubCommand subCommand) {
        this.commands.put(command, subCommand);
    }

    private SubCommand getSubCommand(String subCommand) {
        for (Map.Entry<String, SubCommand> subCommandEntry : commands.entrySet()) {
            if (subCommandEntry.getValue().getSubCommand().get().equalsIgnoreCase(subCommand)) {
                for (String alias : subCommandEntry.getValue().getAliases()) {
                    if (alias.equalsIgnoreCase(subCommand)) {
                        return subCommandEntry.getValue();
                    }
                }


                return subCommandEntry.getValue();
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage("No console senders allowed! Naughty!");
            return true;

        }

        Player player = (Player) sender;

        if (args.length == 0) {
            defaultCommand.execute(player, args);
            return true;
        }
        final SubCommand subCommand = getSubCommand(args[0]);
        if (subCommand == null) {
            player.sendMessage("&cNo sub-command with the name '" + args[0] + "' was found.");
            return true;
        }
        List<String> subArgs = new LinkedList<>();
        for (String args1 : args) {
            if (args1.equals(args[0]))
                continue;

            subArgs.add(args1);
        }
        String[] newargs = subArgs.toArray(new String[subArgs.size()]);
        subArgs.toArray(newargs);
        subCommand.execute(player, newargs);
        return true;
    }

}
