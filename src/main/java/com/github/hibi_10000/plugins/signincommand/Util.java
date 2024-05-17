package com.github.hibi_10000.plugins.signincommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Util {
    public boolean commandInvalid(CommandSender sender, String label) {
        TextComponent help = new TextComponent("§cコマンドが間違っています。 /" + label + " help で使用法を確認してください。");
        help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
        help.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
        sender.spigot().sendMessage(new TextComponent("§a[SignInCommand] "), help);
        return false;
    }

    private void send(CommandSender sender, ChatColor color, String message, Object... args) {
        TranslatableComponent component = formatMessage(message, args);
        component.setColor(color);
        sender.spigot().sendMessage(new TextComponent("§a[SignInCommand] "), component);
    }

    public void send(CommandSender sender, char colorCode, String message, Object... args) {
        send(sender, ChatColor.getByChar(colorCode), message, args);
    }

    public void send(CommandSender sender, String message, Object... args) {
        send(sender, ChatColor.AQUA, message, args);
    }

    public void sendError(CommandSender sender, String message, Object... args) {
        send(sender, ChatColor.RED, message, args);
    }

    public void sendWithLog(CommandSender receiver, String message, Object... args) {
        send(receiver, message, args);
        logAdmin(receiver, message, args);
    }

    private void logAdmin(CommandSender sender, String message, Object... format) {
        TranslatableComponent messageComponent = formatMessage(message, format);
        TranslatableComponent component = formatMessage("chat.type.admin", sender, messageComponent);
        component.setColor(ChatColor.GRAY);
        component.setItalic(true);
        List<World> worlds = Bukkit.getServer().getWorlds();
        if (!(sender instanceof ConsoleCommandSender)
            && Boolean.TRUE.equals(worlds.get(0).getGameRuleValue(GameRule.LOG_ADMIN_COMMANDS))) {
            Bukkit.getServer().getConsoleSender().spigot().sendMessage(component);
        }
        worlds.forEach(world -> {
            if (Boolean.TRUE.equals(world.getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK))) {
                world.getPlayers().forEach(receiver -> {
                    if (sender != receiver && receiver.hasPermission("minecraft.admin.command_feedback")) {
                        receiver.spigot().sendMessage(component);
                    }
                });
            }
        });
    }

    private TranslatableComponent formatMessage(String message, Object... format) {
        return new TranslatableComponent(message,
            Arrays.stream(format).map(obj -> {
                if (obj instanceof CommandSender) return getSenderInfo((CommandSender) obj);
                return obj;
            }).toArray()
        );
    }

    private TextComponent getSenderInfo(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return new TextComponent("Server");
        } else {
            TextComponent component = new TextComponent(sender.getName());
            if (sender instanceof Entity) {
                Entity entity = (Entity) sender;
                component.setHoverEvent(
                    new HoverEvent(
                        HoverEvent.Action.SHOW_ENTITY,
                        new net.md_5.bungee.api.chat.hover.content.Entity(
                            entity.getType().getKey().toString(),
                            entity.getUniqueId().toString(),
                            new TextComponent(sender.getName())
                        )
                    )
                );
                if (sender instanceof Player) {
                    component.setClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + sender.getName() + " ")
                    );
                }
            }
            return component;
        }
    }
}
