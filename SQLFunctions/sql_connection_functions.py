import sqlite3
import sys
import os

sys.path.append(os.path.abspath(""))

with open("ServerLogin\\database") as file:
    lines = file.readlines()
    host = lines[0].strip()
    username = lines[2].strip()
    password = lines[3].strip()
    database = lines[4].strip()


def create_server_connection(host_name, user_name, user_password):
    connection_1 = None
    try:
        connection_1 = sqlite3.connect(
            host=host_name,
            user=user_name,
            passwd=user_password
        )
        print("MySQL Database connection successful")
    except Error as err:
        print(f"Error: '{err}'")

    return connection_1


def create_database(connection_1, query):
    cursor = connection_1.cursor()
    try:
        cursor.execute(query)
        print("Database created successfully")
    except Error as err:
        print(f"Error: '{err}'")


def create_db_connection():
    connection_1 = None
    try:
        connection_1 = sqlite3.connect("audit.db")
        print("MySQL Database connection successful")
    except Error as err:
        print(f"Error: '{err}'")

    return connection_1


def execute_query(connection_1, query):
    cursor = connection_1.cursor()
    try:
        cursor.execute(query)
        connection_1.commit()
        print("Query successful")
    except Error as err:
        print(f"Error: '{err}'")


def read_query(connection_1, query):
    cursor = connection_1.cursor()
    result = None
    try:
        cursor.execute(query)
        result = cursor.fetchall()
        return result
    except Error as err:
        print(f"Error: '{err}'")


connection = create_db_connection()


def get_password():
    return password


def get_database():
    return database


def get_connection():
    return connection