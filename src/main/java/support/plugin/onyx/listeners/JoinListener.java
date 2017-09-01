package support.plugin.onyx.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.profiles.GameProfile;
import support.plugin.onyx.profiles.dto.ChatMode;
import support.plugin.onyx.timer.TimerType;
import support.plugin.onyx.timer.timers.Timer;

/**
 * Created by eric on 01/09/2017.
 */
public class JoinListener implements Listener {

    private Onyx instance;

    public JoinListener(Onyx instance){

        this.instance = instance;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();

        GameProfile profile;

        if(instance.getProfileManager().getUser(player.getUniqueId())==null){

            profile = GameProfile.builder().uuid(player.getUniqueId()).chatMode(ChatMode.PUBLIC).deathban(0L).build();
            instance.getProfileManager().createUser(profile);

        }else{

            profile = instance.getProfileManager().getUser(player.getUniqueId());

        }

        Location spawn = new Location(player.getWorld(), 0, player.getWorld().getHighestBlockYAt(0,0), 0);

        if(!player.hasPlayedBefore()){

            if(instance.getSettings().getBoolean("map.kitmap")){

                player.teleport(spawn);
                return;

            }

            if(!instance.getTimerManager().isSotwActive()){

                Timer timer = new Timer(player, TimerType.INVINCIBILITY, (60000*30) + System.currentTimeMillis());
                instance.getTimerManager().giveTimer(player, timer);
                player.sendMessage(ChatColor.GREEN + "Your invincibility timer is now activated.");

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        timer.setFrozen(true);
                    }
                }.runTaskLaterAsynchronously(Onyx.getInstance(), 4L);

            }

            player.teleport(spawn);
            player.getInventory().clear();
            player.updateInventory();
            profile.setBalance(Onyx.getInstance().getSettings().getDouble("map.player_start.balance"));
            player.sendMessage(ChatColor.GREEN + "Your balance has been set to $"+profile.getBalance());


        }

        if(instance.getFactionManager().getFactionByMember(player.getUniqueId())!=null){

            Faction faction = instance.getFactionManager().getFactionByMember(player.getUniqueId());

            faction.sendMessage(ChatColor.GREEN + "Member online: "+ChatColor.BOLD+faction.getRole(player.getUniqueId()).getPrefix()+player.getName()+ChatColor.GREEN + " ("+faction.getOnlinePlayers().size()+"/"+faction.getFactionMembers().size()+")");

        }

    }

}
