package com.app;
import java.util.* ;

import com.app.exceptions.InvalidDataFormat;
import com.app.exceptions.InvalidTokensNum;

import java.io.File;
import java.io.FileNotFoundException;
public class App {
	/**
	 *  The controller could be defined in term of two classes  
	 */
	Scanner sc ; 
	Controller controller;
	/** 
	 * Print the first n number of patient  
	 * @param num
	 */
	private void printData(int num) { 
		
	}
	/**
	 * Help Text for the program 
	 */
	private void printHelp() { 
		//Print help text 
		System.out.println("Use our interactive shell software with simple syntax\n"
				+ "\t 1.load <input_file>\n"
				+ "\t 2.export <export_file>\n"
				+ "\t 3. get [name][id] \n"
				+ "\t 4. add <name><age><dd-MM-yyyy><data>\n"
				+ "\t 5. remove <PatientList> \n"
				+ "\t 6. search [--id][--name][--age] \n" 
				+ "\t 7. quit \n") ;
		System.out.println("You can also query the data and filter by risk ,"
				+ "blood pressure by using --filter class ") ;
	}

	/* Print Banner for the software 
	 * */
	private void printBanner() { 
		String ANSI_YELLOW = "\u001B[33m";
		String ANSI_RESET = "\u001B[0m";
		String banner = "####### ######  ###  #####  \n"
				+ "#       #     #  #  #     # \n"
				+ "#       #     #  #  #       \n"
				+ "#####   #     #  #   #####  \n"
				+ "#       #     #  #        # \n"
				+ "#       #     #  #  #     # \n"
				+ "####### ######  ###  #####  " ;
		 System.out.println(ANSI_YELLOW
                 +banner + ANSI_RESET);
		 System.out.print("\n")  ;

	}
	int subFixCount() { 
		return 0  ;
	}
	/**
	 * Load a file that contains the current system patient data 
	 * .To be a valid file,each line must follow the syntax: 
	 * <name> <age> <arriveTime> <Data>  
	 * Upon successful creation of the input patient info , the controller will output the input file summary table  
	 * @param file
	 * @return  
	 */
	private void loadFile(String path) { 
		//Parse the input file and store data to the controller 
		// Open the file Scan for each line of the file to 
		// Print input summary details ( Total new patient, Currently vacant doctor)  
		File load = new File(path) ;
		Scanner sc ;
		try {
			sc = new Scanner(load);
			while(sc.hasNextLine()) { 
				String data = sc.nextLine().trim(); 
				//Tokenize the input
				StringTokenizer tc = new StringTokenizer(data," ") ;
				int size  = tc.countTokens();
				String [] patientArray= new String[size] ;
				int i =0;
				while(tc.hasMoreTokens()) {
					patientArray[i]= tc.nextToken().trim() ;
					i++ ;
				}
				String newPatientId;
				try {
					newPatientId = controller.add(patientArray);
					System.out.printf(" New Patient Id added %s\n",newPatientId ); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.print(e.getMessage()) ;
				}
				//Summary of input patient 
			}
			 
		} catch (FileNotFoundException e) {
			System.out.println("File name not found !") ;
		}
	}
	/**
	 * Helper method to export data to file.  
	 */
	private void saveData() {

		//The default name for the export 
		//String  defaultName ="Patient_data.txt" ;
		String path = "." ;
	}
	public void quit() { 
		System.out.println("Do you want to exit the program,all patient data will be lost\n"
				+ "Do you want to safe current data ? (Y/n)") ;
		String op= sc.next(); 
		boolean save =  op.equals("y")||op.equals("Y");
		//Check what choice the user have 
		if(save) { 
			//writeData() ;
			saveData() ;
		}
		System.out.println("Quit Success !")  ;
		System.exit(0);
		
	}
	/**
	 * Check for userinput and create new paitent  
	 */
	public void takePatientInput( String[] data ){
		//TODO: Handle incorrect input data 
		try { 
			//TODO : Modify the controller to throws InvalidDataFormat when invalid input
			String id;
			id =controller.add(data) ;
			System.out.printf(" New Patient Id added %s\n",id); 
		} catch (Exception e) {
			System.out.println(e.getMessage()) ;
			// Handle data rechange name
			}
		}

