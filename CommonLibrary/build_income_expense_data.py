import CommonLibrary.getting_replacement_values
import SQLFunctions.sql_excel_columns
import pandas as pd


def build(audit_id, is_income, data_frame):
    if is_income:
        income_expense_char = "I"
    else:
        income_expense_char = "E"

    csv_data = data_frame
    associated_values_dictionary = dict()

    column_dataframe = SQLFunctions.sql_excel_columns.select_excel_column(audit_id=audit_id, is_income=is_income)
    column_name_list = ["Date", "Description"] + list(column_dataframe["ColumnName"])

    for index, row in column_dataframe.iterrows():
        column_name = row["ColumnName"]
        excel_column_id = row["ExcelColumnID"]
        temp_df = SQLFunctions.sql_excel_columns.select_excel_category_mapping_values(excel_column_id)

        values_list = list(temp_df["CategoryValues"])

        associated_values_dictionary.setdefault(column_name, values_list)

    excel_sheet = pd.DataFrame(columns=column_name_list)

    for index, row in csv_data.iterrows():
        # creating the Income and Expense data frame
        if is_income and csv_data.at[index, "Amount"] > 0 or not is_income and csv_data.at[index, "Amount"] < 0:
            excel_sheet.at[index, "Date"] = csv_data.at[index, "Date"]
            excel_sheet.at[index, "Description"] = csv_data.at[index, "Description"]

            try:
                default_column_df = SQLFunctions.sql_excel_columns.select_default_excel_column(audit_id, is_income)
                default_column_name = default_column_df[0][1]
            except Exception:
                default_column_name = column_name_list[2]
            # convert negative to positive for expense
            if not is_income and csv_data.at[index, "Amount"] < 0:
                csv_data.at[index, "Amount"] = csv_data.at[index, "Amount"] * (-1)

            is_categorised = False
            for category_name, category_values in associated_values_dictionary.items():
                if any(value.casefold() in row[2].casefold() for value in category_values):
                    excel_sheet.at[index, category_name] = csv_data.at[index, "Amount"]
                    is_categorised = True
                    break

            if not is_categorised:
                excel_sheet.at[index, default_column_name] = csv_data.at[index, "Amount"]

    rv = CommonLibrary.getting_replacement_values.replacement_values(audit_id, income_expense_char)
    excel_sheet = excel_sheet.replace(rv, regex=True)

    return excel_sheet


