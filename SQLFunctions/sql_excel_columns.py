import pandas as pd

import os
import sys

sys.path.append(os.path.abspath(""))

from SQLFunctions.sql_connection_functions import *

password = get_password()
database = get_database()
connection = get_connection()


def insert_excel_column(audit_id, column_name, is_default, gst_included, is_income):
    query = f"""
    INSERT INTO excel_columns (audit_id, column_name, is_default, gst_included, is_income)
    VALUES ({audit_id}, '{column_name}', {is_default}, {gst_included}, {is_income})
    """
    execute_query(connection, query)


def select_excel_column(audit_id, is_income):
    query = f"""
    SELECT id, column_name FROM excel_columns
    WHERE audit_id = {audit_id} AND is_income = {is_income}
    """
    return pd.DataFrame.from_records(read_query(connection, query),
                                     columns=["id", "column_name"])


def select_default_excel_column(audit_id, is_income):
    query = f"""
    SELECT id, column_name FROM excel_columns
    WHERE audit_id = {audit_id} AND is_income = {is_income} AND is_default = 1
    """
    return read_query(connection, query)


def select_all_excel_category_mapping():
    query = f"""
    SELECT id, category_values FROM excel_category_mapping
    """

    return pd.DataFrame.from_records(read_query(connection, query),
                                     columns=["excel_category_mapping_id", "category_values"])


def select_excel_category_mapping_values(excel_column_id):
    query = f"""
    SELECT excel_column_id, category_values FROM excel_category_mapping
    INNER JOIN excel_column_selection on id = excel_category_mapping_id
    WHERE excel_column_id = {excel_column_id} 
    """

    return pd.DataFrame.from_records(read_query(connection, query),
                                     columns=["excel_column_id", "category_values"])


def insert_excel_columns_selection(excel_column_id, excel_category_mapping_id):
    query = f"""
    INSERT INTO excel_column_selection (excel_column_id, excel_category_mapping_id)
    VALUES ('{excel_column_id}', '{excel_category_mapping_id}')
    """
    execute_query(connection, query)


def insert_excel_category_mapping(excel_column_id, category_value):
    query = f"""
    INSERT INTO excel_category_mapping (category_values)
    VALUES ('{category_value}')
    """
    execute_query(connection, query)

    last_id_value_query = f"""
        SELECT MAX(ID) FROM excel_category_mapping 
        """
    last_id = read_query(connection, last_id_value_query)
    insert_excel_columns_selection(excel_column_id, last_id[0][0])


def select_last_column_id():
    query = f"""
    SELECT Max(excel_column_id) FROM excel_columns 
    """
    last_id = read_query(connection, query)
    return last_id[0][0]
