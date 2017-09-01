package support.plugin.onyx.economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.profiles.GameProfile;

/**
 * Created by eric on 01/09/2017.
 */
public class TransactionManager {

    private Onyx instance;

    public TransactionManager(Onyx instance){
        this.instance = instance;
    }

    public void transaction(Player sender, Player receiver, double amount, boolean serverSender){

        if(!serverSender){
            GameProfile senderProfile = instance.getProfileManager().getUser(sender.getUniqueId());
            GameProfile receiverProfile = instance.getProfileManager().getUser(receiver.getUniqueId());

            if(senderProfile.getBalance() <= 0){

                sender.sendMessage(ChatColor.RED + "You cannot afford to make this transaction.");
                return;

            }

            if(senderProfile.getBalance()-amount < 0){

                sender.sendMessage(ChatColor.RED + "You cannot afford to make this transaction.");
                return;

            }

            if(amount < 1){

                sender.sendMessage(ChatColor.RED + "You cannot send anything less than $1.");
                return;

            }



        }else{

            GameProfile receiverProfile = instance.getProfileManager().getUser(receiver.getUniqueId());

            receiverProfile.setBalance(amount);
            receiver.sendMessage(ChatColor.GREEN + "Your balance is now " + ChatColor.BOLD + "$"+receiverProfile.getBalance());
            return;

        }

    }

}
