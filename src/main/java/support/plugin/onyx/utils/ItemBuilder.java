package support.plugin.onyx.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by eric on 01/09/2017.
 */
public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(Material material) {
        this(material, 1, (short) 0);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, (short) 0);
    }

    public ItemBuilder(Material material, int amount, short data) {
        itemStack = new ItemStack(material, amount, data);
    }

    public ItemBuilder setName(String name) {

        ItemMeta im = itemStack.getItemMeta();

        im.setDisplayName(name);

        itemStack.setItemMeta(im);

        return this;

    }

    public ItemBuilder setLore(String... lore) {

        ItemMeta im = itemStack.getItemMeta();

        im.setLore(Arrays.stream(lore).collect(Collectors.toList()));

        itemStack.setItemMeta(im);

        return this;

    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {

        itemStack.addUnsafeEnchantment(enchantment, level);

        return this;

    }

}
