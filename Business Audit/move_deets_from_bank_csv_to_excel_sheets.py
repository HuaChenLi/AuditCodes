import pandas as pd
import os
from lxml import etree

# from CommonLibrary.lib_jank import *
from CommonLibrary.date_libraries import *
from CommonLibrary.getting_replacement_values import replacement_values

# Set the quarter and financial year
quarter = 4

financial_year = "2022 - 2023"

financial_year_folder = financial_year[:4] + " Jul - " + financial_year[7:] + " Jun"

# the number of rows the Excel has. Can edit this in case for some reason, 1000 is not enough
number_of_cells = 1000

auditIDList = [2, 3, 4]

# Month Directory
quarter_folder = os.path.join("C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\Business Audit", financial_year_folder, "Q" + str(quarter) + " " + month_period(quarter))

csv_column_names = ["Date", "Amount", "Description", "Balance"]

for auditID in auditIDList:

    if auditID == 2:
        spreadsheet_name = "Business Transaction Account"
    elif auditID == 3:
        spreadsheet_name = "Everyday Offset"
    elif auditID == 4:
        spreadsheet_name = "Mastercard"

    # The csv_data is where the information is getting replaced. This is technically a bug I think, at least with my code
    # because the code shouldn't be replacing all the values with the Expense mappings
    # The code would need to differentiate the 2 before starting the regex values.
    # There is a question of how costly it is. I think it's feasible to build up the data first, and then
    # do the mappings afterwards.

    csv_data_folder_path = os.path.join(quarter_folder, spreadsheet_name + " CSV\CSVData.csv")
    csv_data = pd.read_csv(csv_data_folder_path, names=csv_column_names, header=None)
    csv_data = csv_data[::-1]

    excel_directory = os.path.join(quarter_folder, spreadsheet_name + " " + month_period(quarter) + " " + year(quarter,
                                                                                                               financial_year) + ".xlsx")

    if spreadsheet_name == "Business Transaction Account":
        root = etree.parse(
            "C:/Users/hua-c/Desktop/Coding Stuff/Python Coding/Business Audit/Column Rules/business_transaction_account_col_rules.xml")
    elif spreadsheet_name == "Everyday Offset":
        root = etree.parse(
            "C:/Users/hua-c/Desktop/Coding Stuff/Python Coding/Business Audit/Column Rules/everyday_offset_col_rules.xml")
    elif spreadsheet_name == "Mastercard":
        root = etree.parse("C:/Users/hua-c/Desktop/Coding Stuff/Python Coding/Business Audit/Column Rules/mastercard_col_rules.xml")
    else:
        print(spreadsheet_name)
        print("Have the Column Rules file names been changed or moved?")

    income_col_names = root.findall(".//IncomeColumns//Column")
    expense_col_names = root.findall(".//ExpenseColumns//Column")

    for sheet_title in ["Income", "Expenditure"]:
        if sheet_title == "Income":
            col_names = income_col_names
            incomeExpenseChar = "I"
        else:
            col_names = expense_col_names
            incomeExpenseChar = "E"

        replacement_values = replacement_values(auditID, incomeExpenseChar)

        associated_values_dictionary = dict()

        column_name_list = ["Date", "Description"]

        for index, col_name in enumerate(col_names, 1):
            column_name = col_name.find(".//ColumnName").text
            column_name_list.append(column_name)

            values_list = []
            associated_values = col_name.findall(".//Values//Value")
            temp_values_list = list(col_name.iter("Value"))

            for value in temp_values_list:
                values_list.append(value.text)

            associated_values_dictionary.setdefault(column_name, values_list)

        excel_sheet = pd.DataFrame(columns=column_name_list)

        # setting the temporary date 2 to be a null string so they guarantee the first row is printed
        temp_date_2 = ""

        for index, row in csv_data.iterrows():

            # creating the Income data frame 
            if sheet_title == "Income" and csv_data.at[index, "Amount"] > 0 or sheet_title != "Income" and csv_data.at[index, "Amount"] < 0:
                # if the date is the same, don't print the values
                temp_date = csv_data.at[index, "Date"]

                if temp_date == temp_date_2:
                    date_inserted = ""
                else:
                    date_inserted = csv_data.at[index, "Date"]
                temp_date_2 = csv_data.at[index, "Date"]

                excel_sheet.at[index, "Date"] = date_inserted
                excel_sheet.at[index, "Description"] = csv_data.at[index, "Description"]

                # convert negative to positive for expense
                if sheet_title != "Income" and csv_data.at[index, "Amount"] < 0:
                    csv_data.at[index, "Amount"] = csv_data.at[index, "Amount"] * (-1)

                # smooth brain couldn't think of another way to do it with the break command
                # essentially I"m pre-populating in Misc. then removing it if I find a match
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

        excel_sheet = excel_sheet.replace(replacement_values, regex=True)

        output_filepath = os.path.join("./", financial_year_folder, "Q" + str(quarter) + " " + month_period(quarter), spreadsheet_name + " CSV", sheet_title + ".csv")

        print(output_filepath)
        excel_sheet.to_csv(output_filepath, index=False)

        # Inserting the Data into the Income and Expense sheets
        with pd.ExcelWriter(excel_directory, mode="a", engine="openpyxl", if_sheet_exists="overlay") as excel_writer:
            if sheet_title == "Income":
                excel_sheet.to_excel(excel_writer, sheet_name=sheet_title, index=False, startcol=0, startrow=3,
                                     header=False)
            else:
                excel_sheet.to_excel(excel_writer, sheet_name=sheet_title, index=False, startcol=0, startrow=3,
                                     header=False)

# Load and playing around with Excel
