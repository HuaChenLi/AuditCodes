import SQLFunctions.sql_excel_columns
from lxml import etree
import os

root = etree.parse("C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\Column Rules\my_account_rules.xml")

auditIDList = [2,3,4]

for auditID in auditIDList:

    if auditID == 2:
        spreadsheet_name = "Business Transaction Account"
    elif auditID == 3:
        spreadsheet_name = "Everyday Offset"
    elif auditID == 4:
        spreadsheet_name = "Mastercard"

    if spreadsheet_name == "Business Transaction Account":
        root = etree.parse(
            "C:/Users/hua-c/Desktop/Coding Stuff/Python Coding/Column Rules/business_transaction_account_col_rules.xml")
    elif spreadsheet_name == "Everyday Offset":
        root = etree.parse(
            "C:/Users/hua-c/Desktop/Coding Stuff/Python Coding/Column Rules/everyday_offset_col_rules.xml")
    elif spreadsheet_name == "Mastercard":
        root = etree.parse("C:/Users/hua-c/Desktop/Coding Stuff/Python Coding/Column Rules/mastercard_col_rules.xml")
    else:
        print(spreadsheet_name)
        print("Have the Column Rules file names been changed or moved?")

    income_col_names = root.findall(".//IncomeColumns//Column")
    expense_col_names = root.findall(".//ExpenseColumns//Column")

    column_name_list = []

    for direction in ["I", "E"]:
        if direction == "I":
            temp_column_names = income_col_names
            isIncome = True
        else:
            temp_column_names = expense_col_names
            isIncome = False

        for index, col_name in enumerate(temp_column_names, 1):
            column_name = col_name.find(".//ColumnName").text
            column_name_list.append(column_name)

            SQLFunctions.sql_excel_columns.insert_excel_column(auditID, column_name, False, True, isIncome)

            temp_values_list = list(col_name.iter("Value"))

            last_column_id = SQLFunctions.sql_excel_columns.select_last_column_id()

            for value in temp_values_list:
                value_exists_in_mapping = False

                CategoryMappingValues = SQLFunctions.sql_excel_columns.select_all_excel_category_mapping()

                existing_category_mapping_id = 0

                for i, r in CategoryMappingValues.iterrows():
                    if r["CategoryValues"] == value.text:
                        existing_category_mapping_id = r["ExcelCategoryMappingID"]
                        value_exists_in_mapping = True

                if value_exists_in_mapping:
                    SQLFunctions.sql_excel_columns.insert_excel_columns_selection(last_column_id, existing_category_mapping_id)
                    print("here")
                else:
                    SQLFunctions.sql_excel_columns.insert_excel_category_mapping(last_column_id, value.text)

