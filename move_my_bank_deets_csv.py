# import csv
import os
import sys
import pandas as pd
from lxml import etree
from lib_jank import *
from sql_connection_functions import *

sys.path.append(os.path.abspath('lib_jank_folder'))

financial_year = '2022 - 2023'

financial_year_folder = financial_year[:4] + ' Jul - ' + financial_year[7:] + ' Jun'

# feels like a mess whenever I open up the scripts. I usually have no idea what to actually do to run the reports, and I'm scrambling all the time. I want to add an archive feature. Basically, it will save and store the files in an archive folder.
# My bank account being gone from Commbank is a little annoying. I'm questioning why an extra 3.8K was deducted from my bank account. Even if they closed my Credit Card account, they should have still kept the bank account visible with my past statements.
# Also I want to make the process easier for me. It's pretty annoying not knowing which library to run

# the number of rows the Excel has. Can edit this in case for some reason, 1000 is not enough
number_of_cells = 1000

months_list = ['Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun']

quarters_list = [month_period(1), month_period(2), month_period(3), month_period(4)]

csv_column_names = ['Date', 'Amount', 'Description', 'Balance']

auditID = 1

replacements_values = {
    'Description': {
        r'\b[0-9]+\b\/\b[0-9]+\b\/\b[0-9]+\b': '',  # dates
        r'\*': '',  # remove asterisks
        'Value Date:': '',
        'xx3002': '',
        'xx0453': '',
        r'(AU|VI)*(\s)*AUS CARD': '',
        'PAYPAL': '',
        r'(\s){2,}': '',  # multiple white spaces
        r'^\s': '', # white space at start of description



        r'(.*)(Hero Sushi)(.*)*': 'Hero Sushi',
        r'(.*)(Top Notch)(.*)*': 'Top Notch',
        r'(.*)(EG GROUP)(.*)*': 'EG Group',
        r'(.*)(BELONG)(.*)*': 'Belong',
        r'(.*)(Belong)(.*)*': 'Belong',
        r'(.*)(MCDONALDS)(.*)*': 'McDonalds',
        r'(.*)(GOLD LEAF)(.*)*': 'Gold Leaf',
        r'(.*)(CRUNCHYROLL)(.*)*': 'Crunchyroll',
        r'(.*)(UBER)(.*)*': 'Uber',
        r'(.*)(SPOTIFY)(.*)*': 'Spotify',
        r'(.*)(NoodleBox)(.*)*': 'Noodlebox',
        r'(.*)(Mighty Moonee Ponds)(.*)*': 'Mighty Moonee Ponds',
        r'(.*)(Zeus Street Greek)(.*)*': 'Zeus Street Greek',
        r'(.*)(SUPA IGA)(.*)*': 'IGA',
        r'(.*)(COLES EXPRESS)(.*)*': 'Coles Express',
        r'(.*)(OPTUS)(.*)*': 'Optus',
        r'(.*)(EASTLINK)(.*)*': 'Eastlink',
        r'(.*)(ENERGYAUSTRALIA)(.*)*': 'EnergyAustralia',
        r'(.*)(TANGO)(.*)*': 'Tango Energy',
        r'(.*)(JB HI(-|\s)*FI)(.*)*': 'JB Hi-Fi',
        r'(.*)(WOOLWORTHS)(.*)*': 'Woolworths',
        r'(.*)(MYKI)(.*)*': 'Myki',
        r'(.*)(Sir Duke)(.*)*': 'Sir Duke',
        r'(.*)(7-ELEVEN)(.*)*': '7-Eleven',
        r'(.*)(HUNKY DORY)(.*)*': 'Hunky Dory',
        r'(.*)(KMART)(.*)*': 'Kmart',
        r'(.*)(CHEM WAREHS)(.*)*': 'Chemist Warehouse',
        r'(.*)(CHEMIST WAREHOUSE)(.*)*': 'Chemist Warehouse',
        r'(.*)(HEALTHY PETS)(.*)*': 'Healthy Pets',
        r'(.*)(ALLISON BROWNING)(.*)*': 'Allison Browning',
        r'(.*)(COMMONWEALTH INSURANCE)(.*)*': 'Commonwealth Insurance',
        r'(.*)(BUNNINGS)(.*)*': 'Bunnings',
        r'(.*)(Yarra Valley Water)(.*)*': 'Yarra Valley Water',
        r'(.*)(KFC)(.*)*': 'KFC',
        r'(.*)(XERO)(.*)*': 'Xero',
        r'(.*)(DR BILGE)(.*)*': 'Dr Bilge',
        r'(.*)(SPOTLIGHT)(.*)*': 'Spotlight',
        r'(.*)(ALLIANZ)(.*)*': 'Allianz',
        r'(.*)(MasterCardPayment)(.*)*': 'Mastercard Payment',
        r'(.*)(RACV)(.*)*': 'RACV',
        r'(.*)(ALDI)(.*)*': 'ALDI',
        r'(.*)(Hungry Jacks)(.*)*': 'Hungry Jacks',
        r'(.*)(Home Loan)(.*)*': 'Home Loan',
        r'(Loan Repayment)(.*)*': 'Home Loan',
        r'(.*)(AIA AUSTRALIA)(.*)*': 'AIA Australia',
        r'(.*)(Fast Transfer From Everkeen)': 'Everkeen',
        r'(.*)(CBA CR CARD AUTOPAY)(.*)*': 'Mastercard Autopay',
        r'(.*)(AUTO PAYMENT - THANK YOU)(.*)*': 'Mastercard Autopay',
        r'(.*)(Uniqlo)(.*)*': 'Uniqlo'
        }
    } 



