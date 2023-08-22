def month_period(index):
    if index == 1:
        return 'Jul - Sep'
    elif index == 2:
        return 'Oct - Dec'
    elif index == 3:
        return 'Jan - Mar'
    elif index == 4:
        return 'Apr - Jun'


def year(index, financial_year):
    if index == 1 or index == 2:
        return financial_year[:4]
    else:
        return financial_year[7:]


def beginning_date(index, financial_year):
    if index == 1:
        return '01 Jul ' + year(index, financial_year)
    elif index == 2:
        return '01 Oct ' + year(index, financial_year)
    elif index == 3:
        return '01 Jan ' + year(index, financial_year)
    elif index == 4:
        return '01 Apr ' + year(index, financial_year)


def ending_date(index, financial_year):
    if index == 1:
        return '30 Sep ' + year(index, financial_year)
    elif index == 2:
        return '31 Dec ' + year(index, financial_year)
    elif index == 3:
        return '31 Mar ' + year(index, financial_year)
    elif index == 4:
        return '30 Jun ' + year(index, financial_year)


