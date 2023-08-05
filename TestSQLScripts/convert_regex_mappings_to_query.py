from SQLFunctions.select_mappings import select_mapping_query, insert_mapping_selection, insert_mapping_table_and_mapping_selection
from SQLFunctions.sql_connection_functions import *

password = "Bdvej746Js$2jd"
database = "MyDataBase"

connection = create_server_connection("localhost", "root", password)
connection = create_db_connection("localhost", "root", password, database)

business_list = [
    ["Hero Sushi", "Hero Sushi"],
    ["Top Notch", "Top Notch"],
    ["EG GROUP", "EG Group"],
    ["BELONG", "Belong"],
    ["Belong", "Belong"],
    ["MCDONALDS", "McDonalds"],
    ["GOLD LEAF", "Gold Leaf"],
    ["CRUNCHYROLL", "Crunchyroll"],
    ["UBER", "Uber"],
    ["SPOTIFY", "Spotify"],
    ["NoodleBox", "Noodlebox"],
    ["Mighty Moonee Ponds", "Mighty Moonee Ponds"],
    ["Zeus Street Greek", "Zeus Street Greek"],
    ["SUPA IGA", "IGA"],
    ["COLES EXPRESS", "Coles Express"],
    ["OPTUS", "Optus"],
    ["EASTLINK", "Eastlink"],
    ["ENERGYAUSTRALIA", "EnergyAustralia"],
    ["TANGO", "Tango Energy"],
    ["JB HI-FI", "JB Hi-Fi"],
    ["WOOLWORTHS", "Woolworths"],
    ["MYKI", "Myki"],
    ["Sir Duke", "Sir Duke"],
    ["7-ELEVEN", "7-Eleven"],
    ["HUNKY DORY", "Hunky Dory"],
    ["KMART", "Kmart"],
    ["CHEM WAREHS", "Chemist Warehouse"],
    ["CHEMIST WAREHOUSE", "Chemist Warehouse"],
    ["HEALTHY PETS", "Healthy Pets"],
    ["ALLISON BROWNING", "Allison Browning"],
    ["COMMONWEALTH INSURANCE", "Commonwealth Insurance"],
    ["BUNNINGS", "Bunnings"],
    ["Yarra Valley Water", "Yarra Valley Water"],
    ["KFC", "KFC"],
    ["XERO", "Xero"],
    ["DR BILGE", "Dr Bilge"],
    ["SPOTLIGHT", "Spotlight"],
    ["ALLIANZ", "Allianz"],
    ["MasterCardPayment", "Mastercard Payment"],
    ["RACV", "RACV"],
    ["ALDI", "ALDI"],
    ["Hungry Jacks", "Hungry Jacks"],
    ["Home Loan", "Home Loan"],
    ["Loan Repayment", "Home Loan"],
    ["AIA AUSTRALIA", "AIA Australia"],
    ["Fast Transfer From Everkeen", "Everkeen"]
]


# This is interesting, so turns out my thing had the same list, but with more stuff, so only the first set of SQLs did anything
# I can still use this chunk I think, since for the function that adds the mapping, I would need to do this check
# Because I don't want duplicate values

sql_df = select_mapping_query(1, "E")

for business_row in business_list:
    mappingAlreadyExists = False
    for index, mapping_row in sql_df.iterrows():
        if business_row[0] == mapping_row["map_from"] and business_row[1] == mapping_row["map_to"]:
            insert_mapping_selection(auditID=2, mappingTableID=index + 1, incomeExpenseChar="E")
            insert_mapping_selection(auditID=3, mappingTableID=index + 1, incomeExpenseChar="E")
            insert_mapping_selection(auditID=4, mappingTableID=index + 1, incomeExpenseChar="E")
            mappingAlreadyExists = True

    if not mappingAlreadyExists:
        insert_mapping_table_and_mapping_selection(business_row[0], business_row[1], 2, "E")
        insert_mapping_table_and_mapping_selection(business_row[0], business_row[1], 3, "E")
        insert_mapping_table_and_mapping_selection(business_row[0], business_row[1], 4, "E")

