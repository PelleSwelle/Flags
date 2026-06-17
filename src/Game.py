import sqlite3
import API
from Flag import Flag


class Game:
    def __init__(self, database_path: str):
        self.connection = sqlite3.connect(database_path)
        self.cursor = self.connection.cursor()
        self.current_flag = None

    def set_current_flag(self, flag_id: int):
        self.current_flag = API.get_flag(flag_id)
