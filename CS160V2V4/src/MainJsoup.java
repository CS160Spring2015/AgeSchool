import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainJsoup {
	static ArrayList<DBEntry> data;
	DBEntry current;

	public MainJsoup(){
		data = new ArrayList<DBEntry>();
		current = new DBEntry();
	}

	public static void main(String[] args) {
		MainJsoup mainJsoup = new MainJsoup();
		mainJsoup.parse("http://www.internet4classrooms.com/science.htm");

		/*for (DBEntry db : data) {
			System.out.println(db.lesson_link + " time: " + db.time_scraped);
		}*/

		StoreData storeData = new StoreData(data);
		storeData.action();
	}

	public void parse(String url){
		Document document;
		Elements links;
		LinkedList<String> grades = new LinkedList<String>();
		LinkedList<String> gradesLinks = new LinkedList<String>();

		try{
			document = Jsoup.connect(url).get();

			links = document.select("blockquote > a"); //select second ol and select all link with href

			int i = 0;
			for (Element element : links) {
				if(i==5){
					gradesLinks.add("http://www.internet4classrooms.com/skills_5th_science_new.htm");
					grades.add(element.text());
				}
				else if(i==6){
					gradesLinks.add("http://www.internet4classrooms.com/skills_6th_science_new.htm");
					grades.add(element.text());
				}
				else{
					grades.add(element.text());
					gradesLinks.add(element.attr("abs:href"));
				}
				i++;
			}

			for(int j = 0; j < grades.size(); j++){
				current.student_grades = grades.get(j).substring(0, 1).toLowerCase();
				parsePageTwo(gradesLinks.get(j));
			}
		}
		catch(IOException exception){System.out.println(exception);}
	}

	private void parsePageTwo(String url) throws IOException{
		Document document;
		Elements links;
		document = Jsoup.connect(url).get();

		links = document.select("blockquote").select("a[href]");

		for (int i = 0; i < links.size()-1; i++) {
			current.category = links.get(i).text();
			parsePageThree(links.get(i).attr("abs:href"));
		}
	}

	private void parsePageThree(String url) throws IOException{
		Document document;
		Elements links;
		document = Jsoup.connect(url).get();

		links = document.select("blockquote > table >tbody > tr");
		for (Element element : links) {
			if(!element.select("td").get(0).select("a").attr("abs:href").isEmpty()){
				parsePageFour(element.select("td").get(0).select("a").attr("abs:href"));
			}
		}
	}

	private void parsePageFour(String url) throws IOException{
		Document document;
		Elements links;
		//System.out.println(url);
		document = Jsoup.connect(url).get();

		links = document.select("blockquote > ol > li");

		for (Element element : links) {
			if(element.select("ol").isEmpty() && !element.select("img[src=/images/icon-interactive.gif]").isEmpty()){
				String[] title_description = element.text().split("-");
				if(title_description.length >= 2){
					current.title = title_description[0].trim();
					current.description = title_description[1].trim();
					current.lesson_link = element.select("a[href]").attr("abs:href");
					current.time_scraped = System.currentTimeMillis(); 

					DBEntry newCurrent = new DBEntry(current); //deep copy
					data.add(newCurrent);
				}
			}
			else if(!element.select("ol").isEmpty() && element.select("> a").isEmpty()){
				Elements insideElements;
				insideElements = element.select("ol > li");
				for (Element insideElement : insideElements) {
					if(!insideElement.select("img[src=/images/icon-interactive.gif]").isEmpty()){
						String[] title_description = insideElement.text().split("-");
						if(title_description.length >= 2){
							current.title = title_description[0].trim();
							current.description = title_description[1].trim();
							current.lesson_link = insideElement.select("a[href]").attr("abs:href");
							current.time_scraped = System.currentTimeMillis(); 

							DBEntry newCurrent = new DBEntry(current); //deep copy
							data.add(newCurrent);
						}
					}
				}

			}
		}
	}

}
