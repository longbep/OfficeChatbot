# This files contains your custom actions which can be used to run
# custom Python code.
#
# See this guide on how to implement these action:
# https://rasa.com/docs/rasa/core/actions/#custom-actions/


# This is a simple example for a custom action which utters "Hello World!"

from typing import Any, Text, Dict, List

from rasa_sdk import Action, Tracker
from datetime import datetime
from rasa_sdk.events import SlotSet
from rasa.core.events import AllSlotsReset

import service
db = "rasa"


def get_list(elements, val) -> Text:
    string = ""
    for ele in elements:
        string += "\n" + ele[val]
    return string


def get_sql(table, condition) -> Text:
    return "SELECT * FROM " + db + "." + table + " " + condition


class ActionGetTime(Action):

    def name(self) -> Text:
        return "action_ask_thoi_gian"

    def run(
            self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:

        now = datetime.now()

        return [SlotSet("time", now.strftime("%X"))]


class ActionGetDate(Action):

    def name(self) -> Text:
        return "action_ask_ngay"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:

        now = datetime.now()

        return [SlotSet("date", now.strftime("%x"))]


class ActionGetEvent(Action):

    def name(self) -> Text:
        return "action_ask_event"

    def run(
            self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:

        sql = get_sql("event", "")

        list_event = service.get_list(sql)

        return [SlotSet("list_event", get_list(list_event, "tenev"))]

class ActionGetListRoom(Action):

    def name(self) -> Text:
        return "action_ask_danh_sach_phong_ban"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        sql = get_sql("phongban", "")

        list_room = service.get_list(sql)

        return [SlotSet("list_room", get_list(list_room, "tenpb"))]


class ActionGetLocationRoom(Action):

    def name(self) -> Text:
        return "action_ask_vi_tri_phong_ban"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        tenpb = tracker.get_slot("ten_phong_ban")

        sql = get_sql("phongban", "WHERE tenpb LIKE '" + tenpb + "'")

        room = service.get_record(sql)

        if room is None:
            return [SlotSet("dia_diem_phong_ban", None)]

        return [SlotSet("dia_diem_phong_ban", room["diadiem"])]


class ActionGetListEmployeeRoom(Action):

    def name(self) -> Text:
        return "action_ask_danh_sach_nhan_vien_thuoc_phong_ban"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        tenpb = tracker.get_slot("ten_phong_ban")

        sql_get_id = get_sql("phongban", "WHERE tenpb LIKE '" + tenpb + "'")

        id_pb = service.get_record(sql_get_id)["idpb"]

        list_sql = get_sql("nhanvien", "WHERE idpb LIKE '" + str(id_pb) + "'")
        list_employee = service.get_list(list_sql)

        return [SlotSet("list_employee", get_list(list_employee, "hoten"))]


class ActionLeaderRoom(Action):

    def name(self) -> Text:
        return "action_ask_truong_phong"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        tenpb = tracker.get_slot("ten_phong_ban")

        sql_get_id = get_sql("phongban", "WHERE tenpb LIKE '" + tenpb + "'")

        id_pb = service.get_record(sql_get_id)["idpb"]

        if id_pb is None:
            return [SlotSet("truong_phong", None)]

        sql = get_sql("nhanvien", "WHERE idpb = " + str(id_pb) + " AND idchucvu = '1'")

        nv = service.get_record(sql)

        if nv is None:
            return [SlotSet("truong_phong", None)]

        return [SlotSet("truong_phong", nv["hoten"])]


class ActionInfoEvent(Action):

    def name(self) -> Text:
        return "action_ask_thong_tin_event"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        ten_event = tracker.get_slot("ten_event")

        sql = get_sql("event", "WHERE tenev LIKE '" + ten_event + "'")

        event = service.get_record(sql)

        if event is None:
            return [SlotSet("thoi_gian_event", None), SlotSet("dia_diem_event", None)]

        return [SlotSet("thoi_gian_event", str(event['thoigiantochuc'])), SlotSet("dia_diem_event", event['diadiem'])]


class ActionEmployeeEvent(Action):
    def name(self) -> Text:
        return "action_ask_nhan_vien_event"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        ten_event = tracker.get_slot("ten_event")
        sql = get_sql("event", "WHERE tenev LIKE '" + ten_event + "'")

        idevent = service.get_record(sql)["idev"]
        list_sql = get_sql("chitietevent", "WHERE idev = " + str(idevent))
        list_idemployee = service.get_list(list_sql)
        string =""
        for index in list_idemployee:
            sql_id = get_sql("nhanvien", "WHERE idnv = " + str(index["idnv"]))
            string += "\n" + service.get_record(sql_id)["hoten"]

        return [SlotSet("list_nhan_vien_event", string)]


class ActionTimeEvent(Action):

    def name(self) -> Text:
        return "action_ask_thoi_gian_event"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        ten_event = tracker.get_slot("ten_event")

        sql = get_sql("event", "WHERE tenev LIKE '" + ten_event + "'")

        event = service.get_record(sql)

        if event is None:
            return [SlotSet("thoi_gian_event", None)]

        return [SlotSet("thoi_gian_event", str(event['thoigiantochuc']))]


class ActionLocationEvent(Action):

    def name(self) -> Text:
        return "action_ask_dia_diem_event"

    def run(
            self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        ten_event = tracker.get_slot("ten_event")

        sql = get_sql("event", "WHERE tenev LIKE '" + ten_event + "'")

        event = service.get_record(sql)

        if event is None:
            return [SlotSet("dia_diem_event", None)]

        return [SlotSet("dia_diem_event", event['diadiem'])]


class ActionGetListProject(Action):

    def name(self) -> Text:
        return "action_ask_danh_sach_du_an"

    def run(
            self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        sql = get_sql("duan", "")

        list_project = service.get_list(sql)

        return [SlotSet("list_project", get_list(list_project, "tenda"))]


class ActionTimeProject(Action):

    def name(self) -> Text:
        return "action_ask_thoi_gian_thuc_hien_du_an"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        tenda = tracker.get_slot("ten_du_an")

        sql = get_sql("duan", "WHERE tenda LIKE '" + tenda + "'")

        project = service.get_record(sql)

        if project is None:
            return [SlotSet("thoi gian du an", None)]

        return [SlotSet("thoi gian du an", str(project["thoigianthuchien"]))]


class ActionLocationProject(Action):

    def name(self) -> Text:
        return "action_ask_dia_diem_thuc_hien_du_an"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        tenda = tracker.get_slot("ten_du_an")

        sql = get_sql("duan", "WHERE tenda LIKE '" + tenda + "'")

        project = service.get_record(sql)

        if project is None:
            return [SlotSet("dia_diem", None)]

        return [SlotSet("dia_diem", project["diadiem"])]


class ActionEmployeeProject(Action):

    def name(self) -> Text:
        return "action_ask_nhan_vien_du_an"

    def run(
        self, dispatcher, tracker: Tracker, domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        tenda = tracker.get_slot("ten_du_an")

        sql = get_sql("duan", "WHERE tenda LIKE '" + tenda + "'")

        idda = service.get_record(sql)["idda"]
        list_sql = get_sql("chitietduan", "WHERE idda = " + str(idda))
        list_idemployee = service.get_list(list_sql)
        string = ""
        for index in list_idemployee:
            sql_id = get_sql("nhanvien", "WHERE idnv = " + str(index["idnv"]))
            string += "\n" + service.get_record(sql_id)["hoten"]

        return [SlotSet("list_nhan_vien_du_an", string)]

