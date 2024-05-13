package com.github.hibi_10000.plugins.signincommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class SignInCommand extends JavaPlugin {
    private final SignUtil signUtil = new SignUtil();
    private final Util util = new Util();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("signincommand")) return false;
        if (args.length == 0) return util.commandInvalid(sender, label);

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("");
            sender.sendMessage("§a[SignInCommand] §6Help");
            sender.sendMessage("§6 Command§r:");

            TextComponent cmd1 = new TextComponent(" - §b/" + label + " set [Line:<1|2|3|4>] [InCommand:/<command>]");
            cmd1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " set \"§aをチャットにセット")));
            cmd1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " set "));
            sender.spigot().sendMessage(cmd1);

            TextComponent cmd2 = new TextComponent(" - §b/" + label + " delete [Line:<1|2|3|4>]");
            cmd2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " delete \"§aをチャットにセット")));
            cmd2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " delete "));
            sender.spigot().sendMessage(cmd2);

            TextComponent cmd3 = new TextComponent(" - §b/" + label + " list");
            cmd3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " list\"§aを実行")));
            cmd3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " list"));
            sender.spigot().sendMessage(cmd3);

            TextComponent cmd4 = new TextComponent(" - §b/" + label + " help");
            cmd4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
            cmd4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
            sender.spigot().sendMessage(cmd4);

            sender.sendMessage("");
            sender.sendMessage("§6 Permissions§r:");
            sender.sendMessage(" -§b signincommand.command");

            sender.sendMessage("");

            TextComponent helpothers = new TextComponent(" §aOtherInfo");
            helpothers.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックでその他の情報を表示")));
            helpothers.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/version SignInCommand"));
            sender.spigot().sendMessage(helpothers);

            sender.sendMessage("");

            return true;
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player)){
                util.sendError(sender, "このコマンドはコンソールでは実行できません。");
                return false;
            }
            if (args.length >= 3) {
                if (args[1].equals("1") || args[1].equals("2") || args[1].equals("3") || args[1].equals("4")) {
                    Player p = (Player) sender;
                    Block target = p.getTargetBlock(null, 4);
                    if (!signUtil.checkSign(target)) {
                        util.send(sender, "§e看板にカーソルを合わせて実行してください。");
                        return false;
                    }
                    if (!args[2].startsWith("/")) {
                        util.sendError(sender, "埋め込むコマンドにはコマンドの接頭辞\"/\"を必ず付けてください。");
                        return false;
                    }

                    int line = Integer.parseInt(args[1]);
                    String inCommand = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replaceFirst("^/", "");
                    signUtil.setCommand(target, line, inCommand);
                    util.send(sender, "%s %s %s の看板の%s行目にコマンドを§a設定§bしました。", target.getX(), target.getY(), target.getZ(), args[1]);
                    return true;
                }
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (!(sender instanceof Player)) {
                util.sendError(sender, "このコマンドはコンソールでは実行できません。");
                return false;
            }
            if (args.length >= 2) {
                if (args[1].equals("1") || args[1].equals("2") || args[1].equals("3") || args[1].equals("4")) {
                    Player p = (Player) sender;
                    Block target = p.getTargetBlock(null, 4);
                    if (!signUtil.checkSign(target)) {
                        util.send(sender, "§e看板にカーソルを合わせて実行してください。");
                        return false;
                    }
                    int line = Integer.parseInt(args[1]);
                    signUtil.removeCommand(target, line);
                    util.send(sender, "%s %s %s の看板の%s行目のコマンドを§c削除§bしました。", target.getX(), target.getY(), target.getZ(), args[1]);
                    return true;
                }
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!(sender instanceof Player)) {
                util.sendError(sender, "このコマンドはコンソールでは実行できません。");
                return false;
            }
            if (args.length == 1) {
                Player p = (Player) sender;
                Block target = p.getTargetBlock(null, 4);
                if (!signUtil.checkSign(target)) {
                    util.send(sender, "§e看板にカーソルを合わせて実行してください。");
                    return false;
                }

                util.send(sender, "%s %s %s の看板", target.getX(), target.getY(), target.getZ());

                TextComponent message = new TextComponent(" §b/Dataを実行する");
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/data\"§aを実行")));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/data get block "
                    + target.getX() + " " + target.getY() + " " + target.getZ()));
                sender.spigot().sendMessage(message);

                if (!signUtil.hasCommand(target)) {
                    sender.sendMessage("§c この看板にコマンドは設定されていません。");
                    return true;
                }

                for (int i = 1; i <= 4; i++) {
                    String text = signUtil.getCommand(target, i);
                    if (text != null) {
                        sender.sendMessage(" §bLine" + i + ": コマンド: \"/" + text);
                    } else {
                        sender.sendMessage(" §cLine" + i + "にコマンドは設定されていません。");
                    }
                }
                return true;
            }
        }
        return util.commandInvalid(sender, label);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("signincommand")) return null;
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")
                && (args[1].equals("1") || args[1].equals("2") || args[1].equals("3") || args[1].equals("4"))
                && !args[2].startsWith("/")) {
                return Collections.singletonList("/");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("delete")) {
                return Arrays.asList("1", "2", "3", "4");
            }
        } else if (args.length == 1) {
            return Arrays.asList("set", "delete", "list", "help");
        }
        return Collections.emptyList();
    }

}
