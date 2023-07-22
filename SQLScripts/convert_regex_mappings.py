# import sql_connection_functions
import sqlite3

con = sqlite3.connect("C:\\sqlite\\MyDataBase")
cur = con.cursor()



query = """
,1,E,'Hero Sushi' 'Hero Sushi'),
,1,E,'Top Notch' 'Top Notch'),
,1,E,'EG GROUP' 'EG Group'),
,1,E,'BELONG' 'Belong'),
,1,E,'Belong' 'Belong'),
,1,E,'MCDONALDS' 'McDonalds'),
,1,E,'GOLD LEAF' 'Gold Leaf'),
,1,E,'CRUNCHYROLL' 'Crunchyroll'),
,1,E,'UBER' 'Uber'),
,1,E,'SPOTIFY' 'Spotify'),
,1,E,'NoodleBox' 'Noodlebox'),
,1,E,'Mighty Moonee Ponds' 'Mighty Moonee Ponds'),
,1,E,'Zeus Street Greek' 'Zeus Street Greek'),
,1,E,'SUPA IGA' 'IGA'),
,1,E,'COLES EXPRESS' 'Coles Express'),
,1,E,'OPTUS' 'Optus'),
,1,E,'EASTLINK' 'Eastlink'),
,1,E,'ENERGYAUSTRALIA' 'EnergyAustralia'),
,1,E,'TANGO' 'Tango Energy'),
,1,E,'JB HI-FI' 'JB Hi-Fi'),
,1,E,'WOOLWORTHS' 'Woolworths'),
,1,E,'MYKI' 'Myki'),
,1,E,'Sir Duke' 'Sir Duke'),
,1,E,'7-ELEVEN' '7-Eleven'),
,1,E,'HUNKY DORY' 'Hunky Dory'),
,1,E,'KMART' 'Kmart'),
,1,E,'CHEM WAREHS' 'Chemist Warehouse'),
,1,E,'CHEMIST WAREHOUSE' 'Chemist Warehouse'),
,1,E,'HEALTHY PETS' 'Healthy Pets'),
,1,E,'ALLISON BROWNING' 'Allison Browning'),
,1,E,'COMMONWEALTH INSURANCE' 'Commonwealth Insurance'),
,1,E,'BUNNINGS' 'Bunnings'),
,1,E,'Yarra Valley Water' 'Yarra Valley Water'),
,1,E,'KFC' 'KFC'),
,1,E,'XERO' 'Xero'),
,1,E,'DR BILGE' 'Dr Bilge'),
,1,E,'SPOTLIGHT' 'Spotlight'),
,1,E,'ALLIANZ' 'Allianz'),
,1,E,'MasterCardPayment' 'Mastercard Payment'),
,1,E,'RACV' 'RACV'),
,1,E,'ALDI' 'ALDI'),
,1,E,'Hungry Jacks' 'Hungry Jacks'),
,1,E,'Home Loan' 'Home Loan'),
,1,E,r'(Loan Repayment' 'Home Loan'),
,1,E,'AIA AUSTRALIA' 'AIA Australia'),
,1,E,'Fast Transfer From Everkeen)': 'Everkeen'),
,1,E,'CBA CR CARD AUTOPAY' 'Mastercard Autopay'),
,1,E,'AUTO PAYMENT - THANK YOU' 'Mastercard Autopay'),
,1,E,'Uniqlo' 'Uniqlo)'
        """
       
# i = 0
# for line in query.splitlines():
#     print("(" + str(i) + line)
#     i = i + 1





query = """
INSERT INTO mapping_table mt (AuditID, MappingTableID, IncomeExpense )
VALUES
(1,1,'E'),
(1,2,'E'),
(1,3,'E'),
(1,4,'E'),
(1,5,'E'),
(1,6,'E'),
(1,7,'E'),
(1,8,'E'),
(1,9,'E'),
(1,10,'E'),
(1,11,'E'),
(1,12,'E'),
(1,13,'E'),
(1,14,'E'),
(1,15,'E'),
(1,16,'E'),
(1,17,'E'),
(1,18,'E'),
(1,19,'E'),
(1,20,'E'),
(1,21,'E'),
(1,22,'E'),
(1,23,'E'),
(1,24,'E'),
(1,25,'E'),
(1,26,'E'),
(1,27,'E'),
(1,28,'E'),
(1,29,'E'),
(1,30,'E'),
(1,31,'E'),
(1,32,'E'),
(1,33,'E'),
(1,34,'E'),
(1,35,'E'),
(1,36,'E'),
(1,37,'E'),
(1,38,'E'),
(1,39,'E'),
(1,40,'E'),
(1,41,'E'),
(1,42,'E'),
(1,43,'E'),
(1,44,'E'),
(1,45,'E'),
(1,46,'E'),
(1,47,'E'),
(1,48,'E'),
(1,49,'E')
        """


res = cur.execute(query)
