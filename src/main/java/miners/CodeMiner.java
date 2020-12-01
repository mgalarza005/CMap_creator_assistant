package miners;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import domain.*;
import domain.cmap.creator.TermTable;
import domain.cmap.creator.Term;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import AldagaiAgerpenComparator.TermApparitionComparator;
import AldagaiAgerpenComparator.TermChainedComparator;
import main.MainClass;
import utils.FeatureSizeUtil;
import utils.GenericUtils;
import utils.Pair;
import utils.PositionalXMLReader;

public class CodeMiner {
	//create an print writer for writing to a file
	private static String javaScriptClass="--JAVASCRIPT CLASSES--"+ "\n"+ "\n"+ "\n";
	private static String codeWithVariables="--ALDAGAI--" + "\n"+ "\n"+ "\n";
	private static String codeWithVariables2="";
	private static String clusterTerms="";
	private static String codeWithFunctions="--FUNTZIOAK--"+ "\n"+ "\n"+ "\n";
	private static String kodea="--KODEA--"+ "\n"+ "\n"+ "\n";

	private static String MinedFiles="";

	private static CodeFile cf;
	private static BufferedReader bf;
	private static int readingIndex;

	private static boolean reprocess = false;
	private static boolean checkVPString = false;
	private static String vpString = "";

	private static TermTable termTable = new TermTable(new ArrayList<Term>());


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