password = "Bdvej746Js$2jd"
database = "MyDataBase"

connection = create_server_connection("localhost", "root", password)
connection = create_db_connection("localhost", "root", password, database)

query = """
SELECT map_from,map_to FROM mapping_table 
INNER JOIN mapping_selection ON AuditID = MappingTableID 
WHERE AuditID = 1 AND (IncomeExpense = 'E' OR IncomeExpense = 'B')
"""

data_sql_1 = read_query(connection,query)

print(data_sql_1[1][1])
print(type(data_sql_1))




root = etree.parse('C:/Users/hua-c/Desktop/Coding Stuff/Python Coding/Column Rules/my_account_rules.xml')
income_col_names = root.findall('.//IncomeColumns//Column')
expense_col_names = root.findall('.//ExpenseColumns//Column')

root_excel_directory = "C:\\Users\\hua-c\\Desktop\\Coding Stuff\\Python Coding\\My Audit\\My_Audit_2022"

bank_accounts = ['Mastercard', 'Smart Access']

csv_data = pd.DataFrame(columns=csv_column_names)

# merge Mastercard and Smart Access transactions csv into 1 file
for account in bank_accounts:
    csv_data_folder_path = os.path.join(root_excel_directory, account, 'CSVData.csv')
    collected_data = pd.read_csv(csv_data_folder_path, names=csv_column_names, header=None)
    collected_data = collected_data.replace(replacements_values, regex=True)
    csv_data = csv_data.merge(collected_data, how='outer')

csv_data['Date'] = pd.to_datetime(csv_data['Date'], format='%d/%m/%Y')
csv_data.sort_values(by='Date', inplace=True)

for sheet_title in ['Income', 'Expenditure']:
    if sheet_title == 'Income':
        col_names = income_col_names
    else:
        col_names = expense_col_names

    associated_values_dictionary = dict()

    column_name_list = ['Date', 'Description']

    for index, col_name in enumerate(col_names, 1):
        column_name = col_name.find('.//ColumnName').text
        column_name_list.append(column_name)

        values_list = []
        associated_values = col_name.findall('.//Values//Value')
        temp_values_list = list(col_name.iter('Value'))

        for value in temp_values_list:
            values_list.append(value.text)

        associated_values_dictionary.setdefault(column_name, values_list)

    excel_sheet = pd.DataFrame(columns=column_name_list)

    for index, row in csv_data.iterrows():

        # creating the Income data frame 
        if sheet_title == 'Income' and csv_data.at[index, 'Amount'] > 0 or sheet_title != 'Income' and csv_data.at[index, 'Amount'] < 0:

            excel_sheet.at[index, 'Date'] = csv_data.at[index, 'Date']
            excel_sheet.at[index, 'Description'] = csv_data.at[index, 'Description']

            # convert negative to posistive for expense
            if sheet_title != 'Income' and csv_data.at[index, 'Amount'] < 0:
                csv_data.at[index, 'Amount'] = csv_data.at[index, 'Amount'] * (-1)

            # smooth brain couldn't think of another way to do it with the break command
            # essentially I'm prepopulating in Misc. then removing it if I find a match
            if 'Misc. without GST' in column_name_list:
                excel_sheet.at[index, 'Misc. without GST'] = csv_data.at[index, 'Amount']
                for category_name, category_values in associated_values_dictionary.items():
                    if any(value.casefold() in row[2].casefold() for value in category_values):
                        excel_sheet.at[index, category_name] = csv_data.at[index, 'Amount']
                        excel_sheet.at[index, 'Misc. without GST'] = ''

            else:
                excel_sheet.at[index, 'Misc.'] = csv_data.at[index, 'Amount']
                for category_name, category_values in associated_values_dictionary.items():
                    if any(value.casefold() in row[2].casefold() for value in category_values):
                        excel_sheet.at[index, category_name] = csv_data.at[index, 'Amount']
                        excel_sheet.at[index, 'Misc.'] = ''

    output_filepath = os.path.join(root_excel_directory, sheet_title + '.csv')

    print(output_filepath)
    excel_sheet.to_csv(output_filepath, index=False)
