import SQLFunctions.excel_columns
from lxml import etree

root = etree.parse("C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\Column Rules\my_account_rules.xml")

income_col_names = root.findall(".//IncomeColumns//Column")

column_name_list = []

for index, col_name in enumerate(income_col_names, 1):
    column_name = col_name.find(".//ColumnName").text
    column_name_list.append(column_name)

    SQLFunctions.excel_columns.insert_excel_column(1, column_name, False, True, True)

    temp_values_list = list(col_name.iter("Value"))

    last_column_id = SQLFunctions.excel_columns.select_last_column_id()

    for value in temp_values_list:
        value_exists_in_mapping = False

        CategoryMappingValues = SQLFunctions.excel_columns.select_all_excel_category_mapping()

        existing_category_mapping_id = 0

        for i, r in CategoryMappingValues.iterrows():
            if r["CategoryValues"] == value.text:
                existing_category_mapping_id = r["ExcelCategoryMappingID"]
                value_exists_in_mapping = True

        if value_exists_in_mapping:
            temp_table = SQLFunctions.excel_columns.select_excel_category_mapping_id(existing_category_mapping_id)
            SQLFunctions.excel_columns.insert_excel_columns_selection(last_column_id, existing_category_mapping_id)
            print("here")
        else:
            SQLFunctions.excel_columns.insert_excel_category_mapping(last_column_id, value.text)