	public static void printVariablesInFile (String i) throws IOException {
		FileWriter fw = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/outputWithVariables.txt");
		try {
			fw.write(codeWithVariables);
			fw.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void printFunctionInFile (String r) throws IOException {
		FileWriter fw1 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/outputWithFunctions.txt");
		try {
			fw1.write(codeWithFunctions);
			fw1.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	} 


	public static void printCodeInFile (String i) throws IOException {
		FileWriter fw = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/output.txt");
		try {
			fw.write(kodea);
			fw.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void printTermTable() throws IOException {
		Collections.sort(termTable.getTermTable(), new TermChainedComparator(new TermApparitionComparator()));

		System.out.println("AldagaiTauleko terminoak eta haien agerpen kopurua 'termTable.txt' fitxategian idatziko dira");

		for(int i=0; i<termTable.getTermTable().size(); i++ ) {
			if(termTable.getTermTable().get(i).getapparitionCont()>10) {
				codeWithVariables2 += "Terminoa: "+ termTable.getTermTable().get(i).getTermName() + 
						"   eta agerpen kopurua:   " + termTable.getTermTable().get(i).getapparitionCont() +
						"    Agertzen da:		[";


				for(int l=0; l<termTable.getTermTable().get(i).getWhereThisTermAppears().size()-1; l++) {
					//codeWithVariables2 += "["+ termTable.getTermTable().get(i).getWhereThisTermAppears().get(l) + "; ";
					codeWithVariables2 += termTable.getTermTable().get(i).getWhereThisTermAppears().get(l).getName() + ": " + termTable.getTermTable().get(i).getWhereThisTermAppears().get(l).getPath() + ";";
				}
				//codeWithVariables2 += termTable.getTermTable().get(i).getWhereThisTermAppears().get(termTable.getTermTable().get(i).getWhereThisTermAppears().size()-1)+ "; ";
				codeWithVariables2 += termTable.getTermTable().get(i).getWhereThisTermAppears().get((termTable.getTermTable().get(i).getWhereThisTermAppears().size())-1).getName()+ ": " + termTable.getTermTable().get(i).getWhereThisTermAppears().get(termTable.getTermTable().get(i).getWhereThisTermAppears().size()-1).getPath() + "]";
				codeWithVariables2 += "\n";

			}
		}
		idatzitermTablekFitxategiBatean();
		printMinedFiles();
		inprimatutermTablerenInfo();

	}
	public static void printMinedFiles() throws IOException {
		FileWriter fw5 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/minedFiles.txt");
		try {

			fw5.write(MinedFiles);
			fw5.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	public static void printTermTableForCluster() throws IOException {
		Collections.sort(termTable.getTermTable(), new TermChainedComparator(new TermApparitionComparator()));

		System.out.println("TermTable's term are going to be written for cluster in 'forCluster.txt' file \n");

		for(int i=0; i<termTable.getTermTable().size(); i++ ) {
			//&& termTable.getTermTable().get(i).firsChartUpper()
			//termTable.getTermTable().get(i).getUpperCaseCont()<2
			if(termTable.getTermTable().get(i).getapparitionCont()>10  && termTable.getTermTable().get(i).firsChartUpper()) {
				clusterTerms += termTable.getTermTable().get(i).getTermName() + " ";
			}				
		}
		clusterTerms.trim();

		FileWriter fw4 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/forCluster.txt");
		try {

			fw4.write(clusterTerms.trim());
			fw4.close();
		}catch (Exception e){
			e.printStackTrace();
		}



	}
	private static void idatzitermTablekFitxategiBatean() throws IOException {
		FileWriter fw4 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/termTable.txt");
		try {

			fw4.write(codeWithVariables2);
			fw4.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public static void inprimatutermTablerenInfo() {
		System.out.println("Termino kopurua (termTable.size()): " +termTable.getTermTable().size() + ")");

	}
	public static void clusterTerms() throws IOException {
		//String pandas="pip install pandas";
		//String sklearn="pip install sklearn";
		//String matplotlib="pip install matplotlib";


		//String pandas="pip install pandas";
		//Process p1 =Runtime.getRuntime().exec("pip intall pandas");

		//String sklearn="pip install sklearn";
		//Process p2 =Runtime.getRuntime().exec("pip intall sklearn");

		//String matplotlib="pip install matplotlib";
		//Process p3 =Runtime.getRuntime().exec("pip intall matplotlib");


		String c = "python C:\\Users\\MIKEL1\\git\\CMap_creator_assistant\\CMap_creator_assistant\\cluster.py";
		Process p=Runtime.getRuntime().exec(c);



		String s=null;
		BufferedReader stdInput = new BufferedReader(new 
				InputStreamReader(p.getInputStream()));
/*
		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(p.getErrorStream()));
*/
		// read the output from the command
		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
		}

		/*

        // read any errors from the attempted command
        //System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
		 */
		System.out.println("Amaitu da, begiratu clusterDone.txt fitxategia");

	}

	public static void countApparitions() {

		readingIndex = 0;

		try {

			BufferedReader bf1 = new BufferedReader(new FileReader("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/output.txt"));

			String line;


			while ( (line = bf1.readLine()) != null ) {
				String[] words = line.split(" ");

				for(int i=0; i<words.length; i++) {
					if ((words[i] != " ") && (!words[i].isEmpty()) && words[i].length()>2){
						if(termTable.appears(words[i])){
							termTable.sumApparition(words[i]);
						}
					}
				}

			}
			bf1.close();
			System.out.println("Gehiketak egin dira!!");
		}catch (Exception e){
			e.printStackTrace();
		}

	}


	public static void extractVPsFromFile(CodeFile code, String type, SPL spl) throws IOException {

		cf = code;

		MainClass.getLogger().info("Starting mining of " + cf.getFilename());

		MinedFiles += "Fitxategia: "+ cf.getFilename() + "		Path-a: "+ cf.getPath() +"\n";

		if (type.contentEquals("ps:pvsclxml")) {
			// XML mode
			extractVPsFromXMLFile(cf,spl);
		} else {
			// Non XML mode
			extractVPsFromNonXMLFile(cf,spl);
			javaScriptClass += cf.getFilename() + "\n";
		}

		for (VariationPoint vp : cf.getVariationPoints()) {
			MainClass.getLogger().info("(" + vp.getId() + "): " + vp.getExpresion() + " -> " + vp.getReferencedFeatures());
		}

		MainClass.getLogger().info("Mining of " + cf.getFilename() + " ended.");
		idatziFitxategiak();
	}

	public static void idatziFitxategiak() throws IOException {
		FileWriter fw3 = new FileWriter("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/Fitxategiak.txt");
		try {

			fw3.write(javaScriptClass);
			fw3.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Main function for extracting VPs on non XML file
	private static void extractVPsFromNonXMLFile(CodeElement code, SPL spl) {

		readingIndex = 0;

		try {

			bf = new BufferedReader(new FileReader(MainClass.getCodeFolder() + "/" + cf.getPath() + "/" + cf.getFilename()));

			//BufferedReader bf = new BufferedReader(new FileReader("/Users/MIKEL1/git/CMap_creator_assistant/CMap_creator_assistant/output.txt"));

			String line;
			ArrayList<Feature> feats;
			System.out.println("Path-a:  " + code.getPath().toString() + "Klase izena:  "+  cf.getFilename());
			while ((line = bf.readLine()) != null) {
				kodea+=line + "\n";
				cf.setContent(cf.getContent().concat(line+"\n"));

				
				//Aldagaien izena lortzeko
				
				if (line.contains("var") || line.contains(" var") || line.contains("var ")) {
					String[] kk = line.split("var");
					if(kk.length>1) {
						String g = kk[1];
						String[] lerroarenBigarrenZatia = g.split(" ");

						if(g.contains("=")) {

							if(lerroarenBigarrenZatia.length>1) {
								String emaitza = lerroarenBigarrenZatia[1];
								if(emaitza!=null && !emaitza.contains(",") && !emaitza.contains("_") && !emaitza.contains("$")  && !termTable.itsStopWord(emaitza) && emaitza.length()>3) {
									if(termTable.appears(emaitza)){
										termTable.sumApparition(emaitza);
										termTable.addPath(emaitza, cf.getFilename(), cf.getPath());
									}else {
										Term a= new Term(emaitza,1);
										a.addNewFile(cf.getFilename(), cf.getPath());
										termTable.getTermTable().add(a);

									}
								}
								//Bi aldagai baino gehiago aldi berean definitzen badira
							}else if (!(termTable.itsStopWord(lerroarenBigarrenZatia[1]))){
								for(int p=1; p<lerroarenBigarrenZatia.length; p++) {
									String[] emaitza = lerroarenBigarrenZatia[p].split(",");
									if(emaitza[0].contains(";") && !emaitza[0].contains("_")) {
										String[] emaitzaAux = lerroarenBigarrenZatia[p].split(";");
										if(emaitzaAux!=null && emaitzaAux[0].length()>3) {
											if(termTable.appears(emaitzaAux[0])){
												termTable.sumApparition(emaitzaAux[0]);
												termTable.addPath(emaitzaAux[0], cf.getFilename(), cf.getPath());
												codeWithVariables += emaitza[0]+ "\n";
												codeWithVariables += emaitzaAux[0]+ "\n";
											}else {
												Term a= new Term(emaitzaAux[0],1);
												a.addNewFile(cf.getFilename(), cf.getPath());
												termTable.getTermTable().add(a);
												codeWithVariables += emaitzaAux[0]+ "\n";
											}
										}


									}else {
										if(emaitza!=null) {
											if(termTable.appears(emaitza[0])){
												termTable.sumApparition(emaitza[0]);
												termTable.addPath(emaitza[0], cf.getFilename(), cf.getPath());
												codeWithVariables += emaitza[0]+ "\n";
											}else {
												Term a= new Term(emaitza[0],1);
												a.addNewFile(cf.getFilename(), cf.getPath());
												termTable.getTermTable().add(a);

												codeWithVariables += emaitza[0]+ "\n";

											}
										}
									}
								}			
							}

						}
					}
				}
				
				// conts bidez definitutako aldagaiak
				if (line.contains("const") || line.contains(" const") || line.contains("const ")) {
					String[] kk = line.split("const");
					if(kk.length>1) {
						String g = kk[1];
						String[] lerroarenBigarrenZatia = g.split(" ");

						if(g.contains("=")) {

							if(lerroarenBigarrenZatia.length>1) {
								String emaitza = lerroarenBigarrenZatia[1];
								if(emaitza!=null && !emaitza.contains(",") && !emaitza.contains("_")  && !emaitza.contains("$") && !termTable.itsStopWord(emaitza) && emaitza.length()>3) {
									if(termTable.appears(emaitza)){
										termTable.sumApparition(emaitza);
										termTable.addPath(emaitza, cf.getFilename(), cf.getPath());
									}else {
										Term a= new Term(emaitza,1);
										a.addNewFile(cf.getFilename(), cf.getPath());
										termTable.getTermTable().add(a);

									}
								}
								//Bi aldagai baino gehiago aldi berean definitzen badira
							}else if (!(termTable.itsStopWord(lerroarenBigarrenZatia[1]))){
								for(int p=1; p<lerroarenBigarrenZatia.length; p++) {
									String[] emaitza = lerroarenBigarrenZatia[p].split(",");
									if(emaitza[0].contains(";") && !emaitza[0].contains("_")) {
										String[] emaitzaAux = lerroarenBigarrenZatia[p].split(";");
										if(emaitzaAux!=null && emaitzaAux[0].length()>3) {
											if(termTable.appears(emaitzaAux[0])){
												termTable.sumApparition(emaitzaAux[0]);
												termTable.addPath(emaitzaAux[0], cf.getFilename(), cf.getPath());
												codeWithVariables += emaitza[0]+ "\n";
												codeWithVariables += emaitzaAux[0]+ "\n";
											}else {
												Term a= new Term(emaitzaAux[0],1);
												a.addNewFile(cf.getFilename(), cf.getPath());
												termTable.getTermTable().add(a);
												codeWithVariables += emaitzaAux[0]+ "\n";
											}
										}


									}else {
										if(emaitza!=null) {
											if(termTable.appears(emaitza[0])){
												termTable.sumApparition(emaitza[0]);
												termTable.addPath(emaitza[0], cf.getFilename(), cf.getPath());
												codeWithVariables += emaitza[0]+ "\n";
											}else {
												Term a= new Term(emaitza[0],1);
												a.addNewFile(cf.getFilename(), cf.getPath());
												termTable.getTermTable().add(a);

												codeWithVariables += emaitza[0]+ "\n";

											}
										}
									}
								}			
							}

						}
					}
				}
				// let bidez definitutako aldagaiak
				if (line.contains("let") || line.contains(" let") || line.contains("let ")) {
					String[] kk = line.split("let");
					if(kk.length>1) {
						String g = kk[1];
						String[] lerroarenBigarrenZatia = g.split(" ");

						if(g.contains("=")) {

							if(lerroarenBigarrenZatia.length>1) {
								String emaitza = lerroarenBigarrenZatia[1];
								if(emaitza!=null && !emaitza.contains(",") && !emaitza.contains("_")  && !emaitza.contains("$") && !termTable.itsStopWord(emaitza) && emaitza.length()>3) {
									if(termTable.appears(emaitza)){
										termTable.sumApparition(emaitza);
										termTable.addPath(emaitza, cf.getFilename(), cf.getPath());
									}else {
										Term a= new Term(emaitza,1);
										a.addNewFile(cf.getFilename(), cf.getPath());
										termTable.getTermTable().add(a);

									}
								}
								//Bi aldagai baino gehiago aldi berean definitzen badira
							}else if (!(termTable.itsStopWord(lerroarenBigarrenZatia[1]))){
								for(int p=1; p<lerroarenBigarrenZatia.length; p++) {
									String[] emaitza = lerroarenBigarrenZatia[p].split(",");
									if(emaitza[0].contains(";") && !emaitza[0].contains("_")) {
										String[] emaitzaAux = lerroarenBigarrenZatia[p].split(";");
										if(emaitzaAux!=null && emaitzaAux[0].length()>3) {
											if(termTable.appears(emaitzaAux[0])){
												termTable.sumApparition(emaitzaAux[0]);
												termTable.addPath(emaitzaAux[0], cf.getFilename(), cf.getPath());
												codeWithVariables += emaitza[0]+ "\n";
												codeWithVariables += emaitzaAux[0]+ "\n";
											}else {
												Term a= new Term(emaitzaAux[0],1);
												a.addNewFile(cf.getFilename(), cf.getPath());
												termTable.getTermTable().add(a);
												codeWithVariables += emaitzaAux[0]+ "\n";
											}
										}


									}else {
										if(emaitza!=null) {
											if(termTable.appears(emaitza[0])){
												termTable.sumApparition(emaitza[0]);
												termTable.addPath(emaitza[0], cf.getFilename(), cf.getPath());
												codeWithVariables += emaitza[0]+ "\n";
											}else {
												Term a= new Term(emaitza[0],1);
												a.addNewFile(cf.getFilename(), cf.getPath());
												termTable.getTermTable().add(a);

												codeWithVariables += emaitza[0]+ "\n";

											}
										}
									}
								}			
							}

						}
					}
				}




				//Funtzioen izena lortzeko
				if ((line.contains("function") || line.contains(" function") || line.contains("function ")) && (!line.contains("'function'"))) {
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
									if( !(azkenEmaitza.isEmpty() || azkenEmaitza.contains("{") || azkenEmaitza.contains("(") || azkenEmaitza.contains(",") || termTable.itsStopWord(azkenEmaitza) && !emaitza.contains("_"))) {
										if(azkenEmaitza.length()>3 && !azkenEmaitza.contains("_")&& !azkenEmaitza.contains("$")) {


											if(termTable.appears(azkenEmaitza)){
												termTable.sumApparition(azkenEmaitza);
												termTable.addPath(azkenEmaitza, cf.getFilename(), cf.getPath());
												codeWithVariables += azkenEmaitza+ "\n";
												codeWithFunctions += azkenEmaitza+ "\n";	
											}else {
												Term a= new Term(azkenEmaitza,1);
												//gehitu non dagoen terminoa
												a.addNewFile(cf.getFilename(), cf.getPath());
												termTable.getTermTable().add(a);
												codeWithVariables += azkenEmaitza+ "\n";
												codeWithFunctions += azkenEmaitza+ "\n";	
											}
										}
									}

								}
							}
						}
					}
				}

				// Class terms
				if (line.contains("class")  && line.contains("{")) {
					String[] clss = line.split("class");
					if(clss.length>1) {
						String aux = clss[1];
						String[] lineSecondPart = aux.split(" ");

						if(lineSecondPart.length>1) {
							String result = lineSecondPart[1];
							if(result!=null && !result.contains(",") && !result.contains(")") && !result.contains("_")  && !termTable.itsStopWord(result) && result.length()>3) {
								if(termTable.appears(result)){
									termTable.sumApparition(result);
									termTable.addPath(result, cf.getFilename(), cf.getPath());
								}else {
									Term t= new Term(result,10);
									t.addNewFile(cf.getFilename(), cf.getPath());
									termTable.getTermTable().add(t);

								}
							}

						}


					}
				}
				
				//HTML file's terms
				String[] span=null;
				String[] resultWithNumber=null;
				String[] result=null;
				if (line.contains("<span ") && line.contains(".")) {
					span= line.split(">");
					resultWithNumber= span[span.length-1].split("<");
					if(resultWithNumber[0].contains(" ")) {
						result = resultWithNumber[0].split(" ");
						if (result[0].length()<7 && !result[result.length-1].contains(".")) {
							if(termTable.appears(result[result.length-1])){
								termTable.sumApparition(result[result.length-1]);
								termTable.addPath(result[result.length-1], cf.getFilename(), cf.getPath());
							}else {
								Term a= new Term(result[result.length-1],10);
								a.addNewFile(cf.getFilename(), cf.getPath());
								termTable.getTermTable().add(a);

							}
						}
					}
				}



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
			printCodeInFile(line);
			printVariablesInFile(codeWithVariables);
			printFunctionInFile(codeWithFunctions);
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
				System.out.println("Kodea: " + line);
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
