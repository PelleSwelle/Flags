import os
from dataclasses import dataclass, field
from typing import List, Optional

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
    score: Optional[float] = None

    reference_surface: pygame.Surface = field(init=False)
    assembly_surface: pygame.Surface = field(init=False)
    min_x: float = field(init=False)
    min_y: float = field(init=False)
    assembly_area_rect: pygame.Rect = field(init=False)

    def __post_init__(self):
        if not self.pieces:
            self.min_x = self.min_y = 0
            self.reference_surface = pygame.Surface((1, 1), pygame.SRCALPHA)
            self.assembly_surface = pygame.Surface((1, 1), pygame.SRCALPHA)
            self.assembly_area_rect = pygame.Rect(0, 0, 1, 1)
            return

        self.min_x = min(p.correct_x for p in self.pieces)
        self.min_y = min(p.correct_y for p in self.pieces)
        max_x = max(p.correct_x + p.image.get_width() for p in self.pieces)
        max_y = max(p.correct_y + p.image.get_height() for p in self.pieces)

        surf_size = (int(max_x - self.min_x), int(max_y - self.min_y))

        from API import normalize_asset_path

        normalized_asset_path = normalize_asset_path(self.pieces[0].asset_path)
        path_to_reference_image = os.path.join(
            os.path.dirname(normalized_asset_path), "reference.png"
        )
        if os.path.exists(path_to_reference_image):
            self.reference_surface = pygame.image.load(
                path_to_reference_image
            ).convert_alpha()
        else:
            self.reference_surface = pygame.Surface(surf_size, pygame.SRCALPHA)
            for piece in self.pieces:
                pos = (
                    int(piece.correct_x - self.min_x),
                    int(piece.correct_y - self.min_y),
                )
                self.reference_surface.blit(piece.image, pos)

        self.assembly_surface = pygame.Surface(
            self.reference_surface.get_size(), pygame.SRCALPHA
        )

        center_x = Global.SCREEN_WIDTH // 2
        center_y = Global.SCREEN_HEIGHT // 2
        self.assembly_area_rect = self.reference_surface.get_rect(
            center=(center_x, center_y)
        )

    def _render_assembly(self):
        self.assembly_surface.fill((0, 0, 0, 0))
        for piece in self.pieces:
            position = (int(piece.rect.x - self.min_x), int(piece.rect.y - self.min_y))
            self.assembly_surface.blit(piece.image, position)

    def draw_pieces(self):
        self._render_assembly()

        for piece in self.pieces:
            screen_x = self.assembly_area_rect.x + int(piece.rect.x - self.min_x)
            screen_y = self.assembly_area_rect.y + int(piece.rect.y - self.min_y)
            Global.display_surf.blit(piece.image, (screen_x, screen_y))

        pygame.draw.rect(
            Global.display_surf, (255, 255, 255), self.assembly_area_rect, 2
        )

        self.draw_active_outline()
        self.display_score()

    def display_score(self):
        if self.score is not None:
            font = pygame.font.Font(None, 36)
            text = font.render(f"Score: {self.score:.1f}%", True, (255, 255, 255))
            text_rect = text.get_rect(
                center=(
                    self.assembly_area_rect.centerx,
                    self.assembly_area_rect.bottom + 30,
                )
            )
            Global.display_surf.blit(text, text_rect)

    def draw_active_outline(self):
        for piece in self.pieces:
            if piece.is_active:
                screen_x = self.assembly_area_rect.x + int(piece.rect.x - self.min_x)
                screen_y = self.assembly_area_rect.y + int(piece.rect.y - self.min_y)
                pygame.draw.rect(
                    Global.display_surf,
                    (255, 0, 0),
                    (screen_x, screen_y, piece.rect.width, piece.rect.height),
                    3,
                )

    def draw_hover_tooltip(self):
        mouse_pos = pygame.mouse.get_pos()
        for piece in reversed(self.pieces):
            screen_x = self.assembly_area_rect.x + int(piece.rect.x - self.min_x)
            screen_y = self.assembly_area_rect.y + int(piece.rect.y - self.min_y)
            piece_rect = pygame.Rect(
                screen_x, screen_y, piece.rect.width, piece.rect.height
            )
            if piece_rect.collidepoint(mouse_pos) and piece.meaning:
                draw_tooltip(Global.display_surf, piece.meaning, mouse_pos)
                break

    def compare(self) -> float:
        import API

        self._render_assembly()
        reference_pixels = pygame.PixelArray(self.reference_surface)
        current_pixels = pygame.PixelArray(self.assembly_surface)
        total = reference_pixels.shape[0] * reference_pixels.shape[1]
        matches = 0
        for x in range(reference_pixels.shape[0]):
            for y in range(reference_pixels.shape[1]):
                if reference_pixels[x][y] == current_pixels[x][y]:
                    matches += 1
        self.score = matches / total * 100
        API.set_flag_score(self.id, self.score)
        return self.score

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


def draw_tooltip(surface, text, mouse_pos, max_width=300):
    font = pygame.font.Font(None, 22)
    lines = _wrap_text(text, font, max_width)
    line_height = font.get_linesize()
    tw = min(max(font.size(l)[0] for l in lines) + 20, max_width + 20)
    th = line_height * len(lines) + 16

    tx = mouse_pos[0] + 15
    ty = mouse_pos[1] - 10
    if tx + tw > Global.SCREEN_WIDTH:
        tx = mouse_pos[0] - tw - 15
    if ty + th > Global.SCREEN_HEIGHT:
        ty = Global.SCREEN_HEIGHT - th - 5
    if ty < 0:
        ty = 5

    bg = pygame.Surface((tw, th), pygame.SRCALPHA)
    bg.fill((0, 0, 0, 200))
    surface.blit(bg, (tx, ty))

    for i, line in enumerate(lines):
        text_surf = font.render(line, True, (255, 255, 255))
        surface.blit(text_surf, (tx + 10, ty + 8 + i * line_height))


def _wrap_text(text: str, font, max_width: int) -> list[str]:
    words = text.split(" ")
    lines = []
    current = ""
    for word in words:
        test = f"{current} {word}".strip()
        if font.size(test)[0] > max_width and current:
            lines.append(current)
            current = word
        else:
            current = test
    if current:
        lines.append(current)
    return lines
