import pandas as pd

from SQLFunctions.sql_connection_functions import *

password = "Bdvej746Js$2jd"
database = "MyDataBase"

connection = create_db_connection("localhost", "root", password, database)


def select_mapping_query(auditID, incomeExpenseChar):
    query = f"""
    SELECT id,map_from,map_to FROM mapping_table 
    INNER JOIN mapping_selection ON AuditID = MappingTableID 
    WHERE AuditID = {auditID} AND (IncomeExpense = '{incomeExpenseChar}' OR IncomeExpense = 'B')
    """

    return pd.DataFrame.from_records(read_query(connection, query), columns=["id", "map_from", "map_to"])

