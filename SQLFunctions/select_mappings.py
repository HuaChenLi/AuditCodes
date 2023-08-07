import pandas as pd

from SQLFunctions.sql_connection_functions import *

password = get_password()
database = get_database()
connection = get_connection()


def select_mapping_query(auditID, incomeExpenseChar):
    query = f"""
    SELECT id,map_from,map_to FROM mapping_table 
    INNER JOIN mapping_selection ON AuditID = MappingTableID 
    WHERE AuditID = {auditID} AND (IncomeExpense = '{incomeExpenseChar}' OR IncomeExpense = 'B')
    """

    return pd.DataFrame.from_records(read_query(connection, query), columns=["id", "map_from", "map_to"])


def insert_mapping_selection(audit_id, mapping_table_id, income_expense_char):
    query = f"""
    INSERT INTO mapping_selection (AuditID, MappingTableID, IncomeExpense)
    VALUES ({audit_id}, {mapping_table_id}, '{income_expense_char}')    
    """
    execute_query(connection, query)


def insert_mapping_table_and_mapping_selection(map_from, map_to, audit_id, income_expense_char):
    query = f"""
    INSERT INTO mapping_table (map_from, map_to)
    VALUES ('{map_from}', '{map_to}')
    """
    execute_query(connection, query)

    last_id_value_query = f"""
    SELECT MAX(id) FROM mapping_table 
    """
    last_id = read_query(connection, last_id_value_query)
    insert_mapping_selection(audit_id, last_id[0][0], income_expense_char)


