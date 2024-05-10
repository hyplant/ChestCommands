/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.logging;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Lists;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.legacy.UpgradeExecutorException;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTaskException;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.fcommons.ExceptionUtils;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.config.exception.ConfigSyntaxException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.ErrorLog;

public class PrintableErrorCollector extends ErrorCollector {

    @Override
    public void logToConsole() {
        final StringBuilder output = new StringBuilder();

        if (this.errors.size() > 0) {
            output.append(ChestCommands.CHAT_PREFIX).append(ChatColor.RED).append("Encountered ").append(this.errors.size())
                    .append(" error(s) on load:\n");
            output.append(" \n");

            int index = 1;
            for (final ErrorLog error : this.errors) {
                final ErrorPrintInfo printFormat = this.getErrorPrintInfo(index, error);
                PrintableErrorCollector.printError(output, printFormat);
                index++;
            }
        }

        Bukkit.getConsoleSender().sendMessage(output.toString());
    }

    private ErrorPrintInfo getErrorPrintInfo(final int index, final ErrorLog error) {
        final List<String> message = Lists.newArrayList(error.getMessage());
        String details = null;
        Throwable cause = error.getCause();

        // Recursively inspect the cause until an unknown or null exception is found
        while (true) {
            if (cause instanceof ConfigSyntaxException) {
                message.add(cause.getMessage());
                details = ((ConfigSyntaxException) cause).getSyntaxErrorDetails();
                cause = null; // Do not print stacktrace for syntax exceptions

            } else if (cause instanceof ConfigException || cause instanceof ParseException || cause instanceof UpgradeTaskException
                    || cause instanceof UpgradeExecutorException) {
                message.add(cause.getMessage());
                cause = cause.getCause(); // Print the cause (or nothing if null), not our "known" exception

            } else {
                return new ErrorPrintInfo(index, message, details, cause);
            }
        }
    }

    private static void printError(final StringBuilder output, final ErrorPrintInfo error) {
        output.append(ChatColor.YELLOW).append(error.getIndex()).append(") ");
        output.append(ChatColor.WHITE).append(MessagePartJoiner.join(error.getMessage()));

        if (error.getDetails() != null) {
            output.append(". Details:\n");
            output.append(ChatColor.YELLOW).append(error.getDetails()).append("\n");
        } else {
            output.append(".\n");
        }
        if (error.getCause() != null) {
            output.append(ChatColor.DARK_GRAY);
            output.append("--------[ Exception details ]--------\n");
            output.append(ExceptionUtils.getStackTraceOutput(error.getCause()));
            output.append("-------------------------------------\n");
        }
        output.append(" \n");
        output.append(ChatColor.RESET);
    }

}
