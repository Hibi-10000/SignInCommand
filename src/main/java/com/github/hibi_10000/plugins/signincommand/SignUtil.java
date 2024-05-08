package com.github.hibi_10000.plugins.signincommand;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignUtil {
    public boolean checkSign(Block target) {
        return target.getType().name().contains("SIGN");
        /*target.getType() == Material.ACACIA_SIGN
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
            || target.getType() == Material.WARPED_WALL_SIGN*/
        //return target.getState() instanceof Sign;
    }

    public void setCommand(Block target, int line, String command) {
        Sign s = (Sign) target.getState();
        NBTEditor.set(target, "'" + getJsonText(s.getLine(line - 1), command) + "'", "Text" + line);
    }

    public void removeCommand(Block target, int line) {
        Sign s = (Sign) target.getState();
        NBTEditor.set(target, "'" + getJsonText(s.getLine(line - 1)) + "'", "Text" + line);
    }

    public boolean hasCommand(Block target) {
        for (int i = 1; i <= 4; i++) {
            String jsonText = NBTEditor.getString(target, "Text" + i).replaceAll("^'", "").replaceAll("'$", "");
            JsonObject obj = new Gson().fromJson(jsonText, JsonObject.class);
            if (obj.has("clickEvent")) return true;
        }
        return false;
    }

    public String getCommand(Block target, int line) {
        String jsonText = NBTEditor.getString(target, "Text" + line).replaceAll("^'", "").replaceAll("'$", "");
        JsonObject obj = new Gson().fromJson(jsonText, JsonObject.class);
        JsonElement clickEvent = obj.get("clickEvent");
        if (clickEvent instanceof JsonObject) {
            JsonElement value = ((JsonObject) clickEvent).get("value");
            if (value != null) return value.getAsString();
        }
        return null;
    }

    private String getJsonText(String text) {
        JsonObject obj = new JsonObject();
        obj.addProperty("text", text);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }

    private String getJsonText(String text, String command) {
        JsonObject obj = new JsonObject();
        JsonObject clickEvent = new JsonObject();
        clickEvent.addProperty("action", "run_command");
        clickEvent.addProperty("value", command);
        obj.add("clickEvent", clickEvent);
        obj.addProperty("text", text);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }
}
