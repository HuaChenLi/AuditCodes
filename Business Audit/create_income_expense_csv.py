import pandas as pd
import os
import sys

sys.path.append(os.path.abspath(""))

from CommonLibrary.date_libraries import *
import CommonLibrary.build_income_expense_data

# Set the quarter and financial year
# quarter = int(sys.argv[2])
auditID = int(sys.argv[1])
spreadsheet_name = sys.argv[2]
year_end = int(sys.argv[3])
csvSheets = sys.argv[4].split("/")

financial_year = year_end
financial_year_folder = year_end

# the number of rows the Excel has. Can edit this in case for some reason, 1000 is not enough
number_of_cells = 1000

# Month Directory
quarter_folder = os.path.join('./', str(financial_year_folder))

csv_column_names = ["Date", "Amount", "Description", "Balance"]



csv_data_folder_path = os.path.join(quarter_folder, spreadsheet_name + " CSV\\CSVData.csv")
csv_data = pd.read_csv(csv_data_folder_path, names=csv_column_names, header=None)
csv_data = csv_data[::-1]

excel_directory = os.path.join(quarter_folder, spreadsheet_name + ".xlsx")

for sheet_title in ["Income", "Expenditure"]:
    if sheet_title == "Income":
        is_income = True
    else:
        is_income = False

    excel_sheet = CommonLibrary.build_income_expense_data.build(auditID, is_income, csv_data)

    output_filepath = os.path.join("./", financial_year_folder, spreadsheet_name + " CSV", sheet_title + ".csv")
    excel_sheet.to_csv(output_filepath, index=False)
    print(output_filepath)

    # Inserting the Data into the Income and Expense sheets
    with pd.ExcelWriter(excel_directory, mode="a", engine="openpyxl", if_sheet_exists="overlay") as excel_writer:
        if sheet_title == "Income":
            excel_sheet.to_excel(excel_writer, sheet_name=sheet_title, index=False, startcol=0, startrow=3, header=False)
        else:
            excel_sheet.to_excel(excel_writer, sheet_name=sheet_title, index=False, startcol=0, startrow=3, header=False)
