import pandas as pd

from SQLFunctions.sql_connection_functions import *

password = get_password()
database = get_database()
connection = get_connection()


def insert_excel_column(audit_id, column_name, is_default, gst_included, is_income):
    query = f"""
    INSERT INTO ExcelColumns (AuditID, ColumnName, IsDefault, GSTIncluded, IsIncome)
    VALUES ({audit_id}, '{column_name}', {is_default}, {gst_included}, {is_income})
    """
    execute_query(connection, query)


def select_all_excel_category_mapping():
    query = f"""
    SELECT ExcelCategoryMappingID, CategoryValues FROM ExcelCategoryMapping
    """

    return pd.DataFrame.from_records(read_query(connection, query),
                                     columns=["ExcelCategoryMappingID", "CategoryValues"])


def select_excel_category_mapping_id(category_values):
    query = f"""
    SELECT ExcelCategoryMappingID FROM ExcelCategoryMapping
    WHERE ExcelCategoryMappingID = '{category_values}' 
    """

    return pd.DataFrame.from_records(read_query(connection, query))


def insert_excel_columns_selection(excel_column_id, excel_category_mapping_id):
    query = f"""
    INSERT INTO ExcelColumnSelection (ExcelColumnID, ExcelCategoryMappingID)
    VALUES ('{excel_column_id}', '{excel_category_mapping_id}')
    """
    execute_query(connection, query)


# This function is wrong. I need to think through how to update it to be correct
def insert_excel_category_mapping(excel_column_id, category_value):
    query = f"""
    INSERT INTO ExcelCategoryMapping (CategoryValues)
    VALUES ('{category_value}')
    """
    execute_query(connection, query)

    last_id_value_query = f"""
        SELECT MAX(ExcelCategoryMappingID) FROM ExcelCategoryMapping 
        """
    last_id = read_query(connection, last_id_value_query)
    insert_excel_columns_selection(excel_column_id, last_id[0][0])


def select_last_column_id():
    query = f"""
    SELECT Max(ExpenseColumnID) FROM ExcelColumns 
    """
    last_id = read_query(connection, query)
    return last_id[0][0]
