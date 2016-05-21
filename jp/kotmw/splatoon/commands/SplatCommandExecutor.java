package jp.kotmw.splatoon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class SplatCommandExecutor implements CommandExecutor
{
	@Override
	public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}
