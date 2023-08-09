# import csv
import os
import shutil
import sys
import time

import SQLFunctions.sql_excel_columns

from SQLFunctions.select_mappings import *
from CommonLibrary.date_libraries import month_period
from CommonLibrary.getting_replacement_values import replacement_values

sys.path.append(os.path.abspath("CommonLibrary"))

financial_year = "2022 - 2023"

financial_year_folder = financial_year[:4] + " Jul - " + financial_year[7:] + " Jun"

# feels like a mess whenever I open up the scripts. I usually have no idea what to actually do to run the reports, and I"m scrambling all the time. I want to add an archive feature. Basically, it will save and store the files in an archive folder.
# My bank account being gone from Commbank is a little annoying. I'm questioning why an extra 3.8K was deducted from my bank account. Even if they closed my Credit Card account, they should have still kept the bank account visible with my past statements.
# Also I want to make the process easier for me. It"s pretty annoying not knowing which library to run

# the number of rows the Excel has. Can edit this in case for some reason, 1000 is not enough
number_of_cells = 1000

months_list = ["Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun"]

quarters_list = [month_period(1), month_period(2), month_period(3), month_period(4)]

csv_column_names = ["Date", "Amount", "Description", "Balance"]

auditID = 1

root_excel_directory = "C:\\Users\\hua-c\\Desktop\\Coding Stuff\\Python Coding\\My Audit\\My_Audit_2022"

bank_accounts = ["Mastercard", "Smart Access"]

csv_data = pd.DataFrame(columns=csv_column_names)

# archive process
# actually, do I even want to archive the csv. I feel like the csv isn't necessary
# just need to archive the Income.csv

archive_root = os.path.join("/My Audit/My_Audit_2022/Archive")

timestring = time.strftime("%Y%m%d-%H%M%S")
archive_folder = os.path.join(archive_root + "\\" + timestring)

try:
    os.mkdir(archive_folder)
except:
    print("Can't create archive folder")

try:
    shutil.move("/My Audit/My_Audit_2022/Income.csv", archive_folder + "\Income.csv")
except:
    print("No Income file")

try:
    shutil.move("/My Audit/My_Audit_2022/Expenditure.csv", archive_folder + "\Expenditure.csv")
except:
    print("No Income file")

# merge Mastercard and Smart Access transactions csv into 1 file
for account in bank_accounts:
    csv_data_folder_path = os.path.join(root_excel_directory, account, "CSVData.csv")
    print(csv_data_folder_path)
    collected_data = pd.read_csv(csv_data_folder_path, names=csv_column_names, header=None)
    csv_data = csv_data.merge(collected_data, how="outer")

csv_data["Date"] = pd.to_datetime(csv_data["Date"], format="%d/%m/%Y")
csv_data.sort_values(by="Date", inplace=True)

for sheet_title in ["Income", "Expenditure"]:
    if sheet_title == "Income":
        is_income = True
        incomeExpenseChar = "I"
    else:
        is_income = False
        incomeExpenseChar = "E"

    replacement_value = replacement_values(auditID, incomeExpenseChar)
    associated_values_dictionary = dict()

    column_dataframe = SQLFunctions.sql_excel_columns.select_excel_column(audit_id=auditID, is_income=is_income)
    column_name_list = ["Date", "Description"] + list(column_dataframe["ColumnName"])

    for index, row in column_dataframe.iterrows():
        column_name = row["ColumnName"]
        excel_column_id = row["ExcelColumnID"]
        temp_df = SQLFunctions.sql_excel_columns.select_excel_category_mapping_values(excel_column_id)

        values_list = list(temp_df["CategoryValues"])

        associated_values_dictionary.setdefault(column_name, values_list)

    excel_sheet = pd.DataFrame(columns=column_name_list)

    for index, row in csv_data.iterrows():

        # creating the Income data frame 
        if sheet_title == "Income" and csv_data.at[index, "Amount"] > 0 or sheet_title != "Income" and csv_data.at[index, "Amount"] < 0:

            excel_sheet.at[index, "Date"] = csv_data.at[index, "Date"]
            excel_sheet.at[index, "Description"] = csv_data.at[index, "Description"]

            # convert negative to positive for expense
            if sheet_title != "Income" and csv_data.at[index, "Amount"] < 0:
                csv_data.at[index, "Amount"] = csv_data.at[index, "Amount"] * (-1)

            # smooth brain couldn't think of another way to do it with the break command
            # essentially I"m prepopulating in Misc. then removing it if I find a match
            if "Misc. without GST" in column_name_list:
                excel_sheet.at[index, "Misc. without GST"] = csv_data.at[index, "Amount"]
                for category_name, category_values in associated_values_dictionary.items():
                    if any(value.casefold() in row[2].casefold() for value in category_values):
                        excel_sheet.at[index, category_name] = csv_data.at[index, "Amount"]
                        excel_sheet.at[index, "Misc. without GST"] = ""

            else:
                excel_sheet.at[index, "Misc."] = csv_data.at[index, "Amount"]
                for category_name, category_values in associated_values_dictionary.items():
                    if any(value.casefold() in row[2].casefold() for value in category_values):
                        excel_sheet.at[index, category_name] = csv_data.at[index, "Amount"]
                        excel_sheet.at[index, "Misc."] = ""

    excel_sheet = excel_sheet.replace(replacement_value, regex=True)

    output_filepath = os.path.join(root_excel_directory, sheet_title + ".csv")

    print(output_filepath)
    excel_sheet.to_csv(output_filepath, index=False)


