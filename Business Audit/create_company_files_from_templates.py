import os
import shutil
import openpyxl as xl

financial_year = '2022-2023'


def month_period(period):
    if period == 1:
        return 'Jul - Sep'
    elif period == 2:
        return 'Oct - Dec'
    elif period == 3:
        return 'Jan - Mar'
    elif period == 4:
        return 'Apr - Jun'


def year(half):
    if half == 1 or half == 2:
        return financial_year[:4]
    else:
        return financial_year[5:]


def beginning_date(period):
    if period == 1:
        return '01-Jul-' + year(period)
    elif period == 2:
        return '01-Oct-' + year(period)
    elif period == 3:
        return '01-Jan-' + year(period)
    elif period == 4:
        return '01-Apr-' + year(period)


def ending_date(period):
    if period == 1:
        return '30-Sep-' + year(period)
    elif period == 2:
        return '31-Dec-' + year(period)
    elif period == 3:
        return '31-Mar-' + year(period)
    elif period == 4:
        return '30-Jun-' + year(period)


quarters_list = [month_period(1), month_period(2), month_period(3), month_period(4)]

# Set the templates
template_bus_transact = 'C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\QX MMM - MMM YYYY\Business Transaction Account MMM - MMM YYYY.xlsx'
template_everyday_offset = 'C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\QX MMM - MMM YYYY\Everyday Offset MMM - MMM YYYY.xlsx'
template_mastercard = 'C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\QX MMM - MMM YYYY\Mastercard MMM - MMM YYYY.xlsx'
template_payg_calc = 'C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\QX MMM - MMM YYYY\PAYG Calculations MMM - MMM YYYY.xlsx'
template_business_expenditure = 'C:\\Users\hua-c\Desktop\Coding Stuff\Python Coding\QX MMM - MMM YYYY\Business Specific Expenditure MMM - MMM YYYY.xlsx'

bank_account_list = ['Business Transaction Account', 'Everyday Offset', 'Mastercard']
taxation_list = ['PAYG Calculations', 'Eckersall Construction Pty Ltd']


def grab_template(excel_list_item):
    if excel_list_item == 'Eckersall Construction Pty Ltd':
        return 'Business Specific Expenditure'
    else:
        return excel_list_item


all_folder_list = bank_account_list + taxation_list

# Creating the folders
year_folder = os.mkdir(financial_year)

for index, quarter in enumerate(quarters_list, start=1):
    # Create the quarter folders
    quarter_folder = os.path.join('./', financial_year, 'Q' + str(index) + ' ' + quarter)
    os.mkdir(quarter_folder)

    # Copying the files
    shutil.copy(template_bus_transact, quarter_folder)
    shutil.copy(template_everyday_offset, quarter_folder)
    shutil.copy(template_mastercard, quarter_folder)
    shutil.copy(template_payg_calc, quarter_folder)
    shutil.copy(template_business_expenditure, quarter_folder)

    for excel_list_item in all_folder_list:
        new_template_location = os.path.join(quarter_folder, grab_template(excel_list_item) + ' MMM - MMM YYYY.xlsx')

        # Creating the file name
        excel_file_name = excel_list_item + ' ' + quarter + ' ' + year(index) + '.xlsx'
        excel_file_location = os.path.join(quarter_folder, excel_file_name)
        os.rename(new_template_location, excel_file_location)

    # Create the directories to dump the CSV files for the Bank Account folders
    for excel_list_item in bank_account_list:
        csv_data_folder_path = os.path.join(quarter_folder, excel_list_item + ' CSV')
        os.mkdir(csv_data_folder_path)

    for excel_list_item in bank_account_list + ['Eckersall Construction Pty Ltd']:
        # I have to redefine since the excel_file_location is for a different for loop that doesn't come here
        excel_file_name = excel_list_item + ' ' + quarter + ' ' + year(index) + '.xlsx'
        excel_file_location = os.path.join(quarter_folder, excel_file_name)
        excel_workbook = xl.load_workbook(excel_file_location)

        # Setting the Front Cover Sheet as the active sheet 
        excel_workbook.active = excel_workbook['Front Cover']
        excel_front_cover_sheet = excel_workbook.active

        # Putting the Dates in the Front Cover
        excel_front_cover_sheet['A2'] = beginning_date(index)
        excel_front_cover_sheet['C2'] = ending_date(index)
        excel_workbook.save(filename=excel_file_location)

    # Trying to put the company name in the merged cell
    eckersall_construction_file = os.path.join(quarter_folder,
                                               'Eckersall Construction Pty Ltd' + ' ' + quarter + ' ' + year(
                                                   index) + '.xlsx')
    eckersall_construction_workbook = xl.load_workbook(eckersall_construction_file)
    eckersall_construction_workbook.active = eckersall_construction_workbook['Front Cover']
    eckersall_construction_front_cover_sheet = eckersall_construction_workbook.active
    eckersall_construction_front_cover_sheet['A1'] = 'Eckersall Construction Pty Ltd'
    eckersall_construction_workbook.save(filename=eckersall_construction_file)
