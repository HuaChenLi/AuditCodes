# import csv
import os
import shutil
import sys
import time
import glob

sys.path.append(os.path.abspath("")) # This allows the root libraries to come to be imported below

from SQLFunctions.select_mappings import *
from CommonLibrary.date_libraries import month_period
import CommonLibrary.build_income_expense_data

auditID = 1
actual_year = '2023'

actual_year_folder = actual_year

# Also I want to make the process easier for me. It's pretty annoying not knowing which library to run

# the number of rows the Excel has. Can edit this in case for some reason, 1000 is not enough
number_of_cells = 1000

months_list = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
quarters_list = [month_period(1), month_period(2), month_period(3), month_period(4)]
csv_column_names = ["Date", "Amount", "Description", "Balance"]
bank_accounts = ["Mastercard", "Smart Access"]

root_excel_directory = 'My Audit'

my_audit_folder = os.path.join(root_excel_directory, actual_year_folder)
csv_data = pd.DataFrame(columns=csv_column_names)


# archive process
# actually, do I even want to archive the csv. I feel like the csv isn't necessary
# just need to archive the Income.csv

archive_root = os.path.join(root_excel_directory, "Archive")
try:
    os.mkdir(archive_root)
except:
    print("Can't create archive root")

timestring = time.strftime("%Y%m%d-%H%M%S")
archive_folder = os.path.join(archive_root, actual_year + '-' + timestring)

try:
    os.mkdir(archive_folder)
except:
    print("Can't create archive folder")

try:
    income_csv = os.path.join(root_excel_directory, "Income.csv")
    shutil.move(income_csv, archive_folder + "\Income.csv")
except:
    print("No Income file")

try:
    expenditure_csv = os.path.join(root_excel_directory, "Expenditure.csv")
    shutil.move(expenditure_csv, archive_folder + "\Expenditure.csv")
except:
    print("No Expense file")

# merge Mastercard and Smart Access transactions csv into 1 file
for account in bank_accounts:
    temp_directory_path = os.path.join(my_audit_folder, account)
    print(temp_directory_path)
    csv_data_folder_path = glob.glob(temp_directory_path + '\*.csv')[0]
    collected_data = pd.read_csv(csv_data_folder_path, names=csv_column_names, header=None)
    csv_data = csv_data.merge(collected_data, how="outer")


csv_data["Date"] = pd.to_datetime(csv_data["Date"], format="%d/%m/%Y")
csv_data.sort_values(by="Date", inplace=True)

for sheet_title in ["Income", "Expenditure"]:
    if sheet_title == "Income":
        is_income = True
    else:
        is_income = False

    excel_sheet = CommonLibrary.build_income_expense_data.build(auditID, is_income, csv_data)
    output_filepath = os.path.join(my_audit_folder, sheet_title + ".csv")
    print(output_filepath)
    excel_sheet.to_csv(output_filepath, index=False)

