import pandas as pd

import os
import sys

sys.path.append(os.path.abspath(""))

from SQLFunctions.sql_connection_functions import *

password = get_password()
database = get_database()
connection = get_connection()


def select_mapping_query(auditID, incomeExpenseChar):
    query = f"""
    SELECT id, map_from, map_to FROM mapping_table 
    INNER JOIN mapping_selection ON id = mapping_table_id 
    WHERE audit_id = {auditID} AND (income_expense = '{incomeExpenseChar}' OR income_expense = 'B')
    """

    return pd.DataFrame.from_records(read_query(connection, query), columns=["id", "map_from", "map_to"])
