import pygame
from CoordinateBox import Coordinate_box
import sqlite3
from API import get_flag, get_all_country_names
import pygame_gui
from Controller import Controller
from ui import UI
from GlobalValues import Global
from Game import Game

conn = sqlite3.connect("./database.db")
cursor = conn.cursor()

controller = Controller()

game = Game("./database.db")
game.set_current_flag(2)


class App:
    def __init__(self):
        self._running = True
        self.clock = pygame.time.Clock()
        pygame.font.init()

    def on_init(self):
        pygame.init()
        self.global_coordinate_box = Coordinate_box((0, 0), "", Global.display_surf)
        self._running = True

    def on_event(self, event):
        UI.manager.process_events(event)
        if event.type == pygame.QUIT:
            self._running = False
        if event.type == pygame_gui.UI_BUTTON_PRESSED:
            if event.ui_element == UI.hello_button:
                print("hemnelo")
            if event.ui_element == UI.load_button:
                game.current_flag = UI.flags_dropdown.selected_option
        if event.type == pygame.MOUSEBUTTONDOWN:
            for piece in game.current_flag.pieces:
                if piece.sprite.is_mouse_over():
                    piece.sprite.is_grabbed = True
                    print(f"click on flag at {pygame.mouse.get_pos()}")
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_TAB:
                controller.select_next_piece(game.current_flag.pieces)
            if event.key == pygame.K_a:
                print(get_all_country_names())

    def on_loop(self):
        self.global_coordinate_box.text = str(pygame.mouse.get_pos())
        self.global_coordinate_box.update()
        for piece in game.current_flag.pieces:
            piece.sprite.handle_keys()
            if piece.sprite.is_in_bounds():
                piece.sprite.display_coords()

    def on_render(self):
        for piece in game.current_flag.pieces:
            piece.sprite.draw(Global.display_surf, piece.position)
        Global.display_surf.blit(self.global_coordinate_box.surface)
        pygame.display.flip()
        self.clock.tick()
        Global.display_surf.fill("black")

    def on_cleanup(self):
        pygame.quit()

    def on_execute(self):
        if self.on_init() == False:
            self._running = False

        while self._running:
            time_delta = self.clock.tick(60) / 1000.0
            for event in pygame.event.get():
                self.on_event(event)
            for piece in game.current_flag.pieces:
                piece.sprite.handle_event(event)
                piece.sprite.update_local_coordinates(
                    (
                        pygame.mouse.get_pos()[0] - piece.sprite.image.get_offset()[0],
                        pygame.mouse.get_pos()[1] - piece.sprite.image.get_offset()[1],
                    )
                )
            UI.manager.update(time_delta)
            UI.manager.draw_ui(Global.display_surf)
            self.on_loop()
            self.on_render()
        self.on_cleanup()


if __name__ == "__main__":
    theApp = App()
    theApp.on_execute()
