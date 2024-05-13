package com.github.hibi_10000.plugins.signincommand;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

public class Util {
    public boolean commandInvalid(CommandSender sender, String label) {
        TextComponent help = new TextComponent("§a[SignInCommand] §cコマンドが間違っています。 /" + label + " help で使用法を確認してください。");
        help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
        help.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
        sender.spigot().sendMessage(help);
        return false;
    }

    public void send(CommandSender sender, String message) {
        sender.sendMessage("§a[SignInCommand] §r" + message);
    }
}
