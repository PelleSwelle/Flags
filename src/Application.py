import pygame
import sqlite3
import API
from Flag.Flag import Flag


class Application:
    """The entirety of the game, including menu, gameplay, score screen. Everything that happens from when the player turns on the game."""

    def __init__(self, database_path: str):
        self.connection = sqlite3.connect(database_path)
        self.cursor = self.connection.cursor()
        self.current_flag: Flag | None = None
        self.current_range: list[Flag] | None = None
        self.pass_threshold = 80.0

    def set_current_range(self, range: list[Flag]):
        self.current_range = range

    # TODO: this should take a flag, not an id and then get the flag.
    def set_current_flag(self, id):
        self.current_flag = API.get_flag_by_id(id)
        if self.current_flag and self.current_flag.pieces:
            self.current_flag.pieces[0].is_active = True

    def update(self):
        if self.current_flag:
            for piece in self.current_flag.pieces:
                piece.handle_keys(pygame.key.get_pressed())
