from SQLFunctions.select_mappings import select_mapping_query


def replacement_values(audit_id, income_expense_char):
    rep_values = {
        "Description": {
            r"Value Date:\s\b[0-9]+\b\/\b[0-9]+\b\/\b[0-9]+\b$": "",  # dates
            r"\*": "",  # remove asterisks
            r"(\s){2,}": "",  # multiple white spaces
            r"^\s": ""}  # white space at start of description
    }

    data_sql_1 = select_mapping_query(audit_id, income_expense_char)

    for i, r in data_sql_1.iterrows():
        mappedFromValue = r["map_from"]
        mappedToValue = r["map_to"]

        keyValue = r"(.*)" + mappedFromValue + "(.*)*"

        newMapping = {keyValue: mappedToValue}

        rep_values["Description"].update(newMapping)

    return rep_values
