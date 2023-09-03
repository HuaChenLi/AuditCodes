# Audit Codes
This project was about trying to automate a manual process of splitting things into different categories, simulating 
when a Business needs to split up their expenditures into different categories. The categories are the columns where 
the income and expenses can be divided into. For the text to be human-readable as well, the program is also able to 
folder structures for different years automatically. This is to help with creating the Audits which the company could
use to look at summaries of their income and expenses. 

The first part was about the creation of directories thanks to Python scripts mainly, with a Java GUI to run them. The
second part was about creating a basic GUI from Java that would allow users to create their own columns, create their 
own categorisation of data and 

First, the file structure is created. I learnt how to store the data into tables in MySQL and extract them with Python 
scripts that can be run with Java. This taught me some basic formatting with Python interacting with Excel. 
Additionally, this taught me how to interact with a Database using Python to store data. The Excel Spreadsheet columns
are stored in MySQL database. 

Next, the CSV sheets with the income and expenses would need to have their data cleaned. This requires a bit of manual 
work placing the downloaded CSVs in the correct directories. The Python script then runs and cleans the data with a bit
of regex. Because there are now multiple python scripts that are interacting with the database, I learnt to separate the
SQL calls into a different directory because that would enable for cleaner segmentation of the code. That way if I 
changed databases or anything else that only related to the database, I could make the changes in one file. 

With the SQL functions, I also learnt to store the details outside the git repository and have them read from a text
file. Although this isn't very secure, having the segmented Python scripts means that I just have to look at one area
of my code.

Next, the resulting values are then categorised and placed into their columns. Again, this is done by using MySQL and
locating the information in the database.