	/**
	 * Execute the option choosen by user , in each options you should have a  data check   
	 * There are multiple operation: 
	 *  <ul>
	 * 		<li> quit</li> 
	 * 		<li> load <file_path> </li> 
	 * 		<li> add <Patient>  </li> 
	 * 		<li> edit [<id>,<name>] [--name,-n][--age,-a][--time,-t][--all,-a]  <value>  </li> 
	 * 		<li> remove <PatientName> or {PatientID}  </li> 
	 * 		<li> export <output_file_path> </li> 
	 * 		<li> get <id>  </li> 
	 *  </ul> 
	 * @param option
	 * @return 
	 */
    public void execute(String option){

    	//Tokenize input 
    	StringTokenizer st = new StringTokenizer(option.trim()) ;
    	// Number of user input each loop iteration 
    	final int numTokens=st.countTokens() ;
    	String[] flag  = new String[numTokens] ; 
    	for(int i=0;i<numTokens;i++) { 
    		flag[i] = st.nextToken().trim() ;
    	}
    	String mode  = flag[0] ;
    	switch(mode) { 
    		/**
    		 * User to quit the app,a save prompt will appear for user to select to save the current user data to the system. 
    		 */
				case( "quit"): {
					if(numTokens==1) quit() ;
					break ;
					//else throws Exception.Wrong numbers of arguments 
				}
				case("load"):{ 
					//TODO : Make sure the path is correct and was able to open 
					try { 
						loadFile(flag[1]);
					}catch(Exception err ) { 
						System.out.println(err.getMessage()) ;
					}
					break ;
					
				}
				case("export"):{ 
					//Export parse the  
					break ;
				}
				case("add"):{
					//We need <name><age><arriveTime>
					try {
						takePatientInput(Arrays.copyOfRange(flag,1,flag.length)) ;
					}
					catch(Exception err) {
						System.out.println(err.getMessage()) ;
					}
					break ;
				} 
				case("remove"):{ 
					boolean removeWithName = flag[1].equals("--name")|| flag[1].equals("-n") ;
					boolean removeWithId= flag[1].equals("--id")|| flag[1].equals("-i") ;
					if(!removeWithName && !removeWithId) { 
						System.out.println("Please specify value type with [--name,-n] or [--id ,-i]") ;
						break ;
					}
					if(removeWithName){
						try {
							String revName ="" ;
							for(int i=2;i<flag.length-1;i++) {
								revName+=flag[i]+" " ;
							}
							revName+=flag[flag.length-1] ;

							controller.remove(revName,true) ;
							System.out.println("Patient with name \"" + flag[2]+"\" removed") ;
						} catch(Exception e) { 
							System.out.println(e.getMessage()) ;
						}
					}
					if(removeWithId) { 
						try {
							controller.remove(flag[2],false) ;
							System.out.println("Patient with id \"" + flag[2]+"\" removed") ;
						} catch(Exception e) { 
							System.out.println(e.getMessage()) ;
						}
						
					}
					break;
				}
				case("edit"):{ 
					/**
					 *  Check if ther user input more than one edit option 
					 *  if notValid  println("Only one edit field is allow") ; 
					 *  if(valid){ 
					 *  	edit(Patient,option,value);	
					 *  } 
					 */
					break ;
				}
				case("get"):{ 
					/**
					 * Get patient info from --id  list or --name list , each seperated by the comma (,)
					 */
					boolean getWithName= flag[1].equals("--name")|| flag[1].equals("-n") ;
					boolean getWithId= flag[1].equals("--id")|| flag[1].equals("-i") ;
					if(!getWithName && !getWithId) { 
						System.out.println("Please specify value type with [--name,-n] or [--id ,-i]") ;
						break ;
					}
					if(getWithName){
						try {
							String revName ="" ;
							for(int i=2;i<flag.length-1;i++) {
								revName+=flag[i]+" " ;
							}
							revName+=flag[flag.length-1] ;
							//Get the patient 
							Patient p = controller.get(revName,true) ;
							//TODO : Count the number of patient have the same subfix 
							System.out.println(p.toString()) ;
						} catch(Exception e) { 
							System.out.println(e.getMessage()) ;
						}
					}
					if(getWithId) { 
						try {
							//Get the patient  here
							String getId = flag[2]; 
							Patient p = controller.get(getId,false) ;
							System.out.println(p.toString()) ;
						} catch(Exception e) { 
							System.out.println(e.getMessage()) ;
						}
						
					}
					break;
				}
				case("help"):{ 
					this.printHelp() ;
				}
    	} 
    }
    public App(){ 
    	sc = new Scanner(System.in) ;
    	//Change type of system here
    	controller = new SystemOne() ;
    	printBanner() ;
    	printHelp() ;
    }

    /*
     *  Main loop for the software 
     */
    private void loop(){ 
        String prompt= "EDIS > " ;
        String option ;
        while(true){ 
            System.out.print(prompt) ;
            option = sc.nextLine() ; 
            //Check if the option is empty  
            if(option=="") {
            	continue;
            }
            execute(option) ;
        }
    }
    public static void main(String[] args){
        App n = new App()  ;
        n.loop() ;
    }

}
