package support.plugin.onyx.factions.commands;

import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.commands.handler.SubCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eric on 07/09/2017.
 */
public class FactionInviteCommand extends SubCommand {

    private Onyx instance;

    public FactionInviteCommand(Onyx instance) {
        super(instance, "invite", Arrays.asList("inv"), "Invite a player to your faction", true);
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] args) {

        

    }
}
