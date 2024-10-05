package nl.openminetopia.modules.police.balaclava.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.network.protocol.Packet;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import nl.openminetopia.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UtilityClass
public class BalaclavaUtils {

    public void obfuscate(Player player, boolean obfuscate) {
        if (obfuscate) {
            player.displayName(ChatUtils.color("<obf>Balaclava</obf><reset>"));
            return;
        }
        player.displayName(ChatUtils.color(player.getName()));
    }

    public boolean isBalaclavaItem(ItemStack head) {
        return ItemUtils.isValidItem(head, OpenMinetopia.getDefaultConfiguration().getBalaclavaItems());
    }
}
