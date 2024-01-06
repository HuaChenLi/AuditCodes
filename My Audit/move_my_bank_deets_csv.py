# import csv
import os
import shutil
import sys
import time
import glob

from SQLFunctions.select_mappings import *
from CommonLibrary.date_libraries import month_period
import CommonLibrary.build_income_expense_data

sys.path.append(os.path.abspath("CommonLibrary"))

financial_year = "2023 - 2024"

financial_year_folder = financial_year[:4] + " Jul - " + financial_year[7:] + " Jun"

# Also I want to make the process easier for me. It's pretty annoying not knowing which library to run

# the number of rows the Excel has. Can edit this in case for some reason, 1000 is not enough
number_of_cells = 1000

months_list = ["Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun"]
quarters_list = [month_period(1), month_period(2), month_period(3), month_period(4)]
csv_column_names = ["Date", "Amount", "Description", "Balance"]
bank_accounts = ["Mastercard", "Smart Access"]
root_excel_directory = "C:\\Users\\hua-c\\Desktop\\Coding Stuff\\Python Coding\\My Audit\\My_Audit_2024"
csv_data = pd.DataFrame(columns=csv_column_names)

auditID = 1

# archive process
# actually, do I even want to archive the csv. I feel like the csv isn't necessary
# just need to archive the Income.csv

archive_root = os.path.join("/My Audit/My_Audit_2024/Archive")
timestring = time.strftime("%Y%m%d-%H%M%S")
archive_folder = os.path.join(archive_root + "\\" + timestring)

try:
    os.mkdir(archive_folder)
except:
    print("Can't create archive folder")

try:
    shutil.move("/My Audit/My_Audit_2024/Income.csv", archive_folder + "\Income.csv")
except:
    print("No Income file")

try:
    shutil.move("/My Audit/My_Audit_2024/Expenditure.csv", archive_folder + "\Expenditure.csv")
except:
    print("No Income file")

# merge Mastercard and Smart Access transactions csv into 1 file
    

# for account in bank_accounts:
#     temp_directory_path = os.path.join(root_excel_directory, financial_year_folder, account)
#     print(temp_directory_path)
#     csv_data_folder_path = glob.glob(temp_directory_path + '\*.csv')[0]
#     collected_data = pd.read_csv(csv_data_folder_path, names=csv_column_names, header=None)


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
    else:
        is_income = False

    excel_sheet = CommonLibrary.build_income_expense_data.build(auditID, is_income, csv_data)
    output_filepath = os.path.join(root_excel_directory, sheet_title + ".csv")
    print(output_filepath)
    excel_sheet.to_csv(output_filepath, index=False)

