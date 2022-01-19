package com.programmerdan.minecraft.addgun.guns;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;

import com.programmerdan.minecraft.addgun.AddGun;
import com.programmerdan.minecraft.addgun.ammo.Bullet;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;

public class Recticle implements Runnable {

	public static double FRAME_DELAY = 500.0d;

	private final Player player; // against pro-forma to store this ... doing it at my own risk.
	
	public Recticle(Player player) {
		this.player = player;
	}
	
	@Override
	public void run() {
		if (player == null || player.isDead() ) {
			throw new RuntimeException("Done this recticle");
		}
		PlayerInventory inventory = player.getInventory();
		
		Guns guns = AddGun.getPlugin().getGuns();

		// todo: evaluate using guns.hasGun()
		
		ItemStack main = inventory.getItemInMainHand();
		ItemStack off = inventory.getItemInOffHand();
		
		StandardGun mainGun = main != null && !Material.AIR.equals(main.getType()) ? guns.findGun(main) : null;
		StandardGun offGun = off != null && !Material.AIR.equals(main.getType()) ? guns.findGun(off) : null;
		
		double mainA = 1.0;
		double offA = 1.0;
		
		if (mainGun != null ) {
			Bullet bullet = mainGun.getAmmo(main);
			if (bullet != null) {
				mainA = guns.computeAccuracyFor(mainGun, bullet, player);
			} else {
				mainGun = null;
			}
		}
		
		if (offGun != null) {
			Bullet bullet = offGun.getAmmo(off);
			if (bullet != null) {
				offA = guns.computeAccuracyFor(offGun, bullet, player);
			} else {
				offGun = null;
			}
		}
		
		if (player != null && (offGun != null || mainGun != null)) {
			TextComponent aim = Component.text(ChatColor.GOLD + "Aiming: ");
			if (mainGun != null) {
				StringBuilder msg = new StringBuilder();
				msg.append(ChatColor.WHITE).append(mainGun.getName());
				msg.append(ChatColor.AQUA).append(" [");
				for (int i = 0; i < 16; i++) {
					if (i < 3) {
						msg.append(ChatColor.DARK_RED);
					} else if (i < 7) {
						msg.append(ChatColor.RED);
					} else if (i < 11) {
						msg.append(ChatColor.LIGHT_PURPLE);
					} else if (i < 13) {
						msg.append(ChatColor.BLUE);
					} else {
						msg.append(ChatColor.GREEN);
					}

					if (i <= (1.0 - mainA) * 16) {
						msg.append("|");
					} else {
						msg.append(".");
					}
				}
				msg.append(ChatColor.AQUA).append("] ").append(ChatColor.RESET);
				
				aim.append(Component.text(msg.toString()));
			}
			
			if (offGun != null) {
				StringBuilder msg = new StringBuilder();
				msg.append(ChatColor.WHITE).append(offGun.getName());
				msg.append(ChatColor.AQUA).append(" [");
				for (int i = 0; i < 16; i++) {
					if (i < 3) {
						msg.append(ChatColor.DARK_RED);
					} else if (i < 7) {
						msg.append(ChatColor.RED);
					} else if (i < 11) {
						msg.append(ChatColor.LIGHT_PURPLE);
					} else if (i < 13) {
						msg.append(ChatColor.BLUE);
					} else {
						msg.append(ChatColor.GREEN);
					}

					if (i <= (1.0 - offA) * 16) {
						msg.append("|");
					} else {
						msg.append(".");
					}
				}
				msg.append(ChatColor.AQUA).append("] ").append(ChatColor.RESET);
				
				aim.append(Component.text(msg.toString()));
			}

			player.sendActionBar(aim);
		}
	}
}
