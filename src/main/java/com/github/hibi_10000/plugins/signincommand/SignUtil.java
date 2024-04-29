package com.github.hibi_10000.plugins.signincommand;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignUtil {
    public void setCommand(Block target, int line, String command) {
        Sign s = (Sign) target.getState();
        NBTEditor.set(target, "'" + getJsonText(s.getLine(line - 1), command) + "'", "Text" + line);
    }

    private String getJsonText(String text, String command) {
        JsonObject obj = new JsonObject();
        obj.addProperty("text", text);
        JsonObject clickEvent = new JsonObject();
        clickEvent.addProperty("action", "run_command");
        clickEvent.addProperty("value", command);
        obj.add("clickEvent", clickEvent);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }
}
