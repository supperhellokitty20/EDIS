# EDIS 


## Install 

1. Compile the source code to get the jar file .

2. Test run the compiled source file with flags " java -jar App.jar"

## Testing instructions  

* Run ``App.java``  , in the EDIS to get help ``EDIS> help`` 
* Load the prepared `Test_data`  file into the system  `EDIS> load Test_data` . This will provided some fake data for user to test out the system.


##  Features of the two sytems
	
###1. Add new Patient in  a data line input   

* Command ``add <name> <age> <mm/DD/yyyy>`` . Will add a new patient with provided info. 

* Notice that `<name>` must have no space between names (First,Last) or the names must be seperate with a special character (Foo_Bar) .


###2. Get a patient with id or name  
 Command `` get [--name,-n][--id,-i]``. Will print out the patient info with such name or id passed in . 

###3. Remove a patient with id or name  
 Command `` remove [--name,-n][--id,-i]``. Will remove the patient with given name or id from the system . 

###4. Edit the current patient data from name or id  
 Command `` edit [--name,-n][--id,-i]``.  Will allow user to edit the patient info.If the patient name or id is found.The program will bring the user 
 to a seperate prompt where user will input the edit fields  `name,age,time,all`  and the value to change. 

###5. Export the current patient data to text file  

* Command ``export <file_name>`` will export the current system patient to a file in a format such that can be loaded later in the system with `load <saved_data_file>`  