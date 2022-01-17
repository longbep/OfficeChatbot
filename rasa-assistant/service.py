import pymysql.cursors
from typing import List

connection = pymysql.connect(host='localhost',
                             user='root',
                             password='12345678',
                             db='rasa',
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)


def execute(sql):
    try:
        with connection.cursor() as cursor:
            cursor.execute(sql)
            connection.commit()
    finally:
        cursor.close()


def get_list(sql) -> List:
    try:
        with connection.cursor() as cursor:
            cursor.execute(sql)
            return cursor.fetchall()
    finally:
        cursor.close()


def get_record(sql):
    try:
        with connection.cursor() as cursor:
            cursor.execute(sql)
            return cursor.fetchone()
    finally:
        cursor.close()
