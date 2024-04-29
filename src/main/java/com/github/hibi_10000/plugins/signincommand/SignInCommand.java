package com.github.hibi_10000.plugins.signincommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class SignInCommand extends JavaPlugin {
    private SignUtil signUtil = new SignUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sic")) {
            if (args.length == 0) {
                TextComponent cmdhelp = new TextComponent("§a[SignInCommand] §cコマンドが間違っています。§b/" + label + " help §eで使用法を表示します。");
                cmdhelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
                cmdhelp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
                sender.spigot().sendMessage(cmdhelp);
                return false;
            }

            if (!sender.hasPermission("signincommand.setup")) {
                sender.sendMessage("Unknown command. Type \"/help\" for help.");
                return false;
            }

            if (!sender.isOp()) {
                sender.sendMessage("§a[SignInCommand] §cこのコマンドはサーバーオペレーターのみ使用できます。");
                return false;
            }

            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("");
                sender.sendMessage("§a[SignInCommand v1.0.0] §6Help");
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

                //sender.sendMessage("");
                //sender.sendMessage("§6 Permissons§r:");
                //sender.sendMessage(" - §bsignincommand.setup");

                sender.sendMessage("");

                TextComponent helpothers = new TextComponent(" §aOtherInfo");
                helpothers.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックでその他の情報を表示")));
                helpothers.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/version SignInCommand"));
                sender.spigot().sendMessage(helpothers);

                sender.sendMessage("");

                //sender.sendMessage(" §aDistribution:§b https://dev.bukkit.org/projects/sign-in-command");
                //sender.sendMessage(" §bCreatedBy: Hibi_10000");
                return true;
            } else if (args[0].equalsIgnoreCase("set")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if ((args.length >= 3)) {
                        Block target = p.getTargetBlock(null, 4);
                        if (target.getType().name().contains("SIGN")/* == Material.ACACIA_SIGN
                                || target.getType() == Material.ACACIA_WALL_SIGN
                                || target.getType() == Material.BIRCH_SIGN
                                || target.getType() == Material.BIRCH_WALL_SIGN
                                || target.getType() == Material.CRIMSON_SIGN
                                || target.getType() == Material.CRIMSON_WALL_SIGN
                                || target.getType() == Material.DARK_OAK_SIGN
                                || target.getType() == Material.DARK_OAK_WALL_SIGN
                                || target.getType() == Material.JUNGLE_SIGN
                                || target.getType() == Material.JUNGLE_WALL_SIGN
                                || target.getType() == Material.LEGACY_SIGN
                                || target.getType() == Material.LEGACY_SIGN_POST
                                || target.getType() == Material.LEGACY_WALL_SIGN
                                || target.getType() == Material.OAK_SIGN
                                || target.getType() == Material.OAK_WALL_SIGN
                                || target.getType() == Material.SPRUCE_SIGN
                                || target.getType() == Material.SPRUCE_WALL_SIGN
                                || target.getType() == Material.WARPED_SIGN
                                || target.getType() == Material.WARPED_WALL_SIGN*/) {


                            //p.performCommand("gm 1");
                            //BlockData bd = target.getBlockData();

                            if (!args[2].startsWith("/")) {
                                sender.sendMessage("§a[SignInCommand] §c埋め込むコマンドにはコマンドの接頭辞\"/\"を必ず付けてください。");
                                return false;
                            }

                            String incmd = args[2].replace("/", "");
                            int roopargs = 3;
                            while (!(roopargs == args.length || args.length == 3)) {
                                //sender.sendMessage(args[roopargs]);
                                incmd = incmd + " " + args[roopargs];
                                //sender.sendMessage(incmd);
                                roopargs++;
                            }
                            while (incmd.endsWith("\\")) {
                                incmd = incmd.replaceAll("\\\\$", "");
                            }
                            incmd = incmd.replace("\"", "");

                            /*
                            //sender.sendMessage(NBTEditor.getNBTCompound(target,"Text1").toJson());
                            sender.sendMessage(NBTEditor.getString(target, "Text1"));
                            //sender.sendMessage(NBTEditor.getNBTCompound("").toJson());

                            //NBTCompound nbtc = //NBTEditor.getEmptyNBTCompound();
                            //NBTEditor.getNBTCompound("'{\"text\":\"" + signline[0]+ "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + incmd + "\"}}'");//.fromJson("");



                            //NBTEditor.set(target, "'{\"text\":\"" + signline[0]+ "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + incmd + "\"}}'", "Text1");
                            NBTEditor.set(target, "{BlockEntityTag:{Text1:'{\"text\":\"" + signline[0]+ "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + incmd + "\"}}'}}", "");
                            sender.sendMessage(NBTEditor.getNBTCompound(target,"Text1").toJson());

                            return true;
                            */


                            if (args[1].equals("1") || args[1].equals("2") || args[1].equals("3") || args[1].equals("4")) {
                                int line = Integer.parseInt(args[1]);
                                signUtil.setCommand(target, line, incmd);
                                sender.sendMessage("§a[SignInCommand] §b" + target.getX() + " " + target.getY() + " " + target.getZ()
                                    + " の看板の" + args[1] + "行目にコマンドを§a設定§bしました。");
                                return true;
                            }
                            TextComponent cmdhelp = new TextComponent("§a[SignInCommand] §cコマンドの引数が間違っています。§b/" + label + " help §eで使用法を表示します。");
                            cmdhelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
                            cmdhelp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
                            sender.spigot().sendMessage(cmdhelp);
                            return false;
                        }
                        sender.sendMessage("§a[SignInCommand] §e看板にカーソルを合わせて実行してください。");
                        return false;
                    }
                    TextComponent cmdhelp = new TextComponent("§a[SignInCommand] §cコマンドが間違っています。§b/" + label + " help §eで使用法を表示します。");
                    cmdhelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
                    cmdhelp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
                    sender.spigot().sendMessage(cmdhelp);
                    return false;
                }
                System.out.println("§a[SignInCommand] §cこのコマンドはコンソールでは実行できません。");
                return false;
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if ((args.length >= 2)) {
                        Block target = p.getTargetBlock(null, 4);
                        if (target.getType().name().contains("SIGN")) {
                            Sign s = (Sign) target.getState();
                            String[] signline = s.getLines();

                            try {
                                if (args[1].equalsIgnoreCase("1")) {
                                    //sender.sendMessage("§a[SignInCommand]");

                                    getServer().dispatchCommand(sender,
                                        "data merge block "
                                            + target.getX() + " " + target.getY() + " " + target.getZ()
                                            + " {Text1:'{\"text\":\"" + signline[0] + "\"}'}"
                                    );

                                    sender.sendMessage("§a[SignInCommand] §b" + target.getX() + " " + target.getY() + " " + target.getZ()
                                        + " の看板の" + args[1] + "行目のコマンドを§c削除§bしました。");
                                    return true;

                                } else if (args[1].equalsIgnoreCase("2")) {
                                    //sender.sendMessage("§a[SignInCommand]");

                                    getServer().dispatchCommand(sender,
                                        "data merge block "
                                            + target.getX() + " " + target.getY() + " " + target.getZ()
                                            + " {Text2:'{\"text\":\"" + signline[1] + "\"}'}"
                                    );

                                    sender.sendMessage("§a[SignInCommand] §b" + target.getX() + " " + target.getY() + " " + target.getZ()
                                        + " の看板の" + args[1] + "行目のコマンドを§c削除§bしました。");
                                    return true;

                                } else if (args[1].equalsIgnoreCase("3")) {
                                    //sender.sendMessage("§a[SignInCommand]");

                                    getServer().dispatchCommand(sender,
                                        "data merge block "
                                            + target.getX() + " " + target.getY() + " " + target.getZ()
                                            + " {Text3:'{\"text\":\"" + signline[2] + "\"}'}"
                                    );

                                    sender.sendMessage("§a[SignInCommand] §b" + target.getX() + " " + target.getY() + " " + target.getZ()
                                        + " の看板の" + args[1] + "行目のコマンドを§c削除§bしました。");
                                    return true;

                                } else if (args[1].equalsIgnoreCase("4")) {
                                    //sender.sendMessage("§a[SignInCommand]");

                                    getServer().dispatchCommand(sender,
                                        "data merge block "
                                            + target.getX() + " " + target.getY() + " " + target.getZ()
                                            + " {Text4:'{\"text\":\"" + signline[3] + "\"}'}"
                                    );

                                    sender.sendMessage("§a[SignInCommand] §b" + target.getX() + " " + target.getY() + " " + target.getZ()
                                        + " の看板の" + args[1] + "行目のコマンドを§c削除§bしました。");
                                    return true;

                                }
                                TextComponent cmdhelp = new TextComponent("§a[SignInCommand] §cコマンドの引数が間違っています。§b/" + label + " help §eで使用法を表示します。");
                                cmdhelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
                                cmdhelp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
                                sender.spigot().sendMessage(cmdhelp);
                                return false;

                            } catch (CommandException e) {
                                sender.sendMessage("§a[SignInCommand] §cCommandExceptionERROR");
                                return false;
                            }
                        }
                        sender.sendMessage("§a[SignInCommand] §e看板にカーソルを合わせて実行してください。");
                        return false;
                    }
                    TextComponent cmdhelp = new TextComponent("§a[SignInCommand] §cコマンドが間違っています。§b/" + label + " help §eで使用法を表示します。");
                    cmdhelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
                    cmdhelp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
                    sender.spigot().sendMessage(cmdhelp);
                    return false;
                }
                System.out.println("§a[SignInCommand] §cこのコマンドはコンソールでは実行できません。");
                return false;
            } else if (args[0].equalsIgnoreCase("list")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if ((args.length == 1)) {
                        Block target = p.getTargetBlock(null, 4);
                        if (target.getType().name().contains("SIGN")) {

                            sender.sendMessage("§a[SignInCommand] §b" + target.getX() + " " + target.getY() + " " + target.getZ()
                                + " の看板");

                            TextComponent message = new TextComponent(/*"§a[SignInCommand] */" §b/Dataを実行する");
                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/data\"§aを実行")));
                            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/data get block "
                                + target.getX() + " " + target.getY() + " " + target.getZ()));
                            sender.spigot().sendMessage(message);

                            if (!(NBTEditor.getString(target, "Text1").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")
                                || NBTEditor.getString(target, "Text2").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")
                                || NBTEditor.getString(target, "Text3").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")
                                || NBTEditor.getString(target, "Text4").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")
                            )) {
                                sender.sendMessage(/*"§a[SignInCommand] */"§c この看板にコマンドは設定されていません。");
                                return true;
                            }

                            if (NBTEditor.getString(target, "Text1").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")) {
                                String line11 = NBTEditor.getString(target, "Text1").replaceAll("^\\Q{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"\\E", "");
                                //String line12 = line11.substring(line11.lastIndexOf("},\"text\":\"")).replaceFirst("},\"text\":", "").replaceFirst("}$", "");
                                line11 = line11.replaceAll(line11.substring(line11.lastIndexOf("},\"text\":\"")) + "$", "");

                                sender.sendMessage(/*"§a[SignInCommand] */" §bLine1: コマンド: \"/" + line11);
                            } else {
                                sender.sendMessage(/*"§a[SignInCommand] */" §cLine1にコマンドは設定されていません。");
                            }

                            if (NBTEditor.getString(target, "Text2").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")) {
                                String line21 = NBTEditor.getString(target, "Text2").replaceAll("^\\Q{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"\\E", "");
                                //String line22 = line21.substring(line21.lastIndexOf("},\"text\":\"")).replaceFirst("},\"text\":", "").replaceFirst("}$", "");
                                line21 = line21.replaceAll(line21.substring(line21.lastIndexOf("},\"text\":\"")) + "$", "");

                                sender.sendMessage(/*"§a[SignInCommand] */" §bLine2: コマンド: \"/" + line21);
                            } else {
                                sender.sendMessage(/*"§a[SignInCommand] */" §cLine2にコマンドは設定されていません。");
                            }

                            if (NBTEditor.getString(target, "Text3").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")) {
                                String line31 = NBTEditor.getString(target, "Text3").replaceAll("^\\Q{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"\\E", "");
                                //String line32 = line31.substring(line31.lastIndexOf("},\"text\":\"")).replaceFirst("},\"text\":", "").replaceFirst("}$", "");
                                line31 = line31.replaceAll(line31.substring(line31.lastIndexOf("},\"text\":\"")) + "$", "");

                                sender.sendMessage(/*"§a[SignInCommand] */" §bLine3: コマンド: \"/" + line31);
                            } else {
                                sender.sendMessage(/*"§a[SignInCommand] */" §cLine3にコマンドは設定されていません。");
                            }

                            if (NBTEditor.getString(target, "Text4").startsWith("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"")) {
                                String line41 = NBTEditor.getString(target, "Text4").replaceAll("^\\Q{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"\\E", "");
                                //String line42 = line41.substring(line41.lastIndexOf("},\"text\":\"")).replaceFirst("},\"text\":", "").replaceFirst("}$", "");
                                line41 = line41.replaceAll(line41.substring(line41.lastIndexOf("},\"text\":\"")) + "$", "");

                                sender.sendMessage(/*"§a[SignInCommand] */" §bLine4: コマンド: \"/" + line41);
                            } else {
                                sender.sendMessage(/*"§a[SignInCommand] */" §cLine4にコマンドは設定されていません。");
                            }
                            return true;
                        }
                        sender.sendMessage("§a[SignInCommand] §e看板にカーソルを合わせて実行してください。");
                        return false;
                    }
                    TextComponent cmdhelp = new TextComponent("§a[SignInCommand] §cコマンドが間違っています。§b/" + label + " help §eで使用法を表示します。");
                    cmdhelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
                    cmdhelp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
                    sender.spigot().sendMessage(cmdhelp);
                    return false;
                }
                System.out.println("§a[SignInCommand] §cこのコマンドはコンソールでは実行できません。");
                return false;
            }
            TextComponent cmdhelp = new TextComponent("§a[SignInCommand] §cコマンドが間違っています。§b/" + label + " help §eで使用法を表示します。");
            cmdhelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aクリックで§b\"/" + label + " help\"§aを実行")));
            cmdhelp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " help"));
            sender.spigot().sendMessage(cmdhelp);
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("sic")) return null;
        if (sender.hasPermission("signincommand.setup")) return Collections.emptyList();
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("list")
            ) {
                if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("list")) {
                    return Collections.emptyList();
                }
                return Arrays.asList("1", "2", "3", "4");
            }
            return Collections.emptyList();
        } else if (args.length == 3) {
            if (args[2].startsWith("/")) {
                return Collections.emptyList();
            } else if ((args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("2") || args[1].equalsIgnoreCase("3") || args[1].equalsIgnoreCase("4"))
                && args[0].equalsIgnoreCase("set")) {
                return Collections.singletonList("/");
            }
            return Collections.emptyList();
        } else if (args.length == 1) {
            return Arrays.asList("set", "delete", "list", "help");
        }
        return Collections.emptyList();
    }

}
