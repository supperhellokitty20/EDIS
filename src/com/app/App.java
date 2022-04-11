package com.app;

import java.util.*;

import com.app.Controller.EditOptions;
import com.app.exceptions.InvalidDataFormat;
import com.app.exceptions.InvalidTokensNum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class App {
	/**
	 * The controller could be defined in term of two classes
	 */
	Scanner sc;
	Controller controller;

	/**
	 * Help Text for the program
	 */
	private void printHelp() {
		// Print help text
		System.out.println("Use our interactive shell software with simple syntax\n" + "\t 1.load <input_file>\n"
				+ "\t 2.export <export_file>\n" + "\t 3. get [name][id] \n"
				+ "\t 4. add <Firstname/LastName> <age> <dd-MM-yyyy>\n" + "\t 5. remove <PatientList> \n"
				+ "\t 6. quit \n");
		System.out.println(
				"You can also query the data and filter by risk ," + "blood pressure by using --filter class ");
	}

	/*
	 * Print Banner for the software
	 */
	private void printBanner() {
		String ANSI_YELLOW = "\u001B[33m";
		String ANSI_RESET = "\u001B[0m";
		String banner = "####### ######  ###  #####  \n" + "#       #     #  #  #     # \n"
				+ "#       #     #  #  #       \n" + "#####   #     #  #   #####  \n" + "#       #     #  #        # \n"
				+ "#       #     #  #  #     # \n" + "####### ######  ###  #####  ";
		System.out.println(ANSI_YELLOW + banner + ANSI_RESET);
		System.out.print("\n");

	}

	int subFixCount() {
		return 0;
	}

	/**
	 * Load a file that contains the current system patient data .To be a valid
	 * file,each line must follow the syntax: <name> <age> <arriveTime> <Data> Upon
	 * successful creation of the input patient info , the controller will output
	 * the input file summary table
	 * 
	 * @param file
	 * @return
	 */
	private void loadFile(String path) {
		// Parse the input file and store data to the controller
		// Open the file Scan for each line of the file to
		// Print input summary details ( Total new patient, Currently vacant doctor)
		File load = new File(path);
		Scanner sc;
		try {
			sc = new Scanner(load);
			while (sc.hasNextLine()) {
				String data = sc.nextLine().trim();
				// Tokenize the input
				StringTokenizer tc = new StringTokenizer(data, " ");
				int size = tc.countTokens();
				String[] patientArray = new String[size];
				int i = 0;
				while (tc.hasMoreTokens()) {
					patientArray[i] = tc.nextToken().trim();
					i++;
				}
				String newPatientId;
				try {
					newPatientId = controller.add(patientArray);
					System.out.printf(" New Patient Id added %s\n", newPatientId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.print(e.getMessage());
				}
				// Summary of input patient
			}

		} catch (FileNotFoundException e) {
			System.out.println("File name not found !");
		}
	}

	/**
	 * Helper method to export data to file.
	 */
	private void saveData(String fileName) {

		// The default name for the export
		// String defaultName ="Patient_data.txt" ;
		String path ; 
		if(fileName.isBlank()) { 
			path = "Patient_data.txt";
		}else { 
			path = fileName; 
		}

		File f = new File(path);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(path);
		controller.export(path);
		System.out.println("Data has been saved as: " + path);
	}

	public void quit() {
		System.out.println("Do you want to exit the program,all patient data will be lost\n"
				+ "Do you want to safe current data ? (Y/n)");
		String op = sc.next();
		boolean save = op.equals("y") || op.equals("Y");
		// Check what choice the user have
		if (save) {
			// writeData() ;
			saveData("");
		}
		System.out.println("Quit Success !");
		System.exit(0);

	}

	/**
	 * Check for userinput and create new paitent
	 */
	public void takePatientInput(String[] data) {
		// The input name should have no space between or
		try {
			// TODO : Modify the controller to throws InvalidDataFormat when invalid input
			String id;
			id = controller.add(data);
			System.out.printf(" New Patient Id added %s\n", id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// Handle data rechange name
		}
	}

	/**
	 * Since name in the system dont contain any space this function process the
	 * name , add space to each capital letter represent the different each names
	 * (First name, last Name, middle Name) ;
	 * 
	 * @param n
	 * @return
	 */
	public String formatName(String n) {
		return n.replaceAll("(.)([A-Z])", "$1 $2");
	}

	/**
	 * Execute the option choosen by user , in each options you should have a data
	 * check There are multiple operation:
	 * <ul>
	 * <li>quit</li>
	 * <li>load <file_path></li>
	 * <li>add <Patient></li>
	 * <li>edit [<id>,<name>] [--name,-n][--age,-a][--time,-t][--all,-a] <value>
	 * </li>
	 * <li>remove <PatientName> or {PatientID}</li>
	 * <li>export <output_file_path></li>
	 * <li>get <id></li>
	 * </ul>
	 * 
	 * @param option
	 * @return
	 */
	public void execute(String option) {

		// Tokenize input
		StringTokenizer st = new StringTokenizer(option.trim());
		// Number of user input each loop iteration
		final int numTokens = st.countTokens();
		String[] flag = new String[numTokens];
		for (int i = 0; i < numTokens; i++) {
			flag[i] = st.nextToken().trim();
		}
		String mode = flag[0];
		switch (mode) {
		/**
		 * User to quit the app,a save prompt will appear for user to select to save the
		 * current user data to the system.
		 */
		case ("quit"): {
			if (numTokens == 1)
				quit();
			break;
			// else throws Exception.Wrong numbers of arguments
		}
		case ("load"): {
			// TODO : Make sure the path is correct and was able to open
			if(numTokens<2) {
				System.out.println("Please specify a file name to load");
				break ;
			}
			try {
				loadFile(flag[1]);
			} catch (Exception err) {
				System.out.println(err.getMessage());
			}
			break;

		}
		case ("export"): {
			if(numTokens<2) {
				System.out.println("Please specify a  file path to be saved (default : Patient_data.txt");
				break ;
			}
			//controller.export(flag[1]);
			saveData(flag[1]) ;
			break;
		}
		case ("add"): {
			// We need <name><age><arriveTime>
			if(numTokens<2) {
				System.out.println("Please specify patient  data with [--name,-n] or [--id ,-i]");
				break ;
			}
			try {
				takePatientInput(Arrays.copyOfRange(flag, 1, flag.length));
			} catch (Exception err) {
				System.out.println(err.getMessage());
			}
			break;
		}
		case ("remove"): {
			if(numTokens<2) {
				System.out.println("Please specify patient data with [--name,-n] or [--id ,-i]");
				break ;
			}
			boolean removeWithName = flag[1].equals("--name") || flag[1].equals("-n");
			boolean removeWithId = flag[1].equals("--id") || flag[1].equals("-i");
			if (!removeWithName && !removeWithId) {
				System.out.println("Please specify value type with [--name,-n] or [--id ,-i]");
				break;
			}
			if (removeWithName) {
				try {
					String revName = "";
					for (int i = 2; i < flag.length; i++) {
						revName += flag[i];
					}
					controller.remove(revName, true);
					System.out.println("Patient with name \"" + formatName(revName) + "\" removed");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			if (removeWithId) {
				try {
					controller.remove(flag[2], false);
					System.out.println("Patient with id \"" + flag[2] + "\" removed");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}
			break;
		}
		case ("edit"): {
			/**
			 * Check if ther user input more than one edit option if notValid println("Only
			 * one edit field is allow") ; if(valid){ edit(Patient,option,value); } EDIS >
			 * edit --name "foo bar" --name "Hello World"
			 */
			if(numTokens<2) {
				System.out.println("Please specify patient data with [--name,-n] or [--id ,-i]");
				break ;
			}
			boolean editName = flag[1].equals("--name") || flag[1].equals("-n");
			boolean editId = flag[1].equals("--id") || flag[1].equals("-i");
			if (!editName && !editId) {
				System.out.println("Please specify value with [--name,-n] or [--id ,-i]");
				break;
			}
			// Set the correct key to the system
			// if is edit id then the key is flag[2]
			// but if the key is name scan the name
			String key;
			if (editId) {
				key = flag[2];
				boolean found = controller.search(key, false);
				if (!found) {
					System.out.println("patient with id: " + key + " not found");
					break;
				}

			} else {
				String revName = "";
				for (int i = 2; i < flag.length; i++) {
					revName += flag[i];
				}
				key = revName;
				boolean found = controller.search(key, true);
				if (!found) {
					System.out.println("patient with name: " + formatName(key) + " not found");
					break;
				}
			}
			/**
			 * Refactor in to a seperate prompt to edit patient info
			 */
			System.out.println("Please choose a field: ");
			String field = sc.nextLine();
			Controller.EditOptions userOp = null;
			boolean editAgeVal = field.equals("age") || field.equals("a");
			if (editAgeVal) {
				userOp = EditOptions.AGE;
			}
			boolean editNameVal = field.equals("name") || field.equals("n");
			if (editNameVal) {
				userOp = EditOptions.NAME;
			}
			boolean editTimeVal = field.equals("time") || field.equals("t");
			if (editTimeVal) {
				userOp = EditOptions.ARRIVETIME;
			}
			boolean editAllVal = field.equals("all") || field.equals("a");
			if (editAllVal) {
				userOp = EditOptions.ALL;
			}
			if (userOp == null) {
				System.out.println("Specify : name ,age ,time ,all");
				break;
			}
			// Extract value input from flag
			//
			String value = "";
			System.out.println("Please enter new value of specified field: ");
			value = sc.nextLine().trim();
			if (userOp.equals(EditOptions.NAME)) {
				value.replaceAll(" ", "");
			}
			if (editName) {

				try {
					// edit
					controller.edit(key, true, userOp, value);
					System.out.println("Patient with name \"" + formatName(key) + "\" edited ");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			if (editId) {
				try {
					// edit
					controller.edit(key, false, userOp, value);
					System.out.println("Patient with id \"" + key + "\" editd");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}
			break;
		}
		case ("get"): {
			/**
			 * Get patient info from --id list or --name list , each seperated by the comma
			 * (,)
			 */
			if(numTokens<2) {
				System.out.println("Please specify patient data with [--name,-n] or [--id ,-i]");
				break ;
			}
			boolean getWithName = flag[1].equals("--name") || flag[1].equals("-n");
			boolean getWithId = flag[1].equals("--id") || flag[1].equals("-i");
			if (!getWithName && !getWithId) {
				System.out.println("Please specify value type with [--name,-n] or [--id ,-i]");
				break;
			}
			if (getWithName) {
				// Implement the to also show the patient with the same name
				// For instance a get --name Will Smith_2 will show patient with name Will Smith
				// Will Smith_3
				// Simply by counting the number of subfix in <name>_<subfix>
				try {
					String revName = "";
					for (int i = 2; i < flag.length; i++) {
						revName += flag[i];
					}
					// Get the patient
					if (revName.contains("_")) {
						System.out.println(controller.get(revName, true).toString());
					}
					// If name given with no subfix, will print out all results found under the
					// given name
					else {
						System.out.println("Searching for all " + revName + ":");
						// Gets list of names from the system
						ArrayList<String> names = controller.getNames();
						int cont = 0;
						// check through the list of names to find all names with the given name
						// if found will print out the info for those patients
						for (int i = 0; i < names.size(); i++) {
							if (names.get(i).contains(revName)) {
								System.out.println(controller.get(names.get(i), true).toString());
								cont++;
							}
						}
						System.out.println("Found all " + cont + " results");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			if (getWithId) {
				try {
					// Get the patient here
					String getId = flag[2];
					Patient p = controller.get(getId, false);
					System.out.println(p.toString());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}
			break;
		}
		case ("help"): {
			this.printHelp();
		}
		default:
			System.out.println("This is not an option, try again!");
		}
	}

	public App() {
		sc = new Scanner(System.in);
		// Change type of system here
		boolean corct = true;
		String opt;
		do {
			System.out.println("Welcome, which system would you like to boot?");
			System.out.println("(Type 1 for System One or 2 for System Two)");
			opt = sc.nextLine();
			if (opt.equals("1") || opt.equals("2"))
				corct = false;
			else {
				System.out.println("That is not a system, please try again!");
				corct = true;
			}
		} while (corct);
		if (opt.equals("1"))
			controller = new SystemOne();
		else
			controller = new SystemTwo();
		printBanner();
		printHelp();
	}

	/*
	 * Main loop for the software
	 */
	private void loop() {
		String prompt = "EDIS > ";
		String option;
		while (true) {
			System.out.print(prompt);
			option = sc.nextLine().trim();
			// Check if the option is empty
			if (option == "") {
				continue;
			}
			execute(option);
		}
	}

	public static void main(String[] args) {
		App n = new App();
		n.loop();
	}

}
