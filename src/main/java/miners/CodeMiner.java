package miners;

import java.awt.List;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import domain.*;
import domain.cmap.creator.AldagaiTaula;
import domain.cmap.creator.Aldagaia;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import AldagaiAgerpenComparator.AldagaiAgerpenComparator;
import AldagaiAgerpenComparator.AldagaiChainedComparator;
import main.MainClass;
import utils.FeatureSizeUtil;
import utils.GenericUtils;
import utils.Pair;
import utils.PositionalXMLReader;

public class CodeMiner {
	//Redirigir a un archivo .txt 
	
	
	 

	 //create an print writer for writing to a file
  
	 
	 
	private static String javaScriptKlasea="--JAVASCRIPT KLASEAK--"+ "\n"+ "\n"+ "\n";
	private static String kodeaBariableekin="--ALDAGAI--" + "\n"+ "\n"+ "\n";
	private static String kodeaBariableekin2="--ALDAGAI_TAULAN--" + "\n"+ "\n"+ "\n";

	private static String kodeaFuntzioekin="--FUNTZIOAK--"+ "\n"+ "\n"+ "\n";
	private static String kodea="--KODEA--"+ "\n"+ "\n"+ "\n";

	private static CodeFile cf;
	private static BufferedReader bf;
	private static int readingIndex;

	private static boolean reprocess = false;
	private static boolean checkVPString = false;
	private static String vpString = "";
	
	private static AldagaiTaula aldagaiTaula = new AldagaiTaula(new ArrayList<Aldagaia>());
	
	
	//private static ArrayList<Aldagaia> aldagaiT= new ArrayList<Aldagaia>();
	
	/* REGEX */
	private final static String INSIDE_BRACKETS = "\\((\\w+|\\w+\\(.*\\)|[\\s\\'\\.\\,\\:\\-\\>\\=\\<])+\\)";
	private final static String COMMENT = "\\/[\\/\\*]";

	private static String readLine() {
		try {
			if (checkVPString) {
				reprocess = true;
				return vpString;
			} else {
				reprocess = false;
				readingIndex++;
				return bf.readLine();
			}
		} catch (Exception e) {
			readingIndex--;
			// [ERROR]
			e.printStackTrace();
			return "";
		}
	}
	
