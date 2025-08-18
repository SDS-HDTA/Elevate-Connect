# Humanitarian Design Thinking App (HDTA) - Co-Design-Connect

## Local setup walkthrough
### Step 1 - Installing relevant software
Please install all of the following software onto your machine:
- [Java JDK 17](https://learn.microsoft.com/en-us/java/openjdk/download#openjdk-17)
- [Git](https://git-scm.com/downloads)
- [MySql](https://dev.mysql.com/downloads/installer/) - See Step 4 for install details
- [NodeJS](https://nodejs.org/en/download/current)

### Step 2 - Cloning repository onto your machine
In order to clone the repository onto your machine, please walkthrough and setup Git on your machine.

Once git is installed correctly, please run the following in your terminal:
```
git clone https://github.com/LeftsideMartian/HDTA.git
```
Once this has finished running, the repository will be downloaded to your machine.

### Step 2 - Open repository in VSCode
Next, please open the repository in VSCode.

In VSCode: File -> Open Folder -> Select the repository folder

### Step 3 - Create .env file
In the file explorer, navigate to "Backend/Co-Design-Connect/src/main/resources".

Create a copy of the file ".env.example" and rename is to ".env".

Then fill out the relevant details inside the ".env" file.

### Step 4 - Setting up MySQL and creating the database locally
When walking through the MySQL installer, select the following options when available (otherwise just click Next or Execute):
- Setup Type: Full
- Username and password: leave username as root, and set password to whatever you want (But make sure to write it down)

Next, open "MySQL Workbench" - a GUI application for MySQL.

Under "MySQL Connections", click on "Local instance MySQL", and sign in using your previous username and password ("root", and whatever you set as your password).

In the tab "Query 1", paste all SQL statements from the file "Handover/dbSetup.sql", and execute the query. This will create the database, and insert default rows.

Confirm this by changing the left panel to "Schemas", right clicking and selecting "Refresh All". You should be able to view the database, and its tables.

### Step 5 - Getting the frontend running
Open a terminal in the folder "Frontend/co-design-connect", and run the following two commands:
```
npm install
npm run dev
```

### Step 6 - Getting the backend running