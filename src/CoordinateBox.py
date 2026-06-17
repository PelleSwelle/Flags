import pygame


class Coordinate_box:
    def __init__(self, position: tuple, text: str, screen: pygame.Surface):
        self.font = pygame.font.SysFont("freesansbold", 50)
        self.text = text
        self.surface = self.font.render(text, True, (255, 255, 255))
        self.text_rect = self.surface.get_rect()
        self.text_rect.center = (position[0], position[1])
        self.screen = screen

    def update(self):
        self.surface = self.font.render(self.text, True, (255, 255, 255))
