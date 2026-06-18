import pygame
from pygame import Surface
from CoordinateBox import Coordinate_box


class Debug_ui:
    def __init__(self, screen: Surface):
        self.global_coordinate_box = Coordinate_box((0, 0), "", screen)

    def update(self):
        self.global_coordinate_box.text = str(pygame.mouse.get_pos())
        self.global_coordinate_box.update()

    def draw(self, surface: Surface):
        surface.blit(self.global_coordinate_box.surface)
