package io.Sonam.Game.Handlers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.Sonam.Core;
import io.Sonam.Game.Menu.ItemStacks.MainItems;
import io.Sonam.Game.Menu.KitSelector;
import io.Sonam.Game.SkyWars;
import io.Sonam.Game.Utils.Kits;
import io.Sonam.profiler.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.UUID;

public class ItemListeners implements Listener {

    private SkyWars plugin;
    private MainItems items = new MainItems();
    private HashSet<UUID> uuid = new HashSet<UUID>();
    private static KitSelector kitSelectorC = new KitSelector();

    public ItemListeners(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Action a = e.getAction();
        PlayerProfile profile = Core.getProfileManager().getProfile(e.getPlayer().getUniqueId());
        if(a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
            final Player player = e.getPlayer();
            if(e.getPlayer().getItemInHand().isSimilar(items.getKitSelector())) {
                e.getPlayer().openInventory(kitSelectorC.getSelector(profile));
                return;
            }
            if(e.getPlayer().getItemInHand().isSimilar(items.getLeaveGame())) {
                if(uuid.contains(e.getPlayer().getUniqueId())) {
                    uuid.remove(player.getUniqueId());
                    e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Teleportation Canceled!");
                    return;
                }
                e.getPlayer().sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Teleporting you back in 2 seconds, right click again to cancel");
                uuid.add(e.getPlayer().getUniqueId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        if(uuid.contains(player.getUniqueId())) {
                            uuid.remove(player.getUniqueId());
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF("dev1a");
                            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                        } else {
                            return;
                        }
                    }
                }, 40L);
            }
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        if(e.getWhoClicked().getItemInHand().isSimilar(items.getKitSelector())) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            Player player = (Player) e.getWhoClicked();
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 3, 1);
            player.closeInventory();
            switch (e.getCurrentItem().getType()) {
                case STONE_SWORD:
                    player.sendMessage(ChatColor.GREEN + "You have selected the " + ChatColor.YELLOW + "Champion" + ChatColor.GREEN + " kit!");
                    SkyWars.getKitSelected().put(player.getUniqueId(), Kits.CHAMPION);
                    break;
                case BOW:
                    player.sendMessage(ChatColor.GREEN + "You have selected the " + ChatColor.YELLOW + "Archer" + ChatColor.GREEN + " kit!");
                    SkyWars.getKitSelected().put(player.getUniqueId(), Kits.ARCHER);
                    break;
                case IRON_SWORD:
                    player.sendMessage(ChatColor.GREEN + "You have selected the " + ChatColor.YELLOW + "Knight" + ChatColor.GREEN + " kit!");
                    SkyWars.getKitSelected().put(player.getUniqueId(), Kits.KNIGHT);
                    break;
                case POTION:
                    if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Scout Kit")) {
                        player.sendMessage(ChatColor.GREEN + "You have selected the " + ChatColor.YELLOW + "Scout" + ChatColor.GREEN + " kit!");
                        SkyWars.getKitSelected().put(player.getUniqueId(), Kits.SCOUT);
                        break;
                    }
                    if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Chemist Kit")) {
                        player.sendMessage(ChatColor.GREEN + "You have selected the " + ChatColor.YELLOW + "Chemist" + ChatColor.GREEN + " kit!");
                        SkyWars.getKitSelected().put(player.getUniqueId(), Kits.CHEMIST);
                        break;
                    }
                case FLINT_AND_STEEL:
                    player.sendMessage(ChatColor.GREEN + "You have selected the " + ChatColor.YELLOW + "Pyro" + ChatColor.GREEN + " kit!");
                    SkyWars.getKitSelected().put(player.getUniqueId(), Kits.PYRO);
                    break;
                case GOLD_CHESTPLATE:
                    player.sendMessage(ChatColor.GREEN + "You have selected the " + ChatColor.YELLOW + "Armorer" + ChatColor.GREEN + " kit!");
                    SkyWars.getKitSelected().put(player.getUniqueId(), Kits.ARMORER);
                    break;
                case BARRIER:
                    player.sendMessage(ChatColor.RED + "You do not have that kit unlocked!");
                    break;
                default:
                    break;
            }
        }
    }

}
