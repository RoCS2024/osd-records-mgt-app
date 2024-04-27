osd-records-mgt-app setup

First is setup the Database
There are 3 database you need for osd-records-mgt-app

The violation Table, offense Table, and the comm_serv_rendered Table

Here is the Code for Violation Table 
create table violation (
	id 		NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	violation 	varchar2(64),
	type		varchar2(8),
	comm_serv_hours	number(4)
);

Here is the Code for Offense Table 
CREATE TABLE offense (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    violation_id NUMBER,
    student_id VARCHAR2(32) NOT NULL,
    offense_date TIMESTAMP(6),
    comm_serv_hours NUMBER(4),
    FOREIGN KEY (violation_id) REFERENCES violation(id),
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

Here is the Code for comm_serv_rendered Table 
create table comm_serv_rendered (
	id 		NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	student_id	varchar2(32) not null,
	date_rendered	timestamp(6),
	hours_rendered  number(4),
	foreign key (student_id) references student(student_id)
);

CREATE OR REPLACE TRIGGER match_comm_serv_hours_trigger
BEFORE INSERT OR UPDATE ON offense
FOR EACH ROW
BEGIN
    -- Update comm_serv_hours in the offense table to match the value from the violation table
    IF :NEW.violation_id IS NOT NULL THEN
        SELECT comm_serv_hours
        INTO :NEW.comm_serv_hours
        FROM violation
        WHERE id = :NEW.violation_id;
    END IF;
END;
/

Now that you are done with the database

You can now proceed to create jar in the Back-End of rc-porms

First go the Repository Back-end named "rc-porms" (Prefect Office Record Management System)
Here is the  url code:
https://github.com/RoCS2024/rc-porms.git
![rc-porms-repo](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/6f530504-2b23-4f6a-99de-74a933a8e57c)

Get the code
![code](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/35cff5ac-e96f-444e-90c6-752a85bdfc16)

Create New Folder

Open Git bash
type "git clone" and paste the url code 
![back-end-rc-porms](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/1e1bc695-5598-420b-9160-3ec774defce5)

Open the rc-porms in IntelliJ
![open-rc-porms](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/159e8e8b-46f1-4f05-afaa-7ea5fec8f182)

Delete the Main Class
Right click main, select delete and then press okay
![delete-main-class](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/37e60af4-90cb-4b9f-86a5-73cb86ba1865)

After deleting the Main class

Go to ivy.xml Rename the module and then type "porms"
![ivyxml](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/eab74eca-db6b-4a90-a522-db01d2ec657a)

this part.
Rename the:
module="ivy-test" into module="porms"
![this-part](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/005c81f8-a190-4c57-bf46-2698dca28508)

Like this
![rename-porms-ivyxml](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/091e7ef4-51ee-41cf-b795-4541dfa29535)

Now you are done with ivy.xml

You can now proceed to build.xml
Go to build.xml and then go to create jar
![buildxml-porms](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/b7d39e73-3279-49eb-9132-c561148bf897)

Rename this part:
![rename-this-part-buildxml](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/187dbca2-ee6a-4e47-ac1c-634ec35c740b)

rename the:
 <jar destfile="${build.dir}/${ant.project.name}-${version}.jar" into <jar destfile="${build.dir}/porms.jar"

like this
![rename-porms-buildxml](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/07816512-48a7-45ac-b3bb-63d5c724fdbe)

Now that you are done with ivy.xml and build.xml

Go to Terminal and then type "ant build-jar"
![ant-build-jar](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/310caef8-b27c-4cd8-bc26-3def5cb20aa5)

It will display build-jar Successful
![build-successful](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/c8c87bdb-5f2b-4d68-b10b-eca2481cdd88)

Check the build if the jar exist
![jar-porms-exist](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/ff64048a-6bfb-4f12-a82a-b740f63dc7ba)

And now you are done creating jar in the Back-End of Prefect Record Management System (porms)

You just need to repeat the steps in creating the jar for the User Management System (ums) and for the Student Information Management System (sims)

Now we are ready to import the porms, ums, and sims jar but before that we need to set up the Front-end of the osd-records-mgt-app

First you go to the repo of Front-End Prefect Record Management System named "osd-records-mgt-app"
Here is the url code:
https://github.com/RoCS2024/osd-records-mgt-app.git

![repo-osd-records-mgt-app](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/36f4de31-98c0-405c-81be-7aa4533e2852)

Just like we did before you need to create Folder

Clone the url code of the osd-records-mgt-app
![clone-osd-records-app](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/7649717e-4544-444b-94fc-f8a6db8a7d09)

and then open it in the IntelliJ
![open-osd-records-mgt-app](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/3795375f-9e32-44a5-a9fe-815c151b9c77)

When you open the osd-records-mgt-app there are some possible error you will encounter.
1. First is the dependency
It looks like this
![possible error](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/613f4db9-0137-4469-99f4-6b3657301905)

To fix this first check the pom.xml
![image](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/900280e7-b6c7-4d96-80ab-3532fb420612)

Check if these dependency are in the pom.xml
![dependency](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/73620abd-c412-4b2f-bf6d-a238ed72de02)

if not 
copy this dependency and then add it to pom.xml
        <dependency>
            <groupId>com.oracle.database.jdbc</groupId>
            <artifactId>ojdbc11</artifactId>
            <version>23.3.0.23.09</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j2-impl</artifactId>
            <version>2.23.1</version>
        </dependency>

after that go to Terminal type mvn clean install

2. The second possible error you will encounter is this:

REMINDER THIS IS OPTIONAL WHEN YOUR JAR IS NOT WORKING ON YOU FRONT-END
You will notice that the jar name of ums is umsv2 in my project but the original name of the jar is ums not umsv2 why did i change that into umsv2? 
because when you done creating jar in Back-end and when you import it in your Front End you will notice, 
even if you type the correct name of the jar in the module-info-java and
the 2 jar are all set and when you run the Main View in the Front-end it will display "module not found ums or porms".

To fix that you just need to delete the back-end of the jar that is not working
Create new folder
Clone it again
Repeat the steps on how to create Jar in the Back-End
Rename the jar just like i did in my project for example "umsv2"
and then it is ready to import to the Front End and i can guarantee that it will work

3. The third error you will encounter when you open the Front-end of osd-records-mgt-app is the "module-info-java"
   
As you can see in here the "require porms", "require sims" part and "umsv2" is color red. Because we have not yet imported the jar.

REMINDER: You need 3 jar for the osd-records-mgt app
*porms  (prefect office records management system)
*ums  (user management system)
*sims (student info management system)

![osd-records-mgt-app-module-info-java](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/9b16ec08-a267-47b2-ad2c-41383bb01215)

Now where going to import the 3 jar of the Back-end into the Front-end
Here is the step by step:
1. Go to Project Structure
![osd-records-mgt-app-project-structure](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/c49e934d-17d2-4a1a-b903-d6e135b04248)
2. Go to Modules
![osd-records-mgt-app-modules](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/0cc9468e-bffc-492d-80d6-83255493b8ce)
3.Click the plus button and then choose option 1. Jar or Directories
![osd-records-mgt-app-add-button](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/66b45cd4-44d5-4353-9dbc-2343c0687eff)
4.After that locate the file where you build the jar. Click the Jar and press okay.
![locate-porms-jar](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/febd9cd9-dbba-48c7-be99-b5af6c58f2ed)
5.The jar that you select will add to your dependency.
![porms-jar](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/cdd5b431-9f44-4625-b8a2-ebfba65e5ba1)
6.Repeat the steps when you want to import the ums and sims jar
7.As you can see the 3 jar is in the dependency. Click okay and then it will resolve the error in the modules-info-java
![osd-records-mgt-app-3-jar-imported](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/2efc656b-8dfe-462a-9bc8-bd7d67178931)
8. As you can see the errors are gone
![done](https://github.com/RoCS2024/osd-records-mgt-app/assets/157860683/be585e46-cee9-49f1-abaa-c4031366afe7)

When you are done you can run the Main View of the Front End smoothly enjoy!!


  








