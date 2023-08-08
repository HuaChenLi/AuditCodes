import re
import xlsxwriter
import pandas as pd


column_names = ['Date','Amount','Description','Balance']
csv_data                            = pd.read_csv('./My_Audit_2022\CSVData.csv', names=column_names ,header=None)

replacements_values                 = {
    'Description': {
        r'\b[0-9]+\b\/\b[0-9]+\b\/\b[0-9]+\b'           :'',                                    # dates
        r'(.*)(Hero Sushi)(.*)*'                        :'Hero Sushi',
        r'(.*)(Top Notch)(.*)*'                         :'Top Notch',
        r'(.*)(EG GROUP)(.*)*'                          :'EG Group',
        r'(.*)(BELONG)(.*)*'                            :'Belong',
        r'(.*)(MCDONALDS)(.*)*'                         :'McDonalds',
        r'(.*)(GOLD LEAF)(.*)*'                         :'Gold Leaf',
        r'(.*)(CRUNCHYROLL)(.*)*'                       :'Crunchyroll',
        r'(.*)(UBER)(.*)*'                              :'Uber',
        r'(.*)(SPOTIFY)(.*)*'                           :'Spotify',
        r'(.*)(NoodleBox)(.*)*'                         :'Noodlebox',
        r'(.*)(Mighty Moonee Ponds)(.*)*'               :'Mighty Moonee Ponds',
        r'(.*)(Zeus Street Greek)(.*)*'                 :'Zeus Street Greek',
        r'(.*)(SUPA IGA)(.*)*'                          :'IGA',
        r'(.*)(COLES EXPRESS)(.*)*'                     :'Coles Express',
        r'(.*)(OPTUS)(.*)*'                             :'Optus',
        r'(.*)(EASTLINK)(.*)*'                          :'Eastlink',
        r'(.*)(ENERGYAUSTRALIA)(.*)*'                   :'EnergyAustralia',
        r'(.*)(TANGO)(.*)*'                             :'Tango Energy',
        r'(.*)(JB HI(-|\s)*FI)(.*)*'                    :'JB Hi-Fi',
        r'(.*)(WOOLWORTHS)(.*)*'                        :'Woolworths',
        r'(.*)(MYKI)(.*)*'                              :'Myki',
        r'(.*)(Sir Duke)(.*)*'                          :'Sir Duke',
        r'(.*)(7-ELEVEN)(.*)*'                          :'7-Eleven',
        r'(.*)(HUNKY DORY)(.*)*'                        :'Hunky Dory',
        r'(.*)(KMART)(.*)*'                             :'Kmart',
        r'(.*)(CHEM WAREHS)(.*)*'                       :'Chemist Warehouse',
        r'(.*)(HEALTHY PETS)(.*)*'                      :'Healthy Pets',
        r'(.*)(ALLISON BROWNING)(.*)*'                  :'Allison Browning',
        r'(.*)(COMMONWEALTH INSURANCE)(.*)*'            :'Commonwealth Insurance',
        r'(.*)(BUNNINGS)(.*)*'                          :'Bunnings',
        r'(.*)(Yarra Valley Water)(.*)*'                :'Yarra Valley Water',
        r'(.*)(KFC)(.*)*'                               :'KFC',
        r'(.*)(XERO)(.*)*'                              :'Xero',
        r'(.*)(DR BILGE)(.*)*'                          :'Dr Bilge',
        r'\*'                                           :'',                                    # remove asterisks
        'Value Date:'                                   :'',
        'xx3002'                                        :'',
        'xx0453'                                        :'',
        r'(AU|VI)*(\s)*AUS CARD'                        :'',
        'PAYPAL'                                        :'',
        r'(\s){2,}'                                     :'',                                    # multiple white spaces
        r'^\s'                                          :''}                                    # white space at start of description
        }

csv_data             = csv_data.replace(replacements_values, regex=True)

# list the values in the income category 
wages_values                = ["Salary"]
pay_me_back_values          = ['Damien' , 'Zhijie']
tax_refund_values           = ['ATO']
income_transfers_values     = ['xx6079' , 'QiQiong' , 'Zhi Kang']

# list the values in the expense category
with_friends_values         = ['UBER', 'MANGO', 'LINDOS', 'ZJ', 'Bday' ,'Truman' , 'Zhijie' , 'Universal' , 'SIR DUKE']
takeaway_for_work_values    = ['HERO SUSHI' , 'MIGHTY' , 'MCDONALDS' , 'ZEUS' , 'NOODLEBOX' , 'TOP NOTCH' , 'HUNKY DORY']
groceries_values            = ['WOOLWORTHS' , 'COLES' , 'CHEM WAREHS' , 'SUPA IGA' , 'KMART']
games_values                = ['NINTENDO']
subscriptions_values        = ['SPOTIFY' , 'CRUNCHYROLL' , 'BELONG' ,'OPTUS' , 'TANGO ENERGY', 'ENERGYAUSTRALIA']
transport_values            = ['EG GROUP' , 'MYKI' , 'TOYOTA' , 'EASTLINK' , 'PAYSTAY' , 'VICROADS' , '7-ELEVEN']
tech_values                 = ['SCORPIONTEC' , 'OFFICEWORKS' , 'DAVIECLOTHI' , 'KOGAN' , 'DICKSMITH' , 'JB HI-FI']
expense_transfers_values    = ['xx6079']
business_values             = ['BUSINESS TRANS' , 'EVERYDAY OFFSET' ,  'ASIC' , 'REPCO']
education_values            = ['PEGS' , 'CAMPION']
family_values               = ['GOLD LEAF' , 'ARLBERG' , 'MOUNT BULLER' , 'SKI HIRE' , 'EUROA' , 'FAMILY DNTL' , 'CENTRELINK']


