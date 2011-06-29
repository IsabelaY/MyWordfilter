package xhizors.MyWordfilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MyWordfilterReader {
	
	private MyWordfilter myWordfilter;
	
	public MyWordfilterReader(MyWordfilter instance) {
		myWordfilter = instance;
	}
	
	public void parseFilter() {
		ArrayList<String> lines = createFilters();
		for (String line : lines) {
			String[] content = line.split("#");
			if (content[0].contains("=")) {
				String[] words = content[0].split("=");
				myWordfilter.filter.put(words[0], words[1]);
			}
		}
	}
	
	public ArrayList<String> createFilters() {
		ArrayList<String> lines = new ArrayList<String>();
		File file = new File("plugins" + File.separator + "MyWordfilter" + File.separator + "filter.txt");
		File dir = new File(file.getParent());
		if (!dir.exists()){
            dir.mkdir();
        }
		if(!file.exists()){
            try {
                file.createNewFile();
                MyWordfilter.log.info("[" + MyWordfilter.pluginName + "] creating filter file");
            } catch (IOException e) {
                e.printStackTrace();
                MyWordfilter.log.warning("[" + MyWordfilter.pluginName + "] could not create filter file");
            }
        }
		try {
			BufferedReader input =  new BufferedReader(new FileReader(file));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					lines.add(line);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex){
			ex.printStackTrace();
		}
		return lines;
	}
	
	public void saveFile() {
		File file = new File("plugins" + File.separator + "MyWordfilter" + File.separator + "filter.txt");
		File dir = new File(file.getParent());
		if (!dir.exists()){
            dir.mkdir();
        }
		if(!file.exists()){
            try {
                file.createNewFile();
                MyWordfilter.log.info("[" + MyWordfilter.pluginName + "] creating filter file");
            } catch (IOException e) {
                e.printStackTrace();
                MyWordfilter.log.warning("[" + MyWordfilter.pluginName + "] could not create filter file");
            }
        }
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			try {
				for (String key : myWordfilter.filter.keySet()) {
					writer.write(key + "=" + myWordfilter.filter.get(key));
					writer.newLine();
				}
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}