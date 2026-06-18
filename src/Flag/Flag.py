from dataclasses import dataclass
from typing import List
from .Piece import Piece
from GlobalValues import Global
import pygame

@dataclass
class Flag:
    id: int
    popular_name: str
    official_state_name: str
    endonym: str
    capital_city: str
    region: str
    category: str
    continent: str
    pieces: List[Piece]

    def draw_pieces(self):
        for piece in self.pieces:
            Global.display_surf.blit(piece.image, piece.rect)
            if piece.is_active:
                pygame.draw.rect(Global.display_surf, (255, 0, 0), piece.rect, 3)



    def move_active_piece_up(self):
        print("move piece up")
        for i, piece in enumerate(self.pieces):
            if piece.is_active and i < len(self.pieces) - 1:
                self.pieces[i], self.pieces[i + 1] = self.pieces[i + 1], self.pieces[i]
                break
    def move_active_piece_down(self):
        print("move piece down")
        for i, piece in enumerate(self.pieces):
            if piece.is_active and i > 0:
                self.pieces[i], self.pieces[i - 1] = self.pieces[i - 1], self.pieces[i]
                break
