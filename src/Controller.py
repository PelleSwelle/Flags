from typing import List
import pygame
from Flag.Piece import Piece
from Flag.Flag import Flag


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
        if event.key == pygame.K_UP:
            game.current_flag.move_active_piece_up()
        if event.key == pygame.K_DOWN:
            game.current_flag.move_active_piece_down()
        if event.key == pygame.K_c:
            self.check_correctness(game.current_flag)

    def _get_piece_at(self, pos, game):
        flag = game.current_flag
        if not flag:
            return None
        assembly_x, assembly_y = flag.assembly_area_rect.topleft
        for piece in reversed(flag.pieces):
            screen_x = assembly_x + int(piece.rect.x - flag.min_x)
            screen_y = assembly_y + int(piece.rect.y - flag.min_y)
            
            rect = pygame.Rect(screen_x, screen_y, piece.rect.width, piece.rect.height)
            if rect.collidepoint(pos):
                return piece
        return None
    def check_correctness(self, flag: Flag):
        score = flag.compare()
        print(f"Flag match: {score:.1f}%")
