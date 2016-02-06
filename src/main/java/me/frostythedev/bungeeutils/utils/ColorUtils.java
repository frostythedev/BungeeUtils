package me.frostythedev.bungeeutils.utils;

import net.md_5.bungee.api.ChatColor;

/**
 * Programmed by Tevin on 12/5/2015.
 */
public class ColorUtils {

    public static String toColors(String input) {
        if (input == null) {
            return null;
        }

        return input.replaceAll("(&([a-fk-or0-9]))", "§$2");
    }

    public static ChatColor getLastColor(String input) {
        if (input == null) {
            return ChatColor.RESET;
        }
        int length = input.length();
        for (int index = length - 1; index > -1; index--) {
            char section = input.charAt(index);
            if ((section == '§') && (index < length - 1)) {
                char c = input.charAt(index + 1);
                ChatColor color = ChatColor.getByChar(c);
                if (color != null) {
                    return color;
                }
            }
        }
        return ChatColor.RESET;
    }

    public static String removeColors(String input) {
        if (input == null) {
            return null;
        }
        return ChatColor.stripColor(input.replaceAll("(&([a-fk-or0-9]))", ""));
    }
}