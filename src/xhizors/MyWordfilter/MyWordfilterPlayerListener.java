package xhizors.MyWordfilter;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class MyWordfilterPlayerListener extends PlayerListener {
	
	private MyWordfilter myWordfilter;
	private boolean activated;
	private boolean affectOp;
	
	public MyWordfilterPlayerListener(MyWordfilter myWordfilter) {
		this.myWordfilter = myWordfilter;
		activated = true;
		affectOp = false;
	}
	
	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled()) return;
		if (!activated) return;
		Player ply = event.getPlayer();
		if (myWordfilter.permissionsEnabled) {
			if (!affectOp && myWordfilter.permissions.has(ply, "MyWordfilter.op")) return;
		}
		else if (!affectOp && ply.isOp()) return;
		event.setMessage(myWordfilter.filterMessage(event.getMessage(), ply));
	}
	
	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled()) return;
		String msg = event.getMessage();
		Player ply = event.getPlayer();
		
		if (msg.toLowerCase().startsWith("/wordfiltertoggle")) {
			if (myWordfilter.hasPerm(ply, "MyWordfilter.toggle")) {
				activated = !activated;
				ply.sendMessage("Wordfilter is now " + (activated ? "active" : "inactive"));
			}
		}
		else if (msg.toLowerCase().startsWith("/wordfilteroptoggle")) {
			if (myWordfilter.hasPerm(ply, "MyWordfilter.toggleOp")) {
				affectOp = !affectOp;
				ply.sendMessage("Wordfilter for Ops is now " + (affectOp ? "active" : "inactive"));
			}
		}
		else if (msg.toLowerCase().startsWith("/wordfilteradd ")) {
			if (myWordfilter.hasPerm(ply, "MyWordfilter.add")) {
				msg = msg.replace("/wordfilteradd ", "");
				if (msg.contains("=")) {
					String[] words = msg.split("=");
					words[0] = words[0].trim();
					words[1] = words[1].trim();
					if (words[0] == "" || words[1] == "") {
						ply.sendMessage("Need word");
						return;
					}
					ply.sendMessage(words[0] + " now filtering to " + words[1]);
					myWordfilter.filter.put(words[0], words[1]);
					myWordfilter.reader.saveFile();
				}
				else {
					ply.sendMessage("Incorrect syntax");
				}
			}
		}
		else if (msg.toLowerCase().startsWith("/wordfilterremove ")) {
			if (myWordfilter.hasPerm(ply, "MyWordfilter.remove")) {
				msg = msg.replace("/wordfilterremove ", "");
				msg = msg.trim();
				if (msg != "") {
					if (myWordfilter.filter.containsKey(msg)) {
						ply.sendMessage("Filter for " + msg + " removed");
						myWordfilter.filter.remove(msg);
						myWordfilter.reader.saveFile();
					}
					else {
						ply.sendMessage(msg + " not found");
					}
				}
				else {
					ply.sendMessage("Argument needed");
				}
			}
		}
		else if (msg.toLowerCase().startsWith("/wordfilterreload")) {
			if (myWordfilter.hasPerm(ply, "MyWordfilter.reload")) {
				ply.sendMessage("Reloading Wordfilters from file");
				myWordfilter.reader.parseFilter();
			}
		}
		else if (msg.toLowerCase().startsWith("/wordfilterclear")) {
			if (myWordfilter.hasPerm(ply, "MyWordfilter.clear")) {
				ply.sendMessage("Clearing Wordfilters");
				myWordfilter.filter.clear();
				myWordfilter.reader.saveFile();
			}
		}
		else if (msg.toLowerCase().startsWith("/wordfilterlist")) {
			if (myWordfilter.hasPerm(ply, "MyWordfilter.list")) {
				String list = "";
				for (String key : myWordfilter.filter.keySet()) {
					list = list + key + "=" + myWordfilter.filter.get(key) + "; ";
				}
				ply.sendMessage("List of Wordfilters:");
				ply.sendMessage(list);
			}
		}
	}
	
	public boolean getActivated() {
		return activated;
	}
	
	public boolean getAffectOp() {
		return affectOp;
	}
}