	public static void idatziFitxategianBariableak (String i) throws IOException {
		FileWriter fw = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/outputWithVariables.txt");
		try {
			fw.write(kodeaBariableekin);
			fw.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void idatziFitxategianFuntzioak (String r) throws IOException {
		FileWriter fw1 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/outputWithFunctions.txt");
		try {
			fw1.write(kodeaFuntzioekin);
			fw1.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	} 


	public static void idatziFitxategiBatean (String i) throws IOException {
		FileWriter fw = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/output.txt");
		try {
			fw.write(kodea);
			fw.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void inprimatuAldagaiTaula() {
		Collections.sort(aldagaiTaula.getAldagaiT(), new AldagaiChainedComparator(new AldagaiAgerpenComparator()));
		//aldagaiTaula.getAldagaiT().sort(Aldagaia, agerpenKop);
		
		for(int i=0; i<aldagaiTaula.getAldagaiT().size(); i++ ) {
			kodeaBariableekin2 += "Aldagai izena zena:   "+ aldagaiTaula.getAldagaiT().get(i).getAldagaIzena() + 
					"   eta kontua:   " + aldagaiTaula.getAldagaiT().get(i).getAgerpenKop() + "\n";
		}
		
	}
	public static void idatziAldagaiTaulak() throws IOException {
		FileWriter fw4 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/AldagaiTaula.txt");
		try {
			
			fw4.write(kodeaBariableekin2);
			fw4.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void extractVPsFromFile(CodeFile code, String type, SPL spl) throws IOException {

		cf = code;

		MainClass.getLogger().info("Starting mining of " + cf.getFilename());

		if (type.contentEquals("ps:pvsclxml")) {
			// XML mode
			extractVPsFromXMLFile(cf,spl);
		} else {
			// Non XML mode
			extractVPsFromNonXMLFile(cf,spl);
			javaScriptKlasea += cf.getFilename() + "\n";
		}

		for (VariationPoint vp : cf.getVariationPoints()) {
			MainClass.getLogger().info("(" + vp.getId() + "): " + vp.getExpresion() + " -> " + vp.getReferencedFeatures());
		}
		
		MainClass.getLogger().info("Mining of " + cf.getFilename() + " ended.");
		idatziJSKlaseak();
	}

	public static void idatziJSKlaseak() throws IOException {
		FileWriter fw3 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/jJSKlaseak.txt");
		try {
			
			fw3.write(javaScriptKlasea);
			fw3.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Main function for extracting VPs on non XML file
	private static void extractVPsFromNonXMLFile(CodeElement code, SPL spl) {

		readingIndex = 0;

		try {

			bf = new BufferedReader(
					new FileReader(MainClass.getCodeFolder() + "/" + cf.getPath() + "/" + cf.getFilename()));

			String line;
			ArrayList<Feature> feats;
			System.out.println("KOOOOODEEEEAAAAA!!!!!!!!!!!!!!!");
			System.out.println("Path-a" + code.getPath().toString() + "Klase izena: "+  cf.getFilename());
			while ((line = readLine()) != null) {
				kodea+=line + "\n";
				cf.setContent(cf.getContent().concat(line+"\n"));
				//Aldagaien izena lortzeko
				if (line.contains("var") || line.contains(" var") || line.contains("var ")) {
					String[] kk = line.split("var");
					if(kk.length>1) {
						String g = kk[1];
						String[] lerroarenBigarrenZatia = g.split(" ");
						if(lerroarenBigarrenZatia.length>1) {
							String emaitza = lerroarenBigarrenZatia[1];
							if(emaitza!=null) {
								if(aldagaiTaula.agertzenDa(emaitza)){
									aldagaiTaula.gehituKopurua(emaitza);
								}else {
									Aldagaia a= new Aldagaia(emaitza,1);
									aldagaiTaula.getAldagaiT().add(a);
								}
							}
							kodeaBariableekin += emaitza+ "\n";	
						}
					}
				}
				
				//Funtzioen izena lortzeko
				if (line.contains("function") || line.contains(" function") || line.contains("function ")) {
					String[] kk = line.split("function");
					if(kk.length>1){
						String lerroarenBigarrenZatia = kk[1];
						if(lerroarenBigarrenZatia.contains("(") && lerroarenBigarrenZatia.contains(")") && lerroarenBigarrenZatia.contains("{")){
							String[] funtzioarenIzenaEtaLehenParametroa = lerroarenBigarrenZatia.split(" ");
							if(funtzioarenIzenaEtaLehenParametroa.length>1) {
								String emaitza = funtzioarenIzenaEtaLehenParametroa[1];
								String[] pp = emaitza.split("\\(");
								if(pp.length>0) {
									String azkenEmaitza = pp[0].trim();
									if( azkenEmaitza.isEmpty() || azkenEmaitza.contains("{") || azkenEmaitza.contains("(") || azkenEmaitza.contains(",")) {
										
									}else {
										kodeaFuntzioekin += azkenEmaitza+ "\n";	
									}
							
								}
							}
						}
					}
					
				}
				
				
				
				//System.out.println("Kode lerroa:" + line);
				if (checkVPString) {
					checkVPString = false;
					vpString = "";
				}

				Pair<String, ArrayList<Feature>> r = hasVariationStatement(line,spl);
				if (r != null) {
					String expr = r.a;
					feats = r.b;
					line = line.substring(line.indexOf(expr));
					findNestedVPs(feats, readingIndex, expr, line.substring(line.indexOf(expr)) + "\n",0, spl);
				}
			}
			idatziFitxategiBatean(line);
			idatziFitxategianBariableak(kodeaBariableekin);
			idatziFitxategianFuntzioak(kodeaFuntzioekin);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Recursive function for extracting VPs and nested VPs
	private static Code_VariationPoint findNestedVPs(ArrayList<Feature> feats, int startLine, String expresion,
			String content, int currentLevel, SPL spl) {

		String line;

		try {
			
			ArrayList<Feature> feats2;

			// While reading the file...
			while ((line = readLine()) != null) {

				if (checkVPString) {
					checkVPString = false;
					vpString = "";
				} else {
					content += line + "\n";
				}
				

				// 1: Check for nested VPs or VP statement ends
				Pair<String, ArrayList<Feature>> r;
				String endStatement;
				
				// Order matters!
				Boolean nestedFirst = isNestedVPFirst(line);
				if(nestedFirst) {
					endStatement = hasVariationStatementEnd(line);
					r = hasVariationStatement(line,spl);
				}else {
					r = hasVariationStatement(line,spl);
					endStatement = hasVariationStatementEnd(line);
				}

				Boolean isEnd = (endStatement != null);
				nestedFirst = (r != null) && nestedFirst;

				// 2: Check for nested variation points
				if (nestedFirst) {
					
					// 2.1: Get the information to create the Code_VariationPoint
					String expr2 = r.a;
					feats2 = r.b;

					
					// 2.2: It's a nested VP. Add previous features to the list.
					feats2.addAll(feats);

					Code_VariationPoint nestedVP = findNestedVPs(feats2, readingIndex, expr2, line.substring(line.indexOf(expr2)) + "\n", currentLevel + 1,spl);
					
					// 2.3: Remove the (partial) content of nested VP
					content = content.substring(0,content.indexOf(expr2));
					
					// 2.4: Add full content of nested VP
					content += nestedVP.getContent();
					
					// 2.5: If reprocessing, add it to content
					if(reprocess) content += vpString;
					
					// 2.6: Remove nestedVP size from FeatureSizeUtil on each feature that is in the original set of features (feats),
					// 	    because it's size will be duplicated instead.
					for (Feature f : feats) {
						FeatureSizeUtil.udapteFeatureSizeMinus(f, nestedVP.getVpSize(),  spl);
					}

				} else if (isEnd) { // 2: Check if variation point has ended

					// 2.1: It's the end. Create new Code_VariationPoint
					content = clean(content,expresion,line,endStatement);
					
					// Size in lines: expresion (1)  + content.split("\n").length
					int vpSize = 1 + content.split("\n").length - 1;

					Code_VariationPoint cs = new Code_VariationPoint(startLine, readingIndex, expresion, cf, vpSize,
							content,currentLevel);

					// 2.2: Add features' references to Code_VariationPoint
					// 2.3: Update features' sizes
					for (Feature f : feats) {
						cs.addReferencedFeature(f);
						FeatureSizeUtil.updateFeatureSizePlus(f, vpSize,spl);
					}

					// 2.4: Store
					cf.addVariationPoint(cs);

					return cs;
				}

			}
			
			MainClass.getLogger().severe("Unspected end of Variant Code. Id: " + cf.getId());
			
			return null;
		} catch (Exception e) {
			// [ERROR]
			e.printStackTrace();
		}

		return null;
	}

	private static String clean(String content, String expresion, String line, String endStatement) {
				
		if(! reprocess)			
			content = content.substring(content.indexOf(expresion), content.length() - (line + "\n").length());
		else
			content = content.substring(content.indexOf(expresion), content.length() - endStatement.length() - vpString.length() );
		
		content += endStatement;
		
		return content;
	}

	// Main function for extracting VPs on XML file
	private static void extractVPsFromXMLFile(CodeElement code, SPL spl) {

		try {

			bf = new BufferedReader(
					new FileReader(MainClass.getCodeFolder() + "/" + cf.getPath() + "/" + cf.getFilename()));

			String xmlString = "";
			String line;
			while ((line = readLine()) != null) {
				xmlString += line + "\n";
				cf.setContent(cf.getContent().concat(line+"\n"));
			}

			InputStream is = new ByteArrayInputStream(xmlString.getBytes());
			Document doc = PositionalXMLReader.readXML(is);
			doc.getDocumentElement().normalize();

			findVPsRecursive(doc, spl);

		} catch (Exception e) {
			// [ERROR]
			e.printStackTrace();
		}

	}

	private static void findVPsRecursive(Node root, SPL spl) {
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;

				// Contains this element any variation point inside?
				if (hasVPInside((String) n.getUserData("lineTextContent"))) {
					if (e.hasAttribute("pv:condition")) {
						ArrayList<Feature> feats = extractVPsFromStatement(e.getAttribute("pv:condition"),spl);

						String content = (String) e.getUserData("lineTextContent");
						int vpSize = content.split("\n").length;

						Code_VariationPoint cs = new Code_VariationPoint(
								Integer.parseInt((String) e.getUserData("startLineNumber")),
								Integer.parseInt((String) e.getUserData("endLineNumber")),
								e.getAttribute("pv:condition"), cf, vpSize, content,0);

						for (Feature f : feats) {
							cs.addReferencedFeature(f);
							FeatureSizeUtil.updateFeatureSizePlus(f, vpSize, spl);
						}

						cf.addVariationPoint(cs);

						// Nested VP recursion
						findNestedVPsXML(n, feats,1, spl);

					} else {
						// Normal recursion, no nested VPs
						findVPsRecursive(n,spl);
					}
				}

			}
		}
	}

	private static void findNestedVPsXML(Node root, ArrayList<Feature> feats, int currentLevel, SPL spl) {
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;

				// Contains this element any variation point inside?
				if (hasVPInside((String) n.getUserData("lineTextContent"))) {
					if (e.hasAttribute("pv:condition")) {
						ArrayList<Feature> feats2 = extractVPsFromStatement(e.getAttribute("pv:condition"),spl);

						String content = (String) e.getUserData("lineTextContent");
						int vpSize = content.split("\n").length;
						
						// Update feature size only of nested features
						for(Feature f : feats2) {
							FeatureSizeUtil.updateFeatureSizePlus(f, vpSize,spl);
						}
						
						feats2.addAll(feats);
						
						Code_VariationPoint cs = new Code_VariationPoint(
								Integer.parseInt((String) e.getUserData("startLineNumber")),
								Integer.parseInt((String) e.getUserData("endLineNumber")),
								e.getAttribute("pv:condition"), cf, vpSize, content,currentLevel);

						for (Feature f : feats2) {
							cs.addReferencedFeature(f);
						}

						cf.addVariationPoint(cs);

						// Nested VPs recursion
						findNestedVPsXML(n, feats2,currentLevel + 1,spl);
					} else {
						// Normal recursion, no nested VPs
						findVPsRecursive(n,spl);
					}
				}

			}
		}
	}

	private static boolean hasVPInside(String expresion) {
		// Return true if it has 1 VP or more
		return (expresion.split("pv:condition").length - 1) >= 1;
	}

	private static boolean isNestedVPFirst(String line) {
		int nestedI = -1;
		int endI = -1;

		for (String s : MainClass.VARIATION_POINT_NO_XML_STATEMENTS) {
			if (line.contains(s)) {
				nestedI = line.indexOf(s);
			}
		}

		for (String s : MainClass.VARIATION_POINT_NO_XML_STATEMENTS_END) {
			if (line.contains(s)) {
				endI = line.indexOf(s);
			}
		}
		
		if(endI == -1) return true;
		
		else if(endI == -1 && nestedI == -1) return true; // CHECK THIS!
		
		else if(endI != -1 && nestedI == -1) return false;

		else return (nestedI < endI);
	}

	private static Pair<String, String> cleanVariationStatement(String line, String statement) {
		String r1; // Expresion
		String r2; // Rest

		String startRegex = "[^(" + statement + ")]*";

		// .*(\/[\/\*]\s*PVSCL:IFCOND\((\w+|\w+\(.*\)|[\s\'\.\,\:\-\>\=\<])+\)(\s*\*\/)?\S*)(.*)
		Pattern p1 = Pattern.compile(
				startRegex + "(" + COMMENT + "\\s*" + statement + INSIDE_BRACKETS + "(\\s*\\*\\/)?\\S*)(.*)");
		Matcher m1 = p1.matcher(line);

		Pattern p2 = Pattern.compile(startRegex + "(\\s*" + statement + INSIDE_BRACKETS + "(\\s*\\*\\/)?\\S*)(.*)");
		Matcher m2 = p2.matcher(line);

		if (m1.find()) {
			// Contains "//" or "/*"
			r1 = m1.group(1);
			r2 = m1.group(4);
		} else if (m2.find()) {
			// Doesn't contain comment marks
			r1 = m2.group(1);
			r2 = m2.group(4);
		} else {
			// Failsafe
			r1 = line;
			r2 = "";
		}

		return new Pair<String, String>(r1, r2);
	}

	private static Pair<String, ArrayList<Feature>> hasVariationStatement(String line, SPL spl) {
		for (String s : MainClass.VARIATION_POINT_NO_XML_STATEMENTS) {
			if (line.contains(s)) {

				// Capture what is between (...)
				// (\((\w+|\w+\(.*\)|[\s\'\.\,\:\-\>\=\<])+\))
				Pattern p = Pattern.compile("(" + INSIDE_BRACKETS+ ")");
				Matcher m = p.matcher(line);
				if (m.find()) {
					
					String r = m.group()
							.replaceAll(" or | OR | and | AND | NOT | \\| | \\&", " ");

					// HAS VARIATION STATEMENT
					Pair<String, String> cleaned = cleanVariationStatement(line, s);
					String expr = cleaned.a;
					String rest = cleaned.b;

					// PROCESS THIS LINE AGAIN
					checkVPString = true;
					vpString = rest;

					return new Pair<String, ArrayList<Feature>>(expr, extractVPsFromStatement(r, spl));
				}
			}
		}

		return null;
	}

	public static ArrayList<Feature> extractVPsFromStatement(String line, SPL spl) {
		// FEATURE mode
		ArrayList<Feature> listfeatures = new ArrayList<>();

		for (FeatureModel fm : spl.getFeatureModels()) {
			for (Feature f : fm.getFeatures()) {
				
				Pattern p = Pattern.compile("\\b("+f.getName()+")\\b");
				Matcher m = p.matcher(line);
				
				if (m.find()) {
					listfeatures.add(f);
				}
					
			}
		}

		return listfeatures;
	}

	private static Pair<String, String> cleanVariationStatementEnd(String line, String statement) {

		int i = 0;
		String lag = line.substring(0,line.indexOf(statement));
		for (String s : MainClass.VARIATION_POINT_NO_XML_STATEMENTS) {
			if (line.contains(s)) {
				while(lag.indexOf(s) != -1) {
					lag = lag.replaceFirst(s, "");
					i++;
				}
			}
		}
		
		
		int j = 0;
		String lag2 = line;
		while(lag2.indexOf(statement) != -1) {
			lag2 = lag2.replaceFirst(statement,"");

			j++;
		}
		
		j = j - (i + 1);
		
		String endStatementRegex = ".*" + statement;
		
		// One line
		String regex1 = "(" + GenericUtils.repeat(endStatementRegex , i + 1) + "(\\s*\\*\\/)?)(" + GenericUtils.repeat(endStatementRegex, j) + ".*)";
		Pattern p1 = Pattern.compile(regex1);
		Matcher m1 = p1.matcher(line);
		
		// Multiline
		String regex2 = "(" + GenericUtils.repeat(endStatementRegex , i + 1) + "(\\s*\\*\\/)?)(" + GenericUtils.repeat(endStatementRegex, j) + ".*)";
		Pattern p2 = Pattern.compile(regex2);
		Matcher m2 = p2.matcher(line);
		
		String end;
		String rest;
		
		if(m1.find()) {
			end = m1.group(1);
			rest = m1.group(3) == null ? "" : m1.group(3);
		}else if(m2.find()){
			end = m2.group(1);
			rest = m2.group(3) == null ? "" : m2.group(3);
		}else {
			// FALLBACK
			end = "";
			rest = "";
		}

		return new Pair<String,String>(end,rest);
	}

	private static String hasVariationStatementEnd(String line) {
		for (String s : MainClass.VARIATION_POINT_NO_XML_STATEMENTS_END) {
			if (line.contains(s)) {
				
				Pair<String, String> r = cleanVariationStatementEnd(line, s);
				String statement = r.a;
				String rest = r.b;

				// PROCESS THIS LINE AGAIN
				checkVPString = true;
				vpString = rest;

				return statement;
			}
		}

		return null;
	}

	
	

}
