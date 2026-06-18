import pygame
import sqlite3
import API
from Flag import Flag


class Game:
    def __init__(self, database_path: str):
        self.connection = sqlite3.connect(database_path)
        self.cursor = self.connection.cursor()
        self.current_flag: Flag.Flag | None = None

    def set_current_flag(self, flag_id_or_name: int | str):
        if isinstance(flag_id_or_name, int):
            self.current_flag = API.get_flag_by_id(flag_id_or_name)
        elif isinstance(flag_id_or_name, str):
            self.current_flag = API.get_flag_by_name(flag_id_or_name)
        
        self.current_flag.pieces[0].is_active = True

    def update(self):
        if self.current_flag:
            for piece in self.current_flag.pieces:
                piece.handle_keys(pygame.key.get_pressed())
