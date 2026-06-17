from typing import Tuple
import pygame
from pygame import MOUSEBUTTONUP, Surface, sprite, surface, rect, MOUSEBUTTONDOWN, MOUSEMOTION
from CoordinateBox import Coordinate_box


class Clickable_sprite(sprite.Sprite):
    def __init__(self, image, screen: Surface, name: str):
        super().__init__()
        self.name = name
        self.image: surface.Surface = pygame.image.load(image)
        self.mask: pygame.mask.Mask = pygame.mask.from_surface(self.image)
        self.rect: rect.Rect = self.image.get_rect()
        self.is_clicked: bool = False
        self.is_grabbed = False
        self.local_coordinates = (0, 0)
        self.coordinate_display = Coordinate_box((200, 200), "", screen)
        self.game_surface = screen
        self.is_active = False

    def handle_keys(self):
        key = pygame.key.get_pressed()
        if self.is_active:
            if key[pygame.K_a]:
                self.x -= 1
            elif key[pygame.K_d]:
                self.x += 1
            elif key[pygame.K_w]:
                self.y -= 1
            elif key[pygame.K_s]:
                self.y += 1

    def update_local_coordinates(self, coordinates: Tuple[int, int]):
        self.local_coordinates = coordinates

    def handle_event(self, event) -> None:
        if event.type == MOUSEBUTTONDOWN and self.is_mouse_over():
            self.is_grabbed = True

        elif event.type == MOUSEBUTTONUP:
            self.is_grabbed = False

        elif event.type == MOUSEMOTION and self.is_grabbed:
            self.rect.x = event.pos[0] - self.rect.width
            self.rect.y = event.pos[1] - self.rect.height

    def is_in_bounds(self):
        return (
            self.local_coordinates[0] <= self.image.width
            and self.local_coordinates[1] <= self.image.height
        )

    def draw(self, surface: Surface, xy: pygame.Vector2):
        surface.blit(self.image, xy)

    def display_coords(self):
        self.coordinate_display.text = str(self.local_coordinates)
        self.coordinate_display.update()
        self.game_surface.blit(self.coordinate_display.surface, pygame.mouse.get_pos())

    def is_mouse_over(self):
        if self.is_in_bounds():
            self.coordinate_display.update()
            if self.mask.get_at(self.local_coordinates) == 1:
                return self.mask.get_at(self.local_coordinates)
        return False
