import pandas as pd
import os
import sys

sys.path.append(os.path.abspath(""))

from CommonLibrary.date_libraries import *
import CommonLibrary.build_income_expense_data

# Set the quarter and financial year
quarter = 4

financial_year = "2022 - 2023"

financial_year_folder = financial_year[:4] + " Jul - " + financial_year[7:] + " Jun"

# the number of rows the Excel has. Can edit this in case for some reason, 1000 is not enough
number_of_cells = 1000

auditIDList = [2, 3, 4]

# Month Directory
quarter_folder = os.path.join("Business Audit", financial_year_folder, "Q" + str(quarter) + " " + month_period(quarter))

csv_column_names = ["Date", "Amount", "Description", "Balance"]

for auditID in auditIDList:

    if auditID == 2:
        spreadsheet_name = "Business Transaction Account"
    elif auditID == 3:
        spreadsheet_name = "Everyday Offset"
    elif auditID == 4:
        spreadsheet_name = "Mastercard"

    csv_data_folder_path = os.path.join(quarter_folder, spreadsheet_name + " CSV\CSVData.csv")
    csv_data = pd.read_csv(csv_data_folder_path, names=csv_column_names, header=None)
    csv_data = csv_data[::-1]

    excel_directory = os.path.join(quarter_folder, spreadsheet_name + " " + month_period(quarter) + " " + year(quarter, financial_year) + ".xlsx")

    for sheet_title in ["Income", "Expenditure"]:
        if sheet_title == "Income":
            is_income = True
        else:
            is_income = False

        excel_sheet = CommonLibrary.create_build_income_expense_data.build(auditID, is_income, csv_data)

        output_filepath = os.path.join("./", financial_year_folder, "Q" + str(quarter) + " " + month_period(quarter), spreadsheet_name + " CSV", sheet_title + ".csv")
        excel_sheet.to_csv(output_filepath, index=False)
        print(output_filepath)

        # Inserting the Data into the Income and Expense sheets
        with pd.ExcelWriter(excel_directory, mode="a", engine="openpyxl", if_sheet_exists="overlay") as excel_writer:
            if sheet_title == "Income":
                excel_sheet.to_excel(excel_writer, sheet_name=sheet_title, index=False, startcol=0, startrow=3, header=False)
            else:
                excel_sheet.to_excel(excel_writer, sheet_name=sheet_title, index=False, startcol=0, startrow=3, header=False)
