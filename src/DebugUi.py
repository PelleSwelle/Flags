from pygame import Surface
from CoordinateBox import Coordinate_box

class Debug_ui:
    def __init__(self, screen: Surface):
        self.global_coordinate_box = Coordinate_box((0, 0), "", screen)
