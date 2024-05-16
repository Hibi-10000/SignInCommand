package com.github.hibi_10000.plugins.signincommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

public class Util {
    public boolean commandInvalid(CommandSender sender, String label) {
        TextComponent help = new TextComponent("§cコマンドが間違っています。 /" + label + " help で使用法を確認してください。");
        help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
        help.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
        sender.spigot().sendMessage(new TextComponent("§a[SignInCommand] "), help);
        return false;
    }

    private void send(CommandSender sender, ChatColor color, String message, Object... args) {
        TranslatableComponent component = new TranslatableComponent(message, args);
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
}