income_sheet                = pd.DataFrame({'Date':[],
                            'Description':[],
                            'Misc.':[],
                            'Wages':[],
                            'Pay me Back':[],
                            'Tax Refund':[],
                            'Transfers':[]})

expense_sheet               = pd.DataFrame({'Date':[],
                            'Description':[],
                            'With Friends':[],
                            'Takeaway for Work':[],
                            'Groceries':[],
                            'Games and Merch':[],
                            'Subscriptions':[],
                            'Transport':[],
                            'Tech and Clothes':[],
                            'Transfers':[],
                            'Business':[],
                            'Education':[],
                            'Family':[],
                            'Misc.':[]})


# setting the temporary income and expense date 2 to be a null string so they guarantee the first row is printed
temp_income_date_2          = ''
temp_expense_date_2         = ''

for index, row in csv_data.iterrows():
    # print(row)

    # creating the Income data frame 
    if  csv_data.at[index ,'Amount'] > 0:


        # if the date is the same, don't print the values
        temp_income_date    = csv_data.at[index, 'Date']

        if temp_income_date == temp_income_date_2:
            date_inserted   = ''
        else:
            date_inserted   = csv_data.at[index, 'Date']
        temp_income_date_2  = csv_data.at[index, 'Date']  
        
        income_sheet.at[index,'Date']          = date_inserted
        income_sheet.at[index,'Description']   = csv_data.at[index,'Description']


        if any(value.casefold() in row[2].casefold() for value in wages_values):
            income_sheet.at[index,'Wages']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in pay_me_back_values):
            income_sheet.at[index,'Pay me Back']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in tax_refund_values):
            income_sheet.at[index,'Tax Refund']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in income_transfers_values):
            income_sheet.at[index,'Transfers']   = csv_data.at[index,'Amount']
        else:
            income_sheet.at[index,'Misc.']   = csv_data.at[index,'Amount']


    # creating the Expense data frame
    if  csv_data.at[index ,'Amount'] < 0:


        # if the date is the same, don't print the values
        temp_expense_date    = csv_data.at[index, 'Date']

        if temp_expense_date == temp_expense_date_2:
            date_inserted   = ''
        else:
            date_inserted   = csv_data.at[index, 'Date']
        temp_expense_date_2  = csv_data.at[index, 'Date']  
        
        expense_sheet.at[index,'Date']          = date_inserted
        expense_sheet.at[index,'Description']   = csv_data.at[index,'Description']


        if any(value.casefold() in row[2].casefold() for value in with_friends_values):
            expense_sheet.at[index,'With Friends']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in takeaway_for_work_values):
            expense_sheet.at[index,'Takeaway for Work']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in groceries_values):
            expense_sheet.at[index,'Groceries']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in games_values):
            expense_sheet.at[index,'Games and Merch']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in subscriptions_values):
            expense_sheet.at[index,'Subscriptions']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in transport_values):
            expense_sheet.at[index,'Transport']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in tech_values):
            expense_sheet.at[index,'Tech and Clothes']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in expense_transfers_values):
            expense_sheet.at[index,'Transfers']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in business_values):
            expense_sheet.at[index,'Business']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in education_values):
            expense_sheet.at[index,'Education']   = csv_data.at[index,'Amount']
        elif any(value.casefold() in row[2].casefold() for value in family_values):
            expense_sheet.at[index,'Family']   = csv_data.at[index,'Amount']
        else:
            expense_sheet.at[index,'Misc.']   = csv_data.at[index,'Amount']




sheets                              = {'Income':income_sheet, 'Expense':expense_sheet}

writer                              = pd.ExcelWriter('./My_Audit_2022\My Account Jul 2022 - Jun 2023.xlsx')


for sheet_name in sheets.keys():
    sheets[sheet_name].to_excel(writer, sheet_name=sheet_name, index=False)

writer.save()


my_audit_excel                      = pd.read_excel('./My_Audit_2022\My Account Jul 2022 - Jun 2023.xlsx')
my_audit_excel.head()               

# playing around with XLSX

#workbook        = xlsxwriter.Workbook('My_Audit_2022\My Account Jul 2022 - Jun 2023.xlsx')
#worksheet       = workbook.add_worksheet()
#worksheet.write('C4','Something')

#workbook.close()


