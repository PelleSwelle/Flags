from typing import List
import pygame
from API import get_all_country_names
from Flag.Piece import Piece


class Controller:
    def select_next_piece(self, pieces: List[Piece]):
        for i, piece in enumerate(pieces):
            if piece.is_active:
                piece.is_active = False
                next_index = (i + 1) % len(pieces)
                pieces[next_index].is_active = True
                print(pieces[next_index].id)
                break

    def handle_key_event(self, event, game):
        if event.key == pygame.K_TAB:
            self.select_next_piece(game.current_flag.pieces)
        if event.key == pygame.K_a:
            print(get_all_country_names())
        if event.key == pygame.K_UP:
            game.current_flag.move_active_piece_up()
        if event.key == pygame.K_DOWN:
            game.current_flag.move_active_piece_down()
