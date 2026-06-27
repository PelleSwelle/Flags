import pygame
import sqlite3
import API
from Flag import Flag


class Game:
    def __init__(self, database_path: str):
        self.connection = sqlite3.connect(database_path)
        self.cursor = self.connection.cursor()
        self.current_flag: Flag.Flag | None = None
        self.pass_threshold = 80.0

    def set_current_flag(self, id):
        self.current_flag = API.get_flag_by_id(id)
        if self.current_flag and self.current_flag.pieces:
            self.current_flag.pieces[0].is_active = True

    def update(self):
        if self.current_flag:
            for piece in self.current_flag.pieces:
                piece.handle_keys(pygame.key.get_pressed())
