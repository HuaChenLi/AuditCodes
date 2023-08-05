def select_mapping_query(auditID, incomeExpenseChar) : 
    
    return f"""
    SELECT id,map_from,map_to FROM mapping_table 
    INNER JOIN mapping_selection ON AuditID = MappingTableID 
    WHERE AuditID = {auditID} AND (IncomeExpense = '{incomeExpenseChar}' OR IncomeExpense = 'B')
    """
