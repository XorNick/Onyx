package support.plugin.onyx.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

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
